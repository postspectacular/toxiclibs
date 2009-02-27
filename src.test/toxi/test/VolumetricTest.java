package toxi.test;

import junit.framework.TestCase;
import toxi.geom.Vec3D;
import toxi.volume.RoundBrush;
import toxi.volume.VolumetricSpace;

public class VolumetricTest extends TestCase {

	VolumetricSpace vol;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		vol = new VolumetricSpace(new Vec3D(900, 150, 300), 192, 32, 64);
	}

	public void testBrush() {
		RoundBrush b = new RoundBrush(vol, 90);
		b.drawAtAbsolutePos(new Vec3D(), 1);
	}
}
