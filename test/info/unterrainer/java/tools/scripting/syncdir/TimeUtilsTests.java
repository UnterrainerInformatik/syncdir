package info.unterrainer.java.tools.scripting.syncdir;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;

public class TimeUtilsTests {

	@Test
	public void HumanReadableDuration_Boundaries() throws InterruptedException {
		test(new Long[] { 1L, TimeUtils.SECOND, TimeUtils.MINUTE, TimeUtils.HOUR, TimeUtils.DAY, TimeUtils.WEEK, TimeUtils.MONTH, TimeUtils.YEAR },
				new String[] { "1ms", "1s", "1m", "1h", "1d", "1W", "1M", "1Y" });
	}

	@Test
	public void HumanReadableDuration_OddNumbers1() throws InterruptedException {
		test(new Long[] { 2L, TimeUtils.SECOND + 1, TimeUtils.MINUTE + 1, TimeUtils.HOUR + 1, TimeUtils.DAY + 1, TimeUtils.WEEK + 1, TimeUtils.MONTH + 1,
				TimeUtils.YEAR + 1 }, new String[] { "2ms", "1s", "1m", "1h", "1d", "1W", "1M", "1Y" });
	}

	@Test
	public void HumanReadableDuration_OddNumbers2() throws InterruptedException {
		test(new Long[] { 500L, TimeUtils.SECOND + 500, TimeUtils.MINUTE + TimeUtils.MINUTE / 2, TimeUtils.HOUR + TimeUtils.HOUR / 2,
				TimeUtils.DAY + TimeUtils.DAY / 2, TimeUtils.WEEK + TimeUtils.WEEK / 2, TimeUtils.MONTH + TimeUtils.MONTH / 2,
				TimeUtils.YEAR + TimeUtils.YEAR / 2 }, new String[] { "500ms", 1.5 + "s", 1.5 + "m", 1.5 + "h", 1.5 + "d", 1.5 + "W", 1.5 + "M", 1.5 + "Y" });
	}

	@Test
	public void HumanReadableDuration_OddNumbers2_Negative() throws InterruptedException {
		test(new Long[] { -500L, -TimeUtils.SECOND - 500, -TimeUtils.MINUTE - TimeUtils.MINUTE / 2, -TimeUtils.HOUR - TimeUtils.HOUR / 2,
				-TimeUtils.DAY - TimeUtils.DAY / 2, -TimeUtils.WEEK - TimeUtils.WEEK / 2, -TimeUtils.MONTH - TimeUtils.MONTH / 2,
				-TimeUtils.YEAR - TimeUtils.YEAR / 2 },
				new String[] { "-500ms", -1.5 + "s", -1.5 + "m", -1.5 + "h", -1.5 + "d", -1.5 + "W", -1.5 + "M", -1.5 + "Y" });
	}

	@Test
	public void HumanReadableDuration_Boundaries_LongUnits() throws InterruptedException {
		testLong(new Long[] { 1L, TimeUtils.SECOND, TimeUtils.MINUTE, TimeUtils.HOUR, TimeUtils.DAY, TimeUtils.WEEK, TimeUtils.MONTH, TimeUtils.YEAR },
				new String[] { "1 milliseconds", "1 seconds", "1 minutes", "1 hours", "1 days", "1 weeks", "1 months", "1 years" });
	}

	private void test(Long[] values, String[] expected) {
		for (int i = 0; i < values.length; i++) {
			String r = TimeUtils.humanReadableDuration(Duration.ofMillis(values[i]));
			Assert.assertEquals(expected[i], r);
		}
	}

	private void testLong(Long[] values, String[] expected) {
		for (int i = 0; i < values.length; i++) {
			String r = TimeUtils.humanReadableDurationLongUnits(Duration.ofMillis(values[i]));
			Assert.assertEquals(expected[i], r);
		}
	}
}