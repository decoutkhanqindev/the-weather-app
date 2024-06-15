package com.example.weatherapp.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.model.forecastweathermodel.ForecastWeatherResponse;
import com.example.weatherapp.model.forecastweathermodel.ListItem;
import com.example.weatherapp.viewmodel.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private WeatherViewModel viewModel;

    private FusedLocationProviderClient fusedLocationProviderClient;
    int PERMISSION_ID = 44;

    private double lat = 0, lon = 0;

    private RecyclerView recyclerView;
    private HourlyCurrentWeatherAdapter adapter;
    private ArrayList<ListItem> listItemArrayList = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        mainBinding.next3h5daysBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
            }
        });
    }

    private void updatesCurrentWeatherUI(double lat, double lon) {
        viewModel.getCurrentWeatherResponse(lat, lon).observe(MainActivity.this, new Observer<CurrentWeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(CurrentWeatherResponse currentWeatherResponse) {
                if (currentWeatherResponse != null) {
                    mainBinding.cityName.setText(currentWeatherResponse.getName());
                    mainBinding.weatherDescriptionText.setText(currentWeatherResponse.getWeather().get(0).getDescription());

                    String mainGroup = currentWeatherResponse.getWeather().get(0).getMain();
                    switch (mainGroup) {
                        case "Thunderstorm":
                            mainBinding.weatherDescriptionImg.setImageResource(R.drawable.storm);
                            break;
                        case "Rain":
                            mainBinding.weatherDescriptionImg.setImageResource(R.drawable.rainy);
                            break;
                        case "Snow":
                            mainBinding.weatherDescriptionImg.setImageResource(R.drawable.snowy);
                            break;
                        case "Atmosphere":
                            mainBinding.weatherDescriptionImg.setImageResource(R.drawable.atmosphere);
                            break;
                        case "Clear":
                            mainBinding.weatherDescriptionImg.setImageResource(R.drawable.sunny);
                            break;
                        default:
                            mainBinding.weatherDescriptionImg.setImageResource(R.drawable.cloudy);
                            break;
                    }

                    long timestamp = currentWeatherResponse.getDt();
                    Date date = new Date(timestamp * 1000L);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMMM dd '|' hh:mm a");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String formattedDate = simpleDateFormat.format(date);
                    mainBinding.dateText.setText(formattedDate);

                    double currentTemp = (double) currentWeatherResponse.getMain().getTemp();
                    double tempCelsius = currentTemp - 273.15;
                    @SuppressLint("DefaultLocale") String formattedTemp = String.format("%.0f", tempCelsius) + "°C";
                    mainBinding.currentTemp.setText(formattedTemp);

                    double maxTemp = (double) currentWeatherResponse.getMain().getTempMax();
                    double maxTempCelsius = maxTemp - 273.15;
                    double minTemp = (double) currentWeatherResponse.getMain().getTempMin();
                    double minTempCelsius = minTemp - 273.15;
                    @SuppressLint("DefaultLocale") String formattedMinMaxTemp = String.format("L:%.0f°  H:%.0f°", minTempCelsius, maxTempCelsius);
                    mainBinding.minMaxTemp.setText(formattedMinMaxTemp);

                    if (currentWeatherResponse.getRain() != null) {
                        Double pRain = currentWeatherResponse.getRain().getJsonMember1h();
                        mainBinding.pRainText.setText((pRain * 100) + "%");
                    } else {
                        mainBinding.pRainText.setText("0%");
                    }

                    if (currentWeatherResponse.getWind() != null){
                        Double speed = currentWeatherResponse.getWind().getSpeed();
                        mainBinding.windSpeedText.setText((speed) + " m/s");
                    } else {
                        mainBinding.windSpeedText.setText("error");
                    }

                    if (currentWeatherResponse.getMain().getHumidity() != 0){
                        int pHumidity = currentWeatherResponse.getMain().getHumidity();
                        mainBinding.pHumidityText.setText(pHumidity + "%");
                    } else {
                        mainBinding.pHumidityText.setText("error");
                    }
                }
            }
        });
    }

    private void updatesForecastWeatherUI(double lat, double lon) {
        viewModel.getForecastWeatherResponse(lat, lon).observe(MainActivity.this, new Observer<ForecastWeatherResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ForecastWeatherResponse forecastWeatherResponse) {
                if (forecastWeatherResponse != null) {
                    listItemArrayList = forecastWeatherResponse.getList();
                    recyclerView = mainBinding.recyclerView;
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                    adapter = new HourlyCurrentWeatherAdapter(MainActivity.this, listItemArrayList);
                    adapter.filterCurrentDay();
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Log.e("onComplete: ", "lat:" + lat + "lon:" + lon);
                            updatesCurrentWeatherUI(lat, lon);
                            updatesForecastWeatherUI(lat, lon);
                        } else {
                            requestNewLocationData();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }


    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000)
                .setNumUpdates(1);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            if (lastLocation != null) {
                double lat = lastLocation.getLatitude();
                double lon = lastLocation.getLongitude();
                updatesCurrentWeatherUI(lat, lon);
                updatesForecastWeatherUI(lat, lon);
            }
        }
    };

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}
