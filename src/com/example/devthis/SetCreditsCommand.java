package com.example.devthis;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

public class SetCreditsCommand implements ICommand {

	private CheckAccountActivity _checkAccountAcitivity;
	
	public SetCreditsCommand(CheckAccountActivity a) {
		_checkAccountAcitivity = a;
	}
	
	@Override
	public void execute(String data) {
		JSONObject jData;
		try {
			jData = new JSONObject(data);
			_checkAccountAcitivity.setCredits( jData.getJSONObject("visitor").get("tokens").toString() );
		} catch (JSONException e) {
			_checkAccountAcitivity.setCredits("IDK\n:S");
		}
	}
	
	public HttpUriRequest getRequest() {
		HttpGet httpGet = new HttpGet(MainActivity.API_URL+"visitor/"+MainActivity.getIdTag());
		return httpGet;
	}

	@Override
	public String getPostData() {
		return null;
	}

}
