package info.unterrainer.java.tools.scripting.syncdir;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.Configuration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandLineArgumentsParser {

	private static final Options options = new Options();
	private static String fallbackConfigFn = "";

	static {
		options.addOption(Option.builder("?").desc("see -h").build());
		options.addOption(Option.builder("H").desc("see -h").build());
		options.addOption(Option.builder("h").longOpt("help").desc("displays this information").build());
		options.addOption(Option.builder("A").desc("see -a").build());
		options.addOption(Option
				.builder("a")
				.longOpt("analyze")
				.desc("doesn't synchronize your directories by actually executing the calculated changes. Just prints out the detailed analysis of what would have happened. Argument -p is redundant when using this option")
				.build());
		options.addOption(Option.builder("P").desc("see -p").build());
		options.addOption(Option.builder("p").longOpt("print").desc("prints the detailed analysis of what's going to happen").build());
		options.addOption(Option.builder("D").desc("see -d").build());
		options.addOption(
				Option.builder("d").longOpt("del").desc("when synchronizing, files and directories on the target without matching source get deleted").build());
		options.addOption(Option
				.builder("")
				.argName("source> [<source>...] <target")
				.desc("a space-separated list of source directories followed by a single target directory. If you specify the target directory only, then syncdir assumes you want to use your current directory as source. Encase in \" to support spaces in your directory-names")
				.hasArgs()
				.build());
	}

	public static Configuration parse(String[] args, Configuration config, String f) {
		fallbackConfigFn = f;
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);

			if (hasOneOf("hH?", line)) {
				help();
				System.exit(0);
			}

			if (line.getArgs().length == 0) {
				throw new ParseException("You have to specify at least a single valid target-directory value!");
			}

			List<String> l = new ArrayList<>();
			for (int i = 0; i < line.getArgs().length; i++) {
				String s = line.getArgs()[i];
				if (i == line.getArgs().length - 1) {
					// This is the last entry.
					config.addProperty("targetDir", s);
				} else {
					l.add(s);
				}
			}
			if (l.size() == 0) {
				l.add(".");
			}
			config.addProperty("sourceDirs", l.toArray());

			String mode = "";
			if (hasOneOf("aA", line)) {
				mode += "analyze ";
			} else {
				mode += "sync ";
			}
			if (hasOneOf("pP", line)) {
				mode += "analyze ";
			}
			if (hasOneOf("dD", line)) {
				mode += "delete ";
			}
			config.addProperty("mode", mode);

		} catch (ParseException exp) {
			Utils.sysout("Unexpected exception while parsing the commandline arguments:" + exp.getMessage());
			help();
			System.exit(1);
		}
		return config;
	}

	public static boolean hasOneOf(String l, CommandLine line) {
		for (char c : l.toCharArray()) {
			if (line.hasOption(c + "")) {
				return true;
			}
		}
		return false;
	}

	public static void help() {
		HelpFormatter f = new HelpFormatter();
		f.printHelp("syncdir [options] <source> [<source>...] <target>", options);
		System.out.println("Also, you may specify a valid apache-configuration config file as the first and only parameter. "
				+ "If you don't specify any parameters at all, the program will try to fall back on a file named '"
				+ fallbackConfigFn
				+ "' located in the directory you started the application from.");
	}
}
