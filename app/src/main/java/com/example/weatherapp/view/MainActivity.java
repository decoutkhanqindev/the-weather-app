package com.example.weatherapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.example.weatherapp.model.geocodingmodel.GeocodingResponse;
import com.example.weatherapp.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private WeatherViewModel viewModel;
    private double lat = 0, lon = 0;

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

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        mainBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = mainBinding.cityName.getText().toString().toLowerCase().trim();
                viewModel.getGeocodingResponse(cityName).observe(MainActivity.this, new Observer<GeocodingResponse>() {
                    @Override
                    public void onChanged(GeocodingResponse geocodingResponse) {
                        if (geocodingResponse != null && !geocodingResponse.getResults().isEmpty()) {
                            lat = (double) geocodingResponse.getResults().get(0).getGeometry().getLocation().getLat();
                            lon = (double) geocodingResponse.getResults().get(0).getGeometry().getLocation().getLng();
                            viewModel.getCurrentWeatherResponse(lat, lon).observe(MainActivity.this, new Observer<CurrentWeatherResponse>() {
                                @Override
                                public void onChanged(CurrentWeatherResponse currentWeatherResponse) {
                                    if (currentWeatherResponse != null) {
                                        mainBinding.cityName.setText(currentWeatherResponse.getName());
                                    } else {
                                        Log.e("current weather error", "Current weather response call failed");
                                    }
                                }
                            });
                        } else {
                            Log.e("Geocoding Error", "No results found");
                        }
                    }
                });
            }
        });
    }
}
