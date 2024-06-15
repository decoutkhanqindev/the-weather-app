package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityForecastBinding;
import com.example.weatherapp.model.forecastweathermodel.ForecastWeatherResponse;
import com.example.weatherapp.model.forecastweathermodel.ListItem;
import com.example.weatherapp.viewmodel.WeatherViewModel;

import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity {
    private ActivityForecastBinding binding;
    private WeatherViewModel viewModel;
    private RecyclerView recyclerView;
    private DailyForecastWeatherAdapter dailyAdapter;
    private ArrayList<ListItem> listItemArrayList = new ArrayList<>();
    private double lat = 0, lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lon = intent.getDoubleExtra("lon", 0.0);

        viewModel.getForecastWeatherResponse(lat, lon).observe(this, new Observer<ForecastWeatherResponse>() {
            @Override
            public void onChanged(ForecastWeatherResponse forecastWeatherResponse) {
                if (forecastWeatherResponse != null){
                    listItemArrayList = forecastWeatherResponse.getList();
                    recyclerView = binding.recyclerView2;
                    recyclerView.setLayoutManager(new LinearLayoutManager(ForecastActivity.this));
                    dailyAdapter = new DailyForecastWeatherAdapter(ForecastActivity.this, listItemArrayList);
                    dailyAdapter.filterFromTomorrow();
                    recyclerView.setAdapter(dailyAdapter);
                }
            }
        });

    }
}