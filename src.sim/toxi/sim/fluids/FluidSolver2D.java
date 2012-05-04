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

package toxi.sim.fluids;

/**
 * Optimized Jos Stam style fluid solver with vorticity confinement and buoyancy
 * force.
 * 
 * @author Alexander McKenzie
 * @author Karsten Schmidt
 * 
 *         <p>
 *         optimized by toxi 2006-02-09
 *         <ul>
 *         <li>reduced nesting level of loops</li>
 *         <li>removed need for I() util method by unrolling all index
 *         calculations</li>
 *         <li>renamed variables for better legibility</li>
 *         </ul>
 *         </p>
 **/
public class FluidSolver2D {

    protected int numIterations = 10;

    protected int width, totalWidth, height, totalHeight;
    protected int size;
    protected float timeStep;

    protected float viscosity = 0;
    protected float diffusion = 0.000001f;
    protected float buoyancyA = 0.000625f;
    protected float buoyancyB = 0.025f;

    protected float[] tmp;
    protected float[] d, dOld;
    protected float[] u, uOld;
    protected float[] v, vOld;
    protected float[] curl;

    /**
     * Creates a new instance of the given dimension uses the specified time
     * step.
     * 
     * @param w
     *            matrix width
     * @param h
     *            matrix height
     * @param timeStep
     */
    public FluidSolver2D(int w, int h, float timeStep) {
        this.width = w;
        this.height = h;
        this.totalWidth = w + 2;
        this.totalHeight = h + 2;
        this.timeStep = timeStep;
        size = totalWidth * totalHeight;
        d = new float[size];
        dOld = new float[size];
        u = new float[size];
        uOld = new float[size];
        v = new float[size];
        vOld = new float[size];
        curl = new float[size];
        reset();
    }

    protected void addSource(float[] buffer, float[] prev) {
        for (int i = 0; i < size; i++) {
            buffer[i] += timeStep * prev[i];
        }
    }

    /**
     * Calculate the input array after advection. We start with an input array
     * from the previous timestep and an and output array. For all grid cells we
     * need to calculate for the next timestep, we trace the cell's center
     * position backwards through the velocity field. Then we interpolate from
     * the grid of the previous timestep and assign this value to the current
     * grid cell.
     * 
     * @param b
     *            Flag specifying how to handle boundries.
     * @param d
     *            Array to store the advected field.
     * @param d0
     *            The array to advect.
     * @param du
     *            The x component of the velocity field.
     * @param dv
     *            The y component of the velocity field.
     **/

    protected void advect(int b, float[] d, float[] d0, float[] du, float[] dv) {
        int i0, j0;
        float x, y, s0, t0, s1, t1, scaledTime;
        float wmax = width + 0.5f;
        float hmax = height + 0.5f;

        scaledTime = timeStep * width;

        for (int i = 1, j = 1, idx = i + totalWidth; j <= height;) {
            // go backwards through velocity field
            x = i - scaledTime * du[idx];
            y = j - scaledTime * dv[idx];

            // interpolate results
            if (x > wmax) {
                x = wmax;
            }
            if (x < 0.5f) {
                x = 0.5f;
            }

            i0 = (int) x;

            if (y > hmax) {
                y = hmax;
            }
            if (y < 0.5f) {
                y = 0.5f;
            }

            j0 = (int) y;

            s1 = x - i0;
            s0 = 1 - s1;
            t1 = y - j0;
            t0 = 1 - t1;

            int idx0 = i0 + j0 * totalWidth;
            d[idx] = s0 * (t0 * d0[idx0] + t1 * d0[idx0 + totalWidth]) + s1
                    * (t0 * d0[idx0 + 1] + t1 * d0[idx0 + totalWidth + 1]);

            if (i < width) {
                i++;
                idx++;
            } else {
                i = 1;
                j++;
                idx += 3;
            }
        }
        setBoundary(b, d);
    }

