package com.amrest.fastHire.connections;

import java.net.HttpURLConnection;

/*
 * Helper class <h1>HttpStatusCodes</h1>
 * 
 * @author : Ankita Gulati
 * @version : 2.0
 */
public class HttpStatusCodes {

	private static boolean connected = false;

	public static boolean respCodeFromServer(int statusCode) {

		switch (statusCode) {

		case HttpURLConnection.HTTP_OK:
			connected = true;
			break;

		case HttpURLConnection.HTTP_CREATED:
			connected = true; // for status codes 201
			break;

		case HttpURLConnection.HTTP_ACCEPTED:
			connected = true; // for status codes 202
			break;

		case HttpURLConnection.HTTP_NO_CONTENT:
			connected = true; // for status codes 204-No Content
			break;

		case HttpURLConnection.HTTP_BAD_REQUEST:
			connected = false;
			break;

		case HttpURLConnection.HTTP_UNAUTHORIZED:
			connected = false;
			break;

		case HttpURLConnection.HTTP_FORBIDDEN:
			connected = false;
			break;

		case HttpURLConnection.HTTP_NOT_FOUND:
			connected = false;
			break;

		case HttpURLConnection.HTTP_BAD_METHOD:
			connected = false;
			break;

		case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
			connected = false;
			break;

		case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:
			connected = false;
			break;

		case HttpURLConnection.HTTP_INTERNAL_ERROR:
			connected = false;
			break;

		case HttpURLConnection.HTTP_BAD_GATEWAY:
			connected = false;
			break;

		case HttpURLConnection.HTTP_UNAVAILABLE:
			connected = false;
			break;

		case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
			connected = false;
			break;

		default:
			break;
		}
		return connected;
	}

}
