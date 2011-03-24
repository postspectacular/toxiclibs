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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import toxi.geom.Vec3D;

/**
 * Applies a laplacian smooth function to all vertices in the mesh
 * 
 */
public class LaplacianSmooth implements WEMeshFilterStrategy {

    public void filter(VertexSelector selector, int numIterations) {
        final Collection<Vertex> selection = selector.getSelection();
        if (!(selector.getMesh() instanceof WETriangleMesh)) {
            throw new IllegalArgumentException(
                    "This filter requires a WETriangleMesh");
        }
        final WETriangleMesh mesh = (WETriangleMesh) selector.getMesh();
        final HashMap<Vertex, Vec3D> filtered = new HashMap<Vertex, Vec3D>(
                selection.size());
        for (int i = 0; i < numIterations; i++) {
            filtered.clear();
            for (Vertex v : selection) {
                final Vec3D laplacian = new Vec3D();
                final List<WEVertex> neighbours = ((WEVertex) v).getNeighbors();
                for (WEVertex n : neighbours) {
                    laplacian.addSelf(n);
                }
                laplacian.scaleSelf(1f / neighbours.size());
                filtered.put(v, laplacian);
            }
            for (Vertex v : filtered.keySet()) {
                mesh.vertices.get(v).set(filtered.get(v));
            }
            mesh.rebuildIndex();
        }
        mesh.computeFaceNormals();
        mesh.computeVertexNormals();
    }

    public void filter(WETriangleMesh mesh, int numIterations) {
        filter(new DefaultSelector(mesh).selectVertices(), numIterations);
    }
}
