package toxi.geom.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import toxi.geom.Vec3D;

/**
 * Utility class to read binary STL files and turn them into
 * {@link TriangleMesh} instances.
 */
public class STLReader {

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
        TriangleMesh mesh = null;
        try {
            DataInputStream ds = new DataInputStream(stream);
            // read header, ignore color model
            for (int i = 0; i < 80; i++) {
                ds.read();
            }
            // read num faces
            ds.read(buf, 0, 4);
            int numFaces = bufferToInt();
            mesh = new TriangleMesh(meshName);
            for (int i = 0; i < numFaces; i++) {
                // ignore face normal
                ds.read(buf, 0, 12);
                // face vertices
                Vec3D a = readVector(ds);
                Vec3D b = readVector(ds);
                Vec3D c = readVector(ds);
                mesh.addFace(a, b, c);
                // ignore colour
                ds.read(buf, 0, 2);
            }
            mesh.computeVertexNormals();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mesh;
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
    public TriangleMesh loadBinary(String fileName) {
        TriangleMesh mesh = null;
        try {
            mesh =
                    loadBinary(new FileInputStream(fileName), fileName
                            .substring(fileName.lastIndexOf('/') + 1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mesh;
    }

    private Vec3D readVector(DataInputStream ds) throws IOException {
        // x
        ds.read(buf, 0, 4);
        float x = bufferToFloat();
        // y
        ds.read(buf, 0, 4);
        float y = bufferToFloat();
        // z
        ds.read(buf, 0, 4);
        float z = bufferToFloat();
        return new Vec3D(x, y, z);
    }
}
