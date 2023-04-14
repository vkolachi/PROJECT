package com.agoda.compress.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.CountingOutputStream;

import com.agoda.compress.Compressable;
import com.agoda.utils.FileUtils;

public class Zip implements Compressable {

	// Number used to determine zip file part
	public int partFileNo = 1;

	// Maintains a count of bytes written to the stream
	private CountingOutputStream outStream;

	private static final long ZIP_HEADER_SIZE = FileUtils.MBtobytes(0.02);

	@Override
	public void compress(String inputDir, String outputDir, long maxFileSize) throws IOException {
		List<File> files = FileUtils.getAllFiles(inputDir);
		FileOutputStream fos = new FileOutputStream(outputDir + getPartFileName());
		this.outStream = new CountingOutputStream(fos);
		ZipOutputStream zipOut = new ZipOutputStream(this.outStream);
		for (File file : files) {
			FileInputStream fis = new FileInputStream(file);
			zipOut.putNextEntry(new ZipEntry(FileUtils.getRelativePath(inputDir, file.getPath())));

			byte[] bytes = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(bytes)) >= 0) {
				// write bytes to outstream until no more maxFileSize is reached, then create a
				// new zip file
				if (getAvailableBytesInStream(maxFileSize) - ZIP_HEADER_SIZE > bytesRead) {
					zipOut.write(bytes, 0, bytesRead);
				} else {
					zipOut.closeEntry();
					zipOut.finish();
					this.outStream.close();
					fos = new FileOutputStream(outputDir + getPartFileName());
					this.outStream = new CountingOutputStream(fos);
					zipOut = new ZipOutputStream(this.outStream);
					zipOut.putNextEntry(new ZipEntry(FileUtils.getRelativePath(inputDir, file.getPath())));
					zipOut.write(bytes, 0, bytesRead);
				}
			}
			fis.close();
		}
		zipOut.close();
		this.outStream.close();
		fos.close();
	}

	@Override
	public void extract(String inputDir, String outputDir) {
		List<File> zipFiles = FileUtils.getZipFiles(inputDir);
		// Maintain a map of fileNames and outputStream. Used to fetch the outputStream
		// for multiple parts of the same file
		HashMap<String, FileOutputStream> fosMap = new HashMap<>();
		FileUtils.createDirIfNotExist(outputDir);

		for (File zipFile : zipFiles) {
			try {
				FileInputStream fin = new FileInputStream(zipFile);
				ZipInputStream zipIn = new ZipInputStream(fin);
				ZipEntry zipEntry;
				FileOutputStream fos = null;
				while ((zipEntry = zipIn.getNextEntry()) != null) {
					File originalFile = new File(outputDir + zipEntry.getName());
					if (zipEntry.isDirectory()) {
						originalFile.mkdir();
					} else {
						// Create parent folders
						originalFile.getParentFile().mkdirs();
						originalFile.createNewFile();
						// Check if zipEntry is part of a partially written file
						if (fosMap.containsKey(zipEntry.getName())) {
							fos = fosMap.get(zipEntry.getName());
						} else {
							fos = new FileOutputStream(originalFile);
							fosMap.put(zipEntry.getName(), fos);
						}

						byte[] bytes = new byte[1024];
						int bytesRead;
						while ((bytesRead = zipIn.read(bytes)) >= 0) {
							fos.write(bytes, 0, bytesRead);
						}
					}
				}
				zipIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private String getPartFileName() {
		return "compressed-part-" + String.valueOf(this.partFileNo++) + ".zip";
	}

	private long getAvailableBytesInStream(long maxFileSizeBytes) {
		return maxFileSizeBytes - this.outStream.getByteCount();
	}
}
