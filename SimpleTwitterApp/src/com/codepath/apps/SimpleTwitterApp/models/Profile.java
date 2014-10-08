package com.codepath.apps.SimpleTwitterApp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile {
	private String name;
	private String handle;
	private String id;
	private boolean verified;
	private String imgUrl;
	private String bio;
	private String bannerUrl;
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	/*
	 * Factory method to createa profile out of
	 * profile json object
	 */	
	public static Profile fromJsonObject(JSONObject jsonObject) {
		Profile profile = new Profile();
		try {
			profile.name = jsonObject.getString("name");
			profile.id = jsonObject.getString("id_str");
			profile.handle = jsonObject.getString("screen_name");
			profile.imgUrl = jsonObject.getString("profile_image_url");
			profile.verified = jsonObject.getBoolean("verified");
			profile.bio = jsonObject.getString("description");
			if(jsonObject.has("profile_banner_url")) {
				profile.bannerUrl = jsonObject.getString("profile_banner_url");
			} else {
				profile.bannerUrl = null;
			}
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return profile;
	}
	
	
	/*
	 * Factory method that returns a array of profile 
	 * from a json array of profile objects
	 */
	public static ArrayList<Profile> fromJsonArray(
			JSONArray jsonResponseArray) {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		JSONObject profileObject;
		for(int i=0; i<jsonResponseArray.length(); i++) {
			profileObject = null;
			try {
				profileObject = jsonResponseArray.getJSONObject(i);	
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Profile p = Profile.fromJsonObject(profileObject);
			if(p != null) {
				profiles.add(p);
			}
			
		}
		return profiles;
	}
	
	public String toString() {
		return this.name;
	}
	
}
