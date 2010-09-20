package toxi.volume;

import toxi.geom.AABB;
import toxi.geom.Triangle;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.Mesh3D;
import toxi.math.ScaleMap;

public class MeshVoxelizer {

    public static VolumetricSpace solidifyVolume(VolumetricSpaceArray volume) {
        for (int z = 0; z < volume.resZ; z++) {
            for (int y = 0; y < volume.resY; y++) {
                boolean isFilled = false;
                int startX = 0;
                for (int x = 0; x < volume.resX; x++) {
                    float val = volume.getVoxelAt(x, y, z);
                    if (val > 0) {
                        if (!isFilled) {
                            startX = x;
                            isFilled = true;
                        } else {
                            for (int i = startX; i <= x; i++) {
                                volume.setVoxelAt(i, y, z, 1);
                            }
                            isFilled = false;
                        }
                    }
                }
            }
        }
        return volume;
    }

    public static VolumetricSpace voxelizeMesh(Mesh3D mesh, int res) {
        return voxelizeMesh(mesh, res, res, res);
    }

    public static VolumetricSpace voxelizeMesh(Mesh3D mesh, int resX, int resY,
            int resZ) {
        AABB box = mesh.getBoundingBox();
        Vec3D bmin = box.getMin();
        Vec3D bmax = box.getMax();
        ScaleMap wx = new ScaleMap(bmin.x, bmax.x, 1, resX - 2);
        ScaleMap wy = new ScaleMap(bmin.y, bmax.y, 1, resY - 2);
        ScaleMap wz = new ScaleMap(bmin.z, bmax.z, 1, resZ - 2);
        ScaleMap gx = new ScaleMap(1, resX - 2, bmin.x, bmax.x);
        ScaleMap gy = new ScaleMap(1, resY - 2, bmin.y, bmax.y);
        ScaleMap gz = new ScaleMap(1, resZ - 2, bmin.z, bmax.z);
        VolumetricSpace volume =
                new VolumetricHashMap(box.getExtent().scale(2f), resX, resY,
                        resZ, 0.1f);
        Triangle tri = new Triangle();
        AABB voxel = new AABB(new Vec3D(), volume.voxelSize.scale(0.5f));
        for (Face f : mesh.getFaces()) {
            tri.a = f.a;
            tri.b = f.b;
            tri.c = f.c;
            AABB bounds = tri.getBoundingBox();
            Vec3D min = bounds.getMin();
            Vec3D max = bounds.getMax();
            min =
                    new Vec3D((int) wx.getClippedValueFor(min.x),
                            (int) wy.getClippedValueFor(min.y),
                            (int) wz.getClippedValueFor(min.z));
            max =
                    new Vec3D((int) wx.getClippedValueFor(max.x),
                            (int) wy.getClippedValueFor(max.y),
                            (int) wz.getClippedValueFor(max.z));
            for (int z = (int) min.z; z <= max.z; z++) {
                for (int y = (int) min.y; y <= max.y; y++) {
                    for (int x = (int) min.x; x <= max.x; x++) {
                        if (x < volume.resX1 && y < volume.resY1
                                && z < volume.resZ1) {
                            voxel.set((float) gx.getClippedValueFor(x),
                                    (float) gy.getClippedValueFor(y),
                                    (float) gz.getClippedValueFor(z));
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
