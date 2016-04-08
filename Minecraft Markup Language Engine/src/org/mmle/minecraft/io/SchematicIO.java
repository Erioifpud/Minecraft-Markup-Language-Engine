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

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import org.mmle.minecraft.NBTMap;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public interface SchematicIO extends WorldIO, NBTMap {
    
    public static final String SCHEMATIC_MATERIALS_NAME_ALPHA   = "Alpha";
    public static final String SCHEMATIC_MATERIALS_NAME_CLASSIC = "Classic";
    
    
    public int readWidth();
    
    public int readLength();
    
    public int readHeight();
    
    public String readMaterials();
    
    
    public void writeWidth(int width) throws BoundaryException;
    
    public void writeLength(int length) throws BoundaryException;
    
    public void writeHeigth(int heigth) throws BoundaryException;
    
    
    public CompoundMap toNBT();
    
    @Override
    public CompoundMap getNBT();
    
    
    public CompoundTag toTag();
    
    public CompoundTag toTag(String name);
    
    
    @Override
    public CompoundTag getCurrentTag();
    
    @Override
    public CompoundTag getCurrentTag(String name);
    
}
