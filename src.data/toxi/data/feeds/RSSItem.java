package toxi.data.feeds;

import javax.xml.bind.annotation.XmlElement;

public class RSSItem {

	@XmlElement
	public String title;

	@XmlElement
	public String description;

	@XmlElement
	public String pubDate;

	@XmlElement
	public String guid;

	@XmlElement
	public String link;
}
