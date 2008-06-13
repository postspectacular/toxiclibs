package toxi.geom.util;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import toxi.geom.Vec3D;

/**
 * A simple, but flexible and memory efficient exporter for binary STL files.
 * Custom colour support is implemented via the STLColourModel interface and the
 * exporter comes with the 2 most common format variations defined by the
 * DEFAULT and MATERIALISE constants.
 * 
 * The minimal design of this exporter means it does not build an extra list of
 * faces in RAM and so is able to easily export models with millions of faces.
 * 
 * http://en.wikipedia.org/wiki/STL_(file_format)
 * 
 * @author kschmidt
 * 
 */
public class STLWriter {

	public static final STLColourModel DEFAULT = new DefaultSTLColourModel();

	public static final STLColourModel MATERIALISE = new MaterialiseSTLColourModel(
			0xffffffff);

	protected DataOutputStream ds;

	protected Vec3D scale = new Vec3D(1, 1, 1);

	protected boolean useInvertedNormals = false;

	protected byte[] buf = new byte[4];

	protected STLColourModel colourModel;

	public STLWriter() {
		this(DEFAULT);
	}

	public STLWriter(STLColourModel cm) {
		colourModel = cm;
	}

	public void setScale(float s) {
		scale.set(s, s, s);
	}

	public void setScale(Vec3D s) {
		scale.set(s);
	}

	public void useInvertedNormals(boolean state) {
		useInvertedNormals = state;
	}

	public void beginSave(String fn, int numFaces) {
		try {
			ds = new DataOutputStream(new FileOutputStream(fn));
			writeHeader(numFaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void face(Vec3D a, Vec3D b, Vec3D c) {
		face(a, b, c, 0);
	}

	public void face(Vec3D a, Vec3D b, Vec3D c, int rgb) {
		try {
			// normal
			Vec3D normal = b.sub(a).cross(c.sub(a)).normalize();
			if (useInvertedNormals)
				normal.invert();
			writeVector(normal);
			// vertices
			writeVector(a);
			writeVector(b);
			writeVector(c);
			// vertex attrib (colour)
			if (rgb != 0) {
				writeShort(colourModel.formatRGB(rgb));
			} else {
				writeShort(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void endSave() {
		try {
			ds.flush();
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void writeHeader(int num) throws IOException {
		byte[] header = new byte[80];
		colourModel.formatHeader(header);
		ds.write(header, 0, 80);
		writeInt(num);
	}

	protected void writeShort(int a) throws IOException {
		ds.writeByte(a & 0xff);
		ds.writeByte(a >> 8 & 0xff);
	}

	protected void writeInt(int a) throws IOException {
		prepareBuffer(a);
		ds.write(buf, 0, 4);
	}

	protected void writeFloat(float a) throws IOException {
		prepareBuffer(Float.floatToRawIntBits(a));
		ds.write(buf, 0, 4);
	}

	protected void writeVector(Vec3D v) {
		try {
			writeFloat(v.x * scale.x);
			writeFloat(v.y * scale.y);
			writeFloat(v.z * scale.z);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final void prepareBuffer(int a) {
		buf[0] = (byte) (a >>> 24);
		buf[1] = (byte) (a >> 16 & 0xff);
		buf[2] = (byte) (a >> 8 & 0xff);
		buf[3] = (byte) (a & 0xff);
	}

}
