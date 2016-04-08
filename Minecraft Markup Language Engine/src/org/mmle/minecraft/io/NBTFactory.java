/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mmle.minecraft.io;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.stream.NBTInputStream;
import com.flowpowered.nbt.stream.NBTOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 *
 * @author TRP-2b1148669896
 */
public final class NBTFactory {
    
    private NBTFactory() {}
    
    public static final Tag getGIPNbt(String path) throws IOException {
        return getGIPNbt(new File(path));
    }
    
    public static final Tag getGIPNbt(File file) throws IOException {
        return getGIPNbt(new FileInputStream(file));
    }
    
    public static Tag getGIPNbt(InputStream is) throws IOException {
        try (NBTInputStream nbtIs = new NBTInputStream(new GZIPInputStream(is))) {
            return nbtIs.readTag();
        }
    }
    
    public static final Tag readGIPNbt(InputStream is) throws IOException {
        NBTInputStream nbtIs = new NBTInputStream(new GZIPInputStream(is));
        return nbtIs.readTag();
    }
    
    
    public static final Tag getZIPNbt(String path) throws IOException {
        return getZIPNbt(new File(path));
    }
    
    public static final Tag getZIPNbt(File file) throws IOException {
        return getZIPNbt(new FileInputStream(file));
    }
    
    public static Tag getZIPNbt(InputStream is) throws IOException {
        try (NBTInputStream nbtIs = new NBTInputStream(new InflaterInputStream(is))) {
            return nbtIs.readTag();
        }
    }
    
    public static final Tag readZIPNbt(InputStream is) throws IOException {
        NBTInputStream nbtIs = new NBTInputStream(new InflaterInputStream(is));
        return nbtIs.readTag();
    }
    
    
    public static final Tag getNbt(String path) throws IOException {
        return getNbt(new File(path));
    }
    
    public static final Tag getNbt(File file) throws IOException {
        return getNbt(new FileInputStream(file));
    }
    
    public static Tag getNbt(InputStream is) throws IOException {
        try (NBTInputStream nbtIs = new NBTInputStream(is)) {
            return nbtIs.readTag();
        }
    }
    
    public static final Tag readNbt(InputStream is) throws IOException {
        NBTInputStream nbtIs = new NBTInputStream(is);
        return nbtIs.readTag();
    }
    
    
    //============================
    
    public static final void outGIPNbt(Tag tag, String path) throws IOException {
        outGIPNbt(tag, new File(path));
    }
    
    public static final void outGIPNbt(Tag tag, File file) throws IOException {
        outGIPNbt(tag, new FileOutputStream(file));
    }
    
    public static void outGIPNbt(Tag tag, OutputStream os) throws IOException {
        try (NBTOutputStream nbtOut = new NBTOutputStream(new GZIPOutputStream(os))) {
            nbtOut.writeTag(tag);
            nbtOut.flush();
        }
    }
    
    public static final void writeGIPNbt(Tag tag, OutputStream os) throws IOException {
        NBTOutputStream nbtOut = new NBTOutputStream(new GZIPOutputStream(os));
        nbtOut.writeTag(tag);
        nbtOut.flush();
    }
    
    
    public static final void outZIPNbt(Tag tag, String path) throws IOException {
        outZIPNbt(tag, new File(path));
    }
    
    public static final void outZIPNbt(Tag tag, File file) throws IOException {
        outZIPNbt(tag, new FileOutputStream(file));
    }
    
    private static void outZIPNbt(Tag tag, OutputStream os) throws IOException {
        try (NBTOutputStream nbtOut = new NBTOutputStream(new DeflaterOutputStream(os))) {
            nbtOut.writeTag(tag);
            nbtOut.flush();
        }
    }
    
    public static final void writeZIPNbt(Tag tag, OutputStream os) throws IOException {
        NBTOutputStream nbtOut = new NBTOutputStream(new DeflaterOutputStream(os));
        nbtOut.writeTag(tag);
        nbtOut.flush();
    }
    
    
    public static final void outNbt(Tag tag, String path) throws IOException {
        outNbt(tag, new File(path));
    }
    
    public static final void outNbt(Tag tag, File file) throws IOException {
        outNbt(tag, new FileOutputStream(file));
    }
    
    private static void outNbt(Tag tag, OutputStream os) throws IOException {
        try (NBTOutputStream nbtOut = new NBTOutputStream(os)) {
            nbtOut.writeTag(tag);
            nbtOut.flush();
        }
    }
    
    public static final void writeNbt(Tag tag, OutputStream os) throws IOException {
        NBTOutputStream nbtOut = new NBTOutputStream(os);
        nbtOut.writeTag(tag);
        nbtOut.flush();
    }
    
}
