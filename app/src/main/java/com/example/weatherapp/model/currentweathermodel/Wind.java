package com.example.weatherapp.model.currentweathermodel;

import com.google.gson.annotations.SerializedName;

public class Wind{

	@SerializedName("deg")
	private int deg;

	@SerializedName("speed")
	private Double speed;

	@SerializedName("gust")
	private Double gust;

	public void setDeg(int deg){
		this.deg = deg;
	}

	public int getDeg(){
		return deg;
	}

	public void setSpeed(Double speed){
		this.speed = speed;
	}

	public Double getSpeed(){
		return speed;
	}

	public void setGust(Double gust){
		this.gust = gust;
	}

	public Double getGust(){
		return gust;
	}
}