package com.agoda.app;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.agoda.compress.zip.Zip;
import com.agoda.utils.FileUtils;

public class AppTest {

	// Test if after compressing and decompressing, all files are same and all
	// compressed files are less than maxlength
	@Test
	public void checkCompressDecompressEqual() throws IOException {
		String inputDir = "src/test/resources/testData";
		String outDir = "src/test/resources/compressedTestDir/";
		String extractedDir = "src/test/resources/extractedTestDir/";
		long maxSize = FileUtils.MBtobytes(5.0);
		File outFile = new File(outDir);
		File extractedFile = new File(extractedDir);
		if(!outFile.exists())outFile.mkdir();
		if(!extractedFile.exists())extractedFile.mkdir();
		org.apache.commons.io.FileUtils.cleanDirectory(outFile);
		org.apache.commons.io.FileUtils.cleanDirectory(extractedFile);
		Zip zip = new Zip();

		zip.compress(inputDir, outDir, maxSize);

		zip.extract(outDir, extractedDir);

		assertTrue(areDirsEqual(inputDir, extractedDir));
		assertTrue(areFilesLessThanMaxSize(outDir, maxSize));

	}

	private boolean areFilesLessThanMaxSize(String dir, long maxSize) {
		List<File> files = FileUtils.getAllFiles(dir);
		for (File file : files) {
			if (file.length() > maxSize) {
				return false;
			}
		}
		return true;
	}

	private static boolean areDirsEqual(String dir1, String dir2) {
		List<File> files1 = FileUtils.getAllFiles(dir1);
		List<File> files2 = FileUtils.getAllFiles(dir2);
		if (files1.size() != files2.size()) {
			return false;
		}
		for (int i = 0; i < files1.size(); i++) {
			if (files1.get(i).length() != files2.get(i).length()) {
				return false;
			}
		}
		return true;
	}
}
