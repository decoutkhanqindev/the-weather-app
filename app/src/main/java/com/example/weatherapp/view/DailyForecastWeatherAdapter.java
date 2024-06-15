package com.example.weatherapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
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

        String itemDate = listItem.getDtTxt(); // Định dạng chuỗi ban đầu: yyyy-MM-dd HH:mm:ss
        Date date = null; // Chuyển chuỗi thành đối tượng Date
        try {
            date = inputFormat.parse(itemDate);
            String formattedDate = outputFormat.format(date); // Định dạng lại đối tượng Date thành chuỗi theo định dạng mới
            holder.binding.dateForecast.setText(formattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterFromTomorrow() {
        @SuppressLint("SimpleDateFormat") String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ArrayList<ListItem> filteredList = new ArrayList<>();

        for (ListItem item : listItemArrayList) {
            String itemDate = item.getDtTxt().substring(0, 10); // Lấy phần đầu tiên yyyy-MM-dd từ dt_txt
            if (itemDate.compareTo(todayDate) > 0) { // Chỉ thêm các mục từ ngày mai trở đi
                filteredList.add(item);
            }
        }

        listItemArrayList.clear();
        listItemArrayList.addAll(filteredList);
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
