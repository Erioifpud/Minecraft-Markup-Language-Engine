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
public class EntityImpl extends AbstractNBTMap implements Entity{
    public EntityImpl(String id) {
        super(new CompoundMap());
        if (id == null) {
            throw new NullPointerException();
        }
        setString(TAG_ID, id);
        setShort(TAG_FIRE, (short) -20);
        setShort(TAG_AIR, (short) 300);
        setBoolean(TAG_ON_GROUND, true);
        setPos(new double[] {0, 0, 0});
        setRot(new float[] {0, 0});
        setVel(new double[] {0, 0, 0});
    }

    protected EntityImpl(CompoundMap nbt) {
        super(nbt);
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

    public static EntityImpl fromNBT(CompoundMap entityNbt) {
        String id = ((StringTag) entityNbt.get(TAG_ID)).getValue();
        switch (id) {
            case ID_PLAYER:
                return new Player(entityNbt);
            case ID_VILLAGER:
                return new Villager(entityNbt);
            case ID_PAINTING:
                return new Painting(entityNbt);
            default:
                return new EntityImpl(entityNbt);
        }
    }

    private static final long serialVersionUID = 1L;
}