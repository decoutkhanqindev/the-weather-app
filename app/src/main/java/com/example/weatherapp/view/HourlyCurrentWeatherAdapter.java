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

        // Lọc các mục cho ngày hiện tại
        @SuppressLint("SimpleDateFormat") String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String itemDate = listItem.getDtTxt().substring(0, 10); // Lấy phần đầu tiên yyyy-MM-dd từ dt_txt

        if (!todayDate.equals(itemDate)) {
            // Nếu không phải ngày hôm nay, không làm gì cả
            return;
        }

        // Xử lý thời gian
        String time = listItem.getDtTxt().substring(11, 16); // Lấy phần giờ từ dt_txt (HH:mm)
        holder.binding.hourlyText.setText(time);

        // Xử lý icon thời tiết
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

        // Xử lý nhiệt độ
        double temp = (double) listItem.getMain().getTemp();
        double tempCelsius = temp - 273.15;
        @SuppressLint("DefaultLocale") String formattedTemp = String.format("%.0f°C", tempCelsius);
        holder.binding.hourlyTempText.setText(formattedTemp);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // Phương thức này sẽ lọc dữ liệu để chỉ hiển thị các mốc thời gian của ngày hiện tại
    @SuppressLint("NotifyDataSetChanged")
    public void filterCurrentDay() {
        @SuppressLint("SimpleDateFormat") String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ArrayList<ListItem> filteredList = new ArrayList<>();

        for (ListItem item : listItems) {
            String itemDate = item.getDtTxt().substring(0, 10); // Lấy phần đầu tiên yyyy-MM-dd từ dt_txt
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
