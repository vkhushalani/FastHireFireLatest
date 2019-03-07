package com.amrest.fastHire.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;

public class CreateZip {
	String zipFileName = "documents.zip";

	public byte[] generateZip(List<File> fileList) throws IOException {
		// FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(baos);
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
		byte[] decodedStringBase64Zip = Base64.decodeBase64(baos.toByteArray());
		zipOut.close();
		baos.close();
		return decodedStringBase64Zip;
	}
}
