/**
* This demo is a simple drawing tool using a hexagon shaped pixels whose state can be toggled via mouse clicks.
* As an example of polymorphism, the hexagonal grid itself is defined as an abstract generic class which needs
* to be extended by a concrete implementation which also produces the actual grid cells. This in itself demonstates
* how to use Java generics to produce re-usable classes. In this case, the grid cells produced have an additional
* on/off state associated which is used to tint cells differently depending if the user has clicked on them or not.
*
* Usage: Click on a cell to toggle its state
*/
import toxi.geom.*;
import toxi.color.*;
import toxi.processing.*;

HexGrid grid;
ToxiclibsSupport gfx;

void setup() {
  size(680, 382);
  gfx=new ToxiclibsSupport(this);
  // the HexGrid class itself is declared as abstract class
  // which can't be instantiated by itself. Instead we provide
  // the StatefulHexGrid implementation which extends HexGrid and
  // also implements the makeCell() method to produce grid cell instances
  grid=new StatefulHexGrid(30, new Vec2D(0,0), 14, 10);
}

void draw() {
  background(255);
  // draw all grid cells using their state to define different fill colors
  for(Object c : grid.cells) {
    ((StatefulHexCell)c).draw(gfx);
  }
}

// check all cells if mouse position is within
// and if so toggle that cell's state on/off
void mousePressed() {
  Vec2D mpos=new Vec2D(mouseX,mouseY);
  for(Object c : grid.cells) {
    StatefulHexCell cell=(StatefulHexCell)c;
    if (cell.rollover(mpos)) {
      cell.toggleState();
      break;
    }
  }
}

// concrete implementation of the HexGrid class with additional
// type information for grid cells: StatefulHexCell is the type of cells produced/used
class StatefulHexGrid extends HexGrid<StatefulHexCell> {
  
  public StatefulHexGrid(float radius, Vec2D offset, int cols, int rows) {
    super(radius, offset, cols, rows);
  }
  
  // this function is called by the HexGrid constructor to produce a single grid cell
  // using a computed polygon (hexagon)
  public StatefulHexCell makeCell(Polygon2D poly) {
    return new StatefulHexCell(poly);
  }
}

// our custom grid cell implementation, simply adding a boolean for
// attaching state information and some related helper functions
class StatefulHexCell {
  
  Polygon2D poly;
  boolean isActive;
  
  public StatefulHexCell(Polygon2D poly) {
    this.poly=poly;
  }
  
  public void draw(ToxiclibsSupport gfx) {
    gfx.fill(isActive ? TColor.RED : TColor.YELLOW);
    gfx.polygon2D(poly);
  }
  
  public boolean rollover(Vec2D mpos) {
    return poly.containsPoint(mpos);
  }
  
  public void toggleState() {
    isActive=!isActive;
  }
}
