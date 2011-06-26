/**
 * This demo creates a recursively growing tree using the Vec2D class.
 *
 * (c) 2010 Karsten Schmidt // LGPLv2 licensed
 */

import toxi.geom.*;

Branch tree;

void setup() {
  size(600, 600);
  // create the root branch growing straight up
  tree=new Branch(new Vec2D(width/2, height), new Vec2D(0, -1), 200, 0);
}

void draw() {
  background(255);
  tree.update();
  tree.draw();
}

class Branch {

  int depth;

  Vec2D startPos;
  Vec2D dir;
  Vec2D currPos;

  float len, maxLen;

  List<Branch> children=new ArrayList<Branch>();

  Branch(Vec2D pos, Vec2D dir, float maxLen, int depth) {
    this.startPos=pos;
    this.dir=dir;
    this.depth=depth;
    this.maxLen=maxLen;
  }

  void update() {
    // grow to maxLen
    len+=(maxLen-len)*0.02;
    // update current end pos
    currPos=startPos.add(dir.scale(len));
    // check if we can branch (plus 10% chance only)
    if (canBranch() && random(1)<0.1) {
      // create new direction for child branch
      Vec2D newDir=dir.getRotated(random(-1,1)*PI/4);
      // reduce max length for this branch
      float newLen=maxLen*random(0.33,0.8);
      // add to list of children (increase tree depth)
      children.add(new Branch(currPos.copy(), newDir, newLen, depth+1));
    }
    // update children
    for (Branch c : children) {
      c.update();
    }
  }

  void draw() {
    // recursively draw branch(es)
    line(startPos.x, startPos.y, currPos.x, currPos.y);
    for (Branch c : children) {
      c.draw();
    }
  }

  // define constraints for branching:
  // no more than 4 levels deep
  // min 66% grown to full size
  // no more than 4 child branches
  boolean canBranch() {
    return depth<5 && len/maxLen>0.66 && children.size()<4;
  }
}

