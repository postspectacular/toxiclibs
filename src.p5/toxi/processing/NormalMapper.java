package toxi.processing;

import toxi.color.ReadonlyTColor;
import toxi.geom.Vec3D;

public interface NormalMapper {

    public ReadonlyTColor getRGBForNormal(Vec3D normal);
}
