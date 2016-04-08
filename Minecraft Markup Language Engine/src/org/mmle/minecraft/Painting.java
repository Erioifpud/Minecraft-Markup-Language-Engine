/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mmle.minecraft;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import static org.mmle.minecraft.Constants.*;

/**
 *
 * @author pepijn
 */
public class Painting extends EntityImpl {
    public Painting() {
        super(ID_PAINTING);
    }

    public Painting(CompoundMap nbt) {
        super(nbt);
    }
    
    public byte getDir() {
        return getByte(TAG_DIR);
    }
    
    public void setDir(byte dir) {
        setByte(TAG_DIR, dir);
    }
    
    public String getMotive() {
        return getString(TAG_MOTIVE);
    }
    
    public void setMotive(String motive) {
        setString(TAG_MOTIVE, motive);
    }
    
    public int getTileX() {
        return getInt(TAG_TILE_X);
    }
    
    public void setTileX(int tileX) {
        setInt(TAG_TILE_X, tileX);
    }
    
    public int getTileY() {
        return getInt(TAG_TILE_Y);
    }
    
    public void setTileY(int tileY) {
        setInt(TAG_TILE_Y, tileY);
    }
    
    public int getTileZ() {
        return getInt(TAG_TILE_Z);
    }
    
    public void setTileZ(int tileZ) {
        setInt(TAG_TILE_Z, tileZ);
    }
    
    private static final long serialVersionUID = 1L;
}