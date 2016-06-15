package info.unterrainer.java.tools.scripting.syncdir;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {
	public final long SECOND = 1000;
	public final long MINUTE = SECOND * 60;
	public final long HOUR = MINUTE * 60;
	public final long DAY = HOUR * 24;
	public final long WEEK = DAY * 7;
	public final long MONTH = DAY * 30;
	public final long YEAR = DAY * 360;

	public static String humanReadableDuration(Duration duration) {
		return humanReadableDuration(duration, new String[] { "ms", "s", "m", "h", "d", "W", "M", "Y" });
	}

	public static String humanReadableDurationLongUnits(Duration duration) {
		return humanReadableDuration(duration, new String[] { " milliseconds", " seconds", " minutes", " hours", " days", " weeks", " months", " years" });
	}

	public static String humanReadableDuration(Duration duration, String[] units) {
		String[] u = new String[] { "ms", "s", "m", "h", "d", "W", "M", "Y" };
		if (units != null && units.length == 8) {
			u = units;
		}

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat df = (DecimalFormat) nf;
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(0);

		float[] r = calculateHumanReadableDuration(duration);
		String s = df.format(r[0]);
		s += u[(int) r[1]];
		return s;
	}

	private static float[] calculateHumanReadableDuration(Duration duration) {
		Duration d = duration;
		if (d.isNegative()) {
			d = d.multipliedBy(-1);
		}
		final Long[] boundaries = new Long[] { SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR };

		float value = 0;
		float boundaryIndex = 0;
		long m = d.toMillis();
		for (int i = 0; i < boundaries.length; i++) {
			long l = boundaries[i];
			if (m < l) {
				if (i == 0) {
					value = m;
				} else {
					value = (float) m / (float) boundaries[i - 1];
				}
				boundaryIndex = i;
				break;
			} else {
				if (i == boundaries.length - 1) {
					value = (float) m / (float) boundaries[i];
					boundaryIndex = i + 1;
				}
			}
		}
		return new float[] { duration.isNegative() ? -value : value, boundaryIndex };
	}
}