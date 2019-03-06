package com.amrest.fastHire.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CreateZip {
	String zipFileName = "documents.zip";

	public ZipOutputStream generateZip(List<File> fileList) throws IOException {
		FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (File fileToZip : fileList) {
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}
		zipOut.close();
		fos.close();
		return zipOut;
	}
}
