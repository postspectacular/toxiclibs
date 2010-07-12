package toxi.processing;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.AABB;
import toxi.geom.AxisAlignedCylinder;
import toxi.geom.Cone;
import toxi.geom.Ellipse;
import toxi.geom.Line2D;
import toxi.geom.Line3D;
import toxi.geom.Plane;
import toxi.geom.Polygon2D;
import toxi.geom.Ray2D;
import toxi.geom.Ray3D;
import toxi.geom.Rect;
import toxi.geom.Sphere;
import toxi.geom.Triangle;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.TriangleMesh;

/**
 * In addition to providing new drawing commands, this class provides wrappers
 * for using datatypes of the toxiclibs core package directly with Processing's
 * drawing commands. The class can be configured to work with any PGraphics
 * instance (incl. offscreen buffers).
 */
public class ToxiclibsSupport {

    protected static final Logger logger = Logger
            .getLogger(ToxiclibsSupport.class.getName());

    protected PApplet app;
    protected PGraphics gfx;

    public ToxiclibsSupport(PApplet app) {
        this(app, app.g);
    }

    public ToxiclibsSupport(PApplet app, PGraphics gfx) {
        this.app = app;
        this.gfx = gfx;
    }

    public final void box(AABB box) {
        mesh(box.toMesh(), false, 0);
    }

    public final void box(AABB box, boolean smooth) {
        TriangleMesh mesh = box.toMesh();
        if (smooth) {
            mesh.computeVertexNormals();
        }
        mesh(mesh, smooth, 0);
    }

    public final void cone(Cone cone) {
        mesh(cone.toMesh("cone", 6, 0, true, true), false, 0);
    }

    public final void cone(Cone cone, boolean topClosed, boolean bottomClosed) {
        mesh(cone.toMesh("cone", 6, 0, topClosed, bottomClosed), false, 0);
    }

    public final void cone(Cone cone, int res, boolean smooth) {
        cone(cone, res, true, true, smooth);
    }

    public final void cone(Cone cone, int res, boolean topClosed,
            boolean bottomClosed, boolean smooth) {
        TriangleMesh mesh = cone.toMesh(res);
        if (smooth) {
            mesh.computeVertexNormals();
        }
        mesh(mesh, smooth, 0);
    }

    public final void cylinder(AxisAlignedCylinder cylinder) {
        mesh(cylinder.toMesh(), false, 0);
    }

    public final void cylinder(AxisAlignedCylinder cylinder, int res,
            boolean smooth) {
        TriangleMesh mesh = cylinder.toMesh(res, 0);
        if (smooth) {
            mesh.computeVertexNormals();
        }
        mesh(mesh, smooth, 0);
    }

    public final void ellipse(Ellipse e) {
        Vec2D r = e.getRadii();
        switch (gfx.ellipseMode) {
            case PConstants.CENTER:
                gfx.ellipse(e.x, e.y, r.x * 2, r.y * 2);
                break;
            case PConstants.RADIUS:
                gfx.ellipse(e.x, e.y, r.x, r.y);
                break;
            case PConstants.CORNER:
            case PConstants.CORNERS:
                gfx.ellipse(e.x - r.x, e.y - r.y, r.x * 2, r.y * 2);
                break;
            default:
                logger.warning("invalid ellipse mode: " + gfx.ellipseMode);
        }
    }

    /**
     * @return the gfx
     */
    public final PGraphics getGraphics() {
        return gfx;
    }

    public final void line(Line2D line) {
        gfx.line(line.a.x, line.a.y, line.b.x, line.b.y);
    }

    public final void line(Line3D line) {
        gfx.line(line.a.x, line.a.y, line.a.z, line.b.x, line.b.y, line.b.z);
    }

    public final void line(Vec2D a, Vec2D b) {
        gfx.line(a.x, a.y, b.x, b.y);
    }

    public final void line(Vec3D a, Vec3D b) {
        gfx.line(a.x, a.y, a.z, b.x, b.y, b.z);
    }

    /**
     * Draws a 2D line strip using all points in the given list of vectors.
     * 
     * @param points
     *            point list
     */
    public final void lineStrip2D(List<? extends Vec2D> points) {
        boolean isFilled = gfx.fill;
        gfx.fill = false;
        processVertices2D(points.iterator(), PConstants.POLYGON, false);
        gfx.fill = isFilled;
    }

