package toxi.color.theory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Registry & object factory for default {@link ColorTheoryStrategy}
 * implementations as well as custom ones.
 */
public class ColorTheoryFactory {

	public static final String SINGLE_COMPLEMENT = SingleComplementStrategy.NAME;
	public static final String COMPLEMENTARY = ComplementaryStrategy.NAME;
	public static final String SPLIT_COMPLEMENTARY = SplitComplementaryStrategy.NAME;
	public static final String LEFT_SPLIT_COMPLEMENTARY = LeftSplitComplementaryStrategy.NAME;
	public static final String RIGHT_SPLIT_COMPLEMENTARY = LeftSplitComplementaryStrategy.NAME;
	public static final String ANALOGOUS = AnalogousStrategy.NAME;
	public static final String MONOCHROME = MonochromeTheoryStrategy.NAME;
	public static final String TRIAD = TriadTheoryStrategy.NAME;
	public static final String TETRAD = TetradTheoryStrategy.NAME;
	public static final String COMPOUND = CompoundTheoryStrategy.NAME;

	private static ColorTheoryFactory instance;

	private final HashMap<String, ColorTheoryStrategy> implementations = new HashMap<String, ColorTheoryStrategy>();

	public static ColorTheoryFactory getInstance() {
		if (instance == null) {
			instance = new ColorTheoryFactory();
		}
		return instance;
	}

	private ColorTheoryFactory() {
		registerImplementation(new SingleComplementStrategy());
		registerImplementation(new ComplementaryStrategy());
		registerImplementation(new SplitComplementaryStrategy());
		registerImplementation(new LeftSplitComplementaryStrategy());
		registerImplementation(new RightSplitComplementaryStrategy());
		registerImplementation(new AnalogousStrategy());
		registerImplementation(new MonochromeTheoryStrategy());
		registerImplementation(new TriadTheoryStrategy());
		registerImplementation(new TetradTheoryStrategy());
		registerImplementation(new CompoundTheoryStrategy());
	}

	public void registerImplementation(ColorTheoryStrategy impl) {
		implementations.put(impl.getName(), impl);
	}

	public ColorTheoryStrategy getStrategyForName(String id) {
		return implementations.get(id);
	}

	public ArrayList<String> getRegisteredNames() {
		return new ArrayList<String>(implementations.keySet());
	}

	public ArrayList<ColorTheoryStrategy> getRegisteredStrategies() {
		return new ArrayList<ColorTheoryStrategy>(implementations.values());
	}
}
