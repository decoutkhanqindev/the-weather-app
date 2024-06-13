package com.example.weatherapp.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.R;
import com.example.weatherapp.api.RetrofitInstance;
import com.example.weatherapp.api.ApiService;

import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.model.geocodingmodel.GeocodingResponse;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiServiceRepository {
    private final MutableLiveData<CurrentWeatherResponse> currentWeatherResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<GeocodingResponse> geocodingResponseMutableLiveData = new MutableLiveData<>();
    Application application;

    public ApiServiceRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<CurrentWeatherResponse> getCurrentWeatherResponseMutableLiveData(double lat, double lon) {
        ApiService weatherApiService = RetrofitInstance.getCurrentWeatherApi();
        Call<CurrentWeatherResponse> currentWeatherResponseCall = weatherApiService.getCurrentWeather(lat, lon, application.getString(R.string.API_KEY));
        currentWeatherResponseCall.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                CurrentWeatherResponse currentWeatherResponse = response.body();
                if (response.isSuccessful() && response.body() != null) {
                    currentWeatherResponseMutableLiveData.setValue(response.body());
                } else {
                    Log.e("WeatherRepository", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable throwable) {
                Log.e("onFailure: ", Objects.requireNonNull(throwable.getMessage()));
            }
        });
        return currentWeatherResponseMutableLiveData;
    }

    public MutableLiveData<GeocodingResponse> getGeocodingResponseMutableLiveData(String address){
        ApiService geocodingApiService = RetrofitInstance.getGeocodingApi();
        Call<GeocodingResponse> geocodingResponseCall = geocodingApiService.getGeocoding(address, "AIzaSyAkijbXqV1UpaVa8wzCFLLiaXWJL15oMS4");
        geocodingResponseCall.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                GeocodingResponse geocodingResponse = response.body();
                if (geocodingResponse != null){
                    geocodingResponseMutableLiveData.setValue(geocodingResponse);
                } else{
                    Log.e("WeatherRepository", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                Log.e("onFailure: ", Objects.requireNonNull(throwable.getMessage()));
            }
        });
        return geocodingResponseMutableLiveData;
    }
}
