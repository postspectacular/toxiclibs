package toxi.data.feeds;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import toxi.data.feeds.util.EntityStripper;
import toxi.data.feeds.util.Rfc822DateAdapter;

public class RSSItem {

	@XmlElement
	public String title;

	@XmlElement
	public String description;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(Rfc822DateAdapter.class)
	public XMLGregorianCalendar pubDate;

	@XmlElement
	public String guid;

	@XmlElement
	public String link;

	public String getDescriptionPlain() {
		return EntityStripper.flattenXML(description);
	}

	public String getTitlePlain() {
		return EntityStripper.flattenXML(title);
	}
}
