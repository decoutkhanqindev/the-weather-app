package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.example.weatherapp.databinding.DailyForecastWeatherCardBinding;
import com.example.weatherapp.model.forecastweathermodel.ForecastWeatherResponse;
import com.example.weatherapp.model.forecastweathermodel.ListItem;
import com.example.weatherapp.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.LogManager;

public class ForecastActivity extends AppCompatActivity {
    private ArrayList<ListItem> listItemArrayList = new ArrayList<>();
    private double lat = 0, lon = 0;
    private WeatherViewModel viewModel;

    private ActivityForecastBinding binding;
    private RecyclerView recyclerView2;
    private DailyForecastWeatherAdapter dailyAdapter;

    private DailyForecastWeatherCardBinding dailyBinding;
    private RecyclerView recyclerView3;
    private HourlyForecastWeatherAdapter hourlyAdapter;

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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ForecastWeatherResponse forecastWeatherResponse) {
                if (forecastWeatherResponse != null){
                    listItemArrayList = forecastWeatherResponse.getList();

                    recyclerView2 = binding.recyclerView2;
                    recyclerView2.setLayoutManager(new LinearLayoutManager(ForecastActivity.this));
                    dailyAdapter = new DailyForecastWeatherAdapter(ForecastActivity.this, listItemArrayList);
                    recyclerView2.setAdapter(dailyAdapter);
                    dailyAdapter.filterTomorrowAndUniquesDate();
                    dailyAdapter.notifyDataSetChanged();

                    dailyAdapter.setOnDayForecastClickListener(new OnDayForecastClickListener() {
                        @Override
                        public void onDayForecastClick(String date) {
                            int position = dailyAdapter.getItemPosition(date);
                            // Get the RecyclerView 3 instance from the ViewHolder
                            DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder viewHolder = (DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder) recyclerView2.findViewHolderForAdapterPosition(position);
                            recyclerView3 = Objects.requireNonNull(viewHolder).binding.recyclerView3;

                            if (recyclerView3.getVisibility() == View.VISIBLE){
                                recyclerView3.setVisibility(View.GONE);
                            } else {
                                recyclerView3.setVisibility(View.VISIBLE);
                                recyclerView3.setLayoutManager(new LinearLayoutManager(ForecastActivity.this));
                                hourlyAdapter = new HourlyForecastWeatherAdapter(ForecastActivity.this, dailyAdapter.getFilteredData());
                                Log.d("onDayForecastClick: ", dailyAdapter.getFilteredData().toString());
                                recyclerView3.setAdapter(hourlyAdapter);
                                hourlyAdapter.updateListForDate(date);
                                hourlyAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }
}