    /**
     * Calculate the buoyancy force as part of the velocity solver. Fbuoy =
     * -a*d*Y + b*(T-Tamb)*Y where Y = (0,1). The constants a and b are positive
     * with appropriate (physically meaningful) units. T is the temperature at
     * the current cell, Tamb is the average temperature of the fluid grid. The
     * density d provides a mass that counteracts the buoyancy force.
     * 
     * In this simplified implementation, we say that the temperature is
     * synonymous with density (since smoke is *hot*) and because there are no
     * other heat sources we can just use the density field instead of a new,
     * seperate temperature field.
     * 
     * @param buoy
     *            Array to store buoyancy force for each cell.
     **/

    protected void buoyancy(float[] buoy) {
        float avgTemperature = 0;

        // sum all temperatures
        for (int i = 1, idx = 1 + totalWidth, j = 1; j <= height;) {
            avgTemperature += d[idx];
            if (i < width) {
                i++;
                idx++;
            } else {
                i = 1;
                j++;
                idx += 3;
            }
        }

        // get average temperature
        avgTemperature /= (width * height);

        // for each cell compute buoyancy force
        for (int i = 1, idx = 1 + totalWidth, j = 1; j <= height;) {
            float currD = d[idx];
            buoy[idx] = buoyancyA * currD - buoyancyB
                    * (currD - avgTemperature);
            if (i < width) {
                i++;
                idx++;
            } else {
                i = 1;
                j++;
                idx += 3;
            }
        }
    }

    /**
     * Calculate the curl at position (i, j) in the fluid grid. Physically this
     * represents the vortex strength at the cell. Computed as follows: width =
     * (del x U) where U is the velocity vector at (i, j).
     * 
     * @param i
     *            The x index of the cell.
     * @param j
     *            The y index of the cell.
     **/

    protected final float curl(int idx) {
        float du_dy = (u[idx + totalWidth] - u[idx - totalWidth]) * 0.5f;
        float dv_dx = (v[idx + 1] - v[idx - 1]) * 0.5f;
        return du_dy - dv_dx;
    }

    public final void decay(float decay) {
        for (int i = 0; i < size; i++) {
            u[i] *= decay;
            v[i] *= decay;
            d[i] *= decay;
        }
    }

    /**
     * The basic density solving routine.
     **/
    public void densitySolver() {
        // add density input by mouse
        addSource(d, dOld);
        swap(d, dOld);

        diffusion(0, d, dOld, diffusion);
        swap(d, dOld);

        advect(0, d, dOld, u, v);

        // clear input density array for next frame
        for (int i = 0; i < size; i++) {
            dOld[i] = 0;
        }
    }

    /**
     * Recalculate the input array with diffusion effects. Here we consider a
     * stable method of diffusion by finding the densities, which when diffusion
     * used backward in time yield the same densities we started with. This is
     * achieved through use of a linear solver to solve the sparse matrix built
     * from this linear system.
     * 
     * @param b
     *            Flag to specify how boundaries should be handled.
     * @param c
     *            The array to store the results of the diffusion computation.
     * @param c0
     *            The input array on which we should compute diffusion.
     * @param diffusion
     *            The factor of diffusion.
     **/

    protected void diffusion(int b, float[] c, float[] c0, float diffusion) {
        float a = timeStep * diffusion * width * height;
        linearSolver(b, c, c0, a, 1 + 4 * a);
    }

    /**
     * @return the buoyancyA
     */
    public float getBuoyancyA() {
        return buoyancyA;
    }

    /**
     * @return the buoyancyB
     */
    public float getBuoyancyB() {
        return buoyancyB;
    }

    /**
     * @return the curl
     */
    public float[] getCurl() {
        return curl;
    }

    /**
     * @return the d
     */
    public float[] getDensityField() {
        return d;
    }

    /**
     * @return the diffusion
     */
    public float getDiffusion() {
        return diffusion;
    }

    /**
     * @return the numIterations
     */
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * @return the timeStep
     */
    public float getTimeStep() {
        return timeStep;
    }

    /**
     * @return the totalHeight
     */
    public int getTotalHeight() {
        return totalHeight;
    }

    /**
     * @return the totalWidth
     */
    public int getTotalWidth() {
        return totalWidth;
    }

    /**
     * @return the u
     */
    public float[] getVelocityFieldU() {
        return u;
    }

    /**
     * @return the v
     */
    public float[] getVelocityFieldV() {
        return v;
    }

