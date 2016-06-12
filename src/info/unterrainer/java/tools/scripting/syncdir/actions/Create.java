package info.unterrainer.java.tools.scripting.syncdir.actions;

import info.unterrainer.java.tools.scripting.syncdir.FileData;
import info.unterrainer.java.tools.scripting.syncdir.Utils;
import info.unterrainer.java.tools.utils.StringUtils;
import info.unterrainer.java.tools.utils.files.FileUtils;

import java.io.File;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ FileUtils.class, StringUtils.class, Utils.class })
@AllArgsConstructor()
public class Create extends Action {

	private FileData source;
	private String targetBaseDir;
	private String relativeTarget;

	@Override
	public void doAction() {
		try {
			if (source.isDirectory()) {
				new File(targetBaseDir + relativeTarget).mkdirs();
			} else {
				source.fullPath().copyFile(targetBaseDir + relativeTarget);
			}
		} catch (IOException e) {
			e.getStackTraceAsString().sysout();
		}
	}

	@Override
	public String toString() {
		return "copy to " + targetBaseDir + relativeTarget + " from " + source.toString();
	}
}
