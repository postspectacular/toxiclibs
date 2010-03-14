package toxi.geom.mesh;

public class MaterialiseSTLColorModel implements STLColorModel {

    protected int baseColor;
    protected boolean useFacetColors;

    public MaterialiseSTLColorModel(int rgb) {
        this(rgb, false);
    }

    public MaterialiseSTLColorModel(int rgb, boolean enableFacets) {
        baseColor = rgb;
        useFacetColors = enableFacets;
    }

    /**
     * @param enabled
     *            the useFacetColors to set
     */
    public void enableFacetColors(boolean enabled) {
        this.useFacetColors = enabled;
    }

    /**
     * @return the useFacetColors
     */
    public boolean facetColorsEnabled() {
        return useFacetColors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.mesh.STLColorModel#formatHeader(byte[])
     */
    public void formatHeader(byte[] header) {
        char[] col = new char[] { 'C', 'O', 'L', 'O', 'R', '=' };
        for (int i = 0; i < col.length; i++) {
            header[i] = (byte) col[i];
        }
        header[6] = (byte) (baseColor >> 16 & 0xff);
        header[7] = (byte) (baseColor >> 8 & 0xff);
        header[8] = (byte) (baseColor & 0xff);
        header[9] = (byte) (baseColor >>> 24);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.mesh.STLColorModel#formatRGB(int)
     */
    public int formatRGB(int rgb) {
        int col15bits = (rgb >> 19 & 0x1f);
        col15bits |= (rgb >> 11 & 0x1f) << 5;
        col15bits |= (rgb >> 3 & 0x1f) << 10;
        if (!useFacetColors) {
            // set bit 15 to indicate use of base color
            col15bits |= 0x8000;
        }
        return col15bits;
    }

    /**
     * @return the baseColor
     */
    public int getBaseColor() {
        return baseColor;
    }

    public int getDefaultRGB() {
        // set bit 15 to indicate use of base color
        return 0x8000;
    }

    /**
     * @param baseColor
     *            the baseColor to set
     */
    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }
}
