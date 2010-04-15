package toxi.data.feeds;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import toxi.data.feeds.util.EntityStripper;
import toxi.data.feeds.util.Rfc822DateAdapter;

public class RSSItem {

    @XmlElement
    public String title;

    @XmlElement
    public String description;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(Rfc822DateAdapter.class)
    public XMLGregorianCalendar pubDate;

    @XmlElement
    public String guid;

    @XmlElement
    public String link;

    @XmlElement(name = "category")
    public List<String> categories = new ArrayList<String>();

    @XmlElement(name = "enclosure")
    public List<RSSEnclosure> enclosures = new ArrayList<RSSEnclosure>();

    public String getDescriptionPlain() {
        return EntityStripper.flattenXML(description);
    }

    public String getTitlePlain() {
        return EntityStripper.flattenXML(title);
    }
}
