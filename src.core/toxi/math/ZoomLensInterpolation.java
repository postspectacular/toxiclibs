package toxi.math;

/**
 * This class provides an adjustable zoom lens to either bundle or dilate values
 * around a focal point within a given interval. For a example use cases, please
 * have a look at the provided ScaleMapDataViz and ZoomLens examples.
 * 
 * @author toxi 21 Apr 2009
 * 
 */
public class ZoomLensInterpolation implements InterpolateStrategy {

	protected CircularInterpolation leftImpl = new CircularInterpolation();
	protected CircularInterpolation rightImpl = new CircularInterpolation();

	protected float lensPos;
	protected float lensStrength, absStrength;

	public ZoomLensInterpolation() {
		this(0.5f, 1);
	}

	public ZoomLensInterpolation(float lensPos, float lensStrength) {
		this.lensPos = lensPos;
		this.lensStrength = lensStrength;
		this.absStrength = MathUtils.abs(lensStrength);
		leftImpl.setFlipped(lensStrength > 0);
		rightImpl.setFlipped(lensStrength < 0);
	}

	public float interpolate(float min, float max, float t) {
		float val = min + (max - min) * t;
		if (t < lensPos) {
			val += (leftImpl.interpolate(min, min + (max - min) * lensPos, t
					/ lensPos) - val)
					* absStrength;
		}
		else {
			val += (rightImpl.interpolate(min + (max - min) * lensPos, max,
					(t - lensPos) / (1 - lensPos)) - val)
					* absStrength;
		}
		return val;
	}

	public void setLensPos(float pos, float smooth) {
		lensPos += (MathUtils.clipNormalized(pos) - lensPos) * smooth;
	}

	public void setLensStrength(float str, float smooth) {
		lensStrength += (MathUtils.clip(str, -1, 1) - lensStrength) * smooth;
		absStrength = MathUtils.abs(lensStrength);
		leftImpl.setFlipped(lensStrength > 0);
		rightImpl.setFlipped(lensStrength < 0);
	}
}