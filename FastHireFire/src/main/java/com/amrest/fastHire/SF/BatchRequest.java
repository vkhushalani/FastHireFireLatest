package com.amrest.fastHire.SF;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSet;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSetPart;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchQueryPart;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.ep.EntityProvider;
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
	private BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
	private List<BatchSingleResponse> responses ;
	
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
	private List<AuthenticationHeader> getAuthHeaders(String type) throws NamingException{
		
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
	public void createQueryPart(String uri,String contentId){
		BatchQueryPart request = BatchQueryPart.method("GET").uri(uri).contentId(contentId).build();
		batchParts.add(request);
	}
	
	public void createChangeRequest(String method,String uri,String contentId,String postJson){
		Map<String, String> changeSetHeaders = new HashMap<String, String>();
		changeSetHeaders.put("content-type", "application/json;odata=verbose");
		BatchChangeSetPart changeRequest = BatchChangeSetPart.method(method)
		.uri(uri)
		.headers(changeSetHeaders)
		.body(postJson)
		.contentId(contentId)
		.build();
		changeSet.add(changeRequest);
	}
	public void addChangeSet(){
		batchParts.add(changeSet);
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	private String fetchXCSFRToken() throws ClientProtocolException, IOException, URISyntaxException, NamingException{
		
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
	public void callBatchPOST(String path,String filter) throws URISyntaxException, ClientProtocolException, IOException, NamingException, BatchException, UnsupportedOperationException{
		String urlString = this.createUri(path,filter);
		logger.debug("urlString"+urlString );
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlString);
		
		InputStream payload = EntityProvider.writeBatchRequest(batchParts, BOUNDARY);
		
//		StringWriter writer = new StringWriter();
//		IOUtils.copy(payload, writer, null);
//		String theString = writer.toString();

		
		InputStreamEntity entity = new InputStreamEntity(payload);
//		logger.debug("postJson"+theString);
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-Type", "multipart/mixed; boundary="+BOUNDARY);
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
		HttpEntity responseEntity = response.getEntity();
		logger.debug("Content Type: "+responseEntity.getContentType().getValue());
		logger.debug("responseEntity.getContent()" + responseEntity.getContent());
		this.setResponses(EntityProvider.parseBatchResponse(responseEntity.getContent(), responseEntity.getContentType().getValue()));
		logger.debug("responseJson"+response );
		
		
	}

	public List<BatchSingleResponse> getResponses() {
		return responses;
	}

	private void setResponses(List<BatchSingleResponse> responses) {
		this.responses = responses;
	}
	
	public void initializeBatchParts(){
		this.batchParts = new ArrayList<BatchPart>(); 
	}
	
	
	
	

	
}