    /**
     * Draws a 3D line strip using all points in the given list of vectors.
     * 
     * @param points
     *            point list
     */
    public final void lineStrip3D(List<? extends Vec3D> points) {
        boolean isFilled = gfx.fill;
        gfx.fill = false;
        processVertices3D(points.iterator(), PConstants.POLYGON, false);
        gfx.fill = isFilled;
    }

    /**
     * Draws a mesh instance using flat shading.
     * 
     * @param mesh
     */
    public final void mesh(TriangleMesh mesh) {
        mesh(mesh, false, 0);
    }

    /**
     * Draws a mesh instance.
     * 
     * @param mesh
     * @param smooth
     *            true to enable gouroud shading (uses vertex normals, which
     *            should have been computed beforehand) or false for flat
     *            shading
     */
    public final void mesh(TriangleMesh mesh, boolean smooth) {
        mesh(mesh, smooth, 0);
    }

    /**
     * Draws a mesh instance.
     * 
     * @param mesh
     * @param smooth
     *            true to enable gouroud shading (uses vertex normals, which
     *            should have been computed beforehand) or false for flat
     *            shading
     * @param normalLength
     *            if >0 then face (or vertex) normals are rendered at this
     *            length
     */
    public final void mesh(TriangleMesh mesh, boolean smooth, float normalLength) {
        gfx.beginShape(PConstants.TRIANGLES);
        if (smooth) {
            for (TriangleMesh.Face f : mesh.faces) {
                gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        } else {
            for (TriangleMesh.Face f : mesh.faces) {
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        }
        gfx.endShape();
        if (normalLength > 0) {
            int strokeCol = 0;
            boolean isStroked = gfx.stroke;
            if (isStroked) {
                strokeCol = gfx.strokeColor;
            }
            if (smooth) {
                for (TriangleMesh.Vertex v : mesh.vertices.values()) {
                    Vec3D w = v.add(v.normal.scale(normalLength));
                    Vec3D n = v.normal.scale(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(v.x, v.y, v.z, w.x, w.y, w.z);
                }
            } else {
                float third = 1f / 3;
                for (TriangleMesh.Face f : mesh.faces) {
                    Vec3D c = f.a.add(f.b).addSelf(f.c).scaleSelf(third);
                    Vec3D d = c.add(f.normal.scale(normalLength));
                    Vec3D n = f.normal.scale(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(c.x, c.y, c.z, d.x, d.y, d.z);
                }
            }
            if (isStroked) {
                gfx.stroke(strokeCol);
            } else {
                gfx.noStroke();
            }
        }
    }

    /**
     * Draws the major axes from the given point.
     * 
     * @param o
     *            origin point
     * @param len
     *            axis length
     */
    public final void origin(Vec3D o, float len) {
        gfx.stroke(255, 0, 0);
        gfx.line(o.x, o.y, o.z, o.x + len, o.y, o.z);
        gfx.stroke(0, 255, 0);
        gfx.line(o.x, o.y, o.z, o.x, o.y + len, o.z);
        gfx.stroke(0, 0, 255);
        gfx.line(o.x, o.y, o.z, o.x, o.y, o.z + len);
    }

    /**
     * Draws a square section of a plane at the given size.
     * 
     * @param plane
     *            plane to draw
     * @param size
     *            edge length
     */
    public final void plane(Plane plane, float size) {
        mesh(plane.toMesh(size), false, 0);
    }

    /**
     * Draws a 2D point at the given position.
     * 
     * @param v
     */
    public final void point(Vec2D v) {
        gfx.point(v.x, v.y);
    }

    /**
     * Draws a 3D point at the given position.
     * 
     * @param v
     */
    public final void point(Vec3D v) {
        gfx.point(v.x, v.y, v.z);
    }

    public final void points2D(Iterator<? extends Vec2D> iterator) {
        processVertices2D(iterator, PConstants.POINTS, false);
    }

    public final void points2D(List<? extends Vec2D> points) {
        processVertices2D(points.iterator(), PConstants.POINTS, false);
    }

    public final void points3D(Iterator<? extends Vec3D> iterator) {
        processVertices3D(iterator, PConstants.POINTS, false);
    }

    public final void points3D(List<? extends Vec3D> points) {
        processVertices3D(points.iterator(), PConstants.POINTS, false);
    }

    public final void polygon2D(Polygon2D poly) {
        processVertices2D(poly.vertices.iterator(), PConstants.POLYGON, true);
    }

    public final void processVertices2D(Iterator<? extends Vec2D> iterator,
            int shapeID, boolean closed) {
        gfx.beginShape(shapeID);
        while (iterator.hasNext()) {
            Vec2D v = iterator.next();
            gfx.vertex(v.x, v.y);
        }
        if (closed) {
            gfx.endShape(PConstants.CLOSE);
        } else {
            gfx.endShape();
        }
    }

    public final void processVertices3D(Iterator<? extends Vec3D> iterator,
            int shapeID, boolean closed) {
        gfx.beginShape(shapeID);
        while (iterator.hasNext()) {
            Vec3D v = iterator.next();
            gfx.vertex(v.x, v.y, v.z);
        }
        if (closed) {
            gfx.endShape(PConstants.CLOSE);
        } else {
            gfx.endShape();
        }
    }

    public final void ray(Ray2D ray, float length) {
        Vec2D e = ray.getPointAtDistance(length);
        gfx.line(ray.x, ray.y, e.x, e.y);
    }

    public final void ray(Ray3D ray, float length) {
        Vec3D e = ray.getPointAtDistance(length);
        gfx.line(ray.x, ray.y, ray.z, e.x, e.y, e.z);
    }

    public final void rect(Rect r) {
        switch (gfx.rectMode) {
            case PConstants.CORNER:
                gfx.rect(r.x, r.y, r.width, r.height);
                break;
            case PConstants.CORNERS:
                gfx.rect(r.x, r.y, r.x + r.width, r.y + r.height);
                break;
            case PConstants.CENTER:
                gfx.rect(r.x + r.width * 0.5f, r.y + r.height * 0.5f, r.width,
                        r.height);
                break;
            case PConstants.RADIUS:
                float rw = r.width * 0.5f;
                float rh = r.height * 0.5f;
                gfx.rect(r.x + rw, r.y + rh, rw, rh);
                break;
            default:
                logger.warning("invalid rect mode: " + gfx.rectMode);
        }
    }

    /**
     * @param gfx
     *            the gfx to set
     */
    public final void setGraphics(PGraphics gfx) {
        this.gfx = gfx;
    }

    // TODO replace with mesh drawing, blocked by issue #2
    public final void sphere(Sphere sphere) {
        gfx.pushMatrix();
        gfx.translate(sphere.x, sphere.y, sphere.z);
        gfx.sphere(sphere.radius);
        gfx.popMatrix();
    }

    public final void texturedMesh(TriangleMesh mesh, PImage tex, boolean smooth) {
        gfx.beginShape(PConstants.TRIANGLES);
        gfx.texture(tex);
        if (smooth) {
            for (TriangleMesh.Face f : mesh.faces) {
                if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z);
                }
            }
        } else {
            for (TriangleMesh.Face f : mesh.faces) {
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z);
                }
            }
        }
        gfx.endShape();
    }

