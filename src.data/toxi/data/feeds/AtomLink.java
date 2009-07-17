package toxi.data.feeds;

import javax.xml.bind.annotation.XmlAttribute;

public class AtomLink {

	@XmlAttribute
	public String type, rel, href;
}
