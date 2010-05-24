package toxi.geom;

public interface Shape2D {

    /**
     * Checks if the point is within the given shape.
     * 
     * @return true, if inside
     */
    boolean containsPoint(ReadonlyVec2D p);

    /**
     * Computes the area of the shape.
     * 
     * @return area
     */
    float getArea();

    /**
     * Computes the shape's circumference.
     * 
     * @return circumference
     */
    float getCircumference();
}
