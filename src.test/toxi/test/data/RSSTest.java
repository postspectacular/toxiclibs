package toxi.test.data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;
import toxi.data.feeds.RSSFeed;
import toxi.data.feeds.RSSItem;

public class RSSTest extends TestCase {

	private RSSFeed rss;

	@Override
	public void setUp() {
		try {
			JAXBContext context = JAXBContext.newInstance(RSSFeed.class);
			File file = new File("test/testrss.xml");
			rss = (RSSFeed) context.createUnmarshaller().unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void testDateParser() {
		XMLGregorianCalendar date = rss.channel.items.get(0).pubDate;
		System.out.println(date);
	}

	public void testDumpAll() {
		for (RSSItem i : rss.channel.items) {
			System.out.println(i.title);
		}
	}

	public void testStripEntities() {
		String stripped = rss.channel.items.get(0).getTitlePlain();
		System.out.println("stripped: " + stripped);
	}

	public void testTitle() {
		String expected = "toxi: @pamela_dust I &lt;3 SIAD and missing the CNC lab too, how about next semester? ;)";
		assertTrue(expected.equals(rss.channel.items.get(0).title));
	}
}