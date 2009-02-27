package toxi.geom.util;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import toxi.geom.Vec3D;

/**
 * A simple, but flexible and memory efficient exporter for binary STL files.
 * Custom color support is implemented via the STLcolorModel interface and the
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

	public static final STLColorModel DEFAULT = new DefaultSTLColorModel();

	public static final STLColorModel MATERIALISE = new MaterialiseSTLColorModel(
			0xffffffff);

	protected DataOutputStream ds;

	protected Vec3D scale = new Vec3D(1, 1, 1);

	protected boolean useInvertedNormals = false;

	protected byte[] buf = new byte[4];

	protected STLColorModel colorModel;

	public STLWriter() {
		this(DEFAULT);
	}

	public STLWriter(STLColorModel cm) {
		colorModel = cm;
	}

	public void beginSave(String fn, int numFaces) {
		try {
			ds = new DataOutputStream(new FileOutputStream(fn));
			writeHeader(numFaces);
		} catch (Exception e) {
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

	public void face(Vec3D a, Vec3D b, Vec3D c) {
		face(a, b, c, 0);
	}

	public void face(Vec3D a, Vec3D b, Vec3D c, int rgb) {
		try {
			// normal
			Vec3D normal = b.sub(a).cross(c.sub(a)).normalize();
			if (useInvertedNormals) {
				normal.invert();
			}
			writeVector(normal);
			// vertices
			writeVector(a);
			writeVector(b);
			writeVector(c);
			// vertex attrib (color)
			if (rgb != 0) {
				writeShort(colorModel.formatRGB(rgb));
			} else {
				writeShort(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final void prepareBuffer(int a) {
		buf[3] = (byte) (a >>> 24);
		buf[2] = (byte) (a >> 16 & 0xff);
		buf[1] = (byte) (a >> 8 & 0xff);
		buf[0] = (byte) (a & 0xff);
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

	protected void writeFloat(float a) throws IOException {
		prepareBuffer(Float.floatToRawIntBits(a));
		ds.write(buf, 0, 4);
	}

	protected void writeHeader(int num) throws IOException {
		byte[] header = new byte[80];
		colorModel.formatHeader(header);
		ds.write(header, 0, 80);
		writeInt(num);
	}

	protected void writeInt(int a) throws IOException {
		prepareBuffer(a);
		ds.write(buf, 0, 4);
	}

	protected void writeShort(int a) throws IOException {
		ds.writeByte(a & 0xff);
		ds.writeByte(a >> 8 & 0xff);
	}

	protected void writeVector(Vec3D v) {
		try {
			writeFloat(v.x * scale.x);
			writeFloat(v.y * scale.y);
			writeFloat(v.z * scale.z);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
