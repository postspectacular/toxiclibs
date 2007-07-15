/*
 * Processing demo use case for the sphere intersector & reflector
 * util of the toxi.geom package
 * 
 * You can build your own reflectors by implementing the
 * toxi.geom.Reflector interface
 */
import processing.opengl.*;
import toxi.geom.*;

Vec3D pos=new Vec3D(0,200,0);  // ray start position/origin
Vec3D target=new Vec3D(0,0,0); // ray end point / target position
Vec3D dir=target.sub(pos).normalize(); // resulting ray direction

Vec3D sOrigin = new Vec3D(0,0,0); // centre of sphere
float sRadius= 100;  // sphere radius

Reflector isect=new SphereIntersectorReflector(sOrigin,sRadius);

Vec3D camRot = new Vec3D();

void setup() {
	size(500,500,OPENGL);
}

void draw() {
	background(255);
	noFill();
	translate(width/2,height/2,0);

	// update ray target
	if (mousePressed) {
		target.x=(width/2-mouseX)/(width*0.5)*sRadius*2;
		target.y=(height/2-mouseY)/(height*0.5)*sRadius*2;
		dir=target.sub(pos).normalize();
	} else {
		camRot.x=mouseY*0.01;
		camRot.y=mouseX*0.01;
	}
	rotateX(camRot.x);
	rotateY(camRot.y);

	// compute the reflected ray direction
	Ray3D reflectedRay=isect.reflectRay(new Ray3D(pos,dir));

	// does ray intersect sphere at all?
	if(reflectedRay!=null) {
		// get the intersection point
		Vec3D isectPos=isect.getIntersectionPoint();

		// calc the mirrored point
		Vec3D posMirrored=reflectedRay.getPointAtDistance(isect.getIntersectionDistance());

		// show the intersection point & sphere's normal vector at intersection
		Vec3D sphereNormal=isect.getNormalAtIntersection();

		pushMatrix();
		stroke(0,255,0);
		translate(isectPos);
		box(5);
		beginShape(LINES);
		vertex(0,0,0);
		vertex(sphereNormal.scale(sRadius));
		endShape();
		popMatrix();

		// reflected point
		pushMatrix();
		translate(posMirrored);
		stroke(255,160,0);
		box(5);
		popMatrix();

		// reflected ray
		beginShape(LINES);
		stroke(255,0,0);
		vertex(isectPos);
		vertex(pos);
		stroke(255,160,0);
		vertex(isectPos);
		vertex(posMirrored);
		endShape();
	}

	// show sphere
	pushMatrix();
	stroke(0,20);
	translate(sOrigin);
	scale(sRadius);
	sphere(1);
	popMatrix();

	// ray origin
	stroke(255,0,0);
	pushMatrix();
	translate(pos);
	box(5);
	popMatrix();

	// ray target
	stroke(0,255,255);
	pushMatrix();
	translate(target);
	box(5);
	popMatrix();

}

void translate(Vec3D v) {
	translate(v.x, v.y, v.z);
}

void vertex(Vec3D v) {
	vertex(v.x, v.y, v.z);
}
