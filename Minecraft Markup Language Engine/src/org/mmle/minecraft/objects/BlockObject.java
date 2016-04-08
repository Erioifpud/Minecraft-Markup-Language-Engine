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

import org.mmle.minecraft.Direction;
import org.mmle.minecraft.Material;
import org.mmle.minecraft.TileEntity;
import org.mmle.util.Destroyable;

/**
 *
 * @author Bug 3050429487@qq.com
 */
public final class BlockObject implements Destroyable, MinecraftObject, WorldItem<Integer> {

    public BlockObject(WorldObject worldObject, int x, int y, int z, Material material) {
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.material = material;
        this.worldObject = worldObject;
        
        init_to_world();
    }
    
    private void init_to_world () {
        worldObject.addBlock(this);
    }
    
    public TileEntity getTileEntity() {
        return worldObject.getTileEntity(x, y, z);
    }
    
    public Direction getDirection() {
        return material.getDirection();
    }
    
    public void setDirection(Direction direction) {
        material = material.setDirection(direction);
    }
    
    public void rotate(int steps) {
        material = material.rotate(steps);
    }
    
    public void mirror(Direction axis) {
        material = material.mirror(axis);
    }
    
    public void invert() {
        material = material.invert();
    }
    
    public final int x, y, z;
    
    private Material material;
    private WorldObject worldObject;

    @Override
    public boolean destroy() {
        if(worldObject != null)
        {
            worldObject.removeBlock(this);
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
    public BlockObject to(WorldObject worldObject) {
        return to(worldObject, x, y, z);
    }

    @Override
    public BlockObject to(WorldObject worldObject, Integer x, Integer y, Integer z) {
        return new BlockObject(worldObject, x, y, z, material);
    }

    @Override
    public Integer[] getCoordInWorld() {
        return new Integer[] {x, y, z};
    }

    @Override
    public Integer getXCoordInWorld() {
        return x;
    }

    @Override
    public Integer getYCoordInWorld() {
        return y;
    }

    @Override
    public Integer getZCoordInWorld() {
        return z;
    }
    
}
