package toxi.test.data;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;
import toxi.data.feeds.AtomEntry;
import toxi.data.feeds.AtomFeed;
import toxi.data.feeds.AtomLink;
import toxi.data.feeds.util.Iso8601DateAdapter;

public class AtomTest extends TestCase {

    private AtomFeed feed;

    @Override
    public void setUp() {
        try {
            JAXBContext context = JAXBContext.newInstance(AtomFeed.class);
            // File file = new File("test/testatom.xml");
            File file = new File("test/flickr.atom");
            feed = (AtomFeed) context.createUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void testDateParser() {
        XMLGregorianCalendar date = feed.entries.get(0).timePublished;
        System.out.println(date);
    }

    public void testDumpAll() {
        for (AtomEntry i : feed.entries) {
            System.out.println(i.title);
        }
    }

    public void testEnclosures() {
        for (AtomEntry e : feed) {
            System.out.println(e.title);
            List<AtomLink> enc = e.getEnclosuresForType("image/jpeg");
            for (AtomLink l : enc) {
                System.out.println("\t" + l);
            }
        }
    }

    public void testIso8601Format() {
        Iso8601DateAdapter format = new Iso8601DateAdapter();
        String[] dates = new String[] { "2009-07-16T15:45:07Z",
                "2009-07-22T21:13:45-07:00" };
        for (String dateString : dates) {
            XMLGregorianCalendar date = null;
            try {
                date = format.unmarshal(dateString);
            } catch (Exception e) {
            }
            System.out.println(dateString + ": " + date);
            assertNotNull(date);
        }
    }
}