package toxi.test.data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import toxi.data.feeds.RSSFeed;
import toxi.data.feeds.RSSItem;

public class RSSTest {

	public static void load() {
		try {
			JAXBContext context = JAXBContext.newInstance(RSSFeed.class);
			File file = new File("test/rssfeed.xml");

			RSSFeed rss = (RSSFeed) context.createUnmarshaller()
					.unmarshal(file);
			for (RSSItem i : rss.channel.items) {
				System.out.println(i.title);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		load();
	}
}
