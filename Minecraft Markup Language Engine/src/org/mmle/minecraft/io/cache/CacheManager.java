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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class CacheManager<K, T> {
    
    
    public CacheManager(
            Cache<K, T>              cache, 
            CacheItemFilter<K, T>    cacheItemFilter, 
            CacheItemProcessor<K, T> cacheItemProcessor
    ) {
        this(cache, cacheItemFilter, cacheItemProcessor, new Timer("CacheManager-", true));
    }
    
    public CacheManager(
            Cache<K, T>              cache, 
            CacheItemFilter<K, T>    cacheItemFilter, 
            CacheItemProcessor<K, T> cacheItemProcessor,
            Timer                    cacheUpdateTimer
    ) {
        this.cache              = cache;
        
        this.cacheItemFilter    = cacheItemFilter;
        this.cacheItemProcessor = cacheItemProcessor;
        
        this.cacheUpdateTimer   = cacheUpdateTimer;
    }
    
    
    private final Cache<K, T> cache;
    
    private final CacheItemFilter<K, T> cacheItemFilter;
    private final CacheItemProcessor<K, T> cacheItemProcessor;
    
    private final Timer cacheUpdateTimer;
    
    
    public void startWork(long delay, long period) {
        checkWhetherWork();
        
        cacheUpdateTimer.schedule(cacheFilterDrive, delay, period);
        workingFlag = true;
    }
    
    public void startWork(Date firstTime, long period) {
        checkWhetherWork();
        
        cacheUpdateTimer.schedule(cacheFilterDrive, firstTime, period);
        workingFlag = true;
    }
    
    public void startWorkAtFixedRate(long delay, long period) {
        checkWhetherWork();
        cacheUpdateTimer.scheduleAtFixedRate(cacheFilterDrive, delay, period);
        workingFlag = true;
    }
    
    public void startWorkAtFixedRate(Date firstTime, long period) {
        checkWhetherWork();
        cacheUpdateTimer.scheduleAtFixedRate(cacheFilterDrive, firstTime, period);
        workingFlag = true;
    }
    
    public boolean cancelWork() {
        if(!workingFlag)
            return false;
        
        cacheFilterDrive.cancel();
        workingFlag = false;
        return true;
    }
    
    private void checkWhetherWork() {
        if(workingFlag)
            throw new InternalError("Cache manager has been in the work ");
    }

    
    public void cleanout() {
        synchronized(cacheFilterDrive) {
            cacheFilterDrive.run();
        }
    }
    
    private boolean workingFlag = false;
    private final CacheFilterDrive cacheFilterDrive = new CacheFilterDrive();
    
    
    private class CacheFilterDrive extends TimerTask{
        
        @Override
        public void run() {
            int maxCacheSize = cache.maxSize();
            int expectCacheSize = cache.expectSize();
            int currentCacheSize = cache.currentSize();
            
            if(currentCacheSize > expectCacheSize)
            {
                cache.cleanout();
                
                Lock cacheWriteLock = cache.getWriteLock();
                cacheWriteLock.tryLock();

                CacheItemAndInvalidValue[] invalidQueue = each(cache, maxCacheSize, expectCacheSize, currentCacheSize);
                removeInvalidCache(invalidQueue);

                cacheWriteLock.unlock();
            }
        }
        
        private CacheItemAndInvalidValue[] each(Cache<K, T> cache, int maxCacheSize, int expectCacheSize, int currentCacheSize) {
            
            CacheItemAndInvalidValue[] invalidCacheItemBuffer = new CacheItemAndInvalidValue[currentCacheSize - expectCacheSize];
            
            for(CacheItem<K, T> cacheItem : cache) 
            {
                if(cacheItem != null)
                {
                    K cacheItemObjectKey = cacheItem.cacheItemObjectKey;
                    T cacheItemObject = cacheItem.cacheItemObject;

                    float cacheImportanceValue = cacheItemFilter.cacheImportanceAssess(cacheItemObjectKey, cacheItemObject, maxCacheSize, expectCacheSize, currentCacheSize);
                    tryAddCacheToInvalidQueue(invalidCacheItemBuffer, cacheItem, cacheImportanceValue);
                }
            }
            
            return invalidCacheItemBuffer;
        }
        
        private void tryAddCacheToInvalidQueue(CacheItemAndInvalidValue[] queue, CacheItem<K, T> cacheItem, float cacheImportance) {
            float cacheInvalidValue = 1 - cacheImportance;
            
            for(int i = 0; i < queue.length; i++)
            {
                CacheItemAndInvalidValue cacheItemAndInvalidValue = queue[i];
                
                if(cacheItemAndInvalidValue == null)
                {
                    queue[i] = new CacheItemAndInvalidValue(cacheItem, cacheInvalidValue);
                    return;
                }
                else if(cacheItemAndInvalidValue.replace(cacheItem, cacheInvalidValue))
                {
                    return;
                }
            }
        }
        
        private void removeInvalidCache(CacheItemAndInvalidValue[] invalidQueue) {
            for(CacheItemAndInvalidValue cacheItemAndInvalidValue: invalidQueue)
            {
                if(cacheItemAndInvalidValue != null)
                {
                    CacheItem<K, T> cacheItem = cacheItemAndInvalidValue.cacheItem;

                    cacheItem.overloadable = cacheItemProcessor.overloadable(cacheItem.cacheItemObjectKey, cacheItem.cacheItemObject);
                    cache.invalidate(cacheItemAndInvalidValue.cacheItem);
                }
            }
        }
        
        private class CacheItemAndInvalidValue<K, T> {
            CacheItem<K, T> cacheItem;
            float cacheInvalidValue;
            
            CacheItemAndInvalidValue() {
                this(null, 0);
            }
            
            CacheItemAndInvalidValue(CacheItem<K, T> cacheItem, float cacheInvalidValue) {
                this.cacheItem = cacheItem;
                this.cacheInvalidValue = cacheInvalidValue;
            }
            
            boolean replace(CacheItem<K, T> newCacheItem, float newCacheInvalidValue) {
                if(newCacheInvalidValue > cacheInvalidValue)
                {
                    this.cacheItem = newCacheItem;
                    this.cacheInvalidValue = newCacheInvalidValue;
                    return true;
                }
                
                return false;
            }
        }
    }
    
    
}
