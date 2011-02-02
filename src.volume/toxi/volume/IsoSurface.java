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

package toxi.volume;

import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

/**
 * IsoSurface class based on C version by Paul Bourke and Lingo version by
 * myself.
 */
public interface IsoSurface {

    /**
     * Computes the surface mesh for the given iso value. An existing mesh
     * container can be reused (will be cleared) or created automatically (if
     * null). In the latter case a simple {@link TriangleMesh} instance is
     * created.
     * 
     * @param mesh
     *            existing mesh container or null
     * @param iso
     *            surface iso value
     * @return Mesh3D instance
     */
    public Mesh3D computeSurfaceMesh(Mesh3D mesh, final float iso);

    /**
     * Resets mesh vertices to default positions and clears face index. Needs to
     * be called inbetween successive calls to
     * {@link #computeSurfaceMesh(Mesh3D, float)}.
     */
    public void reset();
}
