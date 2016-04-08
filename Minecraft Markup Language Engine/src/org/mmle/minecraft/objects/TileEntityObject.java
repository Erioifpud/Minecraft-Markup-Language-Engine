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
package org.mmle.minecraft.objects;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.IntTag;
import java.util.Objects;
import org.mmle.minecraft.AbstractNBTMap;
import static org.mmle.minecraft.Constants.TAG_ID;
import static org.mmle.minecraft.Constants.TAG_X;
import static org.mmle.minecraft.Constants.TAG_Y;
import static org.mmle.minecraft.Constants.TAG_Z;
import org.mmle.minecraft.TileEntity;
import org.mmle.minecraft.TileEntityImpl;
import org.mmle.util.Destroyable;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public final class TileEntityObject extends AbstractNBTMap implements TileEntity, Destroyable, MinecraftObject, WorldItem<Integer>{
    
    public TileEntityObject(WorldObject worldObject, TileEntityImpl tileEntity) {
        super(tileEntity.getNBT());
        
        Objects.requireNonNull(worldObject);
        
        this.worldObject = worldObject;
        
        init_to_world();
    }
    
    private TileEntityObject(WorldObject worldObject, int x, int y, int z, CompoundMap nbt) {
        super(nbt.clone());
        
        setNewEntityObjectPos(nbt, x, y, z);
        this.worldObject = worldObject;
        
        init_to_world();
    }
    
    private static void setNewEntityObjectPos(CompoundMap nbt, int x, int y, int z) {
        nbt.put(new IntTag(TAG_X, x));
        nbt.put(new IntTag(TAG_Y, y));
        nbt.put(new IntTag(TAG_Z, z));
    }
    
    private void init_to_world () {
        worldObject.addTileEntity(this);
    }
    
    @Override
    public String getId() {
        return getString(TAG_ID);
    }

    @Override
    public int getX() {
        return getInt(TAG_X);
    }

    @Override
    public void setX(int x) {
        setInt(TAG_X, x);
    }

    @Override
    public int getY() {
        return getInt(TAG_Y);
    }

    @Override
    public void setY(int y) {
        setInt(TAG_Y, y);
    }

    @Override
    public int getZ() {
        return getInt(TAG_Z);
    }

    @Override
    public void setZ(int z) {
        setInt(TAG_Z, z);
    }
    
    public TileEntityImpl toTileEntityImpl() {
        return TileEntityImpl.fromNBT(toNBT());
    } 
    
    public WorldObject getWorld() {
        return worldObject;
    }
    
    private WorldObject worldObject;

    @Override
    public boolean destroy() {
        if(worldObject != null)
        {
            worldObject.removeTileEntity(this);
            worldObject = null;
            return true;
        }
        
        return false;
    }

    @Override
    public boolean destroyed() {
        return worldObject == null;
    }

    @Override
    public WorldItem to(WorldObject worldObject) {
        return TileEntityObject.this.to(worldObject, getX(), getY(), getZ());
    }

    @Override
    public WorldItem to(WorldObject worldObject, Integer x, Integer y, Integer z) {
        CompoundMap nbt = toNBT();
        return new TileEntityObject(worldObject, x, y, z, nbt);
    }

    @Override
    public Integer[] getCoordInWorld() {
        return new Integer[] {getXCoordInWorld(), getYCoordInWorld(), getZCoordInWorld()};
    }

    @Override
    public Integer getXCoordInWorld() {
        return getX();
    }

    @Override
    public Integer getYCoordInWorld() {
        return getY();
    }

    @Override
    public Integer getZCoordInWorld() {
        return getZ();
    }
    
}
