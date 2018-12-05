package com.amrest.fastHire.SF;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchQueryPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.authentication.AuthenticationHeader;
import com.sap.core.connectivity.api.authentication.AuthenticationHeaderProvider;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class BatchRequest {

	Logger logger = LoggerFactory.getLogger(BatchRequest.class);
	private String BOUNDARY = "batch_123";
	private DestinationConfiguration destConfiguration;
	private List<BatchPart> batchParts = new ArrayList<BatchPart>();
	
	public void configureDestination(String destName) throws NamingException {
		Context ctx = new InitialContext();
		ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctx.lookup("java:comp/env/connectivityConfiguration");
		this.destConfiguration = configuration.getConfiguration(destName);
	}
	
	private String getDestProperty(String propName){
		return this.destConfiguration.getProperty(propName);
	}
	
	private String createUri(String path,String filter) throws MalformedURLException, URISyntaxException {
		String urlString = this.getDestProperty("URL");
		URL url= new URL(urlString+path+filter);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		urlString = uri.toASCIIString();
	
			return urlString;
	 }
	public List<AuthenticationHeader> getAuthHeaders(String type) throws NamingException{
		
		List<AuthenticationHeader> headers = null;
		
		Context ctx = new InitialContext();
		AuthenticationHeaderProvider authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/myAuthHeaderProvider");
		switch(type){
		case "OAuth2SAMLBearerAssertion": 
			headers = authHeaderProvider.getOAuth2SAMLBearerAssertionHeaders(this.destConfiguration);
			break;
		
		}
		return headers;
	}
	public void createQueryPart(String method,String uri,String contentId){
		BatchQueryPart request = BatchQueryPart.method("GET").uri("$metadata").build();
		batchParts.add(request);
	}
	@SuppressWarnings({ "deprecation", "resource" })
	public String fetchXCSFRToken() throws ClientProtocolException, IOException, URISyntaxException, NamingException{
		
		String urlString = this.createUri("","");
		logger.debug("GEt urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		if(!this.getDestProperty("Authentication").equalsIgnoreCase("BasicAuthentication")){
			List<AuthenticationHeader> authHeaders = this.getAuthHeaders(this.getDestProperty("Authentication"));
			
			for (AuthenticationHeader header : authHeaders){
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
		Header[] headers = response.getHeaders("x-csrf-token");
		return headers[0].getValue();
		
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	public HttpResponse callBatchPOST(String path,String filter,String postJson) throws URISyntaxException, ClientProtocolException, IOException, NamingException{
		String urlString = this.createUri(path,filter);
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlString);
		
		StringEntity entity = new StringEntity(postJson);
		logger.debug("postJson"+postJson );
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "multipart/mixed; boundary="+BOUNDARY);
		request.setHeader("x-csrf-token",this.fetchXCSFRToken());
		
		if(!this.getDestProperty("Authentication").equalsIgnoreCase("BasicAuthentication")){
			List<AuthenticationHeader> authHeaders = this.getAuthHeaders(this.getDestProperty("Authentication"));
			for (AuthenticationHeader header : authHeaders){
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
		logger.debug("responseJson"+response );
		return response;
		
	}
	
	
	
	

	
}
