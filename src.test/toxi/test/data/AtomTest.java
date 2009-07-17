package toxi.test.data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import junit.framework.TestCase;
import toxi.data.feeds.AtomEntry;
import toxi.data.feeds.AtomFeed;

public class AtomTest extends TestCase {

	private AtomFeed feed;

	@Override
	public void setUp() {
		try {
			JAXBContext context = JAXBContext.newInstance(AtomFeed.class);
			File file = new File("test/testatom.xml");
			feed = (AtomFeed) context.createUnmarshaller().unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void testDateParser() {
		String date = feed.entries.get(0).timePublished;
		System.out.println(date);
	}

	public void testDumpAll() {
		for (AtomEntry i : feed.entries) {
			System.out.println(i.title);
		}
	}
}