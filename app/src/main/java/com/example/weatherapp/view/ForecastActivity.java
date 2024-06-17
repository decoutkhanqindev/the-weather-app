package com.example.weatherapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.ActivityForecastBinding;
import com.example.weatherapp.model.forecastweathermodel.ForecastWeatherResponse;
import com.example.weatherapp.model.forecastweathermodel.ListItem;
import com.example.weatherapp.viewmodel.WeatherViewModel;

import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity {

    private ArrayList<ListItem> listItemArrayList = new ArrayList<>();

    private ActivityForecastBinding binding;
    private RecyclerView recyclerView2;
    private DailyForecastWeatherAdapter dailyAdapter;

    private RecyclerView recyclerView3;
    private HourlyForecastWeatherAdapter hourlyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        ImageView backBtn = binding.backBtn;
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ForecastActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Initialize ViewModel
        WeatherViewModel viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        // Retrieve latitude and longitude from intent
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lon = intent.getDoubleExtra("lon", 0.0);

        // Observe forecast data changes
        viewModel.getForecastWeatherResponse(lat, lon).observe(this, forecastWeatherResponse -> {
            if (forecastWeatherResponse != null){
                listItemArrayList = forecastWeatherResponse.getList();

                // Set up RecyclerView 2 (daily forecast)
                recyclerView2 = binding.recyclerView2;
                recyclerView2.setLayoutManager(new LinearLayoutManager(ForecastActivity.this));
                dailyAdapter = new DailyForecastWeatherAdapter(ForecastActivity.this, listItemArrayList);
                recyclerView2.setAdapter(dailyAdapter);
                dailyAdapter.filterTomorrowAndUniquesDate();

                // Handle day forecast click
                dailyAdapter.setOnDayForecastClickListener(date -> {
                    int position = dailyAdapter.getItemPosition(date);

                    // Retrieve RecyclerView 3 instance from ViewHolder
                    DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder viewHolder =
                            (DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder) recyclerView2.findViewHolderForAdapterPosition(position);
                    if (viewHolder != null) {
                        recyclerView3 = viewHolder.binding.recyclerView3;

                        // Toggle visibility of RecyclerView 3
                        if (recyclerView3.getVisibility() == View.VISIBLE) {
                            recyclerView3.setVisibility(View.GONE);
                        } else {
                            recyclerView3.setVisibility(View.VISIBLE);
                            recyclerView3.setLayoutManager(new LinearLayoutManager(ForecastActivity.this));
                            hourlyAdapter = new HourlyForecastWeatherAdapter(ForecastActivity.this, dailyAdapter.getFilteredData());
                            recyclerView3.setAdapter(hourlyAdapter);
                            hourlyAdapter.updateListForDate(date);
                        }
                    }
                });
            }
        });
    }
}
