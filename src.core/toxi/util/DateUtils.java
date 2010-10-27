package toxi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A Simple timestamp generator/formatter with timezone support.
 */
public class DateUtils {

	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyyMMdd-HHmmss");

	/**
	 * Creates a formatted timestamp string of the current datetime using the
	 * local host timezone.
	 * 
	 * @return timestamp
	 */
	public static final String timeStamp() {
		return timeStamp(new Date(), null);
	}

	/**
	 * Creates a formatted timestamp string of the given date using the local
	 * host timezone.
	 * 
	 * @param date
	 * @return timestamp
	 */
	public static final String timeStamp(Date date) {
		return timeStamp(date, null);
	}

	/**
	 * Creates a formatted timestamp string of the given date using the
	 * specified timezone.
	 * 
	 * @param date
	 * @param zone
	 * @return timestamp
	 */
	public static final String timeStamp(Date date, TimeZone zone) {
		if (zone == null) {
			zone = TimeZone.getDefault();
		}
		FORMAT.setTimeZone(zone);
		return FORMAT.format(date);
	}

	/**
	 * Creates a formatted timestamp string of the given epoch using the local
	 * host timezone.
	 * 
	 * @param t
	 *            unix epoch timestamp
	 * @return timestamp
	 */
	public static final String timeStamp(long t) {
		return timeStamp(new Date(t), null);
	}

	/**
	 * Creates a formatted timestamp string of the given date using the given
	 * timezone ID.
	 * 
	 * @see TimeZone#getTimeZone(String)
	 * 
	 * @param zoneID
	 * @param date
	 * @return timestamp
	 */
	public static final String timeStampForZone(String zoneID, Date date) {
		return timeStamp(date, TimeZone.getTimeZone(zoneID));
	}

	/**
	 * Creates a formatted timestamp string of the current date in GMT.
	 * 
	 * @return timestamp
	 */
	public static final String timeStampGMT() {
		return timeStamp(new Date(), GMT);
	}

	/**
	 * Creates a formatted timestamp string of the given date in GMT.
	 * 
	 * @return timestamp
	 */
	public static final String timeStampGMT(Date date) {
		return timeStamp(date, GMT);
	}

	/**
	 * Creates a formatted timestamp string of the given epoch in GMT.
	 * 
	 * @return timestamp
	 */
	public static final String timeStampGMT(long t) {
		return timeStamp(new Date(t), GMT);
	}
}
