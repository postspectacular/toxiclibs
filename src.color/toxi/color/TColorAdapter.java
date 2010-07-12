package toxi.color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import toxi.color.TColor;

public class TColorAdapter extends XmlAdapter<String, TColor> {

    @Override
    public String marshal(TColor col) throws Exception {
        if (col != null) {
            return col.toHex();
        } else {
            return "";
        }
    }

    @Override
    public TColor unmarshal(String hex) throws Exception {
        if (hex.length() > 0) {
            return TColor.newHex(hex);
        } else {
            return null;
        }
    }
}
