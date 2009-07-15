package toxi.data.feeds.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;

public class Rfc822DateAdapter extends XmlAdapter<String, XMLGregorianCalendar> {

	public static final SimpleDateFormat rfc822DateFormats[] = new SimpleDateFormat[] {
			new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z"),
			new SimpleDateFormat("EEE, d MMM yy HH:mm z"),
			new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z"),
			new SimpleDateFormat("EEE, d MMM yyyy HH:mm z"),
			new SimpleDateFormat("d MMM yy HH:mm z"),
			new SimpleDateFormat("d MMM yy HH:mm:ss z"),
			new SimpleDateFormat("d MMM yyyy HH:mm z"),
			new SimpleDateFormat("d MMM yyyy HH:mm:ss z"), };

	@Override
	public String marshal(XMLGregorianCalendar date) throws Exception {
		return rfc822DateFormats[0].format(date);
	}

	@Override
	public XMLGregorianCalendar unmarshal(String dateString) throws Exception {
		XMLGregorianCalendar calendar = null;
		GregorianCalendar cal = new GregorianCalendar();
		for (SimpleDateFormat f : rfc822DateFormats) {
			try {
				Date d = f.parse(dateString);
				cal.setTime(d);
				DatatypeFactory dataType = DatatypeFactoryImpl.newInstance();
				calendar = dataType.newXMLGregorianCalendar(cal);
				break;
			} catch (ParseException e) {
			}
		}
		return calendar;
	}
}
