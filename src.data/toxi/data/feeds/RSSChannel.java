package toxi.data.feeds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import toxi.data.feeds.util.Rfc822DateAdapter;

public class RSSChannel {

	@XmlElement
	public String title;

	@XmlElement
	public String link;

	@XmlElement
	public String description;

	@XmlElement
	public String language;

	@XmlElement
	public String generator;

	@XmlElement
	@XmlJavaTypeAdapter(Rfc822DateAdapter.class)
	public XMLGregorianCalendar pubDate, lastBuiltDate;

	@XmlElement(name = "item")
	public ArrayList<RSSItem> items = new ArrayList<RSSItem>();
}
