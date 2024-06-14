package com.example.weatherapp.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.HourlyWeatherCardBinding;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {
    Context context;

    public static class HourlyViewHolder extends RecyclerView.ViewHolder{
        private final HourlyWeatherCardBinding binding;
        public HourlyViewHolder(@NonNull HourlyWeatherCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
