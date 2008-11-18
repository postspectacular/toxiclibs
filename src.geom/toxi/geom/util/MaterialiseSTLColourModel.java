package toxi.geom.util;

public class MaterialiseSTLColourModel implements STLColourModel {

	int baseColour;

	public MaterialiseSTLColourModel(int rgb) {
		baseColour = rgb;
	}

	public int formatRGB(int rgb) {
		int col15bits = (rgb >> 19 & 0x1f);
		col15bits |= (rgb >> 11 & 0x1f) << 5;
		col15bits |= (rgb >> 3 & 0x1f) << 10;
		col15bits |= 0x8000;
		return col15bits;
	}

	public void formatHeader(byte[] header) {
		char[] col = new char[] { 'C', 'O', 'L', 'O', 'R', '=' };
		for (int i = 0; i < col.length; i++) {
			header[i] = (byte) col[i];
		}
		header[6] = (byte) (baseColour >> 16 & 0xff);
		header[7] = (byte) (baseColour >> 8 & 0xff);
		header[8] = (byte) (baseColour & 0xff);
		header[9] = (byte) (baseColour >>> 24);
	}
}
