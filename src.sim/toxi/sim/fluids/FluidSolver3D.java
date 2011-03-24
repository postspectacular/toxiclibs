package toxi.sim.fluids;

import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec3D;

public class FluidSolver3D {

    private final int size, size2, size3, sm1, sm2;
    private final float dt;
    public float diff;
    public float visc;

    private final float[] s;
    private final float[] density;

    private final float[] velX, velY, velZ;
    private final float[] velX0, velY0, velZ0;
    private int numIterations = 10;

    public FluidSolver3D(int size, float diffusion, float viscosity, float dt) {
        this.size = size;
        this.size2 = size * size;
        this.size3 = size2 * size;
        this.sm1 = size - 1;
        this.sm2 = size - 2;
        this.diff = diffusion;
        this.visc = viscosity;
        this.dt = dt;
        this.s = new float[size3];
        this.density = new float[size3];
        this.velX = new float[size3];
        this.velY = new float[size3];
        this.velZ = new float[size3];
        this.velX0 = new float[size3];
        this.velY0 = new float[size3];
        this.velZ0 = new float[size3];
    }

    public void addDensity(int x, int y, int z, float amount) {
        density[IX(x, y, z)] += amount;
    }

    public void addVelocity(int x, int y, int z, Vec3D amount) {
        int idx = IX(x, y, z);
        velX[idx] += amount.x;
        velY[idx] += amount.y;
        velZ[idx] += amount.z;
    }

    private void advect(final int b, final float[] d, final float[] d0,
            final float[] velocX, final float[] velocY, final float[] velocZ) {
        int i0, j0, k0;

        final float scaledTime = dt * sm2;

        float s0, s1, t0, t1, u0, u1;
        float x, y, z;

        float maxSize = sm2 + 0.5f;
        float ifloat, jfloat, kfloat;
        int i, j, k;

        for (k = 1, kfloat = 1; k < sm1; k++, kfloat++) {
            for (j = 1, jfloat = 1; j < sm1; j++, jfloat++) {
                for (i = 1, ifloat = 1; i < sm1; i++, ifloat++) {
                    final int idx = IX(i, j, k);
                    x = i - scaledTime * velocX[idx];
                    y = j - scaledTime * velocY[idx];
                    z = k - scaledTime * velocZ[idx];

                    if (x < 0.5f) {
                        x = 0.5f;
                    }
                    if (x > maxSize) {
                        x = maxSize;
                    }
                    i0 = (int) x;
                    if (y < 0.5f) {
                        y = 0.5f;
                    }
                    if (y > maxSize) {
                        y = maxSize;
                    }
                    j0 = (int) y;
                    if (z < 0.5f) {
                        z = 0.5f;
                    }
                    if (z > maxSize) {
                        z = maxSize;
                    }
                    k0 = (int) z;

                    s1 = x - i0;
                    s0 = 1.0f - s1;
                    t1 = y - j0;
                    t0 = 1.0f - t1;
                    u1 = z - k0;
                    u0 = 1.0f - u1;

                    final int ix = IX(i0, j0, k0);
                    d[idx] =

                    s0
                            * (t0 * (u0 * d0[ix] + u1 * d0[ix + size2]) + (t1 * (u0
                                    * d0[ix + size] + u1
                                    * d0[ix + size + size2])))
                            + s1
                            * (t0 * (u0 * d0[ix + 1] + u1 * d0[ix + 1 + size2]) + (t1 * (u0
                                    * d0[ix + 1 + size] + u1
                                    * d0[ix + 1 + size + size2])));
                }
            }
        }
        set_bnd(b, d);
    }

    public void clearAt(int x, int y, int z) {
        final int idx = IX(x, y, z);
        if (idx >= 0 && idx < density.length) {
            density[idx] = 0;
            velX[idx] = velY[idx] = velZ[idx] = 0;
        }
    }

    public final void decay(float decay) {
        for (int i = 0; i < density.length; i++) {
            density[i] *= decay;
        }
    }

    private void diffuse(final int b, final float[] x, final float[] x0,
            final float diff) {
        float a = dt * diff * sm2 * sm2;
        lin_solve(b, x, x0, a, 1 + 6 * a);
    }

    /**
     * @return the density
     */
    public float[] getDensity() {
        return density;
    }

    private final int IX(int x, int y, int z) {
        return x + y * size + z * size2;
    }

    void lin_solve(final int b, final float[] x, final float[] x0,
            final float a, final float c) {
        float cRecip = 1.0f / c;
        for (int k = 0; k < numIterations; k++) {
            for (int m = 1; m < sm1; m++) {
                for (int j = 1, idx = m * size2 + size + 1; j < sm1; j++) {
                    for (int i = 1; i < sm1; i++) {
                        x[idx] = (x0[idx] + a
                                * (x[idx + 1] + x[idx - 1] + x[idx + size]
                                        + x[idx - size] + x[idx + size2] + x[idx
                                        - size2]))
                                * cRecip;
                        idx++;
                    }
                }
            }
            set_bnd(b, x);
        }
    }

