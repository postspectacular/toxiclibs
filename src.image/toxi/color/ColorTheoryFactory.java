package toxi.color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Registry & object factory for color theory strategies.
 */
public class ColorTheoryFactory {

	public static final String SINGLE_COMPLEMENT = SingleComplementStrategy.NAME;
	public static final String COMPLEMENTARY = ComplementaryStrategy.NAME;
	public static final String SPLIT_COMPLEMENTARY = SplitComplementaryStrategy.NAME;
	public static final String LEFT_SPLIT_COMPLEMENTARY = LeftSplitComplementaryStrategy.NAME;
	public static final String RIGHT_SPLIT_COMPLEMENTARY = LeftSplitComplementaryStrategy.NAME;
	public static final String ANALOGOUS = AnalogousStrategy.NAME;

	private static ColorTheoryFactory instance;

	private final HashMap implementations = new HashMap();

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
	}

	public void registerImplementation(ColorTheoryStrategy impl) {
		implementations.put(impl.getName(), impl);
	}

	public ColorTheoryStrategy getStrategyForName(String id) {
		return (ColorTheoryStrategy) implementations.get(id);
	}

	public ArrayList getRegisteredNames() {
		return new ArrayList(implementations.keySet());
	}

	public ArrayList getRegisteredStrategies() {
		return new ArrayList(implementations.values());
	}
}
