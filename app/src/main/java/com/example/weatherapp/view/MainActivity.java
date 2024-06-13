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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.model.currentweathermodel.CurrentWeatherResponse;
import com.example.weatherapp.viewmodel.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private WeatherViewModel viewModel;

    private FusedLocationProviderClient fusedLocationProviderClient;
    int PERMISSION_ID = 44;

    private double lat = 0, lon = 0;

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
    }

    private void updatesCurrentWeatherUI(double lat, double lon) {
        viewModel.getCurrentWeatherResponse(lat, lon).observe(MainActivity.this, new Observer<CurrentWeatherResponse>() {
            @Override
            public void onChanged(CurrentWeatherResponse currentWeatherResponse) {
                if (currentWeatherResponse != null) {
                    mainBinding.cityName.setText(currentWeatherResponse.getName());
                    mainBinding.weatherDescriptionText.setText(currentWeatherResponse.getWeather().get(0).getDescription());

                    long timestamp = currentWeatherResponse.getDt();
                    Date date = new Date(timestamp * 1000L);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMMM dd '|' hh:mm a");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String formattedDate = simpleDateFormat.format(date);
                    mainBinding.dateText.setText(formattedDate);

                    double currentTemp = (double) currentWeatherResponse.getMain().getTemp();
                    double tempCelsius  = currentTemp - 273.15;
                    @SuppressLint("DefaultLocale") String formattedTemp = String.format("%.0f", tempCelsius) + "°C";
                    mainBinding.currentTemp.setText(formattedTemp);

                    double maxTemp = (double) currentWeatherResponse.getMain().getTempMax();
                    double maxTempCelsius  = maxTemp - 273.15;
                    double minTemp = (double) currentWeatherResponse.getMain().getTempMin();
                    double minTempCelsius  = minTemp - 273.15;
                    @SuppressLint("DefaultLocale") String formattedMinMaxTemp = String.format("L:%.0f  H:%.0f", minTempCelsius, maxTempCelsius) + "°";
                    mainBinding.minMaxTemp.setText(formattedMinMaxTemp);
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
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            updatesCurrentWeatherUI(lat, lon);
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
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper());
    }

    private final LocationCallback locationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            if (lastLocation != null) {
                double lat = lastLocation.getLatitude();
                double lon = lastLocation.getLongitude();
                updatesCurrentWeatherUI(lat, lon);
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
