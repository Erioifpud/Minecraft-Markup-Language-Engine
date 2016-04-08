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

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class CacheBuilder<K, T> {
    private CacheBuilder() {}
    
    public static <K, T> CacheBuilder<K, T> get() {
        return new CacheBuilder();
    }
    
    public CacheBuilder<K, T> setLoader(CacheItemLoader<K, T> cacheItemLoader) {
        this.cacheItemLoader = cacheItemLoader;
        return this;
    }
    
    public CacheBuilder<K, T> setProcessor(CacheItemProcessor<K, T> cacheItemProcessor) {
        this.cacheItemProcessor = cacheItemProcessor;
        return this;
    }
    
    public CacheBuilder<K, T> setFilter(CacheItemFilter<K, T> cacheFilter) {
        this.cacheFilter = cacheFilter;
        return this;
    }
    
    public CacheBuildResults build(int expectCacheSize) {
        return build(expectCacheSize, expectCacheSize + 5);
    }
    
    public CacheBuildResults build(int expectCacheSize, int maxCacheSize) {
        Cache<K, T> cache = new CacheImpl(cacheItemLoader, cacheItemProcessor, expectCacheSize, maxCacheSize);
        CacheManager<K, T> cacheManager = new CacheManager(cache, cacheFilter, cacheItemProcessor);
        
        return new CacheBuildResults(cache, cacheManager);
    }
    
    private CacheItemLoader<K, T> cacheItemLoader;
    private CacheItemProcessor<K, T> cacheItemProcessor;
    private CacheItemFilter<K, T> cacheFilter;
    
    public class CacheBuildResults {
        private final Cache<K, T> cache;
        private final CacheManager<K, T> cacheManager;
        
        private CacheBuildResults(Cache<K, T> cache, CacheManager<K, T> cacheManager) {
            this.cache = cache;
            this.cacheManager = cacheManager;
        }
        
        public CacheBuilder getBuilder() {
            return CacheBuilder.this;
        }
        
        public Cache<K, T> getCache() {
            return cache;
        }
        
        public CacheManager<K, T> getCacheManager() {
            return cacheManager;
        }
        
        
        public CacheBuildResults makeCacheManagerWork(long delay, long period) {
            cacheManager.startWork(delay, period);
            return this;
        }

        public CacheBuildResults makeCacheManagerWork(Date firstTime, long period) {
            cacheManager.startWork(firstTime, period);
            return this;
        }

        public CacheBuildResults makeCacheManagerWorkAtFixedRate(long delay, long period) {
            cacheManager.startWorkAtFixedRate(delay, period);
            return this;
        }

        public CacheBuildResults makeCacheManagerWorkAtFixedRate(Date firstTime, long period) {
            cacheManager.startWorkAtFixedRate(firstTime, period);
            return this;
        }
        
    }
}
