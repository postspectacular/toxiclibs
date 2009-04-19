package toxi.geom;

public interface DimensionalVector {
	/**
	 * Returns the number of dimensions of this vector.
	 * 
	 * @return dimensions
	 */
	public int getDimensions();

	/**
	 * Returns the vector components as float[] array.
	 * 
	 * @return array
	 */
	public float[] toArray();
}
