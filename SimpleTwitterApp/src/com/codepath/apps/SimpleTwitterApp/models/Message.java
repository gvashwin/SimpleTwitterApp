package com.codepath.apps.SimpleTwitterApp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Message {
	private String id;
	private String text;
	private Profile sender;
	private Profile recepient;
	private String timeStamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Profile getSender() {
		return sender;
	}
	public void setSender(Profile sender) {
		this.sender = sender;
	}
	public Profile getRecepient() {
		return recepient;
	}
	public void setRecepient(Profile recepient) {
		this.recepient = recepient;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String toString() {
		return this.getSender().getName()+ " : "+this.getText();
	}
	
	public static Message fromJsonObject(JSONObject jsonObject) {
		Message msg = new Message();
		try {
			msg.text = jsonObject.getString("text");
			msg.id = jsonObject.getString("id_str");
			msg.timeStamp = jsonObject.getString("created_at");
			msg.sender = Profile.fromJsonObject(jsonObject.getJSONObject("sender"));
			msg.recepient = Profile.fromJsonObject(jsonObject.getJSONObject("recipient"));
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
		return msg;
	}
	
	public static ArrayList<Message> fromJsonArray(
			JSONArray jsonResponseArray) {
		ArrayList<Message> msgList = new ArrayList<Message>();
		JSONObject msgObject;
		for(int i=0; i<jsonResponseArray.length(); i++) {
			msgObject = null;
			try {
				msgObject = jsonResponseArray.getJSONObject(i);	
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Message msg = Message.fromJsonObject(msgObject);
			if(msg != null) {
				msgList.add(msg);
			}
			
		}
		return msgList;
	}
}
