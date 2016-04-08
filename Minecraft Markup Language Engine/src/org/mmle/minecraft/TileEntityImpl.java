/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mmle.minecraft;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.StringTag;

import static org.mmle.minecraft.Constants.*;

/**
 *
 * @author pepijn
 */
public class TileEntityImpl extends AbstractNBTMap implements TileEntity{
    public TileEntityImpl(String id) {
        super(new CompoundMap());
        if (id == null) {
            throw new NullPointerException();
        }
        setString(TAG_ID, id);
    }

    protected TileEntityImpl(CompoundMap tag) {
        super(tag);
    }

    public final String getId() {
        return getString(TAG_ID);
    }

    public final int getX() {
        return getInt(TAG_X);
    }

    public final void setX(int x) {
        setInt(TAG_X, x);
    }

    public final int getY() {
        return getInt(TAG_Y);
    }

    public final void setY(int y) {
        setInt(TAG_Y, y);
    }

    public final int getZ() {
        return getInt(TAG_Z);
    }

    public final void setZ(int z) {
        setInt(TAG_Z, z);
    }

    public static TileEntityImpl fromNBT(CompoundMap tileEntityTag) {
        String id = ((StringTag) tileEntityTag.get(TAG_ID)).getValue();
        switch (id) {
            case ID_CHEST:
                return new Chest(tileEntityTag);
            case ID_SIGN:
                return new WallSign(tileEntityTag);
            default:
                return new TileEntityImpl(tileEntityTag);
        }
    }
    
    private static final long serialVersionUID = 1L;
}