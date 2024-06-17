package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.HourlyForecastWeatherCardBinding;
import com.example.weatherapp.model.forecastweathermodel.ListItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HourlyForecastWeatherAdapter extends RecyclerView.Adapter<HourlyForecastWeatherAdapter.HourlyForecastWeatherAdapterViewHolder> {
    Context context;
    ArrayList<ListItem> listItemArrayList;
    LinkedHashMap<String, ArrayList<ListItem>> listItemLinkedHashMap;

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

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastWeatherAdapterViewHolder holder, int position) {
        ListItem listItem = listItemArrayList.get(position);

        holder.binding.hourlyForecastText.setText(listItem.getDtTxt().substring(11, 16));
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

    public static class HourlyForecastWeatherAdapterViewHolder extends RecyclerView.ViewHolder {
        private final HourlyForecastWeatherCardBinding binding;

        public HourlyForecastWeatherAdapterViewHolder(@NonNull HourlyForecastWeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
