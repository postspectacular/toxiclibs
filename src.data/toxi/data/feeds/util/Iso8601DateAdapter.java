/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

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

    public static final SimpleDateFormat[] ISO8601_FORMATS = new SimpleDateFormat[] {
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
                Date d = ISO8601_FORMATS[0]
                        .parse(dateString + "GMT" + timeZone);
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
