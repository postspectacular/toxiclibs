package toxi.newmesh;

import java.util.ArrayList;
import java.util.List;

public class AttributedEdge {

    public final int a, b;
    public List<AttributedFace> faces;

    public AttributedEdge(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public void addFace(AttributedFace f) {
        if (faces == null) {
            faces = new ArrayList<AttributedFace>(2);
        }
        faces.add(f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AttributedEdge other = (AttributedEdge) obj;
        if (a != other.a) {
            return false;
        }
        if (b != other.b) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + a;
        result = prime * result + b;
        return result;
    }

    public String toString() {
        return String.format("a=%d, b=%d", a, b);
    }
}
