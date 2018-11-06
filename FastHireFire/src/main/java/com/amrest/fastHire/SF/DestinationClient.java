package com.amrest.fastHire.SF;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.authentication.AuthenticationHeaderProvider;
import com.sap.core.connectivity.api.authentication.AuthenticationHeader;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class DestinationClient {
	Logger logger = LoggerFactory.getLogger(DestinationClient.class);
	private String destName;
	private ConnectivityConfiguration configuration;
	private DestinationConfiguration destConfiguration;
	private List<AuthenticationHeader> headers;
	private AuthenticationHeaderProvider authHeaderProvider;
	
	public String getDestName() {
		return destName;
	}
	public void setDestName(String destName) {
		this.destName = destName;
	}
	public void setHeaderProvider() throws NamingException{
		
		Context ctx = new InitialContext();
		this.authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/myAuthHeaderProvider");
	}
	public void setConfiguration() throws NamingException{
		Context ctx = new InitialContext();
		this.configuration = (ConnectivityConfiguration) ctx.lookup("java:comp/env/connectivityConfiguration");
	}
	public void setDestConfiguration(){
		this.destConfiguration = configuration.getConfiguration(this.destName);

	}
	public Map<String, String> getAllDestProperties(){
		return this.destConfiguration.getAllProperties();
	}
	
	public String getDestProperty(String propName){
		return this.destConfiguration.getProperty(propName);
	}
	
	public DestinationConfiguration getDestConfiguration() {
		return destConfiguration;
	}
	public ConnectivityConfiguration getConfiguration() {
		return configuration;
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	public HttpResponse callDestinationGET(String path,String filter) throws ClientProtocolException, IOException, URISyntaxException{
		
		String urlString = this.getDestProperty("URL");
		URL url= new URL(urlString+path+filter);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		urlString = uri.toASCIIString();
		logger.debug("GEt urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		if(!this.getDestProperty("Authentication").equalsIgnoreCase("BasicAuthentication")){
		for (AuthenticationHeader header : this.headers){
			logger.debug("Header: "+ header.getName() + header.getValue());
			request.addHeader(header.getName(), header.getValue());
		}
		}
		else
		{
			String userCredentials = this.getDestProperty("User")+":"+this.getDestProperty("Password");
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
			request.setHeader("Authorization", basicAuth);
		}
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
	}
	
	public HttpResponse callDestinationPOST(String path,String filter,String postJson) throws URISyntaxException, ClientProtocolException, IOException{
		String urlString = this.getDestProperty("URL");
		URL url= new URL(urlString+path+filter);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		urlString = uri.toASCIIString();
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlString);
		
		StringEntity entity = new StringEntity(postJson);
		logger.debug("postJson"+postJson );
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		if(!this.getDestProperty("Authentication").equalsIgnoreCase("BasicAuthentication")){
			for (AuthenticationHeader header : this.headers){
				logger.debug("Header: "+ header.getName() + header.getValue());
				request.addHeader(header.getName(), header.getValue());
			}
			}
			else
			{
				String userCredentials = this.getDestProperty("User")+":"+this.getDestProperty("Password");
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
				request.setHeader("Authorization", basicAuth);
			}
		
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
		
	}
	
	public HttpResponse callDestinationDelete(String path,String filter) throws URISyntaxException, ClientProtocolException, IOException{
		String urlString = this.getDestProperty("URL");
		URL url= new URL(urlString+path+filter);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		urlString = uri.toASCIIString();
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(urlString);
		
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		if(!this.getDestProperty("Authentication").equalsIgnoreCase("BasicAuthentication")){
			for (AuthenticationHeader header : this.headers){
				logger.debug("Header: "+ header.getName() + header.getValue());
				request.addHeader(header.getName(), header.getValue());
			}
			}
			else
			{
				String userCredentials = this.getDestProperty("User")+":"+this.getDestProperty("Password");
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
				request.setHeader("Authorization", basicAuth);
			}
		
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
		
	}
	public void setHeaders(String type){
		switch(type){
		case "OAuth2SAMLBearerAssertion": 
			this.headers = authHeaderProvider.getOAuth2SAMLBearerAssertionHeaders(this.destConfiguration);
			break;
		
		}
	}
}
