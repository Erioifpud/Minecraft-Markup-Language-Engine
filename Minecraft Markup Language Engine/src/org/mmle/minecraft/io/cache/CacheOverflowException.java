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

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class CacheOverflowException extends Exception{
    public CacheOverflowException() {
        // Do nothing
    }

    public CacheOverflowException(String string) {
        super(string);
    }

    public CacheOverflowException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public CacheOverflowException(Throwable thrwbl) {
        super(thrwbl);
    }
    
    public CacheOverflowException(int maxCacheSize, int needSize) {
        super("maximum size of the cache is " + maxCacheSize + ". size of the need is " + needSize);
    }
}
