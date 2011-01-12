/*
 * jgeom: Geometry Library fo Java
 * 
 * Copyright (C) 2005  Samuel Gerber
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package toxi.geom.nurbs;

/**
 * An InterpolationException is thrown if Nurbs could not be interpolated from
 * the given points.
 * 
 * @author sg
 * @version 1.0
 */
public class InterpolationException extends Exception {

    private static final long serialVersionUID = 1L;

    public InterpolationException() {
        super();
    }

    public InterpolationException(String arg0) {
        super(arg0);
    }

    public InterpolationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public InterpolationException(Throwable arg0) {
        super(arg0);
    }

}
