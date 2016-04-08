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


/**
 *
 * @author Bug 3050429487@qq.com
 */
public interface WorldObject extends MinecraftObject{
    public BlockObject getBlock(int x, int y, int z);
    
    public EntityObject getEntity(int x, int y, int z);
    
    public TileEntityObject getTileEntity(int x, int y, int z);
    
    
    void putEntity(double x, double y, double z, EntityObject entity);
    
    void putTileEntity(int x, int y, int z, TileEntityObject tileEntity);
    
    
    void removeBlock(BlockObject block);
    
    void removeEntity(EntityObject entity);
    
    void removeTileEntity(TileEntityObject tileEntity);
    
    
    void addBlock(BlockObject block);
    
    void addEntity(EntityObject entity);
    
    void addTileEntity(TileEntityObject tileEntity);
    
}
