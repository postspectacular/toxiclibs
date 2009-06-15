package toxi.geom;

import toxi.math.MathUtils;

public class Rect {
	public float x, y, width, height;

	public Rect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rect(Vec2D topLeft, Vec2D bottomRight) {
		x = topLeft.x;
		y = topLeft.y;
		width = bottomRight.x - x;
		height = bottomRight.y - y;
	}

	public boolean containsPoint(Vec2D p) {
		if (p.x < x || p.x >= x + width) {
			return false;
		}
		if (p.y < y || p.y >= y + height) {
			return false;
		}
		return true;
	}

	public Vec2D getBottomRight() {
		return new Vec2D(x + width, y + height);
	}

	public Vec2D getDimensions() {
		return new Vec2D(width, height);
	}

	public Vec2D getTopLeft() {
		return new Vec2D(x, y);
	}

	public Rect merge(Rect r) {
		float tmp = MathUtils.max(x + width, r.x + r.width);
		x = MathUtils.min(x, r.x);
		width = tmp - x;
		tmp = MathUtils.max(y + height, r.y + r.height);
		y = MathUtils.min(y, r.y);
		height = tmp - y;
		return this;
	}

	public String toString() {
		return "rect: {x:" + x + ", y:" + y + ", width:" + width + ", height:"
				+ height + "}";
	}
}
