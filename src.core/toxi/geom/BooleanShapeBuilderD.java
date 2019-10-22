package toxi.geom;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class BooleanShapeBuilderD {

    public enum Type {
        UNION,
        INTERSECTION,
        DIFFERENCE,
        XOR;
    }

    private int bezierRes;

    private final Area area;
    private final Type type;

    public BooleanShapeBuilderD(Type type) {
        this(type, 8);
    }

    public BooleanShapeBuilderD(Type type, int bezierRes) {
        this.type = type;
        this.bezierRes = bezierRes;
        area = new Area();
    }

    public BooleanShapeBuilderD addShape(ShapeD2D s) {
        return combineWithArea(new Area(convertToAWTShape(s)));
    }

    public BooleanShapeBuilderD combineWithArea(Area a) {
        switch (type) {
            case UNION:
                area.add(a);
                break;
            case INTERSECTION:
                area.intersect(a);
                break;
            case DIFFERENCE:
                area.subtract(a);
                break;
            case XOR:
                area.exclusiveOr(a);
                break;
        }
        return this;
    }

    public List<PolygonD2D> computeShapes() {
        List<PolygonD2D> shapes = new ArrayList<PolygonD2D>();
        PathIterator i = area.getPathIterator(null);
        double[] buf = new double[6];
        VecD2D prev = new VecD2D();
        PolygonD2D s = null;
        while (!i.isDone()) {
            int id = i.currentSegment(buf);
            switch (id) {
                case PathIterator.SEG_MOVETO:
                    s = new PolygonD2D();
                    shapes.add(s);
                    prev.set(buf[0], buf[1]);
                    s.add(prev.copy());
                    break;
                case PathIterator.SEG_LINETO:
                    prev.set(buf[0], buf[1]);
                    s.add(prev.copy());
                    break;
                case PathIterator.SEG_CUBICTO:
                    VecD2D pa = new VecD2D(buf[0], buf[1]);
                    VecD2D pb = new VecD2D(buf[2], buf[3]);
                    VecD2D pc = new VecD2D(buf[4], buf[5]);
                    for (int t = 0; t <= bezierRes; t++) {
                        s.add(BezierCurveD2D.computePointInSegment(prev, pa, pb,
                                pc,  t / bezierRes));
                    }
                    prev.set(pc);
                    break;
                case PathIterator.SEG_CLOSE:
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Unsupported path segment type: " + id);
            }
            i.next();
        }
        return shapes;
    }

    private Shape convertToAWTShape(ShapeD2D s) {
        if (s instanceof RectD) {
            RectD r = (RectD) s;
            return new Rectangle2D.Double(r.x, r.y, r.width, r.height);
        }
        if (s instanceof TriangleD2D) {
            TriangleD2D t = (TriangleD2D) s;
            Path2D path = new Path2D.Float();
            path.moveTo(t.a.x, t.a.y);
            path.lineTo(t.b.x, t.b.y);
            path.lineTo(t.c.x, t.c.y);
            path.closePath();
            return path;
        }
        if (s instanceof EllipseD) {
            EllipseD e = (EllipseD) s;
            VecD2D r = e.getRadii();
            return new Ellipse2D.Double(e.x - r.x, e.y - r.y, r.x * 2, r.y * 2);
        }
        if (!(s instanceof PolygonD2D)) {
            s = s.toPolygonD2D();
        }
        PolygonD2D poly = (PolygonD2D) s;
        Path2D path = new Path2D.Float();
        VecD2D p = poly.get(0);
        path.moveTo(p.x, p.y);
        for (int i = 1, num = poly.getNumVertices(); i < num; i++) {
            p = poly.get(i);
            path.lineTo(p.x, p.y);
        }
        path.closePath();
        return path;
    }

    public Area getArea() {
        return area;
    }
}
