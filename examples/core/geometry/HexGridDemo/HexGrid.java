// Since Processing doesn't support abstract classes (nor generics) within .pde files this class is defined as standalone Java class.
// 
// This abstract template class computes and stores a number of hexagons spatially arranged as hexagonal grid.
// This class needs to be extended by a concrete implementation providing type information for grid cells
// and an implementation of the makeCell() method to produce single grid cell instances.

import toxi.geom.*;
import toxi.math.*;
import java.util.*;

public abstract class HexGrid<T> {
  float cellWidth, cellHeight;
  List<T> cells=new ArrayList<T>();
  Polygon2D hexProto;

  public HexGrid(float radius, Vec2D offset, int cols, int rows) {
    // compute hexagon prototype for given radius and rotated by 30 degrees
    Polygon2D hexProto=new Circle(radius).toPolygon2D(6).rotate(MathUtils.PI/6);
    cellWidth=hexProto.getBounds().width;
    cellHeight=cellWidth*MathUtils.sin(MathUtils.PI/3);
    // create grid cells using implementation of makeCell()
    for (int x=0; x<cols; x++) {
      for (int y=0; y<rows; y++) {
        cells.add(makeCell(hexProto.copy().translate(getPosForCell(x, y).addSelf(offset))));
      }
    }
  }

  Vec2D getPosForCell(int x, int y) {
    return new Vec2D((x+(1==y%2 ? 0.5f : 0))*cellWidth, y*cellHeight);
  }
  
  // this method MUST be overridden by an implementing subclass (see main .pde file)
  public abstract T makeCell(Polygon2D c);
}

