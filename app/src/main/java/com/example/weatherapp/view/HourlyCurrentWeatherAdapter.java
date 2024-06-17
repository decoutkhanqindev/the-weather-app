package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.HourlyCurrentWeatherCardBinding;
import com.example.weatherapp.model.forecastweathermodel.ListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HourlyCurrentWeatherAdapter extends RecyclerView.Adapter<HourlyCurrentWeatherAdapter.HourlyCurrentWeatherViewHolder> {
    private final Context context;
    private final ArrayList<ListItem> listItems;

    public HourlyCurrentWeatherAdapter(Context context, ArrayList<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public HourlyCurrentWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HourlyCurrentWeatherCardBinding binding = HourlyCurrentWeatherCardBinding.inflate(layoutInflater, parent, false);
        return new HourlyCurrentWeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyCurrentWeatherViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        // Get today's date
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String itemDate = listItem.getDtTxt().substring(0, 10); // Extract yyyy-MM-dd from dt_txt

        // Only display items for the current day
        if (!todayDate.equals(itemDate)) {
            return; // Skip non-current day items
        }

        // Display time
        String time = listItem.getDtTxt().substring(11, 16); // Extract HH:mm from dt_txt
        holder.binding.hourlyText.setText(time);

        // Display weather icon based on weather condition
        String mainGroup = listItem.getWeather().get(0).getMain();
        switch (mainGroup) {
            case "Thunderstorm":
                holder.binding.hourlyImg.setImageResource(R.drawable.storm);
                break;
            case "Rain":
                holder.binding.hourlyImg.setImageResource(R.drawable.rainy);
                break;
            case "Snow":
                holder.binding.hourlyImg.setImageResource(R.drawable.snowy);
                break;
            case "Atmosphere":
                holder.binding.hourlyImg.setImageResource(R.drawable.atmosphere);
                break;
            case "Clear":
                holder.binding.hourlyImg.setImageResource(R.drawable.sunny);
                break;
            default:
                holder.binding.hourlyImg.setImageResource(R.drawable.cloudy);
                break;
        }

        // Display temperature
        double temp = (double) listItem.getMain().getTemp();
        double tempCelsius = temp - 273.15;
        String formattedTemp = String.format(Locale.getDefault(), "%.0f°C", tempCelsius);
        holder.binding.hourlyTempText.setText(formattedTemp);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // Filter the list to show only items for the current day
    @SuppressLint("NotifyDataSetChanged")
    public void filterCurrentDay() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ArrayList<ListItem> filteredList = new ArrayList<>();

        for (ListItem item : listItems) {
            String itemDate = item.getDtTxt().substring(0, 10); // Extract yyyy-MM-dd from dt_txt
            if (todayDate.equals(itemDate)) {
                filteredList.add(item);
            }
        }

        listItems.clear();
        listItems.addAll(filteredList);
        notifyDataSetChanged();
    }

    public static class HourlyCurrentWeatherViewHolder extends RecyclerView.ViewHolder {
        private final HourlyCurrentWeatherCardBinding binding;

        public HourlyCurrentWeatherViewHolder(@NonNull HourlyCurrentWeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