    /**
     * @return the viscosity
     */
    public float getViscosity() {
        return viscosity;
    }

    /**
     * Iterative linear system solver using the Gauss-Seidel relaxation
     * technique. Room for much improvement here...
     * 
     **/

    protected void linearSolver(int b, float[] x, float[] x0, float a, float c) {
        c = 1f / c;
        for (int k = 0; k < numIterations; k++) {
            for (int i = 1, idx = 1 + totalWidth, j = 1; j <= height;) {
                x[idx] = (a
                        * (x[idx - 1] + x[idx + 1] + x[idx - totalWidth] + x[idx
                                + totalWidth]) + x0[idx])
                        * c;
                if (i < width) {
                    i++;
                    idx++;
                } else {
                    i = 1;
                    j++;
                    idx = j * totalWidth + 1;
                }
            }
            setBoundary(b, x);
        }
    }

    /**
     * Use project() to make the velocity a mass conserving, incompressible
     * field. Achieved through a Hodge decomposition. First we calculate the
     * divergence field of our velocity using the mean finite diffusionernce
     * approach, and apply the linear solver to compute the Poisson equation and
     * obtain a "height" field. Now we subtract the gradient of this field to
     * obtain our mass conserving velocity field.
     * 
     * @param x
     *            The array in which the x component of our final velocity field
     *            is stored.
     * @param y
     *            The array in which the y component of our final velocity field
     *            is stored.
     * @param p
     *            A temporary array we can use in the computation.
     * @param div
     *            Another temporary array we use to hold the velocity divergence
     *            field.
     * 
     **/

    protected void project(float[] x, float[] y, float[] p, float[] div) {
        float fact = -0.5f / width;
        for (int i = 1, idx = 1 + totalWidth, j = 1; j <= height;) {
            div[idx] = (x[idx + 1] - x[idx - 1] + y[idx + totalWidth] - y[idx
                    - totalWidth])
                    * fact;
            p[idx] = 0;
            if (i < width) {
                i++;
                idx++;
            } else {
                i = 1;
                j++;
                idx += 3;
            }
        }

        setBoundary(0, div);
        setBoundary(0, p);

        linearSolver(0, p, div, 1, 4);

        fact = -0.5f * width;
        for (int i = 1, idx = 1 + totalWidth, j = 1; j <= height;) {
            x[idx] += fact * (p[idx + 1] - p[idx - 1]);
            y[idx] += fact * (p[idx + totalWidth] - p[idx - totalWidth]);
            if (i < width) {
                i++;
                idx++;
            } else {
                i = 1;
                idx += 3;
                j++;
            }
        }

        setBoundary(1, x);
        setBoundary(2, y);
    }

    /**
     * Reset the datastructures. We use 1d arrays for speed.
     **/
    public void reset() {
        for (int i = 0; i < size; i++) {
            u[i] = uOld[i] = v[i] = vOld[i] = 0.0f;
            d[i] = dOld[i] = curl[i] = 0.0f;
        }
    }

    /**
     * specifies simple boundary conditions.
     * 
     * @param b
     * @param x
     */
    protected void setBoundary(int b, float[] x) {
        int idn = height * totalWidth;
        for (int i = 1, idi = totalWidth; i <= width; i++) {
            x[idi] = b == 1 ? -x[idi + 1] : x[idi + 1];
            x[width + 1 + idi] = b == 1 ? -x[idi + width] : x[idi + width];
            x[i] = b == 2 ? -x[totalWidth + i] : x[totalWidth + i];
            x[i + idn + totalWidth] = b == 2 ? -x[idn + i] : x[idn + i];
            idi += totalWidth;
        }
        x[0] = 0.5f * (x[1] + x[totalWidth]);
        x[idn + totalWidth] = 0.5f * (x[1 + totalWidth + idn] + x[idn]);
        x[width + 1] = 0.5f * (x[width] + x[width + 1 + totalWidth]);
        x[width + 1 + totalWidth + idn] = 0.5f * (x[width + totalWidth + idn] + x[width
                + 1 + idn]);
    }

    /**
     * @param buoyancyA
     *            the buoyancyA to set
     */
    public void setBuoyancyA(float buoyancyA) {
        this.buoyancyA = buoyancyA;
    }

