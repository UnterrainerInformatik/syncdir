package info.unterrainer.java.tools.scripting.syncdir;

import info.unterrainer.java.tools.scripting.syncdir.actions.Action;
import info.unterrainer.java.tools.scripting.syncdir.actions.Create;
import info.unterrainer.java.tools.scripting.syncdir.actions.Delete;
import info.unterrainer.java.tools.scripting.syncdir.actions.Replace;
import info.unterrainer.java.tools.scripting.syncdir.filevisitors.DirectoryNameEqualsVisitor;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.ExtensionMethod;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

@ExtensionMethod({ NullUtils.class, StringUtils.class })
@ParametersAreNonnullByDefault({})
public class SyncDir {

	private static final String programName = "syncdir";
	private static final String fallbackConfigFn = "config.properties";

	private static Configuration config;

	private static String os;
	private static String mode;

	private static String targetDir;
	private static List<String> sourceDirs;

	private static HashMap<String, FileData> sourceDirCache;
	private static HashMap<String, FileData> sourceFileCache;

	private static HashMap<String, FileData> targetDirCache;
	private static HashMap<String, FileData> targetFileCache;

	private static List<Action> dirActions = new ArrayList<>();
	private static List<Action> fileActions = new ArrayList<>();

	public static void main(String[] args) {

		if (args.length > 1) {
			wrongNumberOfArguments(programName, fallbackConfigFn);
		}
		String configFileName = parseArg(args, 0);
		config = readConfigurationFile(configFileName, fallbackConfigFn);

		mode = config.getString("mode");
		if (mode == null || mode.isEmpty()) {
			Utils.sysout("You have to specify a valid mode!");
			System.exit(1);
		}
		mode = mode.toLowerCase();
		if (!mode.contains("sync") && !mode.contains("analyze")) {
			Utils.sysout("You have to specify a valid mode! It has to contain either 'sync' or 'analyze' if you want a print-out analysis.");
			System.exit(1);
		}
		if (!mode.contains("sync") && mode.contains("delete")) {
			Utils.sysout("You have to specify a valid mode! 'delete' is only viable in 'sync' mode, not in 'analyze' mode.");
			System.exit(1);
		}
		os = config.getString("os");
		if (os == null || os.isEmpty() || !os.equals("mac")) {
			os = "windows";
		}

		// Get parameter sourceDirs.
		String[] t = config.getStringArray("sourceDirs");
		sourceDirs = new ArrayList<String>();
		if (t != null) {
			for (String s : t) {
				if (s != null && !s.isBlank()) {
					sourceDirs.add(Utils.normalizeDirectory(s));
				}
			}
		} else {
			Utils.sysout("You have to specify at least a single valid sourceDirs value!");
			System.exit(1);
		}

		// Get parameter targetDir.
		targetDir = config.getString("targetDir");
		if (targetDir == null || targetDir.isBlank()) {
			Utils.sysout("You have to specify a single valid targetDir value!");
			System.exit(1);
		}

		checkSourceDirs();
		checkTargetDir();
		readData();
		dirActions = process(sourceDirCache, targetDirCache);
		fileActions = process(sourceFileCache, targetFileCache);

		if (mode.contains("analyze")) {
			analyze();
		}
		if (mode.contains("sync")) {
			Utils.sysout("### SYNCHRONIZING ##########################################################");
			Utils.sysout("Directories:");
			sync(dirActions, mode.contains("delete"));
			Utils.sysout("############################################################################");
			Utils.sysout("Files:");
			sync(fileActions, mode.contains("delete"));
			Utils.sysout("############################################################################");
		}

		Utils.sysout("Done.");
	}

	private static void wrongNumberOfArguments(String program, String fallbackConfigFn) {
		Utils.sysout("Wrong number of arguments. Usage:\n"
				+ program
				+ "\n"
				+ "or\n"
				+ program
				+ " <configFilePathAndName>\n\n"
				+ "If you specify a config file, it has to be a valid apache-configuration file. "
				+ "If you don't, the program will try to fall back on a file named '"
				+ fallbackConfigFn
				+ "' located in the directory you started the application from.");
		System.exit(1);
	}

