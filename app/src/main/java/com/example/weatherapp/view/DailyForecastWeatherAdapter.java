package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.DailyForecastWeatherCardBinding;
import com.example.weatherapp.model.forecastweathermodel.ListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class DailyForecastWeatherAdapter extends RecyclerView.Adapter<DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder> {
    private final Context context;
    private final ArrayList<ListItem> listItemArrayList;
    private final LinkedHashMap<String, ArrayList<ListItem>> filteredData;
    private OnDayForecastClickListener onDayForecastClickListener;

    public DailyForecastWeatherAdapter(Context context, ArrayList<ListItem> listItemArrayList) {
        this.context = context;
        this.listItemArrayList = listItemArrayList;
        this.filteredData = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, ArrayList<ListItem>> getFilteredData() {
        return filteredData;
    }

    public void setOnDayForecastClickListener(OnDayForecastClickListener onDayForecastClickListener) {
        this.onDayForecastClickListener = onDayForecastClickListener;
    }

    @NonNull
    @Override
    public DailyForecastWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DailyForecastWeatherCardBinding binding = DailyForecastWeatherCardBinding.inflate(layoutInflater, parent, false);
        return new DailyForecastWeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastWeatherViewHolder holder, int position) {
        ListItem listItem = listItemArrayList.get(position);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String itemDate = listItem.getDtTxt();
        try {
            Date currentDate = inputFormat.parse(itemDate);
            String currentFormattedDate = outputFormat.format(currentDate);
            holder.binding.dayForecast.setText(currentFormattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.binding.dayForecast.setOnClickListener(v -> {
            if (onDayForecastClickListener != null) {
                onDayForecastClickListener.onDayForecastClick(itemDate.substring(0, 10));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    public int getItemPosition(String date) {
        for (int i = 0; i < listItemArrayList.size(); i++) {
            if (listItemArrayList.get(i).getDtTxt().startsWith(date)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterTomorrowAndUniquesDate() {
        Date todayDate = new Date();
        SimpleDateFormat spfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedTodayDate = spfDate.format(todayDate);
        LinkedHashMap<String, ArrayList<ListItem>> tempMap = new LinkedHashMap<>();

        for (ListItem item : listItemArrayList) {
            String itemDate = item.getDtTxt().substring(0, 10);
            if (itemDate.compareTo(formattedTodayDate) > 0) {
                tempMap.putIfAbsent(itemDate, new ArrayList<>());
                Objects.requireNonNull(tempMap.get(itemDate)).add(item);
            }
        }

        filteredData.clear();
        filteredData.putAll(tempMap);

        listItemArrayList.clear();
        listItemArrayList.addAll(filteredData.values().stream().map(list -> list.get(0)).collect(Collectors.toList()));
        notifyDataSetChanged();
    }

    public static class DailyForecastWeatherViewHolder extends RecyclerView.ViewHolder {
        final DailyForecastWeatherCardBinding binding;

        DailyForecastWeatherViewHolder(@NonNull DailyForecastWeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
