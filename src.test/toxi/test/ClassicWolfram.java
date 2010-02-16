package toxi.test;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.color.ToneMap;
import toxi.sim.automata.CAMatrix;
import toxi.sim.automata.CAWolfram1D;

public class ClassicWolfram extends PApplet {

    CAMatrix ca;
    CAWolfram1D wolfram;
    ToneMap toneMap;

    int y;
    private boolean doUpdate;

    public void draw() {
        if (doUpdate) {
            ca.update();
            // println(ca.getMatrix());
            int[] m = ca.getMatrix();
            y = (y + 1) % height;
            loadPixels();
            for (int i = 0, idx = y * width; i < m.length; i++) {
                pixels[idx + i] = toneMap.getARGBToneFor(m[i]);
            }
            updatePixels();
            doUpdate = false;
        }
    }

    public void keyPressed() {
        if (key == 'u') {
            doUpdate = true;
            return;
        }
        if (key == 'n') {
            ca.reset();
            ca.addNoise(0.9f, 0, 2);
        }
        if (key == 'p') {
            ca.reset();
            seedPattern();
        }
        if (key == 'w') {
            ca.reset();
            seedPattern();
            wolfram.randomize();
            println(wolfram.getRuleArray());
            println(Long.toHexString(wolfram.getRuleAsBigInt().longValue()));
            doUpdate = true;
        }
        y = 0;
    }

    void seedPattern() {
        ca.setStateAt(ca.getWidth() / 2, 0, 1);
    }

    public void setup() {
        size(256, 256);
        // setup cellular automata matrix
        ca = new CAMatrix(width);
        // create a classic Wolfram automata
        wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x5a);
        // wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x96);
        // wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x65);
        // wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x89);
        // wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x6d);
        ca.setRule(wolfram);
        seedPattern();
        toneMap = new ToneMap(0, 1, TColor.BLACK, TColor.WHITE);
    }

}
