package info.unterrainer.java.tools.scripting.syncdir.actions;

import info.unterrainer.java.tools.scripting.syncdir.FileData;
import info.unterrainer.java.tools.scripting.syncdir.Utils;
import info.unterrainer.java.tools.utils.StringUtils;
import info.unterrainer.java.tools.utils.files.FileUtils;

import java.io.IOException;

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
			source.fullPath().copyFile(target.fullPath());
		} catch (IOException e) {
			e.getStackTraceAsString().sysout();
		}
	}
}
