package com.amrest.fastHire.connections;

import java.net.InetSocketAddress;
import java.net.Proxy;

import com.amrest.fastHire.utilities.URLManager;

/*
 * Helper class <h1>GetProxy</h1>
 * 
 * @author : Ankita Gulati
 * @version : 1.0
 */
public class GetProxy {

	private static final String ON_PREMISE_PROXY = "OnPremise";
	private static final String proxyType = URLManager.dConfiguration.getProperty("ProxyType");

	public static Proxy getProxy() {
		String proxyHost = null;
		int proxyPort;

		// Get proxy for on-premise destinations
		if (ON_PREMISE_PROXY.equals(proxyType)) {			
			proxyHost = System.getenv("HC_OP_HTTP_PROXY_HOST");
			proxyPort = Integer.parseInt(System.getenv("HC_OP_HTTP_PROXY_PORT"));
		} else {
			// Get proxy for internet destinations
			proxyHost = System.getProperty("http.proxyHost");
			proxyPort = Integer.parseInt(System.getProperty("http.proxyPort"));
		}
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
	}

}
