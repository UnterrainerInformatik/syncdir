package info.unterrainer.java.tools.scripting.syncdir;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.unterrainer.java.tools.utils.HrfUtils;
import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.files.FileUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;

@Data
@Accessors(fluent = true)
@ExtensionMethod({ NullUtils.class, FileUtils.class, HrfUtils.class })
public class FileData {
	private boolean isDirectory;
	private boolean isSymbolicLink;
	private boolean isRegularFile;
	private boolean isOther;
	private Date modified;
	private Date created;
	private Path path;
	private String fullPath;
	private String relativePath;
	private String basePath;
	private String relativePathAndName;
	private String name;
	private String fileName;
	private String extension;
	private long size;
	private boolean cacheHit;

	public static FileData fromDir(Path dir, String basePath) {
		try {
			FileData r = new FileData();
			r.basePath(basePath);
			r.path(dir);
			r.modified(new Date(dir.toFile().lastModified()));
			r.fullPath(Utils.normalizeDirectory(r.path().toString()).replace("\\", "/"));
			r.relativePath(r.fullPath().substring(r.basePath().length()));
			r.relativePathAndName(r.relativePath());

			r.isDirectory(true);
			r.name(Utils.normalizeDirectory(dir.getFileName().toString()).replace("\\", "/"));
			return r;
		} catch (Exception e) {
			return null;
		}
	}

	public static FileData fromFile(Path file, BasicFileAttributes attrs, String basePath) {
		try {
			FileData r = new FileData();
			r.basePath(basePath);
			r.path(file);
			r.fullPath(r.path().toString().replace("\\", "/"));

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
			return r;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		String s = "[";
		if (isDirectory) {
			s += "d";
		} else {
			s += "f";
		}
		if (isRegularFile) {
			s += "r";
		}
		if (isSymbolicLink) {
			s += "s";
		}
		if (isOther) {
			s += "o";
		}
		s += "][m:";
		s += new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss.SSS").format(modified);
		s += "][s:";
		s += size.toHumanReadableByteCount();
		s += "]";
		return s + fullPath;
	}
}
