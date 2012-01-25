package toxi.newmesh;

import java.util.HashMap;

public class AttributedFace {

    public int a, b, c;
    public int normal = -1;
    public HashMap<String, int[]> attribs;

    public AttributedFace(int a, int b, int c, HashMap<String, int[]> attribs) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.attribs = attribs;
    }

    public String toString() {
        return String.format("a=%d,b=%d,c=%d,n=%d", a, b, c, normal);
    }
}
