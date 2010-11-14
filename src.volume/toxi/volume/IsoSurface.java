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
