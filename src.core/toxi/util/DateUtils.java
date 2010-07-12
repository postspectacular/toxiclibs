package toxi.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {

    public static final String timeStamp() {
        return timeStamp(new GregorianCalendar());
    }

    public static final String timeStamp(GregorianCalendar date) {
        return String.format("%4d%02d%02d-%02d%02d%02d",
                date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1,
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
                date.get(Calendar.SECOND));
    }
}
