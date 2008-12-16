package toxi.color;

import java.util.ArrayList;
import java.util.StringTokenizer;

import toxi.math.MathUtils;

public class ColorTheme {

	protected String name;
	protected ArrayList<ThemePart> parts = new ArrayList<ThemePart>();
	protected float weightedSum;

	public ColorTheme(String name) {
		this.name = name;
	}

	public ColorTheme addRange(String descriptor, float weight) {
		StringTokenizer st = new StringTokenizer(descriptor, " ,");
		Color col = null;
		ColorRange range = null;
		while (st.hasMoreTokens()) {
			String item = st.nextToken();
			if (ColorRange.getPresetForName(item) != null) {
				range = ColorRange.getPresetForName(item);
			} else if (NamedColor.getForName(item) != null) {
				col = NamedColor.getForName(item);
			}
		}
		if (range != null) {
			addRange(range, col, weight);
		}
		return this;
	}

	public ColorTheme addRange(ColorRange range, Color col, float weight) {
		parts.add(new ThemePart(range, col, weight));
		weightedSum += weight;
		return this;
	}

	public Color getColor() {
		float rnd = MathUtils.random(1f);
		for (ThemePart t : parts) {
			if (t.weight / weightedSum >= rnd) {
				return t.getColor();
			}
			rnd -= t.weight / weightedSum;
		}
		return null;
	}

	public ColorList getColors(int num) {
		ColorList list = new ColorList();
		for (int i = 0; i < num; i++) {
			list.add(getColor());
		}
		return list;
	}

	class ThemePart {
		ColorRange range;
		Color col;
		float weight;

		ThemePart(ColorRange range, Color col, float weight) {
			this.range = range;
			this.col = col;
			this.weight = weight;
		}

		public Color getColor() {
			return range.getColor(col, ColorRange.DEFAULT_VARIANCE);
		}
	}

}