	private static void readData() {
		sourceDirCache = new HashMap<>();
		sourceFileCache = new HashMap<>();

		DirectoryNameEqualsVisitor v = new DirectoryNameEqualsVisitor(targetDir);
		try {
			Files.walkFileTree(new File(targetDir).toPath(), v);
		} catch (IOException e) {
			e.printStackTrace();
		}
		targetDirCache = v.getDirCache();
		targetFileCache = v.getFileCache();

		for (String s : sourceDirs) {
			v = new DirectoryNameEqualsVisitor(s);
			try {
				Files.walkFileTree(new File(s).toPath(), v);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (Entry<String, FileData> e : v.getDirCache().entrySet()) {
				if (!sourceDirCache.containsKey(e.getKey())) {
					sourceDirCache.put(e.getKey(), e.getValue());
				}
			}

			for (Entry<String, FileData> e : v.getFileCache().entrySet()) {
				if (!sourceFileCache.containsKey(e.getKey())) {
					sourceFileCache.put(e.getKey(), e.getValue());
				} else {
					// Memorize newer file.
					FileData ss = sourceFileCache.get(e.getKey());
					FileData sn = e.getValue();
					if (sn.modified().after(ss.modified()) || (ss.modified().equals(sn.modified()) && sn.created().after(ss.created()))) {
						sourceFileCache.remove(e.getKey());
						sourceFileCache.put(e.getKey(), sn);
					}
				}
			}
		}
	}

	private static List<Action> process(HashMap<String, FileData> sourceCache, HashMap<String, FileData> targetCache) {
		List<Action> r = new ArrayList<>();
		for (Entry<String, FileData> e : sourceCache.entrySet()) {
			FileData s = e.getValue();
			if (!targetCache.containsKey(e.getKey())) {
				r.add(new Create(s, Utils.normalizeDirectory(targetDir), s.relativePathAndName()));
			} else {
				FileData t = targetCache.get(e.getKey());
				t.cacheHit(true);
				if (!s.isDirectory() && (!t.modified().equals(s.modified()) || t.size() != s.size())) {
					r.add(new Replace(s, t));
				}
			}
		}
		for (FileData f : targetCache.values()) {
			if (!f.cacheHit() && !Utils.normalizeDirectory(targetDir).equals(Utils.normalizeDirectory(f.fullPath()))) {
				r.add(new Delete(f));
			}
		}
		return r;
	}

	private static void analyze() {
		Utils.sysout("### ANALYSIS ###############################################################");
		Utils.sysout("Directories:");
		printList(dirActions, "");
		Utils.sysout("############################################################################");
		Utils.sysout("Files:");
		printList(fileActions, "");
		Utils.sysout("############################################################################");
	}

	private static void sync(List<Action> actions, boolean delete) {
		for (Action a : actions) {
			if (!(a instanceof Delete) || delete) {
				a.doAction();
			}
		}
	}

	private static void checkSourceDirs() {
		checkFileExists(sourceDirs, "sourceDirs");
	}

	private static void checkTargetDir() {
		File tDir = new File(targetDir);
		if (!tDir.exists()) {
			try {
				Files.createDirectory(tDir.toPath());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * Parses the arguments array at the position 'index' and returns the value as a string.
	 *
	 * @param args the argument-array
	 * @param index the index of the argument to retrieve
	 * @return the argument at position index or null if an error occurred
	 */
	private static String parseArg(String[] args, int index) {
		String result = null;
		if (args.length <= index) {
			return result;
		}

		result = args[index];
		if (!result.isBlank()) {
			result = result.stripQuotes();
		}
		return result;
	}

	/**
	 * After executing this part, the global variable configuration is either set, or the application exited.
	 *
	 * @param configFileName
	 */
	private static Configuration readConfigurationFile(String fn, String fallbackFn) {
		Configuration result = null;
		if (fn != null) {
			result = loadConfigurationFile(fn,
					"The file you specified via the parameter '%s' is missing.\nTrying to fall back to config.properties in execution directory.",
					"The file you specified via the parameter '%s' is not a valid config file.\nTrying to fall back to property file in execution directory.");
		}
		if (result == null) {
			result = loadConfigurationFile(fallbackFn,
					"Config file not found.\nSee to it that there is a proper config file called '%s' in the execution directory or take any other config "
							+ "file and start the program with the path (to that file) and name as a commandline argument.",
					"Config file '%s' is not a valid config file.");
		}
		if (result == null) {
			System.exit(1);
		}
		return result;
	}

	private static Configuration loadConfigurationFile(String fn, String errorMessageNotFound, String errorMessageWrongFormat) {
		Configuration result = null;
		File f = new File(fn);
		if (f.exists()) {
			try {
				result = new PropertiesConfiguration(f);
			} catch (ConfigurationException e) {
				Utils.sysout(String.format(errorMessageWrongFormat, fn));
			}
		} else {
			Utils.sysout(String.format(errorMessageNotFound, fn));
		}
		return result;
	}

	private static void checkFileExists(List<String> dirs, String parameterName) {
		for (String dir : dirs) {
			File d = new File(dir);
			if (!d.exists()) {
				Utils.sysout("The " + parameterName + " you specified [" + d.toString() + "] doesn't exist.");
				System.exit(1);
			}
		}
	}

	private static void printList(List<Action> list, String prefix) {
		for (Action a : list) {
			Utils.sysout(prefix + a);
		}
	}
}