package toxi.test;

import java.math.BigInteger;

import processing.core.PApplet;
import toxi.color.ColorGradient;
import toxi.color.NamedColor;
import toxi.color.TColor;
import toxi.color.ToneMap;
import toxi.sim.automata.CAMatrix;
import toxi.sim.automata.CAWolfram1D;

public class WolframTest extends PApplet {

    /**
     * @param args
     */
    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.WolframTest" });
    }

    CAMatrix ca;
    CAWolfram1D wolfram;
    ToneMap toneMap;

    int y;

    public void draw() {
        loadPixels();
        if (mousePressed) {
            ca.setStateAt(mouseX, 0, 0);
        }
        ca.update();
        int[] m = ca.getMatrix();
        y = (y + 1) % height;
        for (int i = 0, idx = y * width; i < m.length; i++) {
            // pixels[idx + i] = m[i] > 0 ? 0xffffffff : 0xff000000;
            pixels[idx + i] = toneMap.getARGBToneFor(m[i]);
        }
        updatePixels();
    }

    public void keyPressed() {
        if (key == 'r') {
            ca.reset();
            ca.addNoise(0.9f, 0, wolfram.getStateCount());
        }
        if (key == 'w') {
            ca.reset();
            seedPattern();
            wolfram.randomize();
            System.out.println("0x"
                    + Long.toHexString(wolfram.getRuleAsBigInt().longValue()));
        }
        y = 0;
    }

    private void seedPattern() {
        for (int i = 0; i < width; i += 5) {
            ca.setStateAt(i, 0, 1);
        }
    }

    public void setup() {
        size(1024, 512);
        // setup cellular automata matrix
        ca = new CAMatrix(width);
        // wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x9a);
        // wolfram = new CAWolfram1D(2, 64, true).setRuleID(0xf9f327);
        wolfram =
                new CAWolfram1D(3, 64, true).setRuleID(new BigInteger(
                        "b47df39574d51d08", 16));
        // wolfram = new CAWolfram1D(2, 64, true).setRuleID(0x7ce872);
        // wolfram = new CAWolfram1D(2, 4, true).setRuleID(0x7483973);
        // wolfram = new CAWolfram1D(2, 4, true).setRuleID(0x40450e);
        wolfram.setAutoExpire(true);
        ca.setRule(wolfram);
        seedPattern();
        ColorGradient grad = new ColorGradient();
        grad.addColorAt(0, TColor.BLACK);
        grad.addColorAt(1, TColor.RED);
        grad.addColorAt(wolfram.getStateCount() - 1, NamedColor.WHITE);
        toneMap = new ToneMap(0, wolfram.getStateCount() - 1, grad);
        // toneMap = new ToneMap(0, 1, new ColorList(TColor.BLACK,
        // TColor.WHITE));
    }
}
