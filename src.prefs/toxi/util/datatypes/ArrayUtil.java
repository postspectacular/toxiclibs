package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.Random;

/**
 * A collection of array utilities.
 */
public class ArrayUtil {

	/**
	 * Rearranges the array items in random order using the default
	 * java.util.Random generator. Operation is in-place, no copy is created.
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

	/**
	 * Reverses the item order of the supplied array.
	 * 
	 * @param array
	 */
	public static void reverse(Object[] array) {
		int len = array.length - 1;
		int len2 = array.length / 2;
		for (int i = 0; i < len2; i++) {
			Object tmp = array[i];
			array[i] = array[len - i];
			array[len - i] = tmp;
		}
	}

	public static void reverse(byte[] array) {
		int len = array.length - 1;
		int len2 = array.length / 2;
		for (int i = 0; i < len2; i++) {
			byte tmp = array[i];
			array[i] = array[len - i];
			array[len - i] = tmp;
		}
	}

	public static void reverse(short[] array) {
		int len = array.length - 1;
		int len2 = array.length / 2;
		for (int i = 0; i < len2; i++) {
			short tmp = array[i];
			array[i] = array[len - i];
			array[len - i] = tmp;
		}
	}

	public static void reverse(char[] array) {
		int len = array.length - 1;
		int len2 = array.length / 2;
		for (int i = 0; i < len2; i++) {
			char tmp = array[i];
			array[i] = array[len - i];
			array[len - i] = tmp;
		}
	}

	public static void reverse(int[] array) {
		int len = array.length - 1;
		int len2 = array.length / 2;
		for (int i = 0; i < len2; i++) {
			int tmp = array[i];
			array[i] = array[len - i];
			array[len - i] = tmp;
		}
	}

	public static void reverse(float[] array) {
		int len = array.length - 1;
		int len2 = array.length / 2;
		for (int i = 0; i < len2; i++) {
			float tmp = array[i];
			array[i] = array[len - i];
			array[len - i] = tmp;
		}
	}

	public static ArrayList arrayToList(Object[] array) {
		ArrayList list = new ArrayList(array.length);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
}
