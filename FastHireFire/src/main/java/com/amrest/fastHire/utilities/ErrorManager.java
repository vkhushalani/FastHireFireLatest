package com.amrest.fastHire.utilities;

/*
 * Utilities class <h1>ErrorManager</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 2.0
 */
public class ErrorManager {

	private int status;
	private int code;
	private String message;

	public ErrorManager(int status, int code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "{" + "status=" + status + ", code=" + code + ", message=" + message + '}';
	}
}