    public final void triangle(Triangle tri) {
        triangle(tri, true);
    }

    public final void triangle(Triangle tri, boolean isFullShape) {
        if (isFullShape) {
            gfx.beginShape(PConstants.TRIANGLES);
        }
        Vec3D n = tri.computeNormal();
        gfx.normal(n.x, n.y, n.z);
        gfx.vertex(tri.a.x, tri.a.y, tri.a.z);
        gfx.vertex(tri.b.x, tri.b.y, tri.b.z);
        gfx.vertex(tri.c.x, tri.c.y, tri.c.z);
        if (isFullShape) {
            gfx.endShape();
        }
    }

    public final void triangle(Triangle2D tri) {
        triangle(tri, true);
    }

    public final void triangle(Triangle2D tri, boolean isFullShape) {
        if (isFullShape) {
            gfx.beginShape(PConstants.TRIANGLES);
        }
        gfx.vertex(tri.a.x, tri.a.y);
        gfx.vertex(tri.b.x, tri.b.y);
        gfx.vertex(tri.c.x, tri.c.y);
        if (isFullShape) {
            gfx.endShape();
        }
    }

    public final void vertex(Vec2D v) {
        gfx.vertex(v.x, v.y);
    }

    public final void vertex(Vec3D v) {
        gfx.vertex(v.x, v.y, v.z);
    }
}