    private void project(final float[] velocX, final float[] velocY,
            final float[] velocZ, final float[] p, final float[] div) {
        final float invSize = -0.5f * 1f / size;
        for (int k = 1; k < sm1; k++) {
            for (int j = 1; j < sm1; j++) {
                for (int i = 1; i < sm1; i++) {
                    final int idx = IX(i, j, k);
                    div[idx] = (velocX[idx + 1] - velocX[idx - 1]
                            + velocY[idx + size] - velocY[idx - size]
                            + velocZ[idx + size2] - velocZ[idx - size2])
                            * invSize;
                    p[idx] = 0;
                }
            }
        }
        set_bnd(0, div);
        set_bnd(0, p);
        lin_solve(0, p, div, 1, 6);

        float hs = 0.5f * size;
        for (int k = 1; k < sm1; k++) {
            for (int j = 1; j < sm1; j++) {
                for (int i = 1; i < sm1; i++) {
                    final int idx = IX(i, j, k);
                    velocX[idx] -= hs * (p[idx + 1] - p[idx - 1]);
                    velocY[idx] -= hs * (p[idx + size] - p[idx - size]);
                    velocZ[idx] -= hs * (p[idx + size2] - p[idx - size2]);
                }
            }
        }
        set_bnd(1, velocX);
        set_bnd(2, velocY);
        set_bnd(3, velocZ);
    }

    private void set_bnd(int b, float[] x) {
        for (int j = 1; j < sm1; j++) {
            for (int i = 1; i < sm1; i++) {
                final int idx = IX(i, j, 0);
                x[idx] = b == 3 ? -x[idx + size2] : x[idx + size2];
                x[idx + sm1 * size2] = b == 3 ? -x[idx + sm2 * size2] : x[idx
                        + sm2 * size2];
            }
        }
        for (int k = 1; k < sm1; k++) {
            for (int i = 1; i < sm1; i++) {
                final int idx = IX(i, 0, k);
                x[idx] = b == 2 ? -x[idx + size] : x[idx + size];
                x[idx + sm1 * size] = b == 2 ? -x[idx + size * sm2] : x[idx
                        + size * sm2];
            }
        }
        for (int k = 1; k < sm1; k++) {
            for (int j = 1; j < sm1; j++) {
                final int idx = IX(0, j, k);
                x[idx] = b == 1 ? -x[idx + 1] : x[idx + 1];
                x[idx + sm1] = b == 1 ? -x[idx + sm2] : x[idx + sm2];
            }
        }

        x[0] = 0.33f * (x[IX(1, 0, 0)] + x[IX(0, 1, 0)] + x[IX(0, 0, 1)]);
        x[IX(0, sm1, 0)] = 0.33f * (x[IX(1, sm1, 0)] + x[IX(0, sm2, 0)] + x[IX(
                0, sm1, 1)]);
        x[IX(0, 0, sm1)] = 0.33f * (x[IX(1, 0, sm1)] + x[IX(0, 1, sm1)] + x[IX(
                0, 0, sm1)]);
        x[IX(0, sm1, sm1)] = 0.33f * (x[IX(1, sm1, sm1)] + x[IX(0, sm2, sm1)] + x[IX(
                0, sm1, sm2)]);
        x[IX(sm1, 0, 0)] = 0.33f * (x[IX(sm2, 0, 0)] + x[IX(sm1, 1, 0)] + x[IX(
                sm1, 0, 1)]);
        x[IX(sm1, sm1, 0)] = 0.33f * (x[IX(sm2, sm1, 0)] + x[IX(sm1, sm2, 0)] + x[IX(
                sm1, sm1, 1)]);
        x[IX(sm1, 0, sm1)] = 0.33f * (x[IX(sm2, 0, sm1)] + x[IX(sm1, 1, sm1)] + x[IX(
                sm1, 0, sm2)]);
        x[IX(sm1, sm1, sm1)] = 0.33f * (x[IX(sm2, sm1, sm1)]
                + x[IX(sm1, sm2, sm1)] + x[IX(sm1, sm1, sm2)]);
    }

    public void setAt(int x, int y, int z, float dens, ReadonlyVec3D vel) {
        final int idx = IX(x, y, z);
        if (idx >= 0 && idx < density.length) {
            density[idx] = dens;
            velX[idx] = vel.x();
            velY[idx] = vel.y();
            velZ[idx] = vel.z();
        }
    }

    public void update() {
        diffuse(1, velX0, velX, visc);
        diffuse(2, velY0, velY, visc);
        diffuse(3, velZ0, velZ, visc);

        project(velX0, velY0, velZ0, velX, velY);

        advect(1, velX, velX0, velX0, velY0, velZ0);
        advect(2, velY, velY0, velX0, velY0, velZ0);
        advect(3, velZ, velZ0, velX0, velY0, velZ0);

        project(velX, velY, velZ, velX0, velY0);

        diffuse(0, s, density, diff);
        advect(0, density, s, velX, velY, velZ);
    }

}