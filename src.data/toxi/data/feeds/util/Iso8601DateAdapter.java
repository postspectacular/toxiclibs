package toxi.data.feeds.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;

public class Iso8601DateAdapter extends
        XmlAdapter<String, XMLGregorianCalendar> {

    public static final SimpleDateFormat[] ISO8601_FORMATS =
            new SimpleDateFormat[] {
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") };

    @Override
    public String marshal(XMLGregorianCalendar date) throws Exception {
        return ISO8601_FORMATS[0].format(date);
    }

    @Override
    public XMLGregorianCalendar unmarshal(String dateString) throws Exception {
        XMLGregorianCalendar calendar = null;
        GregorianCalendar cal = new GregorianCalendar();
        DatatypeFactory dataType = DatatypeFactoryImpl.newInstance();
        for (SimpleDateFormat f : ISO8601_FORMATS) {
            try {
                Date d = f.parse(dateString);
                cal.setTime(d);
                calendar = dataType.newXMLGregorianCalendar(cal);
                break;
            } catch (ParseException e) {
            }
        }
        if (calendar == null) {
            String timeZone = dateString.substring(dateString.length() - 6);
            dateString = dateString.substring(0, dateString.length() - 6);
            try {
                Date d =
                        ISO8601_FORMATS[0].parse(dateString + "GMT" + timeZone);
                cal.setTime(d);
                calendar = dataType.newXMLGregorianCalendar(cal);
            } catch (ParseException e) {
                cal.setTimeInMillis(0);
                calendar = dataType.newXMLGregorianCalendar(cal);
            }
        }
        return calendar;
    }
}
