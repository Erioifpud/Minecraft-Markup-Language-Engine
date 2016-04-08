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

import com.flowpowered.nbt.CompoundTag;
import java.io.IOException;
import org.mmle.minecraft.Block;
import org.mmle.minecraft.io.NBTFactory;
import org.mmle.minecraft.io.SchematicIO;
import org.mmle.minecraft.io.SchematicIO_V01;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class GetSchematic {
    
    static CompoundTag getSchematicData(String path) throws IOException {
        return (CompoundTag) NBTFactory.getGIPNbt(path);
    }
    
    static void setSchematicData(CompoundTag tag, String path) throws IOException {
        NBTFactory.outGIPNbt(tag, path);
    }
    
    public static final void main(String[] args) throws IOException {
        CompoundTag schematicData = getSchematicData("F:/test/schematic/Resources_GetSchematic_2.schematic");
        
        SchematicIO schematic = new SchematicIO_V01(schematicData.getValue());
        
        
        int blockType = schematic.readBlockType(0, 0, 0);
        
        System.out.println("blockType=" + blockType);
        System.out.println(Block.BLOCK_TYPE_NAMES[blockType]);
        
    }
    
}
