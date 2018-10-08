package com.amrest.fastHire.utilities;

/*
 * Utilities class <h1>CommonFunctions</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 2.0
 */

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

public class CommonFunctions {

	private static final Logger logger = LoggerFactory.getLogger(CommonFunctions.class);

	public static String decode(String body) {
		String decodedBody = null;
		try {
			decodedBody = URLDecoder.decode(body, StandardCharsets.UTF_8.toString());
			return decodedBody;
		} catch (UnsupportedEncodingException e) {
			logger.error(ConstantManager.lineSeparator + "Encoding exception ", e);
		}
		return decodedBody;
	}

	public static URI convertToURI(String url) {
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			logger.error(ConstantManager.lineSeparator + "URI Syntax exception ", e);
		} catch (Exception f) {
			logger.error(ConstantManager.lineSeparator + "Exception exception ", f);
		}

		return uri;
	}

	public String getLoggedInUserNew(HttpServletRequest request) {

		UserProvider userProvider;
		String userID = null;
		User user;

		try {
			request.setCharacterEncoding("UTF-8");
			userProvider = UserManagementAccessor.getUserProvider();
			userProvider.getUser(request.getUserPrincipal().getName());

			user = userProvider.getUser(request.getUserPrincipal().getName());
			userID = user.getAttribute("name");
			
		} catch (PersistenceException ee) {
			logger.error(ConstantManager.lineSeparator + "PersistenceException ", ee);
		} catch (UnsupportedUserAttributeException ee) {
			logger.error(ConstantManager.lineSeparator + "UnsupportedUserAttributeException ", ee);
		} catch (UnsupportedEncodingException ee) {
			logger.error(ConstantManager.lineSeparator + "UnsupportedEncodingException ", ee);
		}
		return userID.toUpperCase();
	}

	public static String getEmail(HttpServletRequest request) {

		UserProvider userProvider;
		String userID = null;
		String emailID = null;
		User user;

		try {
			request.setCharacterEncoding("UTF-8");

			userProvider = UserManagementAccessor.getUserProvider();
			userProvider.getUser(request.getUserPrincipal().getName());

			user = userProvider.getUser(request.getUserPrincipal().getName());
			userID = user.getAttribute("name");
			emailID = user.getAttribute("email");

			ConstantManager.email = emailID;

		} catch (PersistenceException ee) {
			logger.error(ConstantManager.lineSeparator + "PersistenceException ", ee);
		} catch (UnsupportedUserAttributeException ee) {
			logger.error(ConstantManager.lineSeparator + "UnsupportedUserAttributeException ", ee);
		} catch (UnsupportedEncodingException ee) {
			logger.error(ConstantManager.lineSeparator + "UnsupportedEncodingException ", ee);
		}
		return emailID;
	}

}
