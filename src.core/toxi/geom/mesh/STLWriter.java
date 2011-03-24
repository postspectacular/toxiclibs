/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.geom.mesh;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import toxi.geom.Vec3D;

/**
 * A simple, but flexible and memory efficient exporter for binary STL files.
 * Custom color support is implemented via the STLcolorModel interface and the
 * exporter comes with the 2 most common format variations defined by the
 * DEFAULT and MATERIALISE constants.
 * 
 * The minimal design of this exporter means it does not build an extra list of
 * faces in RAM and so is able to easily export models with millions of faces.
 * 
 * http://en.wikipedia.org/wiki/STL_(file_format)
 */
public class STLWriter {

    protected static final Logger logger = Logger.getLogger(STLWriter.class
            .getName());

    public static final int DEFAULT_RGB = -1;

    public static final STLColorModel DEFAULT = new DefaultSTLColorModel();

    public static final STLColorModel MATERIALISE = new MaterialiseSTLColorModel(
            0xffffffff);

    public static final int DEFAULT_BUFFER = 0x10000;

    protected OutputStream ds;
    protected byte[] buf = new byte[4];
    protected int bufferSize;

    protected Vec3D scale = new Vec3D(1, 1, 1);
    protected boolean useInvertedNormals = false;

    protected STLColorModel colorModel;

    public STLWriter() {
        this(DEFAULT, DEFAULT_BUFFER);
    }

    public STLWriter(STLColorModel cm, int bufSize) {
        colorModel = cm;
        this.bufferSize = bufSize;
    }

    public void beginSave(OutputStream stream, int numFaces) {
        logger.info("starting to save STL data to output stream...");
        try {
            ds = new BufferedOutputStream(new DataOutputStream(stream),
                    bufferSize);
            writeHeader(numFaces);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void beginSave(String fn, int numFaces) {
        logger.info("saving mesh to: " + fn);
        try {
            beginSave(new FileOutputStream(fn), numFaces);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void endSave() {
        try {
            ds.flush();
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void face(Vec3D a, Vec3D b, Vec3D c) {
        face(a, b, c, DEFAULT_RGB);
    }

    public void face(Vec3D a, Vec3D b, Vec3D c, int rgb) {
        Vec3D normal = b.sub(a).crossSelf(c.sub(a)).normalize();
        if (useInvertedNormals) {
            normal.invert();
        }
        face(a, b, c, normal, rgb);
    }

    public void face(Vec3D a, Vec3D b, Vec3D c, Vec3D normal, int rgb) {
        try {
            writeVector(normal);
            // vertices
            writeScaledVector(a);
            writeScaledVector(b);
            writeScaledVector(c);
            // vertex attrib (color)
            if (rgb != DEFAULT_RGB) {
                writeShort(colorModel.formatRGB(rgb));
            } else {
                writeShort(colorModel.getDefaultRGB());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final void prepareBuffer(int a) {
        buf[3] = (byte) (a >>> 24);
        buf[2] = (byte) (a >> 16 & 0xff);
        buf[1] = (byte) (a >> 8 & 0xff);
        buf[0] = (byte) (a & 0xff);
    }

    public void setScale(float s) {
        scale.set(s, s, s);
    }

    public void setScale(Vec3D s) {
        scale.set(s);
    }

    public void useInvertedNormals(boolean state) {
        useInvertedNormals = state;
    }

    protected void writeFloat(float a) throws IOException {
        prepareBuffer(Float.floatToRawIntBits(a));
        ds.write(buf, 0, 4);
    }

    protected void writeHeader(int num) throws IOException {
        byte[] header = new byte[80];
        colorModel.formatHeader(header);
        ds.write(header, 0, 80);
        writeInt(num);
    }

    protected void writeInt(int a) throws IOException {
        prepareBuffer(a);
        ds.write(buf, 0, 4);
    }

    protected void writeScaledVector(Vec3D v) {
        try {
            writeFloat(v.x * scale.x);
            writeFloat(v.y * scale.y);
            writeFloat(v.z * scale.z);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeShort(int a) throws IOException {
        buf[0] = (byte) (a & 0xff);
        buf[1] = (byte) (a >> 8 & 0xff);
        ds.write(buf, 0, 2);
    }

    protected void writeVector(Vec3D v) {
        try {
            writeFloat(v.x);
            writeFloat(v.y);
            writeFloat(v.z);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
