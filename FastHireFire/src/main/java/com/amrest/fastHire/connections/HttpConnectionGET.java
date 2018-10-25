package com.amrest.fastHire.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amrest.fastHire.authentication.BasicAuthenticationHeaderProvider;
import com.amrest.fastHire.utilities.ConstantManager;
import com.amrest.fastHire.utilities.URLManager;
import com.sap.cloud.account.TenantContext;
import com.sap.core.connectivity.api.authentication.AuthenticationHeader;
import com.sap.core.connectivity.api.authentication.AuthenticationHeaderProvider;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/*
 * Helper class <h1>HttpConnectionGET</h1>
 * 
 * @author : Ankita Gulati
 * @version : 1.0
 */
public class HttpConnectionGET {

	private DestinationConfiguration destinationConfiguration;
	private URI uri;

	private static final String authTypeBasic = "BasicAuthentication";
	private static final String oAuthauthType = "OAuth2SAMLBearerAssertion";

	@SuppressWarnings("rawtypes")
	private Class className;
	private static final int bufferLength = 8192;
	private static final Logger logger = LoggerFactory.getLogger(HttpConnectionGET.class);

	@Resource
	private TenantContext context = null;

	@SuppressWarnings("rawtypes")
	public HttpConnectionGET(URI uri, DestinationConfiguration dConfig, Class className) {
		this.uri = uri;
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
	@SuppressWarnings("deprecation")
	public String connectToServer() {
		StringBuilder builder = new StringBuilder();
		URL url;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		Proxy proxy = GetProxy.getProxy();
		BufferedReader bufferedReader = null;
		String response = "";

		try {
			url = new URL(uri.normalize().toString());

			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("SAP-Connectivity-ConsumerAccount", URLManager.tContext.getAccountName());
			connection.setRequestMethod("GET");

			List<AuthenticationHeader> authenticationHeaders = getAuthenticationHeaders(destinationConfiguration);
			for (AuthenticationHeader authenticationHeader : authenticationHeaders) {
				connection.addRequestProperty(authenticationHeader.getName(), authenticationHeader.getValue());
			}

			// Get status code from server
			int respCode = connection.getResponseCode();
			logger.info(ConstantManager.lineSeparator + ConstantManager.respCodeLog + className + " : " + respCode);
			boolean connected = HttpStatusCodes.respCodeFromServer(respCode);

			if (connected) {
				inputStream = connection.getInputStream();

				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
						bufferLength);
				while ((response = bufferedReader.readLine()) != null) {
					builder.append(response).append("\n");
				}
			}

			else {
				inputStream = connection.getErrorStream();

				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
						bufferLength);
				while ((response = bufferedReader.readLine()) != null) {
					builder.append(response).append("\n");
				}
			}
		} catch (IOException e) {
			logger.error(ConstantManager.lineSeparator + "Error description for connectToServer :: ", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(ConstantManager.lineSeparator + "Error description for connectToServer :: ", e);
				}
			}
		}
		return builder.toString();
	}

	public String connectToServerNoProxy() {
		StringBuilder builder = new StringBuilder();
		URL url;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		String response = "";

		try {
			url = new URL(uri.normalize().toString());

			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("GET");

			List<AuthenticationHeader> authenticationHeaders = getAuthenticationHeaders(destinationConfiguration);
			for (AuthenticationHeader authenticationHeader : authenticationHeaders) {
				connection.addRequestProperty(authenticationHeader.getName(), authenticationHeader.getValue());
			}

			// Get status code from server
			int respCode = connection.getResponseCode();
			logger.info(ConstantManager.lineSeparator + ConstantManager.respCodeLog + className + " : " + respCode);
			boolean connected = HttpStatusCodes.respCodeFromServer(respCode);

			if (connected) {
				inputStream = connection.getInputStream();

				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
						bufferLength);
				while ((response = bufferedReader.readLine()) != null) {
					builder.append(response).append("\n");
				}
			}

			else {
				inputStream = connection.getErrorStream();

				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
						bufferLength);
				while ((response = bufferedReader.readLine()) != null) {
					builder.append(response).append("\n");
				}
			}
		} catch (IOException e) {
			logger.error(ConstantManager.lineSeparator + "Error description for connectToServerNoProxy :: ", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(ConstantManager.lineSeparator + "Error description for connectToServerNoProxy :: ", e);
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
			else if (oAuthauthType.equalsIgnoreCase(authenticationType)){
				Context ctx = new InitialContext();
				AuthenticationHeaderProvider authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/myAuthHeaderProvider");
				authenticationHeaders = authHeaderProvider.getOAuth2SAMLBearerAssertionHeaders(destinationConfiguration);
			}
			return authenticationHeaders;

		} catch (Exception e) {
			logger.error(ConstantManager.lineSeparator + "Error description for getAuthenticationHeaders :: ", e);
		}
		return authenticationHeaders;
	}
}
