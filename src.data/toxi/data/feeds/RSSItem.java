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

package toxi.data.feeds;

import java.util.ArrayList;
import java.util.List;

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

    @XmlElement(name = "category")
    public List<String> categories = new ArrayList<String>();

    @XmlElement(name = "enclosure")
    public List<RSSEnclosure> enclosures = new ArrayList<RSSEnclosure>();

    public String getDescriptionPlain() {
        return EntityStripper.flattenXML(description);
    }

    public String getTitlePlain() {
        return EntityStripper.flattenXML(title);
    }
}
