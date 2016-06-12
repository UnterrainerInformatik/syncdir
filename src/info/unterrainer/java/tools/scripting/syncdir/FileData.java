package info.unterrainer.java.tools.scripting.syncdir;

import java.nio.file.Path;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
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
}
