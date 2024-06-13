package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Rain{

	@SerializedName("1h")
	private Object jsonMember1h;

	public void setJsonMember1h(Object jsonMember1h){
		this.jsonMember1h = jsonMember1h;
	}

	public Object getJsonMember1h(){
		return jsonMember1h;
	}
}