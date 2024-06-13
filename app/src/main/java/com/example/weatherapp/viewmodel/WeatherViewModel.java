package com.example.weatherapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.model.ApiServiceRepository;
import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.model.geocodingmodel.GeocodingResponse;

public class WeatherViewModel extends AndroidViewModel {
    private final ApiServiceRepository apiServiceRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        this.apiServiceRepository = new ApiServiceRepository(application);
    }

    public MutableLiveData<CurrentWeatherResponse> getCurrentWeatherResponse(double lat, double lon){
        return apiServiceRepository.getCurrentWeatherResponseMutableLiveData(lat, lon);
    }

    public MutableLiveData<GeocodingResponse> getGeocodingResponse(String address){
        return apiServiceRepository.getGeocodingResponseMutableLiveData(address);
    }
}
