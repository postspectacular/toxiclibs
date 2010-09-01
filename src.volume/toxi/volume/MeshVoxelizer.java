package toxi.volume;

import toxi.geom.AABB;
import toxi.geom.Matrix4x4;
import toxi.geom.Triangle;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.Mesh3D;

public class MeshVoxelizer {

    public static VolumetricSpace voxelizeMesh(Mesh3D mesh, int resX, int resY,
            int resZ) {
        mesh.center(null);
        AABB box = mesh.getBoundingBox();
        Vec3D scale = box.getExtent();
        AABB volBox = new AABB(box, scale.scale(1.5f));
        VolumetricSpaceArray volume =
                new VolumetricSpaceArray(volBox.getExtent().scale(2), resX,
                        resY, resZ);
        Triangle tri = new Triangle();
        AABB voxel = new AABB(new Vec3D(), volume.voxelSize.scale(0.5f));
        Matrix4x4 matWorld2Grid =
                new Matrix4x4().scaleSelf(volume.voxelSize.getReciprocal())
                        .translateSelf(volBox.getMin().getInverted());
        Matrix4x4 matGrid2World = matWorld2Grid.getInverted();
        for (Face f : mesh.getFaces()) {
            tri.a = f.a;
            tri.b = f.b;
            tri.c = f.c;
            AABB bounds = tri.getBoundingBox();
            Vec3D min = matWorld2Grid.applyTo(bounds.getMin());
            Vec3D max = matWorld2Grid.applyTo(bounds.getMax());
            for (int z = (int) min.z; z <= max.z; z++) {
                for (int y = (int) min.y; y <= max.y; y++) {
                    for (int x = (int) min.x; x <= max.x; x++) {
                        if (x < volume.resX && y < volume.resY
                                && z < volume.resZ) {
                            voxel.set(matGrid2World.applyToSelf(new Vec3D(x, y,
                                    z)));
                            if (voxel.intersectsTriangle(tri)) {
                                volume.setVoxelAt(x, y, z, 1);
                            }
                        }
                    }
                }
            }
        }
        return volume;
    }
}
