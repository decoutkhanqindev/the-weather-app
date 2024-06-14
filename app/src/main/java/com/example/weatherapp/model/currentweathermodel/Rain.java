package com.example.weatherapp.model.currentweathermodel;

import com.google.gson.annotations.SerializedName;

public class Rain{

	@SerializedName("1h")
	private Double jsonMember1h;

	public void setJsonMember1h(Double jsonMember1h){
		this.jsonMember1h = jsonMember1h;
	}

	public Double getJsonMember1h(){
		return jsonMember1h;
	}
}