package toxi.data.feeds;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import toxi.data.feeds.util.Iso8601DateAdapter;

public class AtomEntry {

    @XmlElement(namespace = AtomFeed.NS)
    public String title, id;

    @XmlElement(namespace = AtomFeed.NS)
    public AtomAuthor author;

    @XmlElement(namespace = AtomFeed.NS)
    public AtomContent content;

    @XmlElement(name = "published", namespace = AtomFeed.NS)
    @XmlJavaTypeAdapter(Iso8601DateAdapter.class)
    public XMLGregorianCalendar timePublished;

    @XmlElement(name = "updated", namespace = AtomFeed.NS)
    @XmlJavaTypeAdapter(Iso8601DateAdapter.class)
    public XMLGregorianCalendar timeUpdated;

    @XmlElement(name = "link", namespace = AtomFeed.NS)
    public List<AtomLink> links = new ArrayList<AtomLink>();

    /**
     * Returns a list of {@link AtomLink}s to enclosed items of the given MIME
     * type.
     * 
     * @param type
     *            MIME type or null to retrieve all enclosures
     * @return list
     */
    public List<AtomLink> getEnclosuresForType(String type) {
        List<AtomLink> enc = null;
        for (AtomLink l : links) {
            if (l.rel.equalsIgnoreCase("enclosure")) {
                if (type == null || l.type.equalsIgnoreCase(type)) {
                    if (enc == null) {
                        enc = new ArrayList<AtomLink>();
                    }
                    enc.add(l);
                }
            }
        }
        return enc;
    }
}
