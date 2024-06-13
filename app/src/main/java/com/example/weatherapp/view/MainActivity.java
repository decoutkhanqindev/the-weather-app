package com.example.weatherapp.view;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private WeatherViewModel weatherViewModel;
    private CurrentWeatherResponse currentWeatherResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        double lat = 10.99, lon = 44.34;
        weatherViewModel.getCurrentWeatherResponse(lat, lon).observe(this, new Observer<CurrentWeatherResponse>() {
            @Override
            public void onChanged(CurrentWeatherResponse currentWeatherResponse) {
                if (currentWeatherResponse != null) {
                    mainBinding.cityName.setText(currentWeatherResponse.getName());
                    // Optionally log the weather response
                    Log.d("TAGY", "Weather response: "  + currentWeatherResponse.getName());
                } else {
                    // Log an error message if the response is null
                    Log.e("TAGY", "Failed to retrieve weather data.");
                }

            }
        });
    }
}