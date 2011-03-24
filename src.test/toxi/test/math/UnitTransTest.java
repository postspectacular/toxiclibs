package toxi.test.math;

import junit.framework.TestCase;
import toxi.math.conversion.UnitTranslator;

public class UnitTransTest extends TestCase {

    public void testAreaConverters() {
        double area = UnitTranslator.POINT_POSTSCRIPT
                * UnitTranslator.POINT_POSTSCRIPT;
        assertEquals(1d, UnitTranslator.squarePointsToInch(area));
        assertEquals(645.16, UnitTranslator.squarePointsToMillis(area));
    }
}
