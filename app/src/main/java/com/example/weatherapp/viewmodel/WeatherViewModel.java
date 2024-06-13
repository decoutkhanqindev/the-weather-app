package com.example.weatherapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.model.WeatherRepository;
import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository weatherRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        this.weatherRepository = new WeatherRepository(application);
    }

    public MutableLiveData<CurrentWeatherResponse> getCurrentWeatherResponse(double lat, double lon){
        return weatherRepository.getCurrentWeatherResponseMutableLiveData(lat, lon);
    }
}
