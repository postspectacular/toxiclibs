package toxi.newmesh;

import toxi.geom.Vec3D;
import toxi.util.datatypes.ItemIndex;

public class MeshVertexCompiler extends MeshAttributeCompiler {

    @Override
    public void compileFace(AttributedFace f, ItemIndex<?> index, float[] buf,
            int offset) {
        Vec3D v = (Vec3D) index.forID(f.a);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        v = (Vec3D) index.forID(f.b);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        v = (Vec3D) index.forID(f.c);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
    }

    @Override
    public ItemIndex<?> getIndex() {
        return mesh.vertices;
    }

    @Override
    public int getStride() {
        return 3;
    }

}
