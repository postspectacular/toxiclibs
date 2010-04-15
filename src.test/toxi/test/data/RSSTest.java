package toxi.test.data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;
import toxi.data.feeds.RSSEnclosure;
import toxi.data.feeds.RSSFeed;
import toxi.data.feeds.RSSItem;

public class RSSTest extends TestCase {

    private RSSFeed rss;

    @Override
    public void setUp() {
        try {
            JAXBContext context = JAXBContext.newInstance(RSSFeed.class);
            File file = new File("test/podcast.xml");
            rss = (RSSFeed) context.createUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void testCategories() {
        System.out.println("categories...");
        for (RSSItem i : rss) {
            for (String cat : i.categories) {
                System.out.println(cat);
            }
        }
    }

    public void testDateParser() {
        XMLGregorianCalendar date = rss.channel.items.get(0).pubDate;
        System.out.println(date);
    }

    public void testDumpAll() {
        for (RSSItem i : rss) {
            System.out.println(i.title);
        }
    }

    public void testEnclosures() {
        System.out.println("enclosures...");
        for (RSSItem i : rss) {
            System.out.println(i.title);
            for (RSSEnclosure e : i.enclosures) {
                System.out.println(e);
            }
        }
    }

    public void testStripEntities() {
        String stripped = rss.channel.items.get(0).getTitlePlain();
        System.out.println("stripped: " + stripped);
    }
}