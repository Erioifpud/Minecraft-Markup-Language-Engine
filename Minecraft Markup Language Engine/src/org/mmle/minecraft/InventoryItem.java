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
public class InventoryItem extends AbstractNBTMap {
    public InventoryItem() {
        super(new CompoundMap());
    }

    public InventoryItem(int type, int damage, int count, int slot) {
        this();
        setType(type);
        setDamage(damage);
        setCount(count);
        setSlot(slot);
    }

    public InventoryItem(CompoundMap nbt) {
        super(nbt);
    }
    
    public InventoryItem(CompoundTag tag) {
        super(tag);
    }

    public final int getCount() {
        return getByte(TAG_COUNT);
    }

    public final void setCount(int count) {
        setByte(TAG_COUNT, (byte) count);
    }

    public final int getDamage() {
        return getShort(TAG_DAMAGE);
    }

    public final void setDamage(int damage) {
        setShort(TAG_DAMAGE, (short) damage);
    }

    public final int getSlot() {
        return getByte(TAG_SLOT);
    }

    public final void setSlot(int slot) {
        setByte(TAG_SLOT, (byte) slot);
    }

    public final int getType() {
        return getShort(TAG_ID);
    }

    public final void setType(int type) {
        setShort(TAG_ID, (short) type);
    }

    private static final long serialVersionUID = 1L;
}