package toxi.newmesh;

import toxi.geom.Vec2D;
import toxi.util.datatypes.ItemIndex;

public class MeshUVCompiler extends MeshAttributeCompiler {

    @Override
    public void compileFace(AttributedFace f, ItemIndex<?> index, float[] buf,
            int offset) {
        int[] vn = f.attribs.get(IndexedTriangleMesh.ATTR_UVCOORDS);
        Vec2D v = (Vec2D) index.forID(vn[0]);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        v = (Vec2D) index.forID(vn[1]);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        v = (Vec2D) index.forID(vn[2]);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
    }

    @Override
    public ItemIndex<?> getIndex() {
        return mesh.attributes.get(IndexedTriangleMesh.ATTR_UVCOORDS);
    }

    @Override
    public int getStride() {
        return 2;
    }

}
