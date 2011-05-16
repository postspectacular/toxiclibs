/**
 * This example shows how to construct a pattern from regular polygons,
 * either by sampling a circle or construction from a given line segment.
 */
import toxi.geom.*;
import toxi.processing.*;

size(640, 480);
smooth();

// setup helper class for rendering
ToxiclibsSupport gfx=new ToxiclibsSupport(this);

// build a regular octagon
Polygon2D octagon=new Circle(66).toPolygon2D(8);
// rotate and put at center
octagon.rotate(PI/8);
octagon.translate(new Vec2D(width/2, height/2));
gfx.polygon2D(octagon);

// iterate over all edges and construct a smaller polygon for each
boolean isEven=true;
for (Line2D edge : octagon.getEdges()) {
  // every 2nd edge gets an hexagon
  if (isEven) {
    fill(255, 255, 0);
    // each edge is a Line2D instance which has end points a & b
    Polygon2D hexagon=Polygon2D.fromBaseEdge(edge.a, edge.b, 6);
    gfx.polygon2D(hexagon);
    // construct 3 more pentagons for some of the hexagon edges
    for (int i=2; i<5; i++) {
      fill(0, 255, 0);
      Polygon2D pentagon=Polygon2D.fromBaseEdge(hexagon.get(i+1), hexagon.get(i), 5);
      gfx.polygon2D(pentagon);
      // add 2 more squares for the center pentagon
      if (i==3) {
        fill(0, 255, 255);
        gfx.polygon2D(Polygon2D.fromBaseEdge(pentagon.get(3), pentagon.get(2), 4));
        gfx.polygon2D(Polygon2D.fromBaseEdge(pentagon.get(4), pentagon.get(3), 4));
      }
    }
  } 
  else {
    // every other octagon edge has a triangle
    fill(255, 0, 255);
    Polygon2D tri=Polygon2D.fromBaseEdge(edge.a, edge.b, 3);
    gfx.polygon2D(tri);
  }
  isEven=!isEven;
}

