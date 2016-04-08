/*
 * Copyright (C) 2016 Bug 3050429487@qq.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mmle.minecraft.io.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class CacheImpl<K, T> extends Cache<K, T> {
    
    CacheImpl(CacheItemLoader<K, T> cacheItemLoader, CacheItemProcessor<K, T> cacheItemProcessor, int expectCacheSize, int maxCacheSize) {
        this(new CacheMap(), cacheItemLoader, cacheItemProcessor, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()), expectCacheSize, maxCacheSize);
    }
    
    CacheImpl(CacheMap<K, T> cacheMap, CacheItemLoader<K, T> cacheItemLoader, CacheItemProcessor<K, T> cacheItemProcessor, Executor preloaderExecutor, int expectCacheSize, int maxCacheSize) {
        this.cacheMap = cacheMap;
        
        this.preloaderExecutor = preloaderExecutor;
        
        this.cacheItemLoader    = cacheItemLoader;
        this.cacheItemProcessor = cacheItemProcessor;
        
        this.expectCacheSize = expectCacheSize;
        this.maxCacheSize    = maxCacheSize;
    }
    
    @Override
    public void tryPreloadItem(K key) {
        preloaderExecutor.execute(new PreloaderRunnable(key));
    }
    
    private class PreloaderRunnable implements Runnable{

        private K key;
        
        public PreloaderRunnable(K key) {
            this.key = key;
        }
        
        @Override
        public void run() {
            tryLoadItem(key);
        }
        
    }

    @Override
    public boolean tryLoadItem(K key) {
        if(cachedItem(key))
            return false;
        
        try {
            CacheItem<K, T> cacheItem = loadItem(key);
            cacheMap.putCacheItemObject(cacheItem);
            
            handleCacheItemLoadEventListener(cacheItem);
            
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean cachedItem(K key) {
        return cacheMap.getCacheItem(key) != null;
    }
    
    @Override
    public CacheItem<K, T> getItem(K key) throws CacheOverflowException, Exception {
        CacheItem<K, T> cacheItem = cacheMap.getCacheItem(key);
        
        if(cacheItem != null)
        {
            handleCacheItemHitEventListener(cacheItem);
            return cacheItem;
        }
        
        cacheItem = loadItem(key);
        cacheMap.putCacheItemObject(cacheItem);
        
        handleCacheItemHitEventListener(cacheItem);
        
        return cacheItem;
    }

    private CacheItem<K, T> loadItem(K key) throws CacheOverflowException, Exception {
        
        int maxSize = maxSize();
        
        if(currentSize() == maxSize())
            throw new CacheOverflowException(maxSize, maxSize + 1);
        
        T cacheItemObject = cacheItemLoader.load(key);
        
        CacheItem<K, T> cacheItem = takeInvalidCacheItemFromBuffer(key);
        
        if(cacheItem == null)
        {
            cacheItem = new CacheItem();
            cacheItem.cacheItemObject = cacheItemObject;
            cacheItem.cacheItemObjectKey = key;
        }
        
        cacheItem.overloadable = cacheItemProcessor.overloadable(key, cacheItemObject);
        
        handleCacheItemLoadEventListener(cacheItem);
        
        return cacheItem;
    }
    
    @Override
    public int maxSize() {
        return maxCacheSize;
    }

    @Override
    public int expectSize() {
        return expectCacheSize;
    }

    @Override
    public int currentSize() {
        return cacheMap.size();
    }

    @Override
    public Lock getWriteLock() {
        return cacheMap.readWriteLock.writeLock();
    }

    @Override
    public Lock getReadLock() {
        return cacheMap.readWriteLock.readLock();
    }

    @Override
    public Iterator<CacheItem<K, T>> iterator() {
        return cacheMap.iterator();
    }

    @Override
    boolean overload(CacheItem<K, T> cacheItem) {
        try {
            T cacheItemObject = cacheItemLoader.load(cacheItem.cacheItemObjectKey);
            
            if(cacheItemObject != null)
            {
                cacheItem.cacheItemObject = cacheItemObject;
                return true;
            }
            
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    void invalidate(CacheItem<K, T> cacheItem) {
        
        if(cacheItem.overloadable)
        {
            logout(cacheItem);
            cacheMap.removeCacheItem(cacheItem.cacheItemObjectKey);
            
            addInvalidCacheItemToBuffer(cacheItem);
            handleCacheItemInvalidateEventListener(cacheItem);
        }
        else
        {
            remove(cacheItem);
        }
    }

    @Override
    void remove(CacheItem<K, T> cacheItem) {
        logout(cacheItem);
        
        cacheMap.removeCacheItem(cacheItem.cacheItemObjectKey);
        handleCacheItemInvalidateEventListener(cacheItem);
    }
    
    private void logout(CacheItem<K, T> cacheItem) {
        cacheItem.invalidated = true;
        cacheItem.cacheItemObject = null;
    }

    @Override
    void clear() {
        Lock writeLock = cacheMap.readWriteLock.writeLock();
        
        try {
            writeLock.lock();
            cleanout();
            
            int count = 0;
            CacheItem<K, T>[] removeList = new CacheItem[currentSize()];
            
            for(CacheItem<K, T> cacheItem : cacheMap) {
                removeList[count++] = cacheItem;
            }
            
            for(CacheItem<K, T> cacheItem : removeList) {
                cacheItem.overloadable = false;
                remove(cacheItem);
            }
        }
        finally {
            writeLock.unlock();
        }
    }
    
    private final Executor preloaderExecutor;
    
    private final CacheItemLoader<K, T> cacheItemLoader;
    private final CacheItemProcessor<K, T> cacheItemProcessor;
    
    private final CacheMap<K, T> cacheMap;
    
    private final Map<CacheItemKey<K, T>, CacheItemWeakReference<K, T>> invalidCacheItemsBuffer = new HashMap();
    private final CacheItemReferenceQueue<K, T> cacheRemoveedReferenceQueue = new CacheItemReferenceQueue();
    
    private final int expectCacheSize;
    private final int maxCacheSize;
    
    private class CacheItemWeakReference<K, T> extends WeakReference<CacheItem<K, T>> {

        CacheItemKey<K, T> cacheItemKey;

        public CacheItemWeakReference(CacheItemKey<K, T> cacheItemKey, CacheItem<K, T> cacheItemReferent, CacheItemReferenceQueue<K, T> queue) {
            super(cacheItemReferent, queue);
            this.cacheItemKey = cacheItemKey;
        }

    }
    
    private class CacheItemKey<K, T> {
        CacheItemWeakReference<K, T> cacheItemReference;
        K cacheItemObjectKey;
        
        CacheItemKey(K cacheItemObjectKey) {
            this.cacheItemObjectKey = cacheItemObjectKey;
        }
        
        CacheItemKey(CacheItem<K, T> cacheItem) {
            this.cacheItemReference = new CacheItemWeakReference(this, cacheItem, cacheRemoveedReferenceQueue);
            this.cacheItemObjectKey = cacheItem.cacheItemObjectKey;
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CacheItemKey)
            {
                CacheItemKey targetCacheItemKey = (CacheItemKey) obj;
                return targetCacheItemKey.cacheItemObjectKey.equals(cacheItemObjectKey);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return cacheItemObjectKey.hashCode();
        }
    }
    
    private void addInvalidCacheItemToBuffer(CacheItem<K, T> cacheItem) {
        CacheItemKey cacheItemKey = new CacheItemKey(cacheItem);
        invalidCacheItemsBuffer.put(cacheItemKey, cacheItemKey.cacheItemReference);
    }
    
    private CacheItem takeInvalidCacheItemFromBuffer(K key) {
        cleanout();
        
        CacheItemKey cacheItemKey = new CacheItemKey(key);
        CacheItemWeakReference<K, T> reference = invalidCacheItemsBuffer.get(cacheItemKey);
        
        return reference == null? null: reference.get();
    }
    
            
    private void cleanoutFailureCacheItemsBuffer() {
        synchronized(invalidCacheItemsBuffer) {
            CacheItemWeakReference<K, T> reference;
            
            while((reference = (CacheItemWeakReference<K, T>) cacheRemoveedReferenceQueue.poll()) != null)
            {
                invalidCacheItemsBuffer.remove(reference.cacheItemKey);
            }
        }
    }
    
    
    
    private final Set<CacheItemLoadEventListener<K, T>>    cacheItemLoadEventListenerSet = new HashSet();
    private final Set<CacheItemHitEventListener<K, T>>     cacheItemHitEventListenerSet  = new HashSet();
    private final Set<CacheItemInvalidateEventListener<K, T>> cacheItemEliminaEventListener = new HashSet();
    
    @Override
    public boolean addCacheItemLoadEventListener(CacheItemLoadEventListener<K, T> listener) {
        return cacheItemLoadEventListenerSet.add(listener);
    }

    @Override
    public boolean addCacheItemHitEventListener(CacheItemHitEventListener<K, T> listener) {
        return cacheItemHitEventListenerSet.add(listener);
    }

    @Override
    public boolean addCacheItemInvalidateEventListener(CacheItemInvalidateEventListener<K, T> listener) {
        return cacheItemEliminaEventListener.add(listener);
    }

    
    @Override
    public boolean containCacheItemLoadEventListener(CacheItemLoadEventListener<K, T> listener) {
        return cacheItemLoadEventListenerSet.contains(listener);
    }

    @Override
    public boolean containCacheItemHitEventListener(CacheItemHitEventListener<K, T> listener) {
        return cacheItemHitEventListenerSet.contains(listener);
    }

    @Override
    public boolean containCacheItemInvalidateEventListener(CacheItemInvalidateEventListener<K, T> listener) {
        return cacheItemEliminaEventListener.contains(listener);
    }

    
    private void  handleCacheItemLoadEventListener(CacheItem<K, T> cacheItem) {
        handleCacheItemLoadEventListener(cacheItem.cacheItemObjectKey, cacheItem.cacheItemObject);
    }
    
    private void handleCacheItemLoadEventListener(K key, T cacheItem) {
        cacheItemLoadEventListenerSet.stream().forEach((listener) -> listener.on(key, cacheItem));
    }
    
    
    private void  handleCacheItemHitEventListener(CacheItem<K, T> cacheItem) {
        handleCacheItemHitEventListener(cacheItem.cacheItemObjectKey, cacheItem.cacheItemObject);
    }
    
    private void  handleCacheItemHitEventListener(K key, T cacheItem) {
        cacheItemHitEventListenerSet.stream().forEach((listener) -> listener.on(key, cacheItem));
    }
    
    
    private void  handleCacheItemInvalidateEventListener(CacheItem<K, T> cacheItem) {
        handleCacheItemInvalidateEventListener(cacheItem.cacheItemObjectKey, cacheItem.cacheItemObject);
    }
    
    private void  handleCacheItemInvalidateEventListener(K key, T cacheItem) {
        cacheItemEliminaEventListener.stream().forEach((listener) -> listener.on(key, cacheItem));
    }
    
    
    @Override
    public boolean removeCacheItemLoadEventListener(CacheItemLoadEventListener<K, T> listener) {
        return cacheItemLoadEventListenerSet.remove(listener);
    }

    @Override
    public boolean removeCacheItemHitEventListener(CacheItemHitEventListener<K, T> listener) {
        return cacheItemHitEventListenerSet.remove(listener);
    }

    @Override
    public boolean removeCacheItemInvalidateEventListener(CacheItemInvalidateEventListener<K, T> listener) {
        return cacheItemEliminaEventListener.remove(listener);
    }
    
    
    @Override
    void cleanout() {
        cleanoutFailureCacheItemsBuffer();
        cacheMap.cleanout();
    }
    
}
