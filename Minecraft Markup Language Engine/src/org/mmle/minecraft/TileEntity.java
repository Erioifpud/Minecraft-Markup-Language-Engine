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
package org.mmle.minecraft;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public interface TileEntity extends NBTItem{
    public String getId();

    public int getX();

    public void setX(int x);

    public int getY();

    public void setY(int y);

    public int getZ();

    public void setZ(int z);
}
