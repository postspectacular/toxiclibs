package toxi.data.feeds;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class RSSFeed {

	@XmlAttribute(name = "version")
	public float version;

	public RSSChannel channel;
}
