package com.amrest.fastHire.SF.scp.users.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amrest.fastHire.authentication.BasicAuthenticationHeaderProvider;
import com.amrest.fastHire.utilities.CommonFunctions;
import com.sap.core.connectivity.api.authentication.AuthenticationHeader;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/*
 * Helper class <h1>HttpConnectionGET</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 1.0
 */
public class HttpConnectionGET {

	private String urlToConnect;
	private DestinationConfiguration destinationConfiguration;
	private static final String authTypeBasic = "BasicAuthentication";
	private static final String authorization = "Bearer ";
	private String authToken;
	private String className;

	private static final Logger logger = LoggerFactory.getLogger(HttpConnectionGET.class);

	public HttpConnectionGET(String url, String token) {
		this.urlToConnect = url;
		this.authToken = token;
	}

	public HttpConnectionGET(String url, DestinationConfiguration dConfig) {
		this.urlToConnect = url;
		this.destinationConfiguration = dConfig;
	}

	public HttpConnectionGET(String url, DestinationConfiguration dConfig, String className) {
		this.urlToConnect = url;
		this.destinationConfiguration = dConfig;
		this.className = className;
	}

	/*
	 * This method is used to connect to the server. It takes the basic
	 * authentication headers from the destination
	 * 
	 * @return String
	 * 
	 * @exception IOException and ServletException
	 */
	public String connectToServer() {
		StringBuilder builder = new StringBuilder();
		URL url;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlToConnect);

			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("GET");

			List<AuthenticationHeader> authenticationHeaders = getAuthenticationHeaders(destinationConfiguration);
			for (AuthenticationHeader authenticationHeader : authenticationHeaders) {
				connection.addRequestProperty(authenticationHeader.getName(), authenticationHeader.getValue());
			}

			// Reading data
			inputStream = connection.getInputStream();
			int respCode = connection.getResponseCode();

			// logging the response
			logger.info(MessageFormat.format("Resp code for {0}{1}", respCode, CommonFunctions.CRLF));

			if (respCode != HttpURLConnection.HTTP_OK) {
				throw new ServletException("Expected response status code is 200 but it is " + respCode + " .");
			} else {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				String resp;
				while ((resp = bufferedReader.readLine()) != null) {
					builder.append(resp).append("\n");
				}

				return builder.toString();
			}
		} catch (IOException e) {
			logger.error(
					MessageFormat.format("IO error description for connectToServer {0}{1} ", e, CommonFunctions.CRLF));
		} catch (ServletException e) {
			logger.error(MessageFormat.format("Servlet error description for connectToServer {0}{1} ", e,
					CommonFunctions.CRLF));
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(MessageFormat.format("IO error description(finally block) for connectToServer :: ", e,
							CommonFunctions.CRLF));
				}
			}
		}
		return builder.toString();
	}

	/*
	 * This method is used to connect to the server for Reports. It takes the basic
	 * authentication headers from the destination
	 * 
	 * @return String
	 * 
	 * @exception IOException and ServletException
	 */

	public String connectToServerForReports() {
		StringBuilder builder = new StringBuilder();
		URL url;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlToConnect);

			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(300000);
			connection.setReadTimeout(300000);
			connection.setRequestMethod("GET");

			List<AuthenticationHeader> authenticationHeaders = getAuthenticationHeaders(destinationConfiguration);
			for (AuthenticationHeader authenticationHeader : authenticationHeaders) {
				connection.addRequestProperty(authenticationHeader.getName(), authenticationHeader.getValue());
			}

			// Reading data
			inputStream = connection.getInputStream();
			int respCode = connection.getResponseCode();

			// logging the response
			logger.info(MessageFormat.format("Resp code for {0}{1}", respCode, CommonFunctions.CRLF));

			if (respCode != HttpURLConnection.HTTP_OK) {
				throw new ServletException("Expected response status code is 200 but it is " + respCode + " .");
			} else {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8), 8192);
				String resp;
				while ((resp = bufferedReader.readLine()) != null) {
					builder.append(resp);
				}

				return builder.toString();
			}
		} catch (IOException e) {
			logger.error(MessageFormat.format("Report IO error description for connectToServer {0}{1} ", e,
					CommonFunctions.CRLF));
		} catch (ServletException e) {
			logger.error(MessageFormat.format("Report Servlet error description for connectToServer {0}{1} ", e,
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
							"Report IO error description(finally block) for connectToServer {0}{1} ", e,
							CommonFunctions.CRLF));
				}
			}
		}
		return builder.toString();
	}

	/*
	 * This method is used to connect to the server and update. It takes the basic
	 * authentication headers from the hard-coded string
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
			connection.setRequestMethod("GET");
			connection.addRequestProperty("Authorization", authorization + authToken);
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");

			// Reading data
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

	/*
	 * This method is used to connect to the server. It takes the basic
	 * authentication headers from the hard-coded string
	 * 
	 * @param response
	 * 
	 * @return String
	 * 
	 * @exception IOException and ServletException
	 */
	public String connectToServerUsingAuth(HttpServletResponse response) {
		StringBuilder builder = new StringBuilder();
		URL url;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlToConnect);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.addRequestProperty("Authorization", authorization + authToken);
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");

			// Reading data
			inputStream = connection.getInputStream();
			int respCode = connection.getResponseCode();

			// logging the response
			logger.info(MessageFormat.format("Resp code for {0}{1}", respCode, CommonFunctions.CRLF));

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

	/*
	 * This method is used to get the authentication headers from the hard-coded
	 * string.
	 * 
	 * @param destinationConfiguration
	 * 
	 * @return List<AuthenticationHeader>
	 * 
	 * @exception Exception
	 */
	private List<AuthenticationHeader> getAuthenticationHeaders(DestinationConfiguration destinationConfiguration) {
		List<AuthenticationHeader> authenticationHeaders = new ArrayList<>();
		try {
			String authenticationType = destinationConfiguration.getProperty("Authentication");

			if (authTypeBasic.equalsIgnoreCase((authenticationType))) {
				BasicAuthenticationHeaderProvider headerProvider = new BasicAuthenticationHeaderProvider();
				authenticationHeaders.add(headerProvider.getAuthenticationHeader(destinationConfiguration));
			}
			return authenticationHeaders;

		} catch (Exception e) {
			logger.error(MessageFormat.format("Error description for getAuthenticationHeaders {0}{1}", e,
					CommonFunctions.CRLF));
		}
		return authenticationHeaders;
	}
}
