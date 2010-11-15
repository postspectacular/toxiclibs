import toxi.color.*;
import toxi.geom.*;
import toxi.processing.*;

int num=30;
List<ColoredPolygon> polygons = new ArrayList<ColoredPolygon>();

ToxiclibsSupport gfx;

void setup() {
  size(680,382);
  smooth();
}

void draw() {
  background(255);
  fill(255,0,0,50);
  noStroke();
  for(ColoredPolygon p : polygons) {
    p.smooth(0.01,0.05);
    fill(p.col.toARGB());
    beginShape();
    for(Vec2D v : p.vertices) {
      vertex(v.x,v.y);
    }
    endShape(CLOSE);
  }
}

void mousePressed() {
  ColoredPolygon poly=new ColoredPolygon(ColorRange.BRIGHT.getColor().setAlpha(random(0.5,0.8)));
  float radius=random(50,200);
  for(int i=0; i<num; i++) {
   poly.add(Vec2D.fromTheta((float)i/num*TWO_PI).scaleSelf(random(0.2,1)*radius).addSelf(mouseX,mouseY));
 }
 polygons.add(poly);
}

class ColoredPolygon extends Polygon2D {
  TColor col;
  
  public ColoredPolygon(TColor col) {
    this.col=col;
  }
}
