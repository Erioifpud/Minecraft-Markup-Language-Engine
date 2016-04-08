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
import org.mmle.minecraft.io.BoundaryException;
import org.mmle.minecraft.io.NBTFactory;
import org.mmle.minecraft.io.SchematicIO;
import org.mmle.minecraft.io.SchematicIO_V01;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class DumpSchematic {
    static CompoundTag getSchematicData(String path) throws IOException {
        return (CompoundTag) NBTFactory.getGIPNbt(path);
    }
    
    static void setSchematicData(CompoundTag tag, String path) throws IOException {
        NBTFactory.outGIPNbt(tag, path);
    }
    
    static void dumpBoundary(SchematicIO target, SchematicIO src) {
        try {
            target.writeWidth  (src.readWidth());
            target.writeHeigth (src.readHeight());
            target.writeLength (src.readLength());
        } catch (BoundaryException ex) {
            throw new InternalError();
        }
    }
    
    static void dumpBiomes(SchematicIO target, SchematicIO src) {
        for(int x = 0; x < src.readWidth(); x++)
            for(int z = 0; z < src.readLength(); z++)
                target.writeBiome(x, z, src.readBiome(x, z));
    }
    
    static void dumpBlocks(SchematicIO target, SchematicIO src) {
        for(int x = 0; x < src.readWidth(); x++)
            for(int y = 0; y < src.readHeight(); y++)
                for(int z = 0; z < src.readLength(); z++)
                {
                    target.writeBlockType(x, y, z, src.readBlockType(x, y, z));
                    target.writeBlockData(x, y, z, src.readBlockData(x, y, z));
                }
    }
    
    static void dumpEntities(SchematicIO target, SchematicIO src) {
        target.writeEntities(src.readEntities());
    }
    
    static void dumpTileEntities(SchematicIO target, SchematicIO src) {
        target.writeTileEntities(src.readTileEntities());
    }
    
    public static final void main(String[] args) throws IOException, BoundaryException {
        String dir = "F:/test/schematic/";
        
        String schematicInFilePath  = dir + "Resources_DumpSchematic_In.schematic";
        String schematicOutFilePath = dir + "Resources_DumpSchematic_Out.schematic";
        
        CompoundTag schematicData = getSchematicData(schematicInFilePath);
        
        SchematicIO schematicIn = new SchematicIO_V01(schematicData.getValue());
        SchematicIO schematicOut = new SchematicIO_V01();
        
        dumpBoundary    (schematicOut, schematicIn);
        dumpBiomes      (schematicOut, schematicIn);
        dumpBlocks      (schematicOut, schematicIn);
        dumpEntities    (schematicOut, schematicIn);
        dumpTileEntities(schematicOut, schematicIn);
        
        setSchematicData(schematicOut.toTag(), schematicOutFilePath);
    }
}
