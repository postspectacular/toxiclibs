package toxi.geom.util;

public interface STLColorModel {
	void formatHeader(byte[] header);
	
	int formatRGB(int rgb);
}
