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
import com.flowpowered.nbt.Tag;
import java.io.IOException;
import java.util.List;
import org.mmle.minecraft.AbstractNBTMap;
import static org.mmle.minecraft.Constants.TAG_ADDBLOCKS;
import static org.mmle.minecraft.Constants.TAG_BIOMES;
import static org.mmle.minecraft.Constants.TAG_BLOCKS;
import static org.mmle.minecraft.Constants.TAG_DATA;
import static org.mmle.minecraft.Constants.TAG_ENTITIES;
import static org.mmle.minecraft.Constants.TAG_HEIGHT;
import static org.mmle.minecraft.Constants.TAG_LENGTH;
import static org.mmle.minecraft.Constants.TAG_MATERIALS;
import static org.mmle.minecraft.Constants.TAG_SCHEMATIC;
import static org.mmle.minecraft.Constants.TAG_TILE_ENTITIES;
import static org.mmle.minecraft.Constants.TAG_WIDTH;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public class SchematicIO_V01 extends AbstractNBTMap implements SchematicIO{

    public SchematicIO_V01() {
        super(TAG_SCHEMATIC, new CompoundMap());
        setMaterisls();
    }
    
    public SchematicIO_V01(CompoundMap nbt) throws DataVersionException {
        super(nbt);
        checkVersion();
    }
    
    private void checkVersion() throws DataVersionException {
        if(!readMaterials().equals(SchematicIO.SCHEMATIC_MATERIALS_NAME_ALPHA))
            throw new DataVersionException(SchematicIO_V01.class, readMaterials());
    }
    
    
    private byte[] getBiomes() {
        return getByteArray(TAG_BIOMES);
    }
    
    private byte[] getBlocks() {
        return getByteArray(TAG_BLOCKS);
    }
    
    private byte[] getAdd() {
        return getByteArray(TAG_ADDBLOCKS);
    }
    
    private byte[] getData() {
        return getByteArray(TAG_DATA);
    }
    
    private List<Tag> getEntities() {
        return getList(TAG_ENTITIES);
    }
    
    private List<Tag> getTileEntities() {
        return getList(TAG_TILE_ENTITIES);
    }
    
    
    private int getWidth() {
        return getShort(TAG_WIDTH);
    }
    
    private int getLength() {
        return getShort(TAG_LENGTH);
    }
    
    private int getHeight() {
        return getShort(TAG_HEIGHT);
    }
    
    private String getMaterisls() {
        return getString(TAG_MATERIALS);
    }
    
    
    private int biomesOffset(int x, int z) {
        int length = getLength();
        int width = getWidth();
        
        return z * length * width + x;
    }
    
    private int biomesSize() {
        int length = getLength();
        int width = getWidth();
        
        
        return length * width;
    }
    
    
    
    private void setBiomes(byte[] biomes) {
        setByteArray(TAG_BIOMES, biomes);
    }
    
    private void setBlocks(byte[] blocks) {
        setByteArray(TAG_BLOCKS, blocks);
    }
    
    private void setAdd(byte[] add) {
        setByteArray(TAG_ADDBLOCKS, add);
    }
    
    private void setData(byte[] data) {
        setByteArray(TAG_DATA, data);
    }
    
    
    private void setWidth(int width) throws BoundaryException {
        checkBoundary("Width", width);
        setShort(TAG_WIDTH, (short) width);
    }
    
    private void setLength(int length) throws BoundaryException {
        checkBoundary("Length", length);
        setShort(TAG_LENGTH, (short) length);
    }
    
    private void setHeight(int height) throws BoundaryException {
        checkBoundary("Height", height);
        setShort(TAG_HEIGHT, (short) height);
    }
    
    
    private void setEntities(List<Tag> entities) {
        setList(TAG_ENTITIES, CompoundTag.class, entities);
    }
    
    private void setTileEntities(List<Tag> tileEntities) {
        setList(TAG_TILE_ENTITIES, CompoundTag.class, tileEntities);
    }
    
    private void setMaterisls() {
        setString(TAG_MATERIALS, SCHEMATIC_MATERIALS_NAME_ALPHA);
    }
    
    
    
    
    private void checkBoundary(String boundaryName, int size) throws BoundaryException {
        if(0 > size || size > Short.MAX_VALUE)
            throw new BoundaryException("Data writer \"" + getCurrentDataProcessorName() + "\" found \"" + boundaryName + "\" exception (boundarySize = " + size + ")");
    }
    
    private void checkBiomesValue(int value) {
        if(value > Byte.MAX_VALUE)
            throw new IllegalArgumentException("Data writer \" " + getCurrentDataProcessorName() + "\" found Illegal biomes parameters (" + value + ")");
    }
    
    private String getCurrentDataProcessorName() {
        return SchematicIO_V01.class.getName();
    }
    
    @Override
    public int readWidth() {
        return getWidth();
    }

    @Override
    public int readLength() {
        return getLength();
    }

    @Override
    public int readHeight() {
        return getHeight();
    }

    @Override
    public String readMaterials() {
        return getMaterisls();
    }

    
    
    @Override
    public void writeWidth(int width) throws BoundaryException {
        setWidth(width);
    }

    @Override
    public void writeLength(int length) throws BoundaryException {
        setLength(length);
    }

    @Override
    public void writeHeigth(int height) throws BoundaryException {
        setHeight(height);
    }

    @Override
    public int readBiome(int x, int z) {
        byte[] biomes = getBiomes();
        
        if(biomes == null)
            return -1;
        
        return biomes[biomesOffset(x, z)];
    }

    @Override
    public List<Tag> readEntities() {
        return getEntities();
    }

    @Override
    public List<Tag> readTileEntities() {
        return getTileEntities();
    }

    @Override
    public void writeBiome(int x, int z, int value) {
        checkBiomesValue(value);
        
        byte[] biomes = getBiomes();
        
        if(biomes == null)
        {
            biomes = new byte[biomesSize()];
            setBiomes(biomes);
        }
        
        biomes[biomesOffset(x, z)] = (byte) value;
    }

    @Override
    public void writeEntities(List<Tag> tag) {
        setEntities(tag);
    }

    @Override
    public void writeTileEntities(List<Tag> tag) {
        setTileEntities(tag);
    }

    
    
    private int blockTypeOffset(int x, int y, int z) {
        int width = getWidth();
        int height = getHeight();
        int length = getLength();
        
        int lw = length * width;
        
        return y * height * lw + z * lw + x;
    }
    
    private int blockDataOffset(int x, int y, int z) {
        return blockTypeOffset(x, y, z);
    }
    
    
    
    private int blockTypeSize() {
        int width = getWidth();
        int height = getHeight();
        int length = getLength();
        
        return width * height * length;
    }
    
    private int blockDataSize() {
        return blockTypeSize();
    }
    
    private int addBlockTypeSize() {
        int blockSize = blockTypeSize();
        
        return blockSize / 2 + (blockSize & 0x1);
    }
    
    
    @Override
    public int readBlockType(int x, int y, int z) {
        return readBlockId(x, y, z) + (readBlockAddId(x, y, z) << 8);
    }
    
    private int readBlockId(int x, int y, int z) {
        byte[] blocks = getBlocks();
        
        if(blocks == null)
            return 0;
        
        return blocks[blockTypeOffset(x, y, z)] & 255;
    }
    
    private int readBlockAddId(int x, int y, int z) {
        byte[] add = getAdd();
        
        if(add == null)
            return 0;
        
        //---------计算 addID的偏移量---------
        int blockOffset = blockTypeOffset(x, y, z);
        int addBlockOffset = blockOffset / 2;
        
        int newDataInByteOffset = (blockOffset + 1 /*因为schematic的add数组中每个字节的4位填充是从高位到低位填充，所以此处需要+1翻转*/ & 0x1) * 4;
        //-----------------------------------
        
        return (add[addBlockOffset] >> newDataInByteOffset) & 15;
    }

    @Override
    public int readBlockData(int x, int y, int z) {
        byte[] data = getData();
        
        if(data == null)
            return 0;
        
        return data[blockDataOffset(x, y, z)];
    }

    
    @Override
    public void writeBlockType(int x, int y, int z, int type) {
        writeBlockId(x, y, z, type);
        writeBlockAddId(x, y, z, type);
    }
    
    private void writeBlockId(int x, int y, int z, int type) {
        byte[] blocks = getBlocks();
        
        if(blocks == null) {
            blocks = new byte[blockTypeSize()];
            setBlocks(blocks);
        }
        
        blocks[blockTypeOffset(x, y, z)] = (byte) (type & 255);
    }
    
    private void writeBlockAddId(int x, int y, int z, int type) {
        if(type < 256)
            return;
        
        byte[] add = getAdd();
        
        if(add == null) {
            add = new byte[addBlockTypeSize()];
            setAdd(add);
        }
        
        //---------计算 addID的偏移量---------
        int blockOffset = blockTypeOffset(x, y, z);
        int addBlockOffset = blockOffset / 2;
        
        int newDataInByteOffset = (blockOffset + 1 /*因为schematic的add数组中每个字节的4位填充是从高位到低位填充，所以此处需要+1翻转*/ & 0x1) * 4;
        int srcDataInByteMask = newDataInByteOffset == 0 ? 15 : 240;
        //-----------------------------------
        
        byte addBlockType = add[addBlockOffset];
        
        addBlockType = (byte) ((addBlockType & srcDataInByteMask) | (type << newDataInByteOffset));
        
        add[addBlockOffset] = addBlockType;
    }
    

    @Override
    public void writeBlockData(int x, int y, int z, int data) {
        byte[] dataArray = getData();
        
        if(dataArray == null) {
            dataArray = new byte[blockDataSize()];
            setData(dataArray);
        }
        
        dataArray[blockDataOffset(x, y, z)] = (byte) (data & 0x255);
    }

    @Override
    public void close() throws IOException {
        
    }

    @Override
    public void flush() {
        
    }
    
    
    
    @Override
    public CompoundMap toNBT() {
        return super.toNBT();
    }
    
    @Override
    public CompoundMap getNBT() {
        return super.getNBT();
    }
    
    @Override
    public CompoundTag toTag() {
        return super.toTag();
    }
    
    @Override
    public CompoundTag toTag(String name) {
        return super.toTag(name);
    }
    
    @Override
    public CompoundTag getCurrentTag() {
        return super.getCurrentTag();
    }
    
    @Override
    public CompoundTag getCurrentTag(String name) {
        return super.getCurrentTag(name);
    }
    
    
    
}
