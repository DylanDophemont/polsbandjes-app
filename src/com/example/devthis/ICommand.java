package com.example.devthis;

import org.apache.http.client.methods.HttpUriRequest;

public interface ICommand {
	
	void execute(String data);
	HttpUriRequest getRequest();
	String getPostData();
	
}