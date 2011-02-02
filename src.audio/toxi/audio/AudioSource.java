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
import toxi.math.MathUtils;

/**
 * A wrapper for {@link AudioBuffer}s and similar to the built in JOAL
 * net.java.games.sound3d.Source, though less restrictive. The class extends
 * {@link Vec3D} and so can be used to position the sound in 3D space (provided
 * the underlying audio hardware does support 3D audio). Unfortunately due to
 * OpenAL's limitations only mono samples can be positioned in that way. Stereo
 * samples will can only manipulated in terms of gain/volume.
 * 
 * <p>
 * If the position of an AudioSource is changed via the public x,y,z vector
 * components, the <code>updatePosition()</code> method needs to be called
 * afterwards in order to reflect the changes in the OpenAL context.
 */
public class AudioSource extends Vec3D {

    protected AL al;

    protected AudioBuffer buffer;

    protected final int id;
    protected int size;

    protected final float[] position = { 0.0f, 0.0f, 0.0f };
    protected final float[] velocity = { 0.0f, 0.0f, 0.0f };
    protected final float[] direction = { 0.0f, 0.0f, 0.0f };

    protected int[] alResult = new int[1];

    protected boolean isLooping;

    public AudioSource(AL al, int id) {
        this(al, id, null);
    }

    public AudioSource(AL al, int id, AudioBuffer buf) {
        super();
        this.al = al;
        this.id = id;
        setBuffer(buf);
    }

    /**
     * Deletes this source, and free its resources. Note, this method does NOT
     * release the associated audio buffer. If you want to remove a source
     * including its wave data use
     * {@link JOALUtil#deleteSource(AudioSource, boolean)} instead.
     * 
     * @return true, if source was removed successfully
     */
    public boolean delete() {
        stop();
        setBuffer(null);
        al.alDeleteSources(1, new int[] { id }, 0);
        return al.alGetError() == AL.AL_NO_ERROR;
    }

    /**
     * Gets the buffer associated with this source.
     * 
     * @return the buffer associated with this source
     */
    public AudioBuffer getBuffer() {
        return buffer;
    }

    /**
     * Gets the number of buffers already processed on this source.
     * 
     * @return the number of buffers already processed on this source.
     */
    public int getBuffersProcessed() {
        al.alGetSourcei(id, AL.AL_BUFFERS_PROCESSED, alResult, 0);
        return alResult[0];
    }

    public final float[] getDirection() {
        return direction;
    }

    public final int getID() {
        return id;
    }

    public final int getOffset() {
        al.alGetSourcei(id, AL.AL_SAMPLE_OFFSET, alResult, 0);
        return alResult[0];
    }

    public final float[] getPosition() {
        return position;
    }

    public final float[] getVelocity() {
        return velocity;
    }

    public final boolean isLooping() {
        return isLooping;
    }

    public final int length() {
        return size;
    }

    public AudioSource play() {
        if (buffer != null) {
            al.alSourcePlay(id);
        }
        return this;
    }

    public AudioSource rewind() {
        if (buffer != null) {
            al.alSourceRewind(id);
        }
        return this;
    }

    /**
     * Sets the buffer associated with this source.
     * 
     * @param buffer
     *            the buffer associated with this source
     */
    public AudioSource setBuffer(AudioBuffer buffer) {
        this.buffer = buffer;
        if (buffer != null) {
            al.alSourcei(id, AL.AL_BUFFER, buffer.getID());
            size = buffer.getSampleSize();
        } else {
            al.alSourcei(id, AL.AL_BUFFER, AL.AL_NONE);
            size = 0;
        }
        return this;
    }

    public AudioSource setDirection(float xx, float yy, float zz) {
        direction[0] = xx;
        direction[1] = yy;
        direction[2] = zz;
        al.alSourcefv(id, AL.AL_DIRECTION, direction, 0);
        return this;
    }

    public AudioSource setDirection(float[] d) {
        if (d.length == 3) {
            direction[0] = d[0];
            direction[1] = d[1];
            direction[2] = d[2];
            al.alSourcefv(id, AL.AL_DIRECTION, direction, 0);
        } else {
            throw new IllegalArgumentException("wrong number of array elements");
        }
        return this;
    }

    public AudioSource setDirection(Vec3D dir) {
        return setDirection(dir.x, dir.y, dir.z);
    }

    public AudioSource setGain(float gain) {
        al.alSourcef(id, AL.AL_GAIN, gain);
        return this;
    }

    public AudioSource setLooping(boolean state) {
        isLooping = state;
        al.alSourcei(id, AL.AL_LOOPING, (state ? AL.AL_TRUE : AL.AL_FALSE));
        return this;
    }

    public AudioSource setOffset(int off) {
        off = MathUtils.clip(off, 0, size - 1);
        al.alSourcei(id, AL.AL_SAMPLE_OFFSET, off);
        return this;
    }

    public AudioSource setPitch(float pitch) {
        al.alSourcef(id, AL.AL_PITCH, pitch);
        return this;
    }

    public AudioSource setPosition(float xx, float yy, float zz) {
        position[0] = xx;
        position[1] = yy;
        position[2] = zz;
        al.alSourcefv(id, AL.AL_POSITION, position, 0);
        return this;
    }

    public AudioSource setPosition(float[] p) {
        if (p.length == 3) {
            x = position[0] = p[0];
            y = position[1] = p[1];
            z = position[2] = p[2];
            al.alSourcefv(id, AL.AL_POSITION, position, 0);
        } else {
            throw new IllegalArgumentException("wrong number of array elements");
        }
        return this;
    }

    public AudioSource setPosition(Vec3D p) {
        return setPosition(p.x, p.y, p.z);
    }

    public AudioSource setReferenceDistance(float d) {
        al.alSourcef(id, AL.AL_REFERENCE_DISTANCE, d);
        return this;
    }

    public AudioSource setVelocity(float xx, float yy, float zz) {
        velocity[0] = xx;
        velocity[1] = yy;
        velocity[2] = zz;
        al.alSourcefv(id, AL.AL_VELOCITY, velocity, 0);
        return this;
    }

    public AudioSource setVelocity(float[] v) {
        if (v.length == 3) {
            velocity[0] = v[0];
            velocity[1] = v[1];
            velocity[2] = v[2];
            al.alSourcefv(id, AL.AL_VELOCITY, velocity, 0);
        } else {
            throw new IllegalArgumentException("wrong number of array elements");
        }
        return this;
    }

    public AudioSource setVelocity(Vec3D p) {
        return setVelocity(p.x, p.y, p.z);
    }

    public AudioSource stop() {
        al.alSourceStop(id);
        return this;
    }

    public String toString() {
        return "AudioSource: id=" + id
                + (buffer != null ? " buffer=" + buffer.toString() : "");
    }

    public AudioSource updatePosition() {
        position[0] = x;
        position[1] = y;
        position[2] = z;
        al.alSourcefv(id, AL.AL_POSITION, position, 0);
        return this;
    }
}