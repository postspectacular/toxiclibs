package toxi.test;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Spline2D;
import toxi.geom.Vec2D;

public class SplineTestApplet extends PApplet {

	float MAX_IMPACT = 0.5f;

	public void draw() {
		background(255);

		Spline2D spline = new Spline2D();
		stroke(255, 0, 0, 100);
		beginShape();
		for (int i = 0; i < 11; i++) {
			Vec2D p = new Vec2D(100, (i + 0.5f) / 10.0f * TWO_PI).toCartesian()
					.add(width / 2, height / 2);
			vertex(p.x, p.y);
			spline.pointList.add(p);
		}
		endShape();

		// highlight the positions of the points with circles
		stroke(0);
		for (Vec2D p : spline.pointList) {
			ellipse(p.x, p.y, 5, 5);
		}

		// calc tightness based on vertical mouse position (centre = linear)
		float tight = (mouseY - height / 2.0f) / (height / 2.0f) * MAX_IMPACT;
		spline.setTightness(tight);

		// sample the curve at a higher resolution
		// so that we get extra points between each original pair of points
		List<Vec2D> vertices = spline.computeVertices(32);

		// draw the smoothened curve
		beginShape();
		for (Vec2D v : vertices) {
			vertex(v.x, v.y);
		}
		endShape();
	}

	public void setup() {
		size(400, 400);
		smooth();
		noFill();
	}

}
