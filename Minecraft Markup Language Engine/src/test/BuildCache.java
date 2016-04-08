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
package test;

import org.mmle.minecraft.io.cache.Cache;
import org.mmle.minecraft.io.cache.CacheBuilder;
import org.mmle.minecraft.io.cache.CacheBuilder.CacheBuildResults;
import org.mmle.minecraft.io.cache.CacheItem;
import org.mmle.minecraft.io.cache.CacheManager;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class BuildCache {
    public static final void main(String[] args) throws Exception {
        CacheBuildResults buildResults = CacheBuilder
                .<Integer, String>get() //获得缓存构建器
                .setLoader((key) -> {   //设置缓存条目加载器
                    System.out.println("load :" + key);
                    return Integer.toString(key);
                })
                .setFilter((key, item, maxSize, expectSize, currentSize) -> { //设置缓存条目过滤器
                    System.out.println("filtering :" + key);
                    return 1;
                })
                .setProcessor((key, item) -> { //设置缓存条目处理器
                    System.out.println("process  :" + key);
                    return false;
                })
                .build(3); //构建一个期望大小为3的缓存，并获得CacheBuildResults
        
        buildResults.makeCacheManagerWork(0, 100); //让缓存管理器开始工作，并设置垃圾回收器的工作周期
        
        Cache<Integer, String>        cache        = buildResults.getCache();    //从buildResults中获得缓存
        CacheManager<Integer, String> cacheManager = buildResults.getCacheManager(); //从buildResults中获得缓存管理器
        
        /*尝试预载缓存条目*/
        for(int i = 0; i < 100; i++)
            cache.tryLoadItem(i);  //尝试预载条目，如果指定条目已经被缓存或者无法被缓存或者缓存已慢将不会对其进行预载操作
        
        cacheManager.cleanout();   //手动启动一次垃圾回收，如果此处不启动可能会导致缓存溢出
        //Thread.sleep(1000);         //线程延迟执行，此处目的是为了等待缓存管理器中的垃圾回收器执行
        CacheItem item = cache.getItem(1); //获得指定位置的缓存条目，如果指定条目不存在将会自动加载指定条目，当然如果缓存已满将会报缓存溢出
        
        System.out.println(item.get());    //获得缓存条目中的对象
        
    }
}
