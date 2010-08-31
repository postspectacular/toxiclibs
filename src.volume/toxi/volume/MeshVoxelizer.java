package toxi.volume;

import toxi.geom.AABB;
import toxi.geom.Triangle;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.Mesh3D;

public class MeshVoxelizer {

    public static VolumetricSpace voxelizeMesh(Mesh3D mesh, int resX, int resY,
            int resZ) {
        Vec3D scale = mesh.getBoundingBox().getExtent();
        Vec3D volScale =
                scale.scale(2 + 2f / resX, 2 + 2f / resY, 2 + 2f / resZ);
        VolumetricSpaceArray volume =
                new VolumetricSpaceArray(volScale, resX + 2, resY + 2, resZ + 2);
        Triangle tri = new Triangle();
        AABB voxel =
                new AABB(new Vec3D(), scale.scale(1f / resX, 1f / resY,
                        1f / resZ));
        for (int z = 1; z < volume.resZ1; z++) {
            for (int y = 1; y < volume.resY1; y++) {
                for (int x = 1; x < volume.resX1; x++) {
                    Vec3D pos =
                            new Vec3D(x - 1, y - 1, z - 1).scaleSelf(
                                    volume.voxelSize).subSelf(volume.halfScale);
                    voxel.set(pos);
                    for (Face f : mesh.getFaces()) {
                        tri.a = f.a;
                        tri.b = f.b;
                        tri.c = f.c;
                        if (voxel.intersectsTriangle(tri)) {
                            volume.setVoxelAt(x, y, z, 1);
                        }
                    }
                }
            }
        }
        return volume;
    }
}
