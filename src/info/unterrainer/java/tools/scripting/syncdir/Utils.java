package info.unterrainer.java.tools.scripting.syncdir;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
	@Nullable
	public static String normalizeDirectory(@Nullable String input) {
		if (input == null || input.equals("")) {
			return input;
		}
		return input.endsWith("/") ? input : input + "/";
	}

	@Nullable
	public static String removeDashTrim(@Nullable String text) {
		if (text == null || text.equals("")) {
			return text;
		}

		String result = text;
		result = result.trim();
		while (result.endsWith("-")) {
			result = result.substring(0, result.length() - 1).trim();
		}
		return result;
	}

	public static List<Match> getPattern(String name, String pattern, int cut) {
		List<Match> result = new ArrayList<Match>();

		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(name);
		while (matcher.find()) {
			String match = matcher.group().substring(cut, matcher.group().length() - cut).trim();
			List<String> groups = new ArrayList<String>();
			for (int i = 0; i < matcher.groupCount(); i++) {
				groups.add(matcher.group(i + 1));
			}
			result.add(new Match(match, groups));
		}

		return result;
	}

	public static void copyLargeFile(String source, String dest) throws IOException {
		FileInputStream sourceStream = null;
		FileOutputStream destStream = null;
		int r = 0;
		byte[] b = new byte[2048];

		sourceStream = new FileInputStream(source);
		destStream = new FileOutputStream(dest);
		while ((r = sourceStream.read(b)) != -1) {
			destStream.write(b, 0, r);
			updateProgressBars(r);
		}
		sourceStream.close();
		destStream.close();
	}

	public static void updateProgressBars(int r) {
		SyncDir.progress += r;
		SyncDir.updateProgressBars();
	}

	public static void sysoutNN(@Nullable String input) {
		if (input != null) {
			sysout(input);
		}
	}

	public static void sysoutNNNE(@Nullable String input) {
		if (input != null && !input.equals("")) {
			sysout(input);
		}
	}

	public static void sysout(String input) {
		System.out.println(input);
	}

	public static void sysout(String[] input) {
		for (int i = 0; i < input.length; i++) {
			if (i > 0) {
				System.out.print(" ");
			}
			System.out.print(input[i]);
		}
		System.out.println();
	}

	public static void sysout() {
		System.out.println();
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
