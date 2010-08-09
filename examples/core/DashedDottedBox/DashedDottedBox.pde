import toxi.geom.*;
import toxi.geom.mesh.*;

List<Line3D> edges = new ArrayList<Line3D>();
// cube radius
float s=100;
// step size between points on each edge
float step=10;

boolean isDashed=true;

void setup() {
  size(400,400,P3D);
  // create a cube mesh
  WETriangleMesh box=new WETriangleMesh();
  box.addMesh(new AABB(new Vec3D(),100).toMesh());
  // scan all edges and only pick out the major axes
  // put them all in our edge/line list for drawing later
  for(WingedEdge e : box.edges.values()) {
    if (e.getDirection().isMajorAxis(0.01)) {
      edges.add(e);
    }
  }
}

void draw() {
  background(0);
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  stroke(255);
  for(Line3D l : edges) {
    if (isDashed) {
      drawDashedLine(l);
    } else {
      drawDottedLine(l);
    }
  }
}

void drawDashedLine(Line3D l) {
  // compute inbetween points every "STEP" units and iterate over them
  List<Vec3D> points=l.splitIntoSegments(null,step,true);
  for(int i=0, num=points.size()-1; i<num; i+=2) {
    Vec3D p=points.get(i);
    Vec3D q=points.get(i+1);
    line(p.x,p.y,p.z,q.x,q.y,q.z);
  }
}

void drawDottedLine(Line3D l) {
  // compute inbetween points every "STEP" units and iterate over them
  for(Vec3D p : l.splitIntoSegments(null,step,true)) {
    point(p.x,p.y,p.z);
  }
}

void keyPressed() {
  isDashed=!isDashed;
}
