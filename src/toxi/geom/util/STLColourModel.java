package toxi.geom.util;

public interface STLColourModel {
	void formatHeader(byte[] header);
	
	int formatRGB(int rgb);
}
