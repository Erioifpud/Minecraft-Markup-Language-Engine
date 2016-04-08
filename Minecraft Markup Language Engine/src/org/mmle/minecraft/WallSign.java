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
public class WallSign extends TileEntityImpl {
    public WallSign() {
        super(ID_SIGN);
    }

    public WallSign(CompoundMap tag) {
        super(tag);
    }
    
    public String[] getText() {
        return new String[] {getString(TAG_TEXT1), getString(TAG_TEXT2), getString(TAG_TEXT3), getString(TAG_TEXT4)};
    }
    
    public void setText(String... text) {
        setString(TAG_TEXT1, text[0]);
        setString(TAG_TEXT2, text[1]);
        setString(TAG_TEXT3, text[2]);
        setString(TAG_TEXT4, text[3]);
    }

    private static final long serialVersionUID = 1L;
}