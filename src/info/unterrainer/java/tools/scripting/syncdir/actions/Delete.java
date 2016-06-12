package info.unterrainer.java.tools.scripting.syncdir.actions;

import info.unterrainer.java.tools.scripting.syncdir.FileData;
import info.unterrainer.java.tools.utils.files.FileUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(FileUtils.class)
@AllArgsConstructor()
public class Delete extends Action {

	private FileData target;

	@Override
	public void doAction() {
		target.fullPath().delete();
	}

	@Override
	public String toString() {
		return "delete " + target.toString();
	}
}
