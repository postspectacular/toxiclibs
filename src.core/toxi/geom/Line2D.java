package toxi.geom;

import toxi.geom.Line2D.LineIntersection.Type;

public class Line2D {

    public static class LineIntersection {

        public static enum Type {
            COINCIDENT, PARALLEL, NON_INTERSECTING, INTERSECTING
        }

        protected final Type type;
        protected final Vec2D pos;

        public LineIntersection(Type type, Vec2D pos) {
            this.type = type;
            this.pos = pos;
        }

        /**
         * @return the pos
         */
        public Vec2D getPos() {
            return pos;
        }

        /**
         * @return the type
         */
        public Type getType() {
            return type;
        }

        public String toString() {
            return "type: " + type + " pos: " + pos;
        }
    }

    public Vec2D a, b;

    public Line2D(Vec2D a, Vec2D b) {
        this.a = a;
        this.b = b;
    }

    public Line2D copy() {
        return new Line2D(a.copy(), b.copy());
    }

    public LineIntersection intersectLine(Line2D l) {
        LineIntersection isec = null;
        float denom =
                (l.b.y - l.a.y) * (b.x - a.x) - (l.b.x - l.a.x) * (b.y - a.y);

        float na =
                (l.b.x - l.a.x) * (a.y - l.a.y) - (l.b.y - l.a.y)
                        * (a.x - l.a.x);
        float nb = (b.x - a.x) * (a.y - l.a.y) - (b.y - a.y) * (a.x - l.a.x);

        if (denom != 0.0) {
            float ua = na / denom;
            float ub = nb / denom;
            if (ua >= 0.0f && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
                isec =
                        new LineIntersection(Type.INTERSECTING, a
                                .interpolateTo(b, ua));
            } else {
                isec = new LineIntersection(Type.NON_INTERSECTING, null);
            }
        } else {
            if (na == 0.0 && nb == 0.0) {
                isec = new LineIntersection(Type.COINCIDENT, null);
            } else {
                isec = new LineIntersection(Type.COINCIDENT, null);
            }
        }
        return isec;
    }
}
