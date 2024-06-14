package com.example.weatherapp.model.forecastweathermodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ListItem{

	@SerializedName("dt")
	private int dt;

	@SerializedName("pop")
	private Object pop;

	@SerializedName("visibility")
	private int visibility;

	@SerializedName("dt_txt")
	private String dtTxt;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("main")
	private Main main;

	@SerializedName("clouds")
	private Clouds clouds;

	@SerializedName("sys")
	private Sys sys;

	@SerializedName("wind")
	private Wind wind;

	@SerializedName("rain")
	private Rain rain;

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setPop(Object pop){
		this.pop = pop;
	}

	public Object getPop(){
		return pop;
	}

	public void setVisibility(int visibility){
		this.visibility = visibility;
	}

	public int getVisibility(){
		return visibility;
	}

	public void setDtTxt(String dtTxt){
		this.dtTxt = dtTxt;
	}

	public String getDtTxt(){
		return dtTxt;
	}

	public void setWeather(List<WeatherItem> weather){
		this.weather = weather;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public void setMain(Main main){
		this.main = main;
	}

	public Main getMain(){
		return main;
	}

	public void setClouds(Clouds clouds){
		this.clouds = clouds;
	}

	public Clouds getClouds(){
		return clouds;
	}

	public void setSys(Sys sys){
		this.sys = sys;
	}

	public Sys getSys(){
		return sys;
	}

	public void setWind(Wind wind){
		this.wind = wind;
	}

	public Wind getWind(){
		return wind;
	}

	public void setRain(Rain rain){
		this.rain = rain;
	}

	public Rain getRain(){
		return rain;
	}
}