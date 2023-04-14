package com.agoda.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class FileUtilsTest {

	List<String> actualFiles = new ArrayList<>();

	@Test
	public void testGetFilesFromDirectory() {
		String directory = "src/test/resources/testData";
		List<String> files = FileUtils.getAllFiles(directory).stream().map(file -> file.getName())
				.collect(Collectors.toList());
		Collections.sort(files);
		assertEquals(files, actualFiles);

	}

	@Before
	public void initializeActualFileList() {
		Collections.addAll(actualFiles, "SampleTextFile_1000kb.txt", "SampleTextFile_100kb.txt",
				"SamplePDFFile_5mb.pdf", ".hiddenfile.txt", "SampleVideo_1280x720_5mb.mp4",
				"SampleVideo_1280x720_30mb.mp4", "SampleAudio_0.7mb.mp3", "SampleJPGImage_200kbmb(1).jpg",
				"SampleGIFImage_135kbmb.gif", "SampleJPGImage_10mbmb.jpg", "SampleJPGImage_200kbmb.jpg",
				"SampleJPGImage_200kbmb(2).jpg", "SampleZIPFile_10mbmb.zip");
		Collections.sort(actualFiles);
	}
}
