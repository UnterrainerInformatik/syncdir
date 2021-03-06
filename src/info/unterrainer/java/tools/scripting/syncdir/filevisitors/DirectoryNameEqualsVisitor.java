/**************************************************************************
 * <pre>
 *
 * Copyright (c) Unterrainer Informatik OG.
 * This source is subject to the Microsoft Public License.
 *
 * See http://www.microsoft.com/opensource/licenses.mspx#Ms-PL.
 * All other rights reserved.
 *
 * (In other words you may copy, use, change and redistribute it without
 * any restrictions except for not suing me because it broke something.)
 *
 * THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * PURPOSE.
 *
 * </pre>
 ***************************************************************************/
package info.unterrainer.java.tools.scripting.syncdir.filevisitors;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import info.unterrainer.java.tools.scripting.syncdir.FileData;
import info.unterrainer.java.tools.scripting.syncdir.Utils;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.files.FileUtils;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ NullUtils.class, FileUtils.class })
public class DirectoryNameEqualsVisitor extends SimpleFileVisitor<Path> {

	private HashMap<String, FileData> dirCache;
	private HashMap<String, FileData> fileCache;
	private String basePath;

	public DirectoryNameEqualsVisitor(String basePath) {
		this.basePath = Utils.normalizeDirectory(basePath);
		dirCache = new HashMap<>();
		fileCache = new HashMap<>();
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		FileData r = FileData.fromDir(dir, basePath);
		if (r != null) {
			if (r.fullPath().contains("/$RECYCLE.BIN/")) {
				return FileVisitResult.SKIP_SUBTREE;
			}
			dirCache.put(r.relativePathAndName(), r);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		FileData r = FileData.fromFile(file, attrs, basePath);
		if (r != null) {
			if (r.fullPath().contains("/$RECYCLE.BIN/")) {
				return FileVisitResult.SKIP_SUBTREE;
			}
			fileCache.put(r.relativePathAndName(), r);
		}
		return FileVisitResult.CONTINUE;
	}

	// If there is some error accessing the file, let the user know. If you don't override this method and an error
	// occurs, an IOException is thrown.
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException e) {
		Utils.sysout(e.toString());
		return FileVisitResult.SKIP_SUBTREE;
	}

	public HashMap<String, FileData> getDirCache() {
		return dirCache;
	}

	public HashMap<String, FileData> getFileCache() {
		return fileCache;
	}
}
