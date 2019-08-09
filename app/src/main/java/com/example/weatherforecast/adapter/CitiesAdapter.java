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

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityHolder>{
    private OnItemClickListener listener;
    private  Vector<CityDO> vec;
    private int selectedPosition = 0 ;

    public CitiesAdapter(Vector<CityDO> cityDOS,OnItemClickListener listener ) {
        vec = cityDOS;
        this.listener = listener;
     }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v   = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,null);
        return new CityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityHolder holder, final int position) {
        final CityDO obj = vec.get(position);
        holder.tvCityName.setText(obj.cityName+"");
        holder.tvCityTemp.setText(obj.cityTemp+"");
        if(position == selectedPosition || selectedPosition ==-1)
        {
            //selectedPositionUi
            listener.onItemClick(obj,position);
            enableUi(holder);

        }else
        {
            disableUi(holder);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(obj,position);
                selectedPosition = position;
                enableUi(holder);
                notifyDataSetChanged();
            }
        });
    }

    private void enableUi(CityHolder holder) {
        holder.tvCityName.setTextColor(Color.WHITE);
        holder.tvCityTemp.setTextColor(Color.WHITE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(30);
        drawable.setColor(Color.BLACK);
        holder.llBack.setBackground(drawable);
    }
    private void disableUi(CityHolder holder) {
        holder.tvCityName.setTextColor(Color.BLACK);
        holder.tvCityTemp.setTextColor(Color.BLACK);
        holder. llBack.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return vec!=null ? vec.size() : 0;
    }

    public class CityHolder extends RecyclerView.ViewHolder {

        private   View llBack;
        private TextView tvCityName;
        private TextView tvCityTemp;
        private View itemView;

        public CityHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCityTemp = itemView.findViewById(R.id.tvCityTemp);
            llBack = itemView.findViewById(R.id.llBack);
        }
    }
    public void refresh(Vector<CityDO> vec ){
        this.vec = vec;
        notifyDataSetChanged();
    }
}
