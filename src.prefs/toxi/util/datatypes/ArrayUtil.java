package toxi.util.datatypes;

import java.util.Random;

/**
 * A collection of array utilities.
 */
public class ArrayUtil {

	/**
	 * Rearranges the array items in random order. Operation is in-place, no
	 * copy is created.
	 * 
	 * @param array
	 */
	public static void shuffle(Object[] array) {
		shuffle(array, new Random());
	}

	/**
	 * Rearranges the array items in random order using the given RNG. Operation
	 * is in-place, no copy is created.
	 * 
	 * @param array
	 * @param rnd
	 */
	public static void shuffle(Object[] array, Random rnd) {
		int N = array.length;
		for (int i = 0; i < N; i++) {
			int r = i + (int) (rnd.nextFloat() * (N - i)); // between i and N-1
			Object swap = array[i];
			array[i] = array[r];
			array[r] = swap;
		}
	}
}
