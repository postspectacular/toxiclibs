package toxi.data.feeds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "feed", namespace = "http://www.w3.org/2005/Atom")
public class AtomFeed {

	public static final String NS = "http://www.w3.org/2005/Atom";
	@XmlElement(namespace = NS)
	public String id, title;

	// FIXME replace with proper date adapter
	@XmlElement(name = "updated", namespace = NS)
	public String lastUpdated;

	@XmlElement(name = "link", namespace = NS)
	public ArrayList<AtomLink> links = new ArrayList<AtomLink>();

	@XmlElement(name = "entry", namespace = NS)
	public ArrayList<AtomEntry> entries = new ArrayList<AtomEntry>();
}
