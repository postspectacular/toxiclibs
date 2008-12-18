package toxi.geom.util;

public class DefaultSTLColorModel implements STLColorModel {

	public void formatHeader(byte[] header) {
	}

	public int formatRGB(int rgb) {
		int col15bits = (rgb >> 3 & 0x1f);
		col15bits |= (rgb >> 11 & 0x1f) << 5;
		col15bits |= (rgb >> 19 & 0x1f) << 10;
		col15bits |= 0x8000;
		return col15bits;
	}

}
