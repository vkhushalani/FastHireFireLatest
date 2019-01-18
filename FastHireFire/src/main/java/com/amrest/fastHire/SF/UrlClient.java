package com.amrest.fastHire.SF;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlClient {
	Logger logger = LoggerFactory.getLogger(UrlClient.class);
	
	private String baseUrl;
	private String path;
	private String filter;
	private String postJson;
	private String userName;
	private String password;
	private String tokenType;
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
		}
	
	public String getPostJson() {
		return postJson;
	}
	public void setPostJson(String postJson) {
		this.postJson = postJson;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	public HttpResponse callDestinationPOST() throws URISyntaxException, ClientProtocolException, IOException{
		URL url= new URL(this.getBaseUrl()+this.getPath()+this.getFilter());
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		String urlString = uri.toASCIIString();
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlString);
		
		StringEntity entity = new StringEntity(this.getPostJson(), "UTF-8");
		logger.debug("postJson"+this.getPostJson() );
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		if(!this.getTokenType().equalsIgnoreCase("Bearer")){
		
			String userCredentials = this.getUserName()+":"+this.getPassword();
			String basicAuth = this.getTokenType()+" " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
			request.setHeader("Authorization", basicAuth);
		}
		else
		{	
			request.setHeader("Authorization", this.getTokenType()+" "+this.getPassword());
			
		}
			
		
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
		
	}
	public HttpResponse callDestinationPUT() throws URISyntaxException, ClientProtocolException, IOException{
		URL url= new URL(this.getBaseUrl()+this.getPath()+this.getFilter());
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		String urlString = uri.toASCIIString();
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut request = new HttpPut(urlString);
		
		StringEntity entity = new StringEntity(this.getPostJson(), "UTF-8");
		logger.debug("postJson"+this.getPostJson() );
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		if(!this.getTokenType().equalsIgnoreCase("Bearer")){
		
			String userCredentials = this.getUserName()+":"+this.getPassword();
			String basicAuth = this.getTokenType()+" " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
			request.setHeader("Authorization", basicAuth);
		}
		else
		{	
			request.setHeader("Authorization", this.getTokenType()+" "+this.getPassword());
			
		}
			
		
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
		
	}
	public HttpResponse callDestinationGET() throws ClientProtocolException, IOException, URISyntaxException{
		
		URL url= new URL(this.getBaseUrl()+this.getPath()+this.getFilter());
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		String urlString = uri.toASCIIString();
		logger.debug("GEt urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		
		if(!this.getTokenType().equalsIgnoreCase("Bearer")){
			
			String userCredentials = this.getUserName()+":"+this.getPassword();
			String basicAuth = this.getTokenType()+" " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
			request.setHeader("Authorization", basicAuth);
		}
		else
		{	
			request.setHeader("Authorization", this.getTokenType()+" "+this.getPassword());
			
		}
	
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
	}
	public HttpResponse callDestinationDELETE() throws URISyntaxException, ClientProtocolException, IOException{
		URL url= new URL(this.getBaseUrl()+this.getPath()+this.getFilter());
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		String urlString = uri.toASCIIString();
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(urlString);
		
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		if(!this.getTokenType().equalsIgnoreCase("Bearer")){
		
			String userCredentials = this.getUserName()+":"+this.getPassword();
			String basicAuth = this.getTokenType()+" " + javax.xml.bind.DatatypeConverter.printBase64Binary(userCredentials.getBytes());
			request.setHeader("Authorization", basicAuth);
		}
		else
		{	
			request.setHeader("Authorization", this.getTokenType()+" "+this.getPassword());
			
		}
			
		
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson"+response );
		return response;
		
	}
}
