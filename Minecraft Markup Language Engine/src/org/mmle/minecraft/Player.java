/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mmle.minecraft;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.Tag;
import java.util.Collections;


import static org.mmle.minecraft.Constants.*;

/**
 *
 * @author pepijn
 */
public class Player extends Mob {
    public Player() {
        super(ID_PLAYER);
        setList(TAG_INVENTORY, CompoundTag.class, Collections.<Tag>emptyList());
        setInt(TAG_SCORE, 0);
        setInt(TAG_DIMENSION, 0);
    }

    public Player(CompoundMap nbt) {
        super(nbt);
    }

    private static final long serialVersionUID = 1L;
}