package toxi.image.colour;

import java.util.Iterator;
import java.util.TreeSet;

import toxi.math.MathUtils;

public class ARGBGradient {

	private class GradPoint implements Comparable {
		float pos;

		int col;

		GradPoint(float p, int c) {
			pos = p;
			col = c;
		}

		public int compareTo(Object o) {
			GradPoint p = (GradPoint) o;
			if (p.pos == pos)
				return 0;
			return pos < p.pos ? -1 : 1;
		}
	}

	private TreeSet gradient;

	private float maxDither;

	public ARGBGradient() {
		gradient = new TreeSet();
	}

	// add a new colour at specified position
	public void addColorAt(float p, int c) {
		gradient.add(new GradPoint(p, c));
	}

	// generic linear interpolation
	int mix(int a, int b, float f) {
		return (int) (a + (b - a) * f);
	}

	// blend 2 colours
	int blend(int a, int b, float f) {
		return mix(a >>> 24, b >>> 24, f) << 24
				| mix((a >> 16) & 0xff, (b >> 16) & 0xff, f) << 16
				| mix((a >> 8) & 0xff, (b >> 8) & 0xff, f) << 8
				| mix(a & 0xff, b & 0xff, f);
	}

	// calculate number of steps of gradient from specified position (only
	// values >0)
	public int[] calcGradient(float pos, int steps) {

		if (gradient.size() == 0)
			return new int[0];

		int[] result = new int[steps];
		float frac;
		GradPoint currPoint = null;
		GradPoint nextPoint = null;
		float endPos = pos + steps;
		// find 1st colour needed, clamp start position to positive values only
		// pos = (pos > -1) ? pos : 0;
		Iterator iter = gradient.iterator();
		while (iter.hasNext()) {
			GradPoint gp = (GradPoint) iter.next();
			if (gp.pos <= pos) {
				currPoint = gp;
			}
		}
		boolean isPremature = currPoint == null;
		TreeSet activeGradient = null;
		if (!isPremature) {
			activeGradient = (TreeSet) gradient.tailSet(currPoint);
		} else {
			// start position is before 1st gradient colour, so use whole
			// gradient
			activeGradient = gradient;
			currPoint = (GradPoint) activeGradient.first();
		}
		float currWidth = 0;
		iter = activeGradient.iterator();
		if (currPoint != activeGradient.last()) {
			nextPoint = (GradPoint) iter.next();
			if (iter.hasNext())
				nextPoint = (GradPoint) iter.next();
			if (isPremature) {
				currWidth = 1f / (currPoint.pos - pos);
			} else {
				if (nextPoint.pos - currPoint.pos > 0) {
					currWidth = 1f / (nextPoint.pos - currPoint.pos);
				}
			}
		}
		int idx = 0;
		while (pos < endPos) {
			if (isPremature) {
				frac = 1 - (currPoint.pos - pos) * currWidth;
			} else {
				frac = (pos - currPoint.pos) * currWidth;
			}
			// switch to next colour?
			if (frac > 1) {
				currPoint = nextPoint;
				isPremature = false;
				if (iter.hasNext()) {
					nextPoint = (GradPoint) iter.next();
					if (currPoint != activeGradient.last()) {
						currWidth = 1f / (nextPoint.pos - currPoint.pos);
					} else
						currWidth = 0;
					frac = (pos - currPoint.pos) * currWidth;
				}
			}
			if (currPoint != activeGradient.last()) {
				float fdith = MathUtils.clip(frac + MathUtils.random(-1f, 1f)
						* maxDither, 0f, 1f);
				result[idx] = blend(currPoint.col, nextPoint.col, fdith);
			} else {
				result[idx] = currPoint.col;
			}
			idx++;
			pos++;
		}
		return result;
	}

	public float getMaxDither() {
		return maxDither;
	}

	public void setMaxDither(float maxDither) {
		this.maxDither = MathUtils.clip(maxDither, 0f, 0.1f);
	}
}
