package toxi.data.feeds;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class AtomContent {

    @XmlAttribute(namespace = AtomFeed.NS)
    public String type;

    @XmlValue
    public String value;
}
