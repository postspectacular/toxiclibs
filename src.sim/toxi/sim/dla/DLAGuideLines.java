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

package toxi.sim.dla;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class DLAGuideLines {

    protected static final Logger logger = Logger.getLogger(DLAGuideLines.class
            .getName());

    public SortedSet<DLASegment> segments;

    public Iterator<DLASegment> iterator;

    private double currT;

    private DLASegment currSegment;

    private Vec3D currPoint;

    private Vec3D workDir;

    public DLAGuideLines() {
        this(new PipelineOrder());
    }

    public DLAGuideLines(Comparator<Line3D> comparator) {
        segments = new TreeSet<DLASegment>(comparator);
    }

    /**
     * 
     * @deprecated use {@link #addPointList(List)} instead
     * @param points
     * @return itself
     */
    @Deprecated
    public DLAGuideLines addCurveStrip(List<Vec3D> points) {
        return addPointList(points);
    }

    public DLAGuideLines addLine(Line3D l) {
        return addLine(l.a, l.b);
    }

    public DLAGuideLines addLine(Vec3D a, Vec3D b) {
        DLASegment s = new DLASegment(a, b, null);
        if (logger.isLoggable(Level.INFO)) {
            logger.info("adding line segment: " + s);
        }
        segments.add(s);
        return this;
    }

    public DLAGuideLines addPoint(Vec3D p) {
        return addLine(p, p);
    }

    public DLAGuideLines addPointList(List<Vec3D> points) {
        int numP = points.size();
        for (int i = 1; i < numP; i++) {
            Vec3D p = i < numP - 1 ? points.get(i + 1) : null;
            DLASegment s = new DLASegment(points.get(i - 1), points.get(i), p);
            if (logger.isLoggable(Level.INFO)) {
                logger.info("adding line segment: " + s);
            }
            segments.add(s);
        }
        return this;
    }

    public double getCurrentSegmentPos() {
        return currT;
    }

    public Vec3D getDirection() {
        if (workDir == null) {
            getPoint();
        }
        return workDir;
    }

    public Vec3D getPoint() {
        workDir = currSegment.getDirection().interpolateToSelf(
                currSegment.getNextDirection(), (float) currT);
        workDir.normalize();
        Vec3D v = currPoint.add(workDir.scale(currSegment.getLength()
                * (float) currT));
        return v;
    }

    public boolean isComplete() {
        return !iterator.hasNext() && currT >= 1.0;
    }

    public DLAGuideLines reset() {
        iterator = segments.iterator();
        currT = 0;
        currSegment = iterator.next();
        currPoint = currSegment.a.copy();
        return this;
    }

    public DLASegment updatePoint(double delta) {
        currT += delta;
        if (currT >= 1.0) {
            if (iterator.hasNext()) {
                currT -= 1.0;
                currSegment = iterator.next();
                currPoint = currSegment.a.copy();
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("next segment: " + currSegment);
                }
            }
        }
        return currSegment;
    }
}
