package toxi.data.feeds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class RSSChannel {

	@XmlElement
	public String title;

	@XmlElement
	public String link;

	@XmlElement
	public String description;

	@XmlElement
	public String language;

	@XmlElement(name = "item")
	public ArrayList<RSSItem> items = new ArrayList<RSSItem>();
}
