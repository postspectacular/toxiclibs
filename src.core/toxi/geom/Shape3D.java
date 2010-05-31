package toxi.geom;

public interface Shape3D {

    /**
     * Checks if the point is within the given shape/volume.
     * 
     * @return true, if inside
     */
    boolean containsPoint(ReadonlyVec3D p);
}
