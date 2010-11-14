interface BoundaryCheck {
  boolean containsPoint(Vec2D p);
  Vec2D getCentroid();
}

class RectBoundary implements BoundaryCheck {
  Rect rect;

  public RectBoundary(Rect r) {
    rect=r;
  }
  boolean containsPoint(Vec2D p) {
    return rect.containsPoint(p);
  }

  Vec2D getCentroid() {
    return rect.getCentroid();
  }
}

class CircleBoundary implements BoundaryCheck {
  Circle circle;

  public CircleBoundary(Circle c) {
    circle=c;
  }

  boolean containsPoint(Vec2D p) {
    return circle.containsPoint(p);
  }

  Vec2D getCentroid() {
    return circle;
  }
}

