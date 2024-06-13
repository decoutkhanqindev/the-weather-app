package com.example.weatherapp.api;

import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    // current weather
    @GET("data/2.5/weather")
    Call<CurrentWeatherResponse> getCurrentWeather(@Query("lat") double lat,
                                                   @Query("lon") double lon,
                                                   @Query("appid") String apiKey);
}