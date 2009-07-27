package toxi.data.feeds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import toxi.data.feeds.util.Iso8601DateAdapter;

public class AtomEntry {

	@XmlElement(namespace = AtomFeed.NS)
	public String title, id;

	@XmlElement(namespace = AtomFeed.NS)
	public AtomAuthor author;

	// FIXME replace with proper date adapter
	@XmlElement(name = "published", namespace = AtomFeed.NS)
	@XmlJavaTypeAdapter(Iso8601DateAdapter.class)
	public XMLGregorianCalendar timePublished;

	// FIXME replace with proper date adapter
	@XmlElement(name = "updated", namespace = AtomFeed.NS)
	@XmlJavaTypeAdapter(Iso8601DateAdapter.class)
	public XMLGregorianCalendar timeUpdated;

	@XmlElement(name = "link", namespace = AtomFeed.NS)
	public ArrayList<AtomLink> links = new ArrayList<AtomLink>();

}
