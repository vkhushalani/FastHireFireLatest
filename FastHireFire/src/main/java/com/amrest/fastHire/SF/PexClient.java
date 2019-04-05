package com.amrest.fastHire.SF;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class PexClient {
	Logger logger = LoggerFactory.getLogger(PexClient.class);
	private DestinationConfiguration destination;
	private String userid;
	private String countryid;
	private String publicKey;
	private String privateKey;

	public void setJWTInitalization(String inUserid, String inCountryid) {
		this.userid = inUserid;
		this.countryid = inCountryid;
	}

	public void setDestination(DestinationConfiguration destination) throws NamingException, IOException {
		this.destination = destination;
		this.setKeys();
	}

	protected void setKeys() throws IOException {
		final String[] credentialDetails = getAuthenticationCredential();
		this.publicKey = credentialDetails[0];
		this.privateKey = credentialDetails[1];
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public HttpResponse callDestinationGET(String path, String filter)
			throws ClientProtocolException, IOException, URISyntaxException {

		String urlString = this.destination.getProperty("URL");
		URL url = new URL(urlString + path + filter);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
				url.getQuery(), url.getRef());
		urlString = uri.toASCIIString();
		logger.debug("GEt urlString" + urlString);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		request.setHeader("Authorization", getJWTTokken());
		logger.debug("JWT token : " + getJWTTokken());
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson" + response);
		return response;
	}

	public HttpResponse callDestinationPOST(String path, String filter, String postJson)
			throws URISyntaxException, ClientProtocolException, IOException {
		String urlString = this.destination.getProperty("URL");
		URL url = new URL(urlString + path + filter);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
				url.getQuery(), url.getRef());
		urlString = uri.toASCIIString();
		logger.debug("urlString" + urlString);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlString);

		StringEntity entity = new StringEntity(postJson, "UTF-8");
		logger.debug("postJson" + postJson);
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		request.setHeader("Authorization", getJWTTokken());
		logger.debug("JWT token : " + getJWTTokken());
		HttpResponse response = httpClient.execute(request);
//		String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		logger.debug("responseJson" + response);
		return response;

	}

	protected String[] getAuthenticationCredential() throws IOException {

		String[] credentialDetails = new String[2];
		String password = this.destination.getProperty("Password");
		String userName = this.destination.getProperty("User");
		credentialDetails[0] = userName;
		credentialDetails[1] = password;

		return credentialDetails;
	}

	protected String getJWTTokken() throws IOException {
		String newHeader = "";
		String customerid;

		// Read GCC from the URL
		String baseUrl = this.destination.getProperty("URL");
		String requestBaseURL = this.destination.getProperty("URL");
		requestBaseURL = requestBaseURL.replace("https://", "");
		requestBaseURL = requestBaseURL.replace("http://", "");
		String[] requestBaseSegment = requestBaseURL.split("/", 0);
		customerid = requestBaseSegment[1];

		// Process JWT
		try {
			// Initialize bouncycastle library to encode RSA key
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			// Initialize timestamp
			long nowMillis = System.currentTimeMillis();
			Date now = new Date(nowMillis);
			Date exp = new Date(nowMillis + 300000);

			// Generate payload
			JSONObject JPayload = new JSONObject();
			JPayload.put("userid", this.userid);
			JPayload.put("customerid", customerid);
			JPayload.put("countryid", this.countryid);
			JPayload.put("iat", now.getTime() / 1000);
			JPayload.put("exp", exp.getTime() / 1000);
			JPayload.put("aud", baseUrl);
			JPayload.put("sub", "cleaHRsky");
			String JWTpayload = JPayload.toString();

			// Generate PKCS#8 key format from PKCS#1 key format
			byte[] data = Base64.getDecoder().decode(this.privateKey);
			ASN1EncodableVector v = new ASN1EncodableVector();
			v.add(new ASN1Integer(0));
			ASN1EncodableVector v2 = new ASN1EncodableVector();
			v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
			v2.add(DERNull.INSTANCE);
			v.add(new DERSequence(v2));
			v.add(new DEROctetString(data));
			ASN1Sequence seq = new DERSequence(v);
			byte[] privKey = seq.getEncoded("DER");

			// Generate RSA object
			PKCS8EncodedKeySpec privateKs = new PKCS8EncodedKeySpec(privKey);
			KeyFactory privateKf = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = privateKf.generatePrivate(privateKs);

			// Generate JWT token
			String compactJws = Jwts.builder().setHeaderParam("alg", "RS256")
					// .setHeaderParam("typ", "JWT")
					.setHeaderParam("x5c", this.publicKey).setPayload(JWTpayload)
					.signWith(SignatureAlgorithm.RS256, privateKey).compact();

			// Format header with the token
			newHeader = "Bearer " + compactJws.toString();
		} catch (Exception e) {

			throw new IOException("JWT" + e.getMessage(), e);
		}

		return newHeader;
	}

}
