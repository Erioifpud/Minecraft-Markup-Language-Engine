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
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.mmle.minecraft.AbstractNBTMap;
import static org.mmle.minecraft.Constants.TAG_ID;
import static org.mmle.minecraft.Constants.TAG_MOTION;
import static org.mmle.minecraft.Constants.TAG_POS;
import static org.mmle.minecraft.Constants.TAG_ROTATION;
import org.mmle.minecraft.Entity;
import org.mmle.minecraft.EntityImpl;
import org.mmle.util.Destroyable;


/**
 *
 * @author Bug 3050429487@qq.com
 */
public final class EntityObject extends AbstractNBTMap implements Entity, Destroyable, MinecraftObject, WorldItem<Double>{
    
    public EntityObject(WorldObject worldObject, EntityImpl entity) {
        super(entity.getNBT());
        
        Objects.requireNonNull(worldObject);
        
        this.worldObject = worldObject;
        
        init_to_world();
    }
    
    private EntityObject(WorldObject worldObject, double x, double y, double z, CompoundMap nbt) {
        super(nbt.clone());
        
        setNewEntityObjectPos(nbt, x, y, z);
        this.worldObject = worldObject;
        
        init_to_world();
    }
    
    private static void setNewEntityObjectPos(CompoundMap nbt, double x, double y, double z) {
        List<Tag> list = new ArrayList<>(3);
        list.add(new DoubleTag(null, x));
        list.add(new DoubleTag(null, y));
        list.add(new DoubleTag(null, z));
        nbt.put(new ListTag(TAG_POS, DoubleTag.class, list));
    }
    
    private void init_to_world () {
        worldObject.addEntity(this);
    }
    
    @Override
    public final String getId() {
        return getString(TAG_ID);
    }

    @Override
    public final double[] getPos() {
        double[] list = getDoubleList(TAG_POS);
        return (list != null) ? list : new double[3];
    }

    @Override
    public final void setPos(double[] pos) {
        if (pos.length != 3) {
            throw new IllegalArgumentException();
        }
        setDoubleList(TAG_POS, pos);
    }

    @Override
    public final float[] getRot() {
        float[] list = getFloatList(TAG_ROTATION);
        return (list != null) ? list : new float[2];
    }

    @Override
    public final void setRot(float[] rot) {
        if (rot.length != 2) {
            throw new IllegalArgumentException();
        }
        setFloatList(TAG_ROTATION, rot);
    }

    @Override
    public final double[] getVel() {
        double[] list = getDoubleList(TAG_MOTION);
        return (list != null) ? list : new double[3];
    }

    @Override
    public final void setVel(double[] vel) {
        if (vel.length != 3) {
            throw new IllegalArgumentException();
        }
        setDoubleList(TAG_MOTION, vel);
    }
    
    public EntityImpl toEntityImpl() {
        return EntityImpl.fromNBT(toNBT());
    }
    
    public WorldObject getWorld() {
        return worldObject;
    }
    
    private WorldObject worldObject;

    @Override
    public boolean destroy() {
        if(worldObject != null)
        {
            worldObject.removeEntity(this);
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
        double[] coord = getPos();
        return to(worldObject, coord[0], coord[1], coord[2]);
    }

    @Override
    public WorldItem to(WorldObject worldObject, Double x, Double y, Double z) {
        CompoundMap tag = toNBT();
        return new EntityObject(worldObject, x, y, z, tag);
    }

    @Override
    public Double[] getCoordInWorld() {
        double[] pos = getPos();
        return new Double[] {pos[0], pos[1], pos[2]};
    }

    @Override
    public Double getXCoordInWorld() {
        double[] pos = getPos();
        return pos[0];
    }

    @Override
    public Double getYCoordInWorld() {
        double[] pos = getPos();
        return pos[1];
    }

    @Override
    public Double getZCoordInWorld() {
        double[] pos = getPos();
        return pos[2];
    }
    
}
