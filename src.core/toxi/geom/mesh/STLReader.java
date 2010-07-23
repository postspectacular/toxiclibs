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
    public TriangleMesh loadBinary(InputStream stream, String meshName) {
        return loadBinary(stream, meshName, DEFAULT_BUFFER_SIZE);
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
    public TriangleMesh loadBinary(InputStream stream, String meshName,
            int bufSize) {
        TriangleMesh mesh = null;
        try {
            DataInputStream ds =
                    new DataInputStream(
                            new BufferedInputStream(stream, bufSize));
            // read header, ignore color model
            for (int i = 0; i < 80; i++) {
                ds.read();
            }
            // read num faces
            ds.read(buf, 0, 4);
            int numFaces = bufferToInt();
            mesh = new TriangleMesh(meshName, numFaces, numFaces);
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
        }
        return mesh;
    }

    public TriangleMesh loadBinary(String fileName) {
        return loadBinary(fileName, DEFAULT_BUFFER_SIZE);
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
    public TriangleMesh loadBinary(String fileName, int bufSize) {
        TriangleMesh mesh = null;
        try {
            mesh =
                    loadBinary(FileUtils.createInputStream(new File(fileName)),
                            fileName.substring(fileName.lastIndexOf('/') + 1),
                            bufSize);
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
