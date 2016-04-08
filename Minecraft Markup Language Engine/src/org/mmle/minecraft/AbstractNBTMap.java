/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mmle.minecraft;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.FloatTag;
import com.flowpowered.nbt.IntArrayTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.ShortTag;
import com.flowpowered.nbt.StringTag;
import com.flowpowered.nbt.Tag;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A data structure based on an NBT tag or hierarchy of tags.
 *
 * @author pepijn
 */
public abstract class AbstractNBTMap implements NBTMap, Serializable, Cloneable {
    
    protected AbstractNBTMap(CompoundTag nbt) {
        this(nbt.getName(), nbt.getValue());
    }
    
    protected AbstractNBTMap(CompoundMap nbt) {
        Objects.requireNonNull(nbt);
        this.nbt = nbt;
    }

    protected AbstractNBTMap(String name, CompoundMap nbt) {
        Objects.requireNonNull(nbt);
        this.nbt = nbt;
        this.name = name;
    }
    
    protected CompoundMap toNBT() {
        return nbt;
    }
    
    @Override
    public CompoundMap getNBT() {
        return toNBT().clone();
    }
    
    
    private static final WeakReference<CompoundTag> NULL_TAG_WEAK_REFERENCE = new WeakReference(null);
    
    private WeakReference<CompoundTag> defaultCompoundTag = NULL_TAG_WEAK_REFERENCE;
    
    protected CompoundTag toTag() {
        
        if(defaultCompoundTag.get() == null)
        {
            CompoundTag compoundTag = new CompoundTag(this.name, this.nbt);
            defaultCompoundTag = new WeakReference(compoundTag);
        }
        
        return defaultCompoundTag.get();
    }
    
    protected CompoundTag toTag(String name) {
        return new CompoundTag(name, nbt);
    }
    
    
    @Override
    public CompoundTag getCurrentTag() {
        return toTag().clone();
    }
    
    @Override
    public CompoundTag getCurrentTag(String name) {
        return toTag(name).clone();
    }
    
    
    protected final boolean containsTag(String name) {
        return nbt.contains(name);
    }

    protected final long getLong(String name) {
        return getLong(name, 0L);
    }

    protected final long getLong(String name, long defaultValue) {
        LongTag longTag = (LongTag) nbt.get(name);
        return (longTag != null) ? longTag.getValue() : defaultValue;
    }

    protected final void setLong(String name, long value) {
        nbt.put(name, new LongTag(name, value));
    }

    protected final int getInt(String name) {
        return getInt(name, 0);
    }

    protected final int getInt(String name, int defaultValue) {
        IntTag intTag = (IntTag) nbt.get(name);
        return (intTag != null) ? intTag.getValue() : defaultValue;
    }

    protected final void setInt(String name, int value) {
        nbt.put(name, new IntTag(name, value));
    }

    protected final String getString(String name) {
        return getString(name, null);
    }

    protected final String getString(String name, String defaultValue) {
        StringTag stringTag = (StringTag) nbt.get(name);
        return (stringTag != null) ? stringTag.getValue() : defaultValue;
    }

    protected final void setString(String name, String value) {
        if (value != null) {
            nbt.put(name, new StringTag(name, value));
        } else {
            nbt.put(name, null);
        }
    }

    protected final short getShort(String name) {
        return getShort(name, (short) 0);
    }

    protected final short getShort(String name, short defaultValue) {
        ShortTag shortTag = (ShortTag) nbt.get(name);
        return (shortTag != null) ? shortTag.getValue() : defaultValue;
    }

    protected final void setShort(String name, short value) {
        nbt.put(name, new ShortTag(name, value));
    }

    protected final byte getByte(String name) {
        return getByte(name, (byte) 0);
    }

    protected final byte getByte(String name, byte defaultValue) {
        ByteTag byteTag = (ByteTag) nbt.get(name);
        return (byteTag != null) ? byteTag.getValue() : defaultValue;
    }

    protected final void setByte(String name, byte value) {
        nbt.put(name, new ByteTag(name, value));
    }

    protected final boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    protected final boolean getBoolean(String name, boolean defaultValue) {
        ByteTag byteTag = (ByteTag) nbt.get(name);
        return (byteTag != null) ? (byteTag.getValue() != 0) : defaultValue;
    }

    protected final void setBoolean(String name, boolean value) {
        nbt.put(name, new ByteTag(name, value ? (byte) 1 : (byte) 0));
    }
    
    protected final float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    protected final float getFloat(String name, float defaultValue) {
        FloatTag floatTag = (FloatTag) nbt.get(name);
        return (floatTag != null) ? floatTag.getValue() : defaultValue;
    }
    
    protected final void setFloat(String name, float value) {
        nbt.put(name, new FloatTag(name, value));
    }
    
    protected final double getDouble(String name) {
        return getDouble(name, 0.0);
    }

    protected final double getDouble(String name, double defaultValue) {
        DoubleTag doubleTag = (DoubleTag) nbt.get(name);
        return (doubleTag != null) ? doubleTag.getValue() : defaultValue;
    }
    
    protected final void setDouble(String name, double value) {
        nbt.put(name, new DoubleTag(name, value));
    }

    protected final <T extends Tag> List<T> getList(String name) {
        ListTag listTag = (ListTag) nbt.get(name);
        return (listTag != null) ? (List<T>) listTag.getValue() : null;
    }

    protected final <T extends Tag> void setList(String name, Class<T> type, List<Tag> list) {
        nbt.put(name, new ListTag(name, type, list));
    }
    
    protected final double[] getDoubleList(String name) {
        List<DoubleTag> list = getList(name);
        if (list != null) {
            double[] array = new double[list.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = list.get(i).getValue();
            }
            return array;
        } else {
            return null;
        }
    }

    protected final void setDoubleList(String name, double[] values) {
        List<Tag> list = new ArrayList<>(values.length);
        for (double value : values) {
            list.add(new DoubleTag(null, value));
        }
        nbt.put(name, new ListTag(name, DoubleTag.class, list));
    }

    protected final float[] getFloatList(String name) {
        List<FloatTag> list = getList(name);
        if (list != null) {
            float[] array = new float[list.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = list.get(i).getValue();
            }
            return array;
        } else {
            return null;
        }
    }

    protected final void setFloatList(String name, float[] values) {
        List<Tag> list = new ArrayList<>(values.length);
        for (float value : values) {
            list.add(new FloatTag(null, value));
        }
        nbt.put(name, new ListTag(name, FloatTag.class, list));
    }

    protected final byte[] getByteArray(String name) {
        ByteArrayTag byteArrayTag = (ByteArrayTag) nbt.get(name);
        return (byteArrayTag != null) ? byteArrayTag.getValue() : null;
    }

    protected final void setByteArray(String name, byte[] bytes) {
        nbt.put(name, new ByteArrayTag(name, bytes));
    }

    protected final int[] getIntArray(String name) {
        IntArrayTag intArrayTag = (IntArrayTag) nbt.get(name);
        return (intArrayTag != null) ? intArrayTag.getValue() : null;
    }

    protected final void setIntArray(String name, int[] values) {
        nbt.put(name, new IntArrayTag(name, values));
    }

    @Override
    public String toString() {
        return nbt.toString();
    }
    
    @Override
    public AbstractNBTMap clone() {
        try {
            AbstractNBTMap clone = (AbstractNBTMap) super.clone();
            clone.nbt = new CompoundMap(nbt);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    private CompoundMap nbt;
    private String name;
    
    private static final long serialVersionUID = 1L;
}