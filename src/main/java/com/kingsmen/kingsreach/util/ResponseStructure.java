package com.kingsmen.kingsreach.util;

import org.springframework.stereotype.Component;

@Component
public class ResponseStructure<T> {

	private String message;
	private int statusCode;
	private T data;

	public int getStatusCode() {
		return statusCode;
	}

	public ResponseStructure<T> setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResponseStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public ResponseStructure<T> setData(T data) {
		this.data = data;
		return this;
	}

}