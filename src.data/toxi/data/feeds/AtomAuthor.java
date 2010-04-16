package toxi.data.feeds;

import javax.xml.bind.annotation.XmlElement;

public class AtomAuthor {

    @XmlElement(namespace = AtomFeed.NS)
    public String name, uri;
}
