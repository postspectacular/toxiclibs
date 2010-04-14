package toxi.geom.mesh;

import toxi.geom.Vec3D;

public class Terrain {

    private float[] elevation;
    private Vec3D[] vertices;

    private int width;
    private int depth;
    private float scale;

    public Terrain(int w, int h, float scale) {
        this.width = w;
        this.depth = h;
        this.elevation = new float[width * depth];
        this.vertices = new Vec3D[width * depth];
        this.scale = scale;
        Vec3D offset = new Vec3D(width, 0, depth).scaleSelf(0.5f);
        for (int z = 0, i = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                vertices[i++] =
                        new Vec3D(x, 0, z).subSelf(offset).scaleSelf(scale);
            }
        }
    }

    public float getHeightAt(int x, int z) {
        return elevation[getIndex(x, z)];
    }

    protected int getIndex(int x, int z) {
        int idx = z * width + x;
        if (idx < 0 || idx > elevation.length) {
            throw new IndexOutOfBoundsException(
                    "the given terrain cell is invalid: " + x + ";" + z);
        }
        return idx;
    }

    public Vec3D getVertexAt(int x, int z) {
        return vertices[getIndex(x, z)];
    }

    public void setElevation(float[] elevation) {
        if (this.elevation.length == elevation.length) {
            for (int i = 0; i < elevation.length; i++) {
                this.vertices[i].y = this.elevation[i] = elevation[i];
            }
        } else {
            throw new IllegalArgumentException(
                    "the given elevation array size does not match terrain");
        }
    }

    public void setHeightAt(int x, int z, float h) {
        int index = getIndex(x, z);
        elevation[index] = h;
        vertices[index].y = h;
    }

    public TriangleMesh toMesh() {
        TriangleMesh mesh =
                new TriangleMesh("terrain", vertices.length,
                        vertices.length * 2);
        for (int z = 1; z < depth; z++) {
            for (int x = 1; x < width; x++) {
                mesh.addFace(vertices[(z - 1) * width + (x - 1)],
                        vertices[(z - 1) * width + x], vertices[z * width
                                + (x - 1)]);
                mesh.addFace(vertices[(z - 1) * width + x], vertices[z * width
                        + x], vertices[z * width + (x - 1)]);
            }
        }
        return mesh;
    }

    public TriangleMesh toMesh(float groundLevel) {
        TriangleMesh mesh = toMesh();
        Vec3D offset = new Vec3D(width, 0, depth).scaleSelf(0.5f);
        float minX = -offset.x * scale;
        float minZ = -offset.z * scale;
        float maxX = (width - 1 - offset.x) * scale;
        float maxZ = (depth - 1 - offset.z) * scale;
        for (int z = 1; z < depth; z++) {
            Vec3D a = new Vec3D(minX, groundLevel, (z - 1 - offset.z) * scale);
            Vec3D b = new Vec3D(minX, groundLevel, (z - offset.z) * scale);
            // left
            mesh.addFace(getVertexAt(0, z - 1), getVertexAt(0, z), a);
            mesh.addFace(getVertexAt(0, z), b, a);
            // right
            a.x = b.x = maxX;
            mesh.addFace(getVertexAt(width - 1, z), getVertexAt(width - 1,
                    z - 1), b);
            mesh.addFace(getVertexAt(width - 1, z - 1), a, b);
        }
        for (int x = 1; x < width; x++) {
            Vec3D a = new Vec3D((x - 1 - offset.x) * scale, groundLevel, minZ);
            Vec3D b = new Vec3D((x - offset.x) * scale, groundLevel, minZ);
            // back
            mesh.addFace(getVertexAt(x, 0), getVertexAt(x - 1, 0), b);
            mesh.addFace(getVertexAt(x - 1, 0), a, b);
            // front
            a.z = b.z = maxZ;
            mesh.addFace(getVertexAt(x - 1, depth - 1), getVertexAt(x,
                    depth - 1), a);
            mesh.addFace(getVertexAt(x, depth - 1), b, a);
        }
        // bottom plane
        mesh.addFace(new Vec3D(minX, groundLevel, minZ), new Vec3D(minX,
                groundLevel, maxZ), new Vec3D(maxX, groundLevel, minZ));
        mesh.addFace(new Vec3D(maxX, groundLevel, minZ), new Vec3D(minX,
                groundLevel, maxZ), new Vec3D(maxZ, groundLevel, maxZ));
        return mesh;
    }
}
