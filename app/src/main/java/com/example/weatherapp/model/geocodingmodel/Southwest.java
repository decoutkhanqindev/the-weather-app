package com.example.weatherapp.model.geocodingmodel;

import com.google.gson.annotations.SerializedName;

public class Southwest{

	@SerializedName("lng")
	private Object lng;

	@SerializedName("lat")
	private Object lat;

	public void setLng(Object lng){
		this.lng = lng;
	}

	public Object getLng(){
		return lng;
	}

	public void setLat(Object lat){
		this.lat = lat;
	}

	public Object getLat(){
		return lat;
	}
}