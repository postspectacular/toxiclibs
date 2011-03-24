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

package toxi.music;

import toxi.util.events.EventDispatcher;

public class QuantizedTimeProvider extends Thread {

    protected long previousTime;
    protected boolean isActive = true;
    protected double interval;
    protected double bpm, targetBPM;
    protected double tempoChangeSpeed = 0.01;

    protected int ticks = 0;
    protected int ticksPerBeat;
    protected int ticksPerBar;
    protected int beatsPerBar;

    protected final EventDispatcher<QuantizedTimeHandler> dispatcher = new EventDispatcher<QuantizedTimeHandler>();

    public QuantizedTimeProvider(double tempo, int beats, int ticks) {
        this.bpm = tempo;
        this.targetBPM = tempo;
        this.beatsPerBar = beats;
        this.ticksPerBeat = ticks;
        updateTicksPerBar();
        this.interval = 1000.0 / (tempo / 60.0) / ticksPerBeat;
        this.previousTime = System.nanoTime();
    }

    /**
     * @return the beatsPerBar
     */
    public int getBeatsPerBar() {
        return beatsPerBar;
    }

    /**
     * @return the dispatcher
     */
    public EventDispatcher<QuantizedTimeHandler> getDispatcher() {
        return dispatcher;
    }

    /**
     * @return the targetBPM
     */
    public double getTargetTempo() {
        return targetBPM;
    }

    /**
     * @return the bpm
     */
    public double getTempo() {
        return bpm;
    }

    /**
     * @return the tempoChangeSpeed
     */
    public double getTempoChangeSpeed() {
        return tempoChangeSpeed;
    }

    /**
     * @return the ticks
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * @return the ticksPerBar
     */
    public int getTicksPerBar() {
        return ticksPerBar;
    }

    /**
     * @return the ticksPerBeat
     */
    public int getTicksPerBeat() {
        return ticksPerBeat;
    }

    public void makeHighPriority() {
        setPriority(Thread.NORM_PRIORITY + 2);
    }

    public void run() {
        try {
            while (isActive) {
                // calculate time difference since last beat & wait if necessary
                double timePassed = (System.nanoTime() - previousTime) * 1.0e-6;
                while (timePassed < interval) {
                    timePassed = (System.nanoTime() - previousTime) * 1.0e-6;
                }
                if (0 == ticks % ticksPerBar) {
                    int barCount = ticks / ticksPerBar;
                    for (QuantizedTimeHandler l : dispatcher) {
                        l.handleBar(barCount);
                    }
                }
                if (0 == ticks % ticksPerBeat) {
                    int beats = ticks / ticksPerBeat;
                    for (QuantizedTimeHandler l : dispatcher) {
                        l.handleBeat(beats);
                    }
                }
                for (QuantizedTimeHandler l : dispatcher) {
                    l.handleTick(ticks);
                }
                ticks++;
                // adjust tempo and interval
                bpm += (targetBPM - bpm) * tempoChangeSpeed;
                setTempo((float) bpm);
                // calculate real time until next beat
                long delay = (long) (interval - (System.nanoTime() - previousTime) * 1.0e-6);
                previousTime = System.nanoTime();
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            }
        } catch (InterruptedException e) {
        }
    }

    /**
     * @param beatsPerBar
     *            the beatsPerBar to set
     */
    public void setBeatsPerBar(int beatsPerBar) {
        this.beatsPerBar = beatsPerBar;
        updateTicksPerBar();
    }

    /**
     * @param targetBPM
     *            the targetBPM to set
     */
    public void setTargetTempo(double targetBPM) {
        this.targetBPM = targetBPM;
    }

    public void setTempo(double bpm) {
        this.bpm = bpm;
        interval = 1000.0 / (bpm / 60.0) / ticksPerBeat;
    }

    /**
     * @param tempoChangeSpeed
     *            the tempoChangeSpeed to set
     */
    public void setTempoChangeSpeed(double tempoChangeSpeed) {
        this.tempoChangeSpeed = tempoChangeSpeed;
    }

    /**
     * @param ticksPerBeat
     *            the ticksPerBeat to set
     */
    public void setTicksPerBeat(int ticksPerBeat) {
        this.ticksPerBeat = ticksPerBeat;
        updateTicksPerBar();
    }

    /**
     * @param ticksPerBar
     *            the ticksPerBar to set
     */
    protected void updateTicksPerBar() {
        ticksPerBar = beatsPerBar * ticksPerBeat;
    }
}