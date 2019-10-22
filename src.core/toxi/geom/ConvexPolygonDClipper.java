/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * A more generic version of the Sutherland-Hodgeman algorithm to limit 2D
 * polygons to convex clipping regions. Uses the clipping region's centroid and
 * {@link Line2D#classifyPoint(ReadonlyVecD2D)} to identify if an edge needs to
 * be clipped or not.
 * 
 * More information: http://en.wikipedia.org/wiki/Sutherland-Hodgman_algorithm
 * 
 * @see SutherlandHodgemanClipper
 * @since 0021
 */
public class ConvexPolygonDClipper implements PolygonClipperD2D {

    protected PolygonD2D bounds;
    protected VecD2D boundsCentroid;

    public ConvexPolygonDClipper(PolygonD2D bounds) {
        setBounds(bounds);
    }

    public PolygonD2D clipPolygonD(PolygonD2D poly) {
        List<VecD2D> points = new ArrayList<VecD2D>(poly.vertices);
        List<VecD2D> clipped = new ArrayList<VecD2D>();
        points.add(points.get(0));
        for (LineD2D clipEdge : bounds.getEdges()) {
            clipped.clear();
            double sign = clipEdge.classifyPoint(boundsCentroid);
            for (int i = 0, num = points.size() - 1; i < num; i++) {
                VecD2D p = points.get(i);
                VecD2D q = points.get(i + 1);
                if (clipEdge.classifyPoint(p) == sign) {
                    if (clipEdge.classifyPoint(q) == sign) {
                        clipped.add(q.copy());
                    } else {
                        clipped.add(getClippedPosOnEdge(clipEdge, p, q));
                    }
                    continue;
                }
                if (clipEdge.classifyPoint(q) == sign) {
                    clipped.add(getClippedPosOnEdge(clipEdge, p, q));
                    clipped.add(q.copy());
                }
            }
            if (clipped.size() > 0
                    && clipped.get(0) != clipped.get(clipped.size() - 1)) {
                clipped.add(clipped.get(0));
            }
            List<VecD2D> t = points;
            points = clipped;
            clipped = t;
        }
        return new PolygonD2D(points).removeDuplicates(0.001f);
    }

    public PolygonD2D getBounds() {
        return bounds;
    }

    protected VecD2D getClippedPosOnEdge(LineD2D clipEdge, VecD2D p, VecD2D q) {
        return clipEdge.intersectLine(new LineD2D(p, q)).getPos();
    }

    protected boolean isKnownVertex(List<VecD2D> list, VecD2D q) {
        for (VecD2D p : list) {
            if (p.equalsWithTolerance(q, 0.001f)) {
                return true;
            }
        }
        return false;
    }

    public void setBounds(PolygonD2D bounds) {
        this.bounds = bounds;
        this.boundsCentroid = bounds.getCentroid();
    }
}
