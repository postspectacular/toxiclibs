package toxi.data.feeds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class AtomEntry {

	@XmlElement(namespace = AtomFeed.NS)
	public String title, id;

	@XmlElement(namespace = AtomFeed.NS)
	public AtomAuthor author;

	// FIXME replace with proper date adapter
	@XmlElement(name = "published", namespace = AtomFeed.NS)
	public String timePublished;

	// FIXME replace with proper date adapter
	@XmlElement(name = "updated", namespace = AtomFeed.NS)
	public String timeUpdated;

	@XmlElement(name = "link", namespace = AtomFeed.NS)
	public ArrayList<AtomLink> links = new ArrayList<AtomLink>();

}
