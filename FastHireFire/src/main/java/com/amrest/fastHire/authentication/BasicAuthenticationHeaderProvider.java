package com.amrest.fastHire.authentication;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.authentication.AuthenticationHeader;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/*
 * Helper class <h1>BasicAuthenticationHeaderProvider</h1>
 * 
 * @author : Ankita Gulati
 * @version : 1.0
 */

public class BasicAuthenticationHeaderProvider {

	private static final String USER_PROPERTY = "User";
	private static final String SEPARATOR = ":";
	private static final String PASSWORD_PROPERTY = "Password";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BASIC_AUTHENTICATION_PREFIX = "Basic ";

	private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationHeaderProvider.class);

	/*
	 * This method forms the Basic Authentication Header for the request.
	 * 
	 * @return AuthenticationHeader
	 * 
	 * @param destinationConfiguration
	 * 
	 * @exception Exception
	 */
	public AuthenticationHeader getAuthenticationHeader(DestinationConfiguration destinationConfiguration) {
		AuthenticationHeaderImpl basicAuthentication = null;
		try {
			StringBuilder resp = new StringBuilder();
			resp.append(destinationConfiguration.getProperty(USER_PROPERTY));
			resp.append(SEPARATOR);
			resp.append(destinationConfiguration.getProperty(PASSWORD_PROPERTY));
			String encodedPwd = DatatypeConverter.printBase64Binary(resp.toString().getBytes(StandardCharsets.UTF_8));
			basicAuthentication = new AuthenticationHeaderImpl(AUTHORIZATION_HEADER,
					BASIC_AUTHENTICATION_PREFIX + encodedPwd);
			return basicAuthentication;
		} catch (Exception e) {
			logger.error("Error description for getAuthenticationHeader :: ", e);
		}
		return basicAuthentication;
	}
}
