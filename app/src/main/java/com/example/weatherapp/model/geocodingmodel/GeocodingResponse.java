package com.example.weatherapp.model.geocodingmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GeocodingResponse{

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("status")
	private String status;

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}