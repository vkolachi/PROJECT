# FileZip

This program performs compression and extraction while also allowing user to specify maximum size of each compressed file

### Problem Statement


Write a program that compresses files and folders into a set of compressed files such that of each
compressed file doesn’t exceed a maximum size. The same program can be used for decompressing the files that it has generated earlier. The output of the decompression should be identical to the original input of the compression process.

When compression is needed, the program takes 3 parameters:
1. Path to Input directory. This directory will contain some files and folders.
2. Path to Output directory. This is where the program should write the compressed files to.
3. Maximum compressed size per file expressed in MB.


When decompression is needed, the program takes 2 parameters:
1. Path to Input directory. This directory contains the compressed files that were generated by the
same program.
2. Path to Output directory. This is where the program should put the decompressed files &
folders.

Consider the following when writing the code:
1. Some files may be greater than the JVM memory.
2. Some input files (even when compressed) may be greater than the maximum size allowed for
output files.
3. You can use zip for compression algorithm (using JDK’s implementation don’t use a third party
library for that) but design the program to allow support different compression algorithms in the
future.
4. When compressing, generate as few files as possible. You are not required to generate the
absolute minimum number of compressed files but doing so would be a plus.
5. `<<Bonus>>` Make the compression process run in parallel to speed up the compression for a
single directory.

Write your production-quality code in Java or Scala. Please test your system adhering to best
practices. Submit your solution by using the link that was included in the email.

### Usage
For compression : <br>
``` compress inputDirectory outputDirectory maxFileSizeInMB ``` <br>
Example : <br>
``` compress src/test/resources/testData src/test/resources/compressedTestDir/ 5.0 ```

For extraction : <br>
``` extract inputDirectory outputDirectory ``` <br>
Example : <br>
``` extract src/test/resources/compressedTestDir/ src/test/resources/extractedTestDir/ ```

### Implementation details

For compressing a directory, this program iterates through all the files and sequentially reads the data, compresses it and writes to the output zip file. It uses a CountingOutputStream to maintain the number of bytes written to the stream. Whenever the size threshold limit is reached, a new zip file is created and written to.

Decompression works similarly, all zipped files are read sequentially in order of when they were created and decompressed file is written to the filesystem. For cases when multiple zip files contain parts of a single file, a mapping of each file's outputstream is maintainted to continue writing to the file's outputstream.

This program works for files which are larger than the available JVM memory as the entire file is never loaded to memory. Also, the absolute minimum number of compressed files are generated using this program.

Note : While runnning the Junit tests, additional files will be created in ```src/test/resources/``` folder to compare extracted files with original files.


### Benchmarks 

Compressed test data(65.6 MB media files) containing 17 files with maxSplitSize of 5 MB in 2.6s to 13 zip files <br>
Compressed same data using default compression in 2.3s <br>
Decompressed 13 zip files to 65.6 MB of data containing 17 files in 0.5s <br>

Compressed 6.7GB containing 1889 files with maxSplitSize of 100MB in 324s <br>
Compressed same data using default compression in 309s <br>
Decompressed 66 zip files to 6.7GB in 99s