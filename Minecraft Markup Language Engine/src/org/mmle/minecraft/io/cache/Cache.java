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

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;


/**
 *
 * @author Bug 3050429487@qq.com
 */
public abstract class Cache<K, T> implements Iterable<CacheItem<K, T>>, Serializable {
    Cache() {}
    
    public abstract void tryPreloadItem(K key);
    
    public abstract boolean tryLoadItem(K key);
    
    public abstract boolean cachedItem(K key);
    
    public abstract CacheItem<K, T> getItem(K key) throws Exception;
    
    
    public abstract int maxSize();
    
    public abstract int expectSize();
    
    public abstract int currentSize();
    
    
    public abstract Lock getWriteLock();
    
    public abstract Lock getReadLock();
    
    
    @Override
    public abstract Iterator<CacheItem<K, T>> iterator();
    
    
    abstract boolean overload(CacheItem<K, T> cacheItem);
    
    abstract void invalidate(CacheItem<K, T> cacheItem);
    
    abstract void remove(CacheItem<K, T> cacheItem);
    
    abstract void clear();
    
    
    public abstract boolean addCacheItemLoadEventListener(CacheItemLoadEventListener<K, T> listener);
    
    public abstract boolean addCacheItemHitEventListener(CacheItemHitEventListener<K, T> listener);
    
    public abstract boolean addCacheItemInvalidateEventListener(CacheItemInvalidateEventListener<K, T> listener);
    
    
    public abstract boolean containCacheItemLoadEventListener(CacheItemLoadEventListener<K, T> listener);
    
    public abstract boolean containCacheItemHitEventListener(CacheItemHitEventListener<K, T> listener);
    
    public abstract boolean containCacheItemInvalidateEventListener(CacheItemInvalidateEventListener<K, T> listener);
    
    
    public abstract boolean removeCacheItemLoadEventListener(CacheItemLoadEventListener<K, T> listener);
    
    public abstract boolean removeCacheItemHitEventListener(CacheItemHitEventListener<K, T> listener);
    
    public abstract boolean removeCacheItemInvalidateEventListener(CacheItemInvalidateEventListener<K, T> listener);
    
    abstract void cleanout();
}
