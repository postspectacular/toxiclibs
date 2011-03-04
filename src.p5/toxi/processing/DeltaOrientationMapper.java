package toxi.processing;

import toxi.color.ReadonlyTColor;
import toxi.color.ToneMap;
import toxi.geom.Vec3D;

public class DeltaOrientationMapper implements NormalMapper {

    protected Vec3D dir;
    protected ToneMap toneMap;
    protected double toneScale;

    public DeltaOrientationMapper(Vec3D dir, ToneMap toneMap) {
        this.dir = dir;
        setToneMap(toneMap);
    }

    public ReadonlyTColor getRGBForNormal(Vec3D normal) {
        float dot = (float) (dir.dot(normal) * toneScale + toneScale);
        return toneMap.getToneFor(dot);
    }

    public void setToneMap(ToneMap toneMap) {
        this.toneMap = toneMap;
        this.toneScale = toneMap.map.getInputMedian();
    }
}
