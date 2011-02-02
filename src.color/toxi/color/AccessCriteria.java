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

package toxi.color;

import java.util.Comparator;

/**
 * Defines standard color component access criterias and associated comparators
 * used to sort colors based on component values. If a new custom accessor is
 * needed (e.g. for sub-classes TColor's), then simply sub-class this class and
 * implement the {@link Comparator} interface and the 2 abstract getter & setter
 * methods defined by this class.
 */
public abstract class AccessCriteria implements Comparator<ReadonlyTColor> {

    public static final AccessCriteria HUE = new HSVAccessor(0);
    public static final AccessCriteria SATURATION = new HSVAccessor(1);
    public static final AccessCriteria BRIGHTNESS = new HSVAccessor(2);

    public static final AccessCriteria RED = new RGBAccessor(0);
    public static final AccessCriteria GREEN = new RGBAccessor(1);
    public static final AccessCriteria BLUE = new RGBAccessor(2);

    public static final AccessCriteria CYAN = new CMYKAccessor(0);
    public static final AccessCriteria MAGENTA = new CMYKAccessor(1);
    public static final AccessCriteria YELLOW = new CMYKAccessor(2);
    public static final AccessCriteria BLACK = new CMYKAccessor(3);

    public static final AccessCriteria ALPHA = new AlphaAccessor();
    public static final AccessCriteria LUMINANCE = new LuminanceAccessor();

    public abstract float getComponentValueFor(ReadonlyTColor col);

    public abstract void setComponentValueFor(TColor col, float value);
}
