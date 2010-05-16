package toxi.music.scale;

public class GenericScale extends AbstractScale {

    public GenericScale(String name, byte[] tones) {
        super(name, tones);
    }

    /**
     * Implements a scale based on a 12 bit integer value, where all 1 bits are
     * used as possible tones for this scale. This way any possible scale can be
     * implemented and easily experimented with.
     * 
     * @param name
     * @param seed
     */
    public GenericScale(String name, int seed) {
        super(name, new byte[0]);
        byte[] tmp = new byte[12];
        int j = 11;
        for (byte i = 11; i >= 0; i--) {
            if ((seed & 1) != 0) {
                tmp[j--] = i;
            }
            seed >>>= 1;
        }
        tones = new byte[11 - j];
        System.arraycopy(tmp, j + 1, tones, 0, tones.length);
    }
}
