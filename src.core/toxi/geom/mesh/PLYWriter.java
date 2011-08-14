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
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.util.FileUtils;

/**
 * Standard Polygon Format (PLY) mesh exporter for toxiclibs
 * {@link TriangleMesh} instances. Generates PLY binary format with optional
 * vertex normal export.
 */
public class PLYWriter {

    protected static final Logger logger = Logger.getLogger(PLYWriter.class
            .getSimpleName());

    private final byte[] buf = new byte[4];

    private boolean doWriteNormals;

    /**
     * Creates a little-endian version of the given float.
     * 
     * @param f
     * @return
     */
    private final byte[] le(float f) {
        return le(Float.floatToRawIntBits(f));
    }

    /**
     * Creates a little-endian version of the given int.
     * 
     * @param i
     * @return
     */
    private final byte[] le(int i) {
        buf[3] = (byte) (i >>> 24);
        buf[2] = (byte) (i >> 16 & 0xff);
        buf[1] = (byte) (i >> 8 & 0xff);
        buf[0] = (byte) (i & 0xff);
        return buf;
    }

    /**
     * Exports the given mesh to the specified {@link OutputStream}, including
     * required header information. The mesh data itself is stored in binary.
     * The output stream itself will be wrapped in a buffered version (128KB) in
     * order to improve write performance.
     * 
     * @param mesh
     *            mesh instance to export
     * @param stream
     *            valid output stream
     */
    public void saveMesh(TriangleMesh mesh, OutputStream stream) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(stream, 0x20000);
            out.write("ply\n".getBytes());
            out.write("format binary_little_endian 1.0\n".getBytes());
            out.write(("element vertex " + mesh.getNumVertices() + "\n")
                    .getBytes());
            out.write("property float x\n".getBytes());
            out.write("property float y\n".getBytes());
            out.write("property float z\n".getBytes());
            if (doWriteNormals) {
                out.write("property float nx\n".getBytes());
                out.write("property float ny\n".getBytes());
                out.write("property float nz\n".getBytes());
            }
            out.write(("element face " + mesh.getNumFaces() + "\n").getBytes());
            out.write("property list uchar uint vertex_indices\n".getBytes());
            out.write("end_header\n".getBytes());
            Vec3D[] verts = new Vec3D[mesh.getNumVertices()];
            float[] normals = mesh.getNormalsForUniqueVerticesAsArray();
            int i = 0, j = 0;
            for (Vec3D v : mesh.getVertices()) {
                verts[i++] = v;
            }
            try {
                for (i = 0, j = 0; i < verts.length; i++, j += 3) {
                    Vec3D v = verts[i];
                    out.write(le(v.x));
                    out.write(le(v.z));
                    out.write(le(v.y));
                    if (doWriteNormals) {
                        out.write(le(normals[j]));
                        out.write(le(normals[j + 2]));
                        out.write(le(-normals[j + 1]));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Face f : mesh.getFaces()) {
                out.write((byte) 3);
                out.write(le(f.a.id));
                out.write(le(f.b.id));
                out.write(le(f.c.id));
            }
            out.flush();
            out.close();
            logger.info(mesh.getNumFaces() + " faces written");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "error exporting mesh", e);
        }
    }

    /**
     * Exports the given mesh to the specified file path, including required
     * header information. The mesh data itself is stored in binary.
     * 
     * @param mesh
     *            mesh instance to export
     * @param path
     *            file path
     */
    public void saveMesh(TriangleMesh mesh, String path) {
        try {
            saveMesh(mesh, FileUtils.createOutputStream(new File(path)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "error exporting mesh", e);
        }
    }

    /**
     * @return true, if normal export is enabled.
     */
    public boolean writeNormals() {
        return doWriteNormals;
    }

    /**
     * Setter enable export of vertex normals (false by default).
     * 
     * @param doWriteNormals
     *            true to enable
     */
    public void writeNormals(boolean doWriteNormals) {
        this.doWriteNormals = doWriteNormals;
    }
}
