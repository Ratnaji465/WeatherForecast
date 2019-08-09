package com.example.weatherforecast.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.model.CityDO;

import java.util.Vector;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.CityHolder>{
    private OnItemClickListener listener;
    private  Vector<CityDO> vec;
    private int selectedPosition = 0 ;

    public ForecastAdapter(Vector<CityDO> cityDOS, OnItemClickListener listener ) {
        vec = cityDOS;
        this.listener = listener;
     }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v   = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast,parent,false);
        return new CityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityHolder holder, final int position) {
        final CityDO obj = vec.get(position);
        holder.tvDate.setText("Date : "+obj.date+"");
        holder.tvCityTemp.setText("Temperature : "+obj.cityTemp+"");
        holder.tvCloudiness.setText("Clouds : "+ obj.cloudiness+"");
        holder.tvDescription.setText("Weather Description : "+obj.description+"");
    }



    @Override
    public int getItemCount() {
        return vec!=null ? vec.size() : 0;
    }

    public class CityHolder extends RecyclerView.ViewHolder {

        private   View llBack;
        private TextView tvDate;
        private TextView tvCityTemp,tvCloudiness,tvDescription;
        private View itemView;

        public CityHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvDate = itemView.findViewById(R.id.tvDate);
            llBack = itemView.findViewById(R.id.llBack);
            tvCityTemp = itemView.findViewById(R.id.tvCityTemp);
            tvCloudiness = itemView.findViewById(R.id.tvCloudiness);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(15);
            drawable.setStroke(5,Color.BLACK);
            llBack.setBackground(drawable);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
    public void refresh(Vector<CityDO> vec ){
        this.vec = vec;
        notifyDataSetChanged();
    }
}
