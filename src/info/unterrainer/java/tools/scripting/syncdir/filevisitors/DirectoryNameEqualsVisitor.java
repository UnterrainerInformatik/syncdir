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

import info.unterrainer.java.tools.scripting.syncdir.FileData;
import info.unterrainer.java.tools.scripting.syncdir.Utils;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.files.FileUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ NullUtils.class, FileUtils.class })
@ParametersAreNonnullByDefault({})
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
		FileData r = new FileData();
		r.basePath(basePath);
		r.path(dir);
		r.fullPath(Utils.normalizeDirectory(r.path().toString()));
		r.relativePath(r.fullPath().substring(r.basePath().length()));
		r.relativePathAndName(r.relativePath() + r.name());

		r.isDirectory(true);
		r.name(Utils.normalizeDirectory(dir.getFileName().toString()).replace("/", "\\"));
		dirCache.put(r.relativePathAndName(), r);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		FileData r = new FileData();
		r.basePath(basePath);
		r.path(file);
		r.fullPath(r.path().toString());

		r.name(file.getFileName().toString());
		r.fileName(r.name().getNameNoExtension());

		r.relativePath(r.fullPath().substring(r.basePath().length()));
		r.relativePath(r.relativePath().substring(0, r.relativePath().length() - r.name().length()));
		r.relativePathAndName(r.relativePath() + r.name());

		r.modified(new Date(attrs.lastModifiedTime().toMillis()));
		r.created(new Date(attrs.creationTime().toMillis()));
		r.isDirectory(attrs.isDirectory());
		r.isSymbolicLink(attrs.isSymbolicLink());
		r.isRegularFile(attrs.isRegularFile());
		r.isOther(attrs.isOther());
		r.extension(r.name().getExtension());
		r.size(attrs.size());
		fileCache.put(r.relativePathAndName(), r);

		return FileVisitResult.CONTINUE;
	}

	// If there is some error accessing the file, let the user know. If you don't override this method and an error
	// occurs, an IOException is thrown.
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return FileVisitResult.CONTINUE;
	}

	public HashMap<String, FileData> getDirCache() {
		return dirCache;
	}

	public HashMap<String, FileData> getFileCache() {
		return fileCache;
	}
}
