package toxi.data.feeds;

import javax.xml.bind.annotation.XmlAttribute;

public class RSSEnclosure {

    @XmlAttribute
    public String url, type;

    @XmlAttribute
    public int length;

    public String toString() {
        return url + " (" + type + ")";
    }

}
