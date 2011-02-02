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

/**
 * Compares 2 colors by one of their CMYK component values.
 */
public class CMYKAccessor extends AccessCriteria {

    private final int component;

    public CMYKAccessor(int comp) {
        component = comp;
    }

    public int compare(ReadonlyTColor a, ReadonlyTColor b) {
        float ca, cb;
        switch (component) {
            case 0:
                ca = a.cyan();
                cb = b.cyan();
                break;
            case 1:
                ca = a.magenta();
                cb = b.magenta();
                break;
            case 2:
                ca = a.yellow();
                cb = b.yellow();
                break;
            case 3:
            default:
                ca = a.black();
                cb = b.black();
        }
        return Float.compare(ca, cb);
    }

    public float getComponentValueFor(ReadonlyTColor col) {
        float comp;
        switch (component) {
            case 0:
                comp = col.cyan();
                break;
            case 1:
                comp = col.magenta();
                break;
            case 2:
                comp = col.yellow();
                break;
            case 3:
            default:
                comp = col.black();
        }
        return comp;
    }

    public void setComponentValueFor(TColor col, float val) {
        switch (component) {
            case 0:
                col.setCyan(val);
                break;
            case 1:
                col.setMagenta(val);
                break;
            case 2:
                col.setYellow(val);
                break;
            case 3:
            default:
                col.setBlack(val);
        }
    }
}
