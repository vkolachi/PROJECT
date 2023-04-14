package com.agoda.compress;

import java.io.IOException;

public interface Compressable {

	void compress(String inputDir, String outputDir, long fileSize) throws IOException;

	void extract(String inputDir, String outputDir);
}
