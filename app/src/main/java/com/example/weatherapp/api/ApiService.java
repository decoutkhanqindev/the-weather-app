package com.example.weatherapp.api;

import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.model.forecastweathermodel.ForecastWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // current weather
    @GET("data/2.5/weather")
    Call<CurrentWeatherResponse> getCurrentWeather (@Query("lat") double lat,
                                                   @Query("lon") double lon,
                                                   @Query("appid") String apiKey);

    @GET("data/2.5/forecast")
    Call<ForecastWeatherResponse> getForecastWeather (@Query("lat") double lat,
                                                      @Query("lon") double lon,
                                                      @Query("appid") String apiKey);
}
