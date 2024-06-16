package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.ForecastWeatherCardBinding;
import com.example.weatherapp.model.forecastweathermodel.ListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;


public class DailyForecastWeatherAdapter extends RecyclerView.Adapter<DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder> {
    Context context;
    ArrayList<ListItem> listItemArrayList;

    public DailyForecastWeatherAdapter(Context context, ArrayList<ListItem> listItemArrayList) {
        this.context = context;
        this.listItemArrayList = listItemArrayList;
    }

    @NonNull
    @Override
    public DailyForecastWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ForecastWeatherCardBinding binding = ForecastWeatherCardBinding.inflate(layoutInflater, parent, false);
        return new DailyForecastWeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastWeatherViewHolder holder, int position) {
        ListItem listItem = listItemArrayList.get(position);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        String itemDate = listItem.getDtTxt(); // Định dạng chuỗi ban đầu: yyyy-MM-dd HH:mm:ss\
        try {
            Date currentDate = inputFormat.parse(itemDate);
            String currentFormattedDate = outputFormat.format(currentDate);
            holder.binding.dateForecast.setText(currentFormattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterTomorrowAndUniquesDate(){
        Date todayDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat spfDate = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTodayDate = spfDate.format(todayDate);
        LinkedHashMap<String, ListItem> listItemLinkedHashMap = new LinkedHashMap<>();

        for (ListItem item : listItemArrayList){
            String itemDate = item.getDtTxt().substring(0, 10);
            if (itemDate.compareTo(formattedTodayDate) > 0){
                listItemLinkedHashMap.putIfAbsent(itemDate, item);
            }
        }

        listItemArrayList.clear();
        listItemArrayList.addAll(listItemLinkedHashMap.values());
        notifyDataSetChanged();
    }

    public static class DailyForecastWeatherViewHolder extends RecyclerView.ViewHolder {
        private final ForecastWeatherCardBinding binding;

        public DailyForecastWeatherViewHolder(@NonNull ForecastWeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
