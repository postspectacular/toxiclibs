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

package toxi.geom.mesh2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import toxi.geom.PolygonD2D;
import toxi.geom.TriangleD2D;
import toxi.geom.VecD2D;

public class VoronoiD {

    public static double DEFAULT_SIZE = 10000;

    protected DelaunayTriangulationD delaunay;
    protected DelaunayTriangleD initialTriangle;
    protected List<VecD2D> sites = new ArrayList<VecD2D>();

    public VoronoiD() {
        this(DEFAULT_SIZE);
    }

    public VoronoiD(double size) {
        initialTriangle = new DelaunayTriangleD(
                new DelaunayVertexD(-size, -size), new DelaunayVertexD(size,
                        -size), new DelaunayVertexD(0, size));
        this.delaunay = new DelaunayTriangulationD(initialTriangle);
    }

    public void addPoint(VecD2D p) {
        sites.add(p.copy());
        delaunay.delaunayPlace(new DelaunayVertexD(p.x, p.y));
    }

    public void addPoints(Collection<? extends VecD2D> points) {
        for (VecD2D p : points) {
            addPoint(p);
        }
    }

    public List<PolygonD2D> getRegions() {
        List<PolygonD2D> regions = new LinkedList<PolygonD2D>();
        HashSet<DelaunayVertexD> done = new HashSet<DelaunayVertexD>(
                initialTriangle);
        for (DelaunayTriangleD triangle : delaunay) {
            for (DelaunayVertexD site : triangle) {
                if (done.contains(site)) {
                    continue;
                }
                done.add(site);
                List<DelaunayTriangleD> list = delaunay.surroundingTriangles(
                        site, triangle);
                PolygonD2D poly = new PolygonD2D();
                for (DelaunayTriangleD tri : list) {
                    DelaunayVertexD circumeter = tri.getCircumcenter();
                    poly.add(new VecD2D(circumeter.coord(0), circumeter.coord(1)));
                }
                regions.add(poly);
            }
        }
        return regions;
    }

    public List<VecD2D> getSites() {
        return sites;
    }

    public List<TriangleD2D> getTriangles() {
        List<TriangleD2D> tris = new ArrayList<TriangleD2D>();
        for (DelaunayTriangleD t : delaunay) {
            tris.add(new TriangleD2D(t.get(0).toVecD2D(), t.get(1).toVecD2D(), t
                    .get(2).toVecD2D()));
        }
        return tris;
    }
}
