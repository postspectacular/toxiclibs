/**
 * This demo creates a recursively growing tree using the Vec2D class.
 *
 * (c) 2010 Karsten Schmidt // LGPLv2 licensed
 */

import toxi.geom.*;
import toxi.math.*;
import java.util.List;

Branch tree;

void setup() {
  size(600, 600);
  // create the root branch growing straight up
  newTree();
}

void draw() {
  background(255);
  tree.update();
  tree.draw();
}

void keyPressed() {
  if (key=='r') {
    newTree();
  }
}

void newTree() {
  tree=new Branch(new Vec2D(width/2, height), new Vec2D(0, -1), 200, 0);
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
    if (canBranch() && MathUtils.randomChance(0.1)) {
      // create new direction for child branch
      float theta=random(PI/8,PI/4);
      if (MathUtils.flipCoin()) theta*=-1;
      Vec2D newDir=dir.getRotated(theta);
      // reduce max length for this branch
      float newLen=maxLen*MathUtils.random(0.33,0.85);
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
    return depth<5 && len/maxLen>0.5 && children.size()<4;
  }
}
