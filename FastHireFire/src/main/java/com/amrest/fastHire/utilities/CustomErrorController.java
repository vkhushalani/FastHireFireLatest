package com.amrest.fastHire.utilities;

/*
 * Utilities class <h1>CustomErrorController</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 2.0
 */
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

	private static final String errorMessage = "Please check the url.";

	@RequestMapping(value = ConstantManager.genError)
	public String error() {
		return errorMessage;
	}

	@Override
	public String getErrorPath() {
		return ConstantManager.genError;
	}

}
