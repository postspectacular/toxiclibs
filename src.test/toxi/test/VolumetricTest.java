package toxi.test;

import junit.framework.TestCase;
import toxi.geom.Vec3D;
import toxi.volume.RoundBrush;
import toxi.volume.VolumetricSpaceArray;

public class VolumetricTest extends TestCase {

	VolumetricSpaceArray vol;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		vol = new VolumetricSpaceArray(new Vec3D(900, 150, 300), 192, 32, 64);
	}

	public void testBrush() {
		RoundBrush b = new RoundBrush(vol, 90);
		b.drawAtAbsolutePos(new Vec3D(), 1);
	}
}
