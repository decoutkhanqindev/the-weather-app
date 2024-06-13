package com.example.weatherapp.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.R;
import com.example.weatherapp.api.RetrofitInstance;
import com.example.weatherapp.api.ApiService;

import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiServiceRepository {
    private final MutableLiveData<CurrentWeatherResponse> currentWeatherResponseMutableLiveData = new MutableLiveData<>();
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
}
