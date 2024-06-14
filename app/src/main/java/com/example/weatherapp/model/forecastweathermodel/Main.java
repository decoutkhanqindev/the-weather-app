package com.example.weatherapp.model.forecastweathermodel;

import com.google.gson.annotations.SerializedName;

public class Main{

	@SerializedName("temp")
	private Object temp;

	@SerializedName("temp_min")
	private Object tempMin;

	@SerializedName("grnd_level")
	private int grndLevel;

	@SerializedName("temp_kf")
	private Object tempKf;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("pressure")
	private int pressure;

	@SerializedName("sea_level")
	private int seaLevel;

	@SerializedName("feels_like")
	private Object feelsLike;

	@SerializedName("temp_max")
	private Object tempMax;

	public void setTemp(Object temp){
		this.temp = temp;
	}

	public Object getTemp(){
		return temp;
	}

	public void setTempMin(Object tempMin){
		this.tempMin = tempMin;
	}

	public Object getTempMin(){
		return tempMin;
	}

	public void setGrndLevel(int grndLevel){
		this.grndLevel = grndLevel;
	}

	public int getGrndLevel(){
		return grndLevel;
	}

	public void setTempKf(Object tempKf){
		this.tempKf = tempKf;
	}

	public Object getTempKf(){
		return tempKf;
	}

	public void setHumidity(int humidity){
		this.humidity = humidity;
	}

	public int getHumidity(){
		return humidity;
	}

	public void setPressure(int pressure){
		this.pressure = pressure;
	}

	public int getPressure(){
		return pressure;
	}

	public void setSeaLevel(int seaLevel){
		this.seaLevel = seaLevel;
	}

	public int getSeaLevel(){
		return seaLevel;
	}

	public void setFeelsLike(Object feelsLike){
		this.feelsLike = feelsLike;
	}

	public Object getFeelsLike(){
		return feelsLike;
	}

	public void setTempMax(Object tempMax){
		this.tempMax = tempMax;
	}

	public Object getTempMax(){
		return tempMax;
	}
}