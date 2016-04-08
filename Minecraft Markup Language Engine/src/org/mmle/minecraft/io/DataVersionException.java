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
package org.mmle.minecraft.io;

import java.io.IOException;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class DataVersionException extends IOException {
    public DataVersionException() {
        // Do nothing
    }

    public DataVersionException(String string) {
        super(string);
    }

    public DataVersionException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public DataVersionException(Throwable thrwbl) {
        super(thrwbl);
    }
    
    public DataVersionException(String dataLoader, String errorDataVersion) {
        super("Data Loader \"" + dataLoader + "\" Unable to load version of \"" + errorDataVersion + "\" data");
    }
    
    public DataVersionException(Class dataLoaderClass, String errorDataVersion) {
        this(dataLoaderClass.getName(), errorDataVersion);
    }
}
