package toxi.data.feeds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class RSSFeed {

    public static RSSFeed newFromStream(InputStream stream) {
        RSSFeed feed = null;
        try {
            JAXBContext context = JAXBContext.newInstance(RSSFeed.class);
            feed = (RSSFeed) context.createUnmarshaller().unmarshal(stream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return feed;
    }

    public static RSSFeed newFromURL(String url) {
        RSSFeed feed = null;
        try {
            feed = newFromStream(new URL(url).openStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feed;
    }

    @XmlAttribute(name = "version")
    public float version;

    public RSSChannel channel;
}
