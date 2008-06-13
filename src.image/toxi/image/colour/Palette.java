package toxi.image.colour;

import toxi.math.FastMath;

public class Palette {
	protected int[] colours;

	protected int lastPickedID = -1;

	public Palette(int[] colours) {
		this.colours = colours;
	}

	public int pickRandomColour() {
		int newID = FastMath.random(colours.length);
		if (colours.length > 1) {
			while (newID == lastPickedID)
				newID = FastMath.random(colours.length);
		}
		lastPickedID = newID;
		return colours[lastPickedID];
	}

	public int getLastPickedColour() {
		return lastPickedID > 0 ? colours[lastPickedID] : 0;
	}
}
