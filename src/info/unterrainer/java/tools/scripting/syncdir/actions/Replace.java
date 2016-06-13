package info.unterrainer.java.tools.scripting.syncdir.actions;

import java.io.IOException;

import info.unterrainer.java.tools.scripting.syncdir.FileData;
import info.unterrainer.java.tools.scripting.syncdir.Utils;
import info.unterrainer.java.tools.utils.StringUtils;
import info.unterrainer.java.tools.utils.files.FileUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ FileUtils.class, StringUtils.class, Utils.class })
@AllArgsConstructor()
public class Replace extends Action {

	private FileData source;
	private FileData target;

	@Override
	public void doAction() {
		target.fullPath().delete();
		try {
			Utils.copyLargeFile(source.fullPath(), target.fullPath());
		} catch (IOException e) {
			e.getStackTraceAsString().sysout();
		}
	}

	@Override
	public String toString() {
		return "replace " + target.toString() + " with " + source.toString();
	}
}
