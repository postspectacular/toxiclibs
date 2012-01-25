package toxi.newmesh;

import toxi.util.datatypes.ItemIndex;

public abstract class MeshAttributeCompiler {

    protected IndexedTriangleMesh mesh;

    public abstract void compileFace(AttributedFace f, ItemIndex<?> index,
            float[] buf, int offset);

    public abstract ItemIndex<?> getIndex();

    public abstract int getStride();

    public void setMesh(IndexedTriangleMesh mesh) {
        this.mesh = mesh;
    }
}
