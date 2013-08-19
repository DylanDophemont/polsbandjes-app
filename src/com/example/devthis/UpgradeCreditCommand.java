package com.example.devthis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class UpgradeCreditCommand implements ICommand {

	private UpgradeCreditActivity _upgradeCreditActivity;
	
	public UpgradeCreditCommand(UpgradeCreditActivity a) {
		this._upgradeCreditActivity = a;
	}
	
	@Override
	public void execute(String data) {
//		JSONObject jData;
//		try {
//			jData = new JSONObject(data);
//			_checkAccountAcitivity.setCredits( jData.getJSONObject("visitor").get("tokens").toString() );
//		} catch (JSONException e) {
//			_checkAccountAcitivity.setCredits("IDK\n:S");
//		}
	}

	@Override
	public HttpUriRequest getRequest() {
		try {
			HttpPost httpPost = new HttpPost(MainActivity.API_URL+MainActivity.getIdTag()+"/reload");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add( new BasicNameValuePair("value", this.getPostData()) );
			httpPost.setEntity( new UrlEncodedFormEntity(params) );
			return httpPost;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public String getPostData() {
		return "{\"amount\":1}";
	}

}
