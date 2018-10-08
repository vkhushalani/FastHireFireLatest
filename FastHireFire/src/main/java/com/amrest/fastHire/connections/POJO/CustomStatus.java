package com.amrest.fastHire.connections.POJO;

public class CustomStatus {

	private String statusCode;

	private String length;

	private String message;

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		String string = "{" + "\"statusCode\"" + ":" + "\"" + statusCode + "\"," + "\"length\"" + ":" + "\"" + length
				+ "\"," + "\"message\"" + ":" + "\"" + message + "\"" + "}";
		return string;
	}
}
