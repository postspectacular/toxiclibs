# toxiclibs
Official master repo (Git version)

Worked up versions of the files in
  toxi.geom
  toxi.geom.mesh
  toxi.geom.mesh2d
  test 
which are based on doubles.
Needed this for my own puropses, as I am developing a CNC machine with 30 meter scale, and 0.01mm resolution.

Tactic was to make a new double based class for each of the current toxicLib classes.   
Appended D to the class name where it was just a word ie
     SphereD  vs Sphere
Where the current class defined dimensionality, put the D preceeding the dimensionality  ie 
     VecD3D  vs Vec3D

Some few of the existing classes have new double methods inserted as they were allready of mixed precisions.  
These include
    toxi.math.MathUtils    as only a double version of random  and EPSD for a double epsilon
    STLReader  no double version is appropriate, as STL has only 32bit numbers
    STLWriter  convert any incomming double classes to floats before writing.
    Matrix     The existing 3 flavors are mixed mode, no changes made.
    GMatrix    is mixed mode, no changes made.

For those classes which have constructors from self-same classes, and which have constructors from more primitive types, included cross
precision constructors, and a file in test for same.
These classes are:
  AABB         AABBD      
  Circle       CircleD    
  Quaternion   QuaternionD
  Sphere       SphereD    
  Vec2D        Vec3D      
  Vec4D        VecD2D     
  VecD3D       VecD4D     
