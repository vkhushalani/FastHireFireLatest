package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.SF.DestinationClient;
import com.amrest.fastHire.SF.UrlClient;

@RestController
@RequestMapping("/FastHireAdmin")
public class SCPOperations {
	
	Logger logger = LoggerFactory.getLogger(SCPOperations.class);
	
	public static final String tokenDestName = "PlatFormClientAuthToken";
	
	@GetMapping("/GetGroups")
	public ResponseEntity <?> GetGroups() throws ClientProtocolException, NamingException, URISyntaxException, IOException{
		JSONObject tokenResponseObject = getoAuthToken();
		String aToken = tokenResponseObject.getString("access_token");
		String tokenType = tokenResponseObject.getString("token_type");
		
		UrlClient client= new UrlClient();
		String cloudHost = System.getenv("HC_HOST");
		String cloudAccount = System.getenv("HC_ACCOUNT");
		client.setBaseUrl("https://api."+cloudHost+"/authorization/v1");
		client.setPath("/accounts/"+cloudAccount+"/groups");
		client.setFilter("");
		client.setTokenType(tokenType);
		client.setUserName("");
		client.setPassword(aToken);
		HttpResponse response = client.callDestinationGET();
		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		if(response.getStatusLine().getStatusCode() == 200){
			return 	ResponseEntity.ok().body(responseString);
			}
		else
		{
			return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/AssignUserToGroup")
	public ResponseEntity <?> assignUserToGroup(@RequestBody String postJson) throws NamingException, ClientProtocolException, URISyntaxException, IOException{
		
		Map<String,String> map = new HashMap<String,String>();  
		
		// get post JSON Object GroupName,Users Comma Separated
				JSONObject postObject = new JSONObject(postJson);
				Iterator<?> keys = postObject.keys();
				
				while(keys.hasNext()) {
				    String key = (String)keys.next();
				    map.put(key, postObject.getString(key));
				}
				
				// get auth Token
				JSONObject tokenResponseObject = getoAuthToken();
				String aToken = tokenResponseObject.getString("access_token");
				String tokenType = tokenResponseObject.getString("token_type");
				
				
				// set the url client 
				UrlClient client= new UrlClient();
				String cloudHost = System.getenv("HC_HOST");
				String cloudAccount = System.getenv("HC_ACCOUNT");
				client.setBaseUrl("https://api."+cloudHost+"/authorization/v1");
				client.setPath("/accounts/"+cloudAccount+"/groups/users/");
				client.setFilter("?groupName="+map.get("GroupName"));
				client.setTokenType(tokenType);
				client.setUserName("");
				client.setPassword(aToken);
				
				// create post json
				String json = "{\"users\":[";
				String usersString = map.get("users");
				String[] parts = usersString.split(",");
				int counter = 0;
				for(String user : parts){
					json = json + "{\"name\" : \""+user+"\"}";
					
					counter = counter + 1;
					if(counter != parts.length){
						json = json + ",";
					}
				}
				json = json +"]}";
				
				client.setPostJson(json);
				HttpResponse response = client.callDestinationPUT();
				if(response.getStatusLine().getStatusCode() == 200){
				return 	ResponseEntity.ok().body("Success");
				}
				
				return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@DeleteMapping("/DeleteUserFromGroup")
	public ResponseEntity <?> deleteUserFromGroup(@RequestBody String postJson) throws NamingException, ClientProtocolException, URISyntaxException, IOException{
		
		Map<String,String> map = new HashMap<String,String>();  
		
		// get post JSON Object GroupName,Users Comma Separated
				JSONObject postObject = new JSONObject(postJson);
				Iterator<?> keys = postObject.keys();
				
				while(keys.hasNext()) {
				    String key = (String)keys.next();
				    map.put(key, postObject.getString(key));
				}
				
				// get auth Token
				JSONObject tokenResponseObject = getoAuthToken();
				String aToken = tokenResponseObject.getString("access_token");
				String tokenType = tokenResponseObject.getString("token_type");
				
				
				// set the url client 
				UrlClient client= new UrlClient();
				String cloudHost = System.getenv("HC_HOST");
				String cloudAccount = System.getenv("HC_ACCOUNT");
				client.setBaseUrl("https://api."+cloudHost+"/authorization/v1");
				client.setPath("/accounts/"+cloudAccount+"/groups/users/");
				client.setTokenType(tokenType);
				client.setUserName("");
				client.setPassword(aToken);
				
				// create filter String
				String filter = "?groupName="+map.get("GroupName")+"&users=";
				String usersString = map.get("users");
				String[] parts = usersString.split(",");
				int counter = 0;
				for(String user : parts){
					filter = filter + user;
					
					counter = counter + 1;
					if(counter != parts.length){
						filter = filter + ";";
					}
				}
				
				
				client.setFilter(filter);
				HttpResponse response = client.callDestinationDELETE();
				if(response.getStatusLine().getStatusCode() == 200){
				return 	ResponseEntity.ok().body("Success");
				}
				
				return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	public JSONObject getoAuthToken() throws NamingException, ClientProtocolException, URISyntaxException, IOException{
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(tokenDestName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		HttpResponse tokenResponse =  destClient.callDestinationPOST("", "", "");
		String tokenResponseJsonString = EntityUtils.toString(tokenResponse.getEntity(), "UTF-8");
		JSONObject tokenResponseObject = new JSONObject(tokenResponseJsonString);
		
		return tokenResponseObject;
		
	}

}
