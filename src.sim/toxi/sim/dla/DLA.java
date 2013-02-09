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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import toxi.geom.PointOctree;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class DLA {

    protected static final Logger logger = Logger
            .getLogger(DLA.class.getName());

    protected int numParticles;
    protected DLAParticle currParticle;
    protected PointOctree octree, octreeGuides;

    protected Vec3D currCurvePoint;
    protected Vec3D dirCurvePoint;
    protected Vec3D minBounds, maxBounds;

    protected DLAGuideLines guidelines;
    protected ArrayList<DLASegment> activeSegments = new ArrayList<DLASegment>();

    protected List<DLAEventListener> listeners = new ArrayList<DLAEventListener>();

    protected int numActiveSegments = 0;

    protected DLAConfiguration config;

    public DLA(float size) {
        octree = createOctree(new Vec3D(-0.5f, -0.5f, -0.5f).scale(size), size);
        octreeGuides = createOctree(new Vec3D(-0.5f, -0.5f, -0.5f).scale(size),
                size);
        minBounds = Vec3D.MAX_VALUE.copy();
        maxBounds = Vec3D.NEG_MAX_VALUE.copy();
    }

    public DLA(float size, DLAConfiguration config, DLAGuideLines guides) {
        this(size);
        this.config = config;
        if (guides != null) {
            this.guidelines = guides;
            parseGuidelines();
            updateCurvePoint();
        }
    }

    public DLA addListener(DLAEventListener l) {
        listeners.add(l);
        logger.info("adding listener: " + l);
        return this;
    }

    public void addParticle(Vec3D p) {
        if (octree.addPoint(p.copy())) {
            numParticles++;
            minBounds.minSelf(p);
            maxBounds.maxSelf(p);
            if (listeners != null) {
                for (DLAEventListener l : listeners) {
                    l.dlaNewParticleAdded(this, p);
                }
            }
        }
    }

    protected void alignAttachedParticle(DLAParticle p, Vec3D target) {
        Vec3D d = p.sub(target).normalize();
        d.interpolateToSelf(dirCurvePoint, config.getCurveAlign());
        d.scaleSelf(config.getGrowthScale());
        d.normalizeTo(config.getParticleRadius());
        p.set(target).addSelf(d);
    }

    /**
     * Checks if the given particle is close to an existing one or a curve. If
     * so, the particle is attached based on the current DLA parameters and the
     * method return true.
     * 
     * @param p
     * @return true, if particle attached.
     */
    protected boolean checkParticle(DLAParticle p) {
        List<Vec3D> parts = octree
                .getPointsWithinSphere(p, config.snapDistance);
        float stickiness = config.getStickiness();
        if (parts != null) {
            float minDist = Integer.MAX_VALUE;
            Vec3D found = null;
            for (Vec3D pp : parts) {
                float d = pp.distanceToSquared(p);
                if (d < minDist) {
                    minDist = d;
                    found = pp;
                }
            }
            if (minDist < config.getSnapDistanceSquared()
                    && Math.random() < stickiness) {
                alignAttachedParticle(p, found);
                addParticle(p);
                return true;
            }
        }
        if (p.sub(currCurvePoint).magSquared() < config
                .getCurveAttachDistanceSquared()) {
            parts = octreeGuides.getPointsWithinSphere(p,
                    config.getCurveAttachDistance());
            if (parts != null) {
                for (int i = parts.size(); i > 0; i--) {
                    if (Math.random() < stickiness) {
                        addParticle(p);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clear() {
        octree.empty();
        octreeGuides.empty();
        reset();
    }

    protected PointOctree createOctree(Vec3D origin, float size) {
        return new PointOctree(origin, size);
    }

    /**
     * @return the config
     */
    public DLAConfiguration getConfig() {
        return config;
    }

    /**
     * @return the currCurvePoint
     */
    public Vec3D getCurrentCurvePoint() {
        return currCurvePoint;
    }

    /**
     * @return the currParticle
     */
    public DLAParticle getCurrentParticle() {
        return currParticle;
    }

    /**
     * @return the guidelines
     */
    public DLAGuideLines getGuidelines() {
        return guidelines;
    }

    /**
     * @return the octreeGuides
     */
    public PointOctree getGuideOctree() {
        return octreeGuides;
    }

    /**
     * @return the numActiveSegments
     */
    public int getNumActiveSegments() {
        return numActiveSegments;
    }

    /**
     * @return the numParticles
     */
    public int getNumParticles() {
        return numParticles;
    }

    public PointOctree getParticleOctree() {
        return octree;
    }

    /**
     * @return the octree
     */
    public List<Vec3D> getParticles() {
        return octree.getPoints();
    }

    protected void parseGuidelines() {
        guidelines.reset();
        octreeGuides.empty();
        while (!guidelines.isComplete()) {
            double density = config.getGuideLineDensity();
            guidelines.updatePoint(density);
            Vec3D p = guidelines.getPoint();
            octreeGuides.addPoint(p);
        }
        guidelines.reset();
    }

    public DLA removeListener(DLAEventListener l) {
        listeners.remove(l);
        logger.info("removing listener: " + l);
        return this;
    }

    public void reset() {
        guidelines.reset();
        updateCurvePoint();
    }

    public void save(String fname, boolean isCentered) {
        List<Vec3D> parts = octree.getPoints();
        if (parts != null) {
            Vec3D origin = minBounds.add(maxBounds).scaleSelf(0.5f);
            logger.info("bounds: " + minBounds + " -> " + maxBounds
                    + " offset origin: " + origin);
            try {
                RandomAccessFile file = new RandomAccessFile(fname, "rw");
                FileChannel channel = file.getChannel();
                int size = parts.size() * 4 * 3;
                MappedByteBuffer buffer = channel.map(
                        FileChannel.MapMode.READ_WRITE, 0, size);
                if (isCentered) {
                    for (Vec3D p : parts) {
                        p = p.sub(origin);
                        buffer.putFloat(p.x);
                        buffer.putFloat(p.y);
                        buffer.putFloat(p.z);
                    }
                } else {
                    for (Vec3D p : parts) {
                        buffer.putFloat(p.x);
                        buffer.putFloat(p.y);
                        buffer.putFloat(p.z);
                    }
                }
                buffer.force();
                channel.close();
                file.close();
                logger.info("written " + parts.size() + " particles to "
                        + fname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsText(String fname, boolean isCentered) {
        List<Vec3D> particles = octree.getPoints();
        if (particles != null) {
            Vec3D origin = minBounds.add(maxBounds).scaleSelf(0.5f);
            logger.info("bounds: " + minBounds + " -> " + maxBounds
                    + " offset origin: " + origin);
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(fname));
                for (Iterator<Vec3D> i = octree.getPoints().iterator(); i
                        .hasNext();) {
                    Vec3D p = i.next();
                    StringBuilder sb = new StringBuilder(36);
                    sb.append(p.x).append(',').append(p.y).append(',')
                            .append(p.z).append("\n");
                    out.write(sb.toString());
                }
                out.close();
                logger.info("written " + particles.size() + " particles to "
                        + fname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(DLAConfiguration config) {
        this.config = config;
    }

    /**
     * @param guidelines
     *            the guidelines to set
     */
    public void setGuidelines(DLAGuideLines guidelines) {
        this.guidelines = guidelines;
        parseGuidelines();
        updateCurvePoint();
    }

    public void update() {
        if (currParticle == null) {
            Vec3D spawnPos = Vec3D.randomVector();
            spawnPos = currCurvePoint.add(spawnPos.scale(MathUtils
                    .random(config.getSpawnRadius())));
            currParticle = new DLAParticle(spawnPos, config.getEscapeRadius(),
                    config.getParticleSpeed(), config.getSearchSpeed());
        }
        currParticle.update(currCurvePoint);
        if (checkParticle(currParticle)) {
            currParticle = null;
            updateCurvePoint();
        }
    }

    public void update(int numIterations) {
        for (int i = 0; i < numIterations; i++) {
            update();
        }
    }

    protected void updateCurvePoint() {
        if (Math.random() < config.getContinuousGrowthRatio()
                && numActiveSegments > 0) {
            DLASegment segment = activeSegments.get((int) (config
                    .getContinuousGrowthCoeff() * (numActiveSegments - 1)));
            float currT = MathUtils.random(1f);
            dirCurvePoint = segment.getDirectionAt(currT);
            currCurvePoint = segment.a.add(dirCurvePoint.scale(segment
                    .getLength() * currT));
        } else {
            DLASegment s = guidelines.updatePoint(config.getCurveSpeed());
            if (!activeSegments.contains(s)) {
                activeSegments.add(s);
                numActiveSegments++;
                if (listeners != null) {
                    for (DLAEventListener l : listeners) {
                        l.dlaSegmentSwitched(this, s);
                    }
                }
            }
            currCurvePoint = guidelines.getPoint();
            dirCurvePoint = guidelines.getDirection();
        }
        if (guidelines.isComplete()) {
            guidelines.reset();
            if (listeners != null) {
                for (DLAEventListener l : listeners) {
                    l.dlaAllSegmentsProcessed(this);
                }
            }
        }
    }
}