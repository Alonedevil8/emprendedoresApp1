package com.spring.emprendedoresApp.models.dtos;

public class ResponseDTO {
	
	private int numOfError;
	
	private String message;

	public int getNumOfError() {
		return numOfError;
	}

	public void setNumOfError(int numOfError) {
		this.numOfError = numOfError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
