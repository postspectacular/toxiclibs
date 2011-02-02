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

package toxi.geom.mesh;

import toxi.geom.Plane;
import toxi.geom.Plane.Classifier;

/**
 * A {@link VertexSelector} implementation for selecting vertices in relation to
 * a given {@link Plane}. Using a plane {@link Classifier} vertices can be
 * selected either: on the plane, in front or behind. A tolerance for this check
 * can be given too.
 * 
 */
public class PlaneSelector extends VertexSelector {

    public Plane plane;
    public float tolerance;
    public Classifier classifier;

    public PlaneSelector(Mesh3D mesh, Plane plane, Plane.Classifier classifier) {
        this(mesh, plane, classifier, 0.0001f);
    }

    public PlaneSelector(Mesh3D mesh, Plane plane, Plane.Classifier classifier,
            float tolerance) {
        super(mesh);
        this.plane = plane;
        this.classifier = classifier;
        this.tolerance = tolerance;
    }

    @Override
    public VertexSelector selectVertices() {
        clearSelection();
        for (Vertex v : mesh.getVertices()) {
            if (plane.classifyPoint(v, tolerance) == classifier) {
                selection.add(v);
            }
        }
        return this;
    }
}
