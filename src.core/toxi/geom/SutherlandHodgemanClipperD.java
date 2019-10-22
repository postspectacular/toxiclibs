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
 * A version of the Sutherland-Hodgeman algorithm to clip 2D polygons optimized
 * for rectangular clipping regions.
 * 
 * More information: http://en.wikipedia.org/wiki/Sutherland-Hodgman_algorithm
 * 
 * @see ConvexPolygonClipper
 */
public class SutherlandHodgemanClipperD implements PolygonClipperD2D {

    protected RectD bounds;

    public SutherlandHodgemanClipperD(RectD bounds) {
        this.bounds = bounds;
    }

    public PolygonD2D clipPolygonD(PolygonD2D poly) {
        List<VecD2D> points = new ArrayList<VecD2D>(poly.vertices);
        List<VecD2D> clipped = new ArrayList<VecD2D>();
        points.add(points.get(0));
        for (int edgeID = 0; edgeID < 4; edgeID++) {
            clipped.clear();
            for (int i = 0, num = points.size() - 1; i < num; i++) {
                VecD2D p = points.get(i);
                VecD2D q = points.get(i + 1);
                if (isInsideEdge(p, edgeID)) {
                    if (isInsideEdge(q, edgeID)) {
                        clipped.add(q.copy());
                    } else {
                        clipped.add(getClippedPosOnEdge(edgeID, p, q));
                    }
                    continue;
                }
                if (isInsideEdge(q, edgeID)) {
                    clipped.add(getClippedPosOnEdge(edgeID, p, q));
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

    /**
     * @return the bounding rect
     */
    public RectD getBounds() {
        return bounds;
    }

    private final VecD2D getClippedPosOnEdge(int edgeID, VecD2D p1, VecD2D p2) {
        switch (edgeID) {
            case 0:
                return new VecD2D(p1.x + ((bounds.y - p1.y) * (p2.x - p1.x))
                        / (p2.y - p1.y), bounds.y);
            case 2:
                double by = bounds.y + bounds.height;
                return new VecD2D(p1.x + ((by - p1.y) * (p2.x - p1.x))
                        / (p2.y - p1.y), by);
            case 1:
                double bx = bounds.x + bounds.width;
                return new VecD2D(bx, p1.y + ((bx - p1.x) * (p2.y - p1.y))
                        / (p2.x - p1.x));

            case 3:
                return new VecD2D(bounds.x, p1.y
                        + ((bounds.x - p1.x) * (p2.y - p1.y)) / (p2.x - p1.x));
            default:
                return null;
        }
    }

    private final boolean isInsideEdge(VecD2D p, int edgeID) {
        switch (edgeID) {
            case 0:
                return p.y >= bounds.y;
            case 2:
                return p.y < bounds.y + bounds.height;
            case 3:
                return p.x >= bounds.x;
            case 1:
                return p.x < bounds.x + bounds.width;
            default:
                return false;
        }
    }

    protected boolean isKnownVertex(List<VecD2D> list, VecD2D q) {
        for (VecD2D p : list) {
            if (p.equalsWithTolerance(q, 0.0001f)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param bounds
     *            the bounding rect to set
     */
    public void setBounds(RectD bounds) {
        this.bounds = bounds;
    }
}
