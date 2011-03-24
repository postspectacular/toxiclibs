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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import toxi.geom.Vec3D;
import toxi.util.FileUtils;

/**
 * Utility class to read binary STL files and turn them into
 * {@link TriangleMesh} instances.
 */
public class STLReader {

    public static final Class<? extends Mesh3D> TRIANGLEMESH = TriangleMesh.class;
    public static final Class<? extends Mesh3D> WEMESH = WETriangleMesh.class;

    private static final int DEFAULT_BUFFER_SIZE = 0x8000;

    private byte[] buf = new byte[12];

    private final float bufferToFloat() {
        return Float.intBitsToFloat(bufferToInt());
    }

    private final int bufferToInt() {
        return byteToInt(buf[0]) | (byteToInt(buf[1]) << 8)
                | (byteToInt(buf[2]) << 16) | (byteToInt(buf[3]) << 24);
    }

    private final int byteToInt(byte b) {
        return (b < 0 ? 256 + b : b);
    }

    /**
     * Attempts to load an STL model from the given {@link InputStream}.
     * Currently no exceptions are being thrown and the method will return null
     * if anything goes wrong during parsing the mesh data.
     * 
     * @param stream
     * @param meshName
     * @return mesh instance or null if unsuccessful
     */
    public Mesh3D loadBinary(InputStream stream, String meshName,
            Class<? extends Mesh3D> meshClass) {
        return loadBinary(stream, meshName, DEFAULT_BUFFER_SIZE, meshClass);
    }

    /**
     * Attempts to load an STL model from the given {@link InputStream}.
     * Currently no exceptions are being thrown and the method will return null
     * if anything goes wrong during parsing the mesh data.
     * 
     * @param stream
     * @param meshName
     * @param bufSize
     *            size of the stream buffer
     * @return mesh instance or null if unsuccessful
     */
    public Mesh3D loadBinary(InputStream stream, String meshName, int bufSize,
            Class<? extends Mesh3D> meshClass) {
        Mesh3D mesh = null;
        try {
            DataInputStream ds = new DataInputStream(new BufferedInputStream(
                    stream, bufSize));
            // read header, ignore color model
            for (int i = 0; i < 80; i++) {
                ds.read();
            }
            // read num faces
            ds.read(buf, 0, 4);
            int numFaces = bufferToInt();
            mesh = meshClass.newInstance();
            mesh.init(meshName, numFaces, numFaces);
            Vec3D a = new Vec3D();
            Vec3D b = new Vec3D();
            Vec3D c = new Vec3D();
            for (int i = 0; i < numFaces; i++) {
                // ignore face normal
                ds.read(buf, 0, 12);
                // face vertices
                readVector(ds, a);
                readVector(ds, b);
                readVector(ds, c);
                mesh.addFace(a, c, b);
                // ignore colour
                ds.read(buf, 0, 2);
            }
            mesh.computeVertexNormals();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mesh;
    }

    public Mesh3D loadBinary(String fileName, Class<? extends Mesh3D> meshClass) {
        return loadBinary(fileName, DEFAULT_BUFFER_SIZE, meshClass);
    }

    /**
     * Attempts to load an STL model from the given file path. Currently no
     * exceptions are being thrown and the method will return null if anything
     * goes wrong during parsing the mesh data.
     * 
     * @param fileName
     *            file path to read model from
     * @return mesh instance or null if unsuccessful
     */
    public Mesh3D loadBinary(String fileName, int bufSize,
            Class<? extends Mesh3D> meshClass) {
        Mesh3D mesh = null;
        try {
            mesh = loadBinary(FileUtils.createInputStream(new File(fileName)),
                    fileName.substring(fileName.lastIndexOf('/') + 1), bufSize,
                    meshClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mesh;
    }

    private Vec3D readVector(DataInputStream ds, Vec3D result)
            throws IOException {
        // x
        ds.read(buf, 0, 4);
        result.x = bufferToFloat();
        // y
        ds.read(buf, 0, 4);
        result.y = bufferToFloat();
        // z
        ds.read(buf, 0, 4);
        result.z = bufferToFloat();
        return result;
    }
}
