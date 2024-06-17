package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.HourlyForecastWeatherCardBinding;
import com.example.weatherapp.model.forecastweathermodel.ListItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HourlyForecastWeatherAdapter extends RecyclerView.Adapter<HourlyForecastWeatherAdapter.HourlyForecastWeatherAdapterViewHolder> {
    private final Context context;
    private final ArrayList<ListItem> listItemArrayList;
    private final LinkedHashMap<String, ArrayList<ListItem>> listItemLinkedHashMap;

    public HourlyForecastWeatherAdapter(Context context, LinkedHashMap<String, ArrayList<ListItem>> listItemLinkedHashMap) {
        this.context = context;
        this.listItemArrayList = new ArrayList<>();
        this.listItemLinkedHashMap = listItemLinkedHashMap;
    }

    @NonNull
    @Override
    public HourlyForecastWeatherAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HourlyForecastWeatherCardBinding binding = HourlyForecastWeatherCardBinding.inflate(layoutInflater, parent, false);
        return new HourlyForecastWeatherAdapterViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull HourlyForecastWeatherAdapterViewHolder holder, int position) {
        ListItem listItem = listItemArrayList.get(position);

        // Display time
        holder.binding.hourlyForecastText.setText(listItem.getDtTxt().substring(11, 16));

        // Display temperature
        double temp = (double) listItem.getMain().getTemp() - 273.15; // Convert Kelvin to Celsius
        @SuppressLint("DefaultLocale") String formattedTemp = String.format("%.0f°C", temp);
        holder.binding.hourlyTempText.setText(formattedTemp);

        // Display weather icon
        String mainGroup = listItem.getWeather().get(0).getMain();
        setWeatherIcon(holder, mainGroup);

        // Display precipitation probability
        if (listItem.getRain() != null) {
            Double pRain = (Double) listItem.getRain().getJsonMember3h();
            holder.binding.pRainForecastText.setText((pRain * 100) + "%");
        } else {
            holder.binding.pRainForecastText.setText("0%");
        }

        // Display wind speed
        if (listItem.getWind() != null) {
            Double speed = (Double) listItem.getWind().getSpeed();
            holder.binding.windSpeedForecastText.setText(String.format("%.1f m/s", speed));
        } else {
            holder.binding.windSpeedForecastText.setText("N/A");
        }

        // Display humidity
        int pHumidity = listItem.getMain().getHumidity();
        holder.binding.pHumidityForecastText.setText(pHumidity + "%");
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateListForDate(String date) {
        listItemArrayList.clear();
        if (listItemLinkedHashMap.containsKey(date)) {
            listItemArrayList.addAll(listItemLinkedHashMap.get(date));
        }
        notifyDataSetChanged();
    }

    private void setWeatherIcon(HourlyForecastWeatherAdapterViewHolder holder, String mainGroup) {
        switch (mainGroup) {
            case "Thunderstorm":
                holder.binding.hourlyForecastImg.setImageResource(R.drawable.storm);
                break;
            case "Rain":
                holder.binding.hourlyForecastImg.setImageResource(R.drawable.rainy);
                break;
            case "Snow":
                holder.binding.hourlyForecastImg.setImageResource(R.drawable.snowy);
                break;
            case "Atmosphere":
                holder.binding.hourlyForecastImg.setImageResource(R.drawable.atmosphere);
                break;
            case "Clear":
                holder.binding.hourlyForecastImg.setImageResource(R.drawable.sunny);
                break;
            default:
                holder.binding.hourlyForecastImg.setImageResource(R.drawable.cloudy);
                break;
        }
    }

    public static class HourlyForecastWeatherAdapterViewHolder extends RecyclerView.ViewHolder {
        private final HourlyForecastWeatherCardBinding binding;

        public HourlyForecastWeatherAdapterViewHolder(@NonNull HourlyForecastWeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
