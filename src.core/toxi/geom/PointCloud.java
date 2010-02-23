package toxi.geom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import toxi.math.MathUtils;

public class PointCloud implements Iterable<Vec3D> {

    protected List<Vec3D> points;

    protected Vec3D min, max;
    protected Vec3D centroid;

    protected float radiusSquared;

    public PointCloud() {
        this(100);
    }

    public PointCloud(int numPoints) {
        points = new ArrayList<Vec3D>();
        clear();
    }

    public PointCloud addAll(List<Vec3D> plist) {
        for (Vec3D p : plist) {
            addPoint(p);
        }
        return this;
    }

    public PointCloud addPoint(Vec3D p) {
        points.add(p);
        min.minSelf(p);
        max.maxSelf(p);
        centroid.set(min.add(max).scaleSelf(0.5f));
        radiusSquared =
                MathUtils.max(radiusSquared, p.distanceToSquared(centroid));
        return this;
    }

    public PointCloud applyMatrix(Matrix4x4 m) {
        for (Vec3D p : points) {
            p.set(m.applyTo(p));
        }
        updateBounds();
        return this;
    }

    public PointCloud center() {
        getCentroid();
        for (Vec3D p : points) {
            p.subSelf(centroid);
        }
        min.subSelf(centroid);
        max.subSelf(centroid);
        centroid.clear();
        return this;
    }

    public PointCloud clear() {
        points.clear();
        min = Vec3D.MAX_VALUE.copy();
        max = Vec3D.MIN_VALUE.copy();
        centroid = new Vec3D();
        return this;
    }

    public PointCloud copy() {
        PointCloud c = new PointCloud(points.size());
        for (Vec3D p : points) {
            c.addPoint(p.copy());
        }
        return c;
    }

    public AABB getBoundingBox() {
        return AABB.fromMinMax(min, max);
    }

    public Sphere getBoundingSphere() {
        return new Sphere(getCentroid(), (float) Math.sqrt(radiusSquared));
    }

    public Vec3D getCentroid() {
        return centroid;
    }

    public Iterator<Vec3D> iterator() {
        return points.iterator();
    }

    public boolean removePoint(Vec3D p) {
        return points.remove(p);
    }

    public PointCloud updateBounds() {
        min = Vec3D.MAX_VALUE;
        max = Vec3D.MIN_VALUE;
        for (Vec3D p : points) {
            min.minSelf(p);
            max.maxSelf(p);
        }
        centroid.set(min.add(max).scaleSelf(0.5f));
        radiusSquared = 0;
        for (Vec3D p : points) {
            radiusSquared =
                    MathUtils.max(radiusSquared, p.distanceToSquared(centroid));
        }
        return this;
    }
}
