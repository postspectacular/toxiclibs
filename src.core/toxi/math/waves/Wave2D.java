package toxi.math.waves;

import toxi.geom.Vec2D;

public class Wave2D {

    public AbstractWave xmod, ymod;
    public Vec2D pos;

    public Wave2D(AbstractWave x, AbstractWave y) {
        xmod = x;
        ymod = y;
        pos = new Vec2D();
    }

    public void update() {
        pos.x = xmod.update();
        pos.y = ymod.update();
    }
}
