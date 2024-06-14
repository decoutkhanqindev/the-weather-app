package com.example.weatherapp.model.forecastweathermodel;

import com.google.gson.annotations.SerializedName;

public class Rain{

	@SerializedName("3h")
	private Object jsonMember3h;

	public void setJsonMember3h(Object jsonMember3h){
		this.jsonMember3h = jsonMember3h;
	}

	public Object getJsonMember3h(){
		return jsonMember3h;
	}
}