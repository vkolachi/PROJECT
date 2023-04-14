package com.agoda.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

	private static final long MEGABYTE = 1000L * 1000L;

	// Recursively walks through all all path in the directory and returns a list of
	// files
	public static List<File> getAllFiles(String directory) {
		try {
			return Files.walk(Paths.get(directory)).filter(Files::isRegularFile).map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Returns all zip files in directory sorted by creation time
	public static List<File> getZipFiles(String directory) {
		File file = new File(directory);
		List<File> files = Arrays.asList(file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".zip");
			}
		}));
		files.sort(new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				try {
					BasicFileAttributes attr1 = Files.getFileAttributeView(f1.toPath(), BasicFileAttributeView.class)
							.readAttributes();

					BasicFileAttributes attr2 = Files.getFileAttributeView(f2.toPath(), BasicFileAttributeView.class)
							.readAttributes();

					return attr1.creationTime().compareTo(attr2.creationTime());
				} catch (IOException e) {
					e.printStackTrace();
					return 1;
				}
			}
		});
		return files;
	}

	// Creates a new directory if it did not exist
	public static void createDirIfNotExist(String directory) {
		File file = new File(directory);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	// Checks if the string is a directory and it exists
	public static boolean validateDirectory(String directory) {
		return Files.isDirectory(Paths.get(directory));
	}

	public static long bytesToMB(long bytes) {
		return bytes / MEGABYTE;
	}

	public static long MBtobytes(Double MB) {
		return (long) (MB * MEGABYTE);
	}

	public static String getRelativePath(String basePath, String absolutePath) {
		if (absolutePath.startsWith(basePath)) {
			return absolutePath.substring(basePath.length());
		}
		return absolutePath;

	}
}
