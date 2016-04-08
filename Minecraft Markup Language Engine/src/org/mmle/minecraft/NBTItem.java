/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mmle.minecraft;

import com.flowpowered.nbt.Tag;

/**
 *
 * @author pepijn
 */
public interface NBTItem {
    Tag getCurrentTag();
    
    Tag getCurrentTag(String name);
}