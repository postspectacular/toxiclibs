package toxi.newmesh;

import toxi.geom.Vec3D;
import toxi.util.datatypes.ItemIndex;

public class MeshFaceNormalCompiler extends MeshAttributeCompiler {

    @Override
    public void compileFace(AttributedFace f, ItemIndex<?> index, float[] buf,
            int offset) {
        Vec3D v = (Vec3D) index.forID(f.normal);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
    }

    @Override
    public ItemIndex<?> getIndex() {
        return mesh.fnormals;
    }

    @Override
    public int getStride() {
        return 3;
    }

}
