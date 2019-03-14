package com.amrest.fastHire.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amrest.fastHire.utilities.CommonFunctions;

/*
 * Helper class <h1>HttpConnectionDELETE</h1>
 * 
 * @author : Anne Delos Reyes
 * @version : 1.0
 */

public class HttpConnectionDELETE {

	private static final String authorization = "Bearer ";

	private String urlToConnect;
	private String authToken;

	private static final Logger logger = LoggerFactory.getLogger(HttpConnectionDELETE.class);

	public HttpConnectionDELETE(String url, String token) {
		this.urlToConnect = url;
		this.authToken = token;
	}

	/*
	 * This method is used to connect to the server and delete. It takes the basic
	 * authentication headers from the hard-coded string
	 * 
	 * @param response
	 * 
	 * @return String
	 * 
	 * @exception IOException and ServletException
	 */
	public String connectToServerUsingAuth() {

		StringBuilder builder = new StringBuilder();
		URL url;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlToConnect);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.addRequestProperty("Authorization", authorization + authToken);
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			inputStream = connection.getInputStream();
			int respCode = connection.getResponseCode();

			// logging the response
			logger.info(MessageFormat.format("Resp code is {0}{1}", respCode, CommonFunctions.CRLF));

			if (respCode != HttpURLConnection.HTTP_OK) {
				throw new ServletException("Expected response status code is 200 but it is " + respCode + " .");
			} else {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String resp;
				while ((resp = bufferedReader.readLine()) != null) {
					builder.append(resp).append("\n");
				}
				return builder.toString();
			}
		} catch (IOException e) {
			logger.error(MessageFormat.format("IO error description for connectToServerUsingAuth {0}{1}", e,
					CommonFunctions.CRLF));
		} catch (ServletException e) {
			logger.error(MessageFormat.format("Servlet error description for connectToServerUsingAuth {0}{1}", e,
					CommonFunctions.CRLF));
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(MessageFormat.format(
							"IO error description(finally block) for connectToServerUsingAuth {0}{1}", e,
							CommonFunctions.CRLF));
				}
			}
		}
		return builder.toString();
	}
}
