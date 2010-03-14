package toxi.geom.mesh;

public interface STLColorModel {

    void formatHeader(byte[] header);

    int formatRGB(int rgb);

    int getDefaultRGB();
}
