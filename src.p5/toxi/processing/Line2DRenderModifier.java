package toxi.processing;

import toxi.geom.ReadonlyVec2D;

public interface Line2DRenderModifier {

    public void apply(ToxiclibsSupport gfx, ReadonlyVec2D a, ReadonlyVec2D b);
}
