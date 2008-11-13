package toxi.color;

public interface ColorTheoryStrategy {
	String getName();

	ColorList createListFromColour(Color src);
}
