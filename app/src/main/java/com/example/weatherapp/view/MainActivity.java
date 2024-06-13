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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.model.geocodingmodel.GeocodingResponse;
import com.example.weatherapp.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private WeatherViewModel viewModel;
    private double lat = 37.4222804, lon = -122.0843428;
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

       viewModel.getCurrentWeatherResponse(lat, lon).observe(this, new Observer<CurrentWeatherResponse>() {
           @Override
           public void onChanged(CurrentWeatherResponse currentWeatherResponse) {
               if (currentWeatherResponse != null){
                   mainBinding.cityName.setText(currentWeatherResponse.getName());
               } else{
                   Log.e("error", "Current weather response called is failed");
               }
           }
       });
    }
}