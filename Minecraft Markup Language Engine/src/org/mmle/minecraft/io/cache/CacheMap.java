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
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Bug 3050429487@qq.com
 */
class CacheMap<K, T> implements Iterable<CacheItem<K, T>> {
    
    CacheMap() {
        this(new HashMap());
    }
    
    CacheMap(Map<K, CacheItemWeakReference<K, T>> cacheMap) {
        this.cacheMap = cacheMap;
    }
    
    
    public CacheItem<K, T> getCacheItem(K key) {
        Lock readLock = readWriteLock.readLock();
        
        try {
            readLock.lock();
            CacheItemWeakReference<K, T> reference = cacheMap.get(key);
            
            return reference == null ? null : reference.get();
        }
        finally {
            readLock.unlock();
        }
    }
    
    public void putCacheItemObject(CacheItem<K, T> cacheItem) {
        Lock writeLock = readWriteLock.writeLock();
        
        try {
            writeLock.lock();
            CacheItemWeakReference cacheItemPhantomReference = new CacheItemWeakReference(cacheItem, cacheRemoveedReferenceQueue);
            cacheMap.put(cacheItem.cacheItemObjectKey, cacheItemPhantomReference);
        }
        finally {
            writeLock.unlock();
        }
    }
    
    public void removeCacheItem(K key) {
        Lock writeLock = readWriteLock.writeLock();
        
        try {
            writeLock.lock();
            
            CacheItemWeakReference<K, T> reference = cacheMap.remove(key);
            reference.enqueue();
        }
        finally {
            writeLock.unlock();
        }
    }
    
    
    public void cleanout() {
        Lock writeLock = readWriteLock.writeLock();
        
        try {
            writeLock.lock();
            CacheItemWeakReference<K, T> reference;
            
            while((reference = (CacheItemWeakReference<K, T>) cacheRemoveedReferenceQueue.poll()) != null)
            {
                cacheMap.remove(reference.cacheItemObjectKey);
            }
        }
        finally {
            writeLock.unlock();
        }
    }
    
    
    @Override
    public Iterator<CacheItem<K, T>> iterator() {
        cleanout();
        
        return new Iterator<CacheItem<K, T>>() {

            private final Iterator<CacheItemWeakReference<K, T>> it = cacheMap.values().iterator();
            
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public CacheItem<K, T> next() {
                return it.next().get();
            }
            
        };
    }
    
    public int size() {
        cleanout();
        return cacheMap.size();
    }
    
    private final Map<K, CacheItemWeakReference<K, T>> cacheMap;
    private final CacheItemReferenceQueue<K, T> cacheRemoveedReferenceQueue = new CacheItemReferenceQueue();
    
    final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    private static class CacheItemWeakReference<K, T> extends WeakReference<CacheItem<K, T>> {

        K cacheItemObjectKey;

        public CacheItemWeakReference(CacheItem<K, T> cacheItemReferent, CacheItemReferenceQueue<K, T> queue) {
            super(cacheItemReferent, queue);
            this.cacheItemObjectKey = cacheItemReferent.cacheItemObjectKey;
        }

    }
    
}
