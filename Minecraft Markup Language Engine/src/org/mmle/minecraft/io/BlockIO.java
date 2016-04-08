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

/**
 *
 * @author Bug 3050429487@qq.com
 */
public interface BlockIO extends Import, Export {
    public int readBlockType(int x, int y, int z);
    
    public int readBlockData(int x, int y, int z);
    
    public void writeBlockType(int x, int y, int z, int type);
    
    public void writeBlockData(int x, int y, int z, int data);
}