    /**
     * @param buoyancyB
     *            the buoyancyB to set
     */
    public void setBuoyancyB(float buoyancyB) {
        this.buoyancyB = buoyancyB;
    }

    /**
     * @param curl
     *            the curl to set
     */
    public void setCurl(float[] curl) {
        this.curl = curl;
    }

    /**
     * @param diffusion
     *            the diffusion to set
     */
    public void setDiffusion(float diffusion) {
        this.diffusion = diffusion;
    }

    /**
     * @param numIterations
     *            the numIterations to set
     */
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    /**
     * @param timeStep
     *            the timeStep to set
     */
    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    /**
     * @param viscosity
     *            the viscosity to set
     */
    public void setViscosity(float viscosity) {
        this.viscosity = viscosity;
    }

    /**
     * util array swapping method
     * 
     * @param a
     *            first array
     * @param b
     *            second array
     */
    protected final void swap(float[] a, float[] b) {
        tmp = a;
        a = b;
        b = tmp;
    }

    /**
     * The basic velocity solving routine as described by Stam.
     **/

    public void velocitySolver() {

        // add velocity that was input by mouse
        addSource(u, uOld);
        addSource(v, vOld);

        // add in vorticity confinement force
        vorticityConfinement(uOld, vOld);
        addSource(u, uOld);
        addSource(v, vOld);

        // add in buoyancy force
        buoyancy(vOld);
        addSource(v, vOld);

        // swapping arrays for economical mem use
        // and calculating diffusionusion in velocity.
        swap(u, uOld);
        diffusion(0, u, uOld, viscosity);

        swap(v, vOld);
        diffusion(0, v, vOld, viscosity);

        // we create an incompressible field
        // for more effective advection.
        project(u, v, uOld, vOld);

        swap(u, uOld);
        swap(v, vOld);

        // self advect velocities
        advect(1, u, uOld, uOld, vOld);
        advect(2, v, vOld, uOld, vOld);

        // make an incompressible field
        project(u, v, uOld, vOld);

        // clear all input velocities for next frame
        for (int i = 0; i < size; i++) {
            uOld[i] = vOld[i] = 0;
        }
    }

    /**
     * Calculate the vorticity confinement force for each cell in the fluid
     * grid. At a point (i,j), Fvc = N x width where width is the curl at (i,j)
     * and N = del |width| / |del |width||. N is the vector pointing to the
     * vortex center, hence we add force perpendicular to N.
     * 
     * @param Fvc_x
     *            The array to store the x component of the vorticity
     *            confinement force for each cell.
     * @param Fvc_y
     *            The array to store the y component of the vorticity
     *            confinement force for each cell.
     **/

    public void vorticityConfinement(float[] Fvc_x, float[] Fvc_y) {
        float dw_dx, dw_dy;
        float length;
        float v;

        // Calculate magnitude of curl(u,v) for each cell. (|width|)
        for (int i = 1, j = 1, idx = i + totalWidth; j <= height;) {
            float c = curl(idx);
            curl[idx] = c > 0 ? c : -c;
            if (i < width) {
                i++;
                idx++;
            } else {
                i = 1;
                j++;
                idx += 3; // j*totalWidth+1;
            }
        }

        for (int i = 2, j = 2, idx = i + totalWidth * j; j < height;) {
            // Find derivative of the magnitude (n = del |width|)
            dw_dx = (curl[idx + 1] - curl[idx - 1]) * 0.5f;
            dw_dy = (curl[idx + totalWidth] - curl[idx - totalWidth]) * 0.5f;

            // Calculate vector length. (|n|)
            // Add small factor to prevent divide by zeros.
            length = 1f / ((float) Math.sqrt(dw_dx * dw_dx + dw_dy * dw_dy) + 0.000001f);

            // N = ( n/|n| )
            dw_dx *= length;
            dw_dy *= length;

            v = curl[idx];

            // N x width
            Fvc_x[idx] = dw_dy * -v;
            Fvc_y[idx] = dw_dx * v;

            if (i < width - 1) {
                i++;
                idx++;
            } else {
                i = 2;
                j++;
                idx += 5; // j*totalWidth+2;
            }
        }
    }
}