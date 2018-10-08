package com.amrest.fastHire.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Helper class <h1>ExtractProperties</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 1.0
 */

public class ExtractProperties {

	private Scanner scanner;
	private static final String propFileName = "config.properties";
	private static final Logger logger = LoggerFactory.getLogger(ExtractProperties.class);

	/*
	 * Read the input file (properties file) and extract the text. Check for the
	 * = or : index, if > 0 then put in lMap.
	 * 
	 * @return LinkedHashMap<Object, Object>
	 * 
	 * @exception Exception
	 */
	public LinkedHashMap<Object, Object> extractProps() {
		LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
		try {
			InputStream file = getClass().getClassLoader().getResourceAsStream(propFileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));

			scanner = new Scanner(bufferedReader);

			while (scanner.hasNext()) {
				scanner.useDelimiter("\n");
				String next = (String) scanner.next();

				if (!next.startsWith("#") && !next.startsWith("!")) {
					int pos;
					if ((pos = next.indexOf("=")) > 0 || (pos = next.indexOf(":")) > 0) {
						hashMap.put(next.substring(0, pos), next.substring(pos + 1).trim());
					} else {
						hashMap.put(next, "");
					}
				}
			}
			return hashMap;
		} catch (Exception e) {
			logger.error("Error description for extractProps :: ", e);
		}
		return hashMap;
	}

}
