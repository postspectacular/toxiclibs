package toxi.data.feeds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import toxi.data.feeds.util.Iso8601DateAdapter;

@XmlRootElement(name = "feed", namespace = AtomFeed.NS)
public class AtomFeed {

    public static final String NS = "http://www.w3.org/2005/Atom";

    public static AtomFeed newFromStream(InputStream stream) {
        AtomFeed feed = null;
        try {
            JAXBContext context = JAXBContext.newInstance(AtomFeed.class);
            feed = (AtomFeed) context.createUnmarshaller().unmarshal(stream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return feed;
    }

    public static AtomFeed newFromURL(String url) {
        AtomFeed feed = null;
        try {
            feed = newFromStream(new URL(url).openStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feed;
    }

    @XmlElement(namespace = NS)
    public String id, title;

    @XmlElement(name = "updated", namespace = NS)
    @XmlJavaTypeAdapter(Iso8601DateAdapter.class)
    public XMLGregorianCalendar lastUpdated;

    @XmlElement(name = "link", namespace = NS)
    public ArrayList<AtomLink> links = new ArrayList<AtomLink>();

    @XmlElement(name = "entry", namespace = NS)
    public ArrayList<AtomEntry> entries = new ArrayList<AtomEntry>();
}
