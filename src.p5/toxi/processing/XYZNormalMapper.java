package toxi.processing;

import toxi.color.ReadonlyTColor;
import toxi.color.TColor;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;

public class XYZNormalMapper implements NormalMapper {

    public static final Matrix4x4 normalMap = new Matrix4x4().translateSelf(
            0.5, 0.5, 0.5).scaleSelf(0.4999);

    public ReadonlyTColor getRGBForNormal(Vec3D normal) {
        normal = normalMap.applyTo(normal);
        return TColor.newRGB(normal.x, normal.y, normal.z);
    }
}
