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

package toxi.audio;

import net.java.games.joal.AL;
import toxi.geom.Vec3D;

/**
 * The concept of a SoundListener refers directly to the user's instance in the
 * virtual (audio) world. By setting the 3D position, velocity and orientation
 * of the listener, the underlying audio hardware can produce a realistic 3D
 * spatial sound simulation (incl. doppler effect, volume falloff etc.).
 * 
 * <p>
 * Like {@link AudioSource}, this class extends {@link Vec3D} and so if the
 * position of the listener is changed via the public x,y,z vector components,
 * the <code>updatePosition()</code> method needs to be called afterwards in
 * order to reflect these changes in the OpenAL context.
 */
public class SoundListener extends Vec3D {

    protected JOALUtil liboal;

    protected final float[] position = { 0.0f, 0.0f, 0.0f };
    protected final float[] velocity = { 0.0f, 0.0f, 0.0f };
    protected final float[] orient = { 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };

    protected SoundListener(JOALUtil lib) {
        super();
        liboal = lib;
        setGain(1f);
        setPosition(position);
        setVelocity(velocity);
        setOrientation(orient);
    }

    public final float[] getOrientation() {
        return orient;
    }

    public final float[] getPosition() {
        return position;
    }

    public final float[] getVelocity() {
        return velocity;
    }

    public SoundListener setGain(float gain) {
        liboal.getAL().alListenerf(AL.AL_GAIN, gain);
        return this;
    }

    public SoundListener setOrientation(float upX, float upY, float upZ,
            float forwardX, float forwardY, float forwardZ) {
        orient[0] = upX;
        orient[1] = upY;
        orient[2] = upZ;
        orient[3] = forwardX;
        orient[4] = forwardY;
        orient[5] = forwardZ;
        liboal.getAL().alListenerfv(AL.AL_ORIENTATION, orient, 0);
        return this;
    }

    public SoundListener setOrientation(float[] o) {
        if (o.length == 6) {
            orient[0] = o[0];
            orient[1] = o[1];
            orient[2] = o[2];
            orient[3] = o[3];
            orient[4] = o[4];
            orient[5] = o[5];
            liboal.getAL().alListenerfv(AL.AL_ORIENTATION, orient, 0);
        } else {
            throw new IllegalArgumentException("wrong number of array elements");
        }
        return this;
    }

    public SoundListener setPosition(float xx, float yy, float zz) {
        x = position[0] = xx;
        y = position[1] = yy;
        z = position[2] = zz;
        liboal.getAL().alListenerfv(AL.AL_POSITION, position, 0);
        return this;
    }

    public SoundListener setPosition(float[] p) {
        if (p.length == 3) {
            x = position[0] = p[0];
            y = position[1] = p[1];
            z = position[2] = p[2];
            liboal.getAL().alListenerfv(AL.AL_POSITION, position, 0);
        } else {
            throw new IllegalArgumentException("wrong number of elements");
        }
        return this;
    }

    public SoundListener setVelocity(float xx, float yy, float zz) {
        velocity[0] = xx;
        velocity[1] = yy;
        velocity[2] = zz;
        liboal.getAL().alListenerfv(AL.AL_VELOCITY, velocity, 0);
        return this;
    }

    public SoundListener setVelocity(float[] v) {
        if (v.length == 3) {
            velocity[0] = v[0];
            velocity[1] = v[1];
            velocity[2] = v[2];
            liboal.getAL().alListenerfv(AL.AL_VELOCITY, velocity, 0);
        } else {
            throw new IllegalArgumentException("wrong number of elements");
        }
        return this;
    }

    public SoundListener updatePosition() {
        position[0] = x;
        position[1] = y;
        position[2] = z;
        liboal.getAL().alListenerfv(AL.AL_POSITION, position, 0);
        return this;
    }
}