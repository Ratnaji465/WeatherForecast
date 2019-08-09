package com.example.weatherforecast.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapter.ForecastAdapter;
import com.example.weatherforecast.api.GetService;
import com.example.weatherforecast.api.RetrofitClient;
import com.example.weatherforecast.db.DbHelper;
import com.example.weatherforecast.model.CityDO;
import com.example.weatherforecast.model.ForecastDO;
import com.example.weatherforecast.model.Main;
import com.example.weatherforecast.model.WeatherDO;

import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ForecastActivity extends AppCompatActivity {

    private RecyclerView rvForecast;
    private ForecastAdapter adapter;
    private String cityName;
    private Vector<CityDO> vector = new Vector<CityDO>();
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        db = new DbHelper(this,"forecasting.db",null,1);
        rvForecast = (RecyclerView)findViewById(R.id.rvForecast);
        rvForecast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ForecastAdapter(vector,null);
        rvForecast.setAdapter(adapter);
        cityName = getIntent().getExtras().getString("city");
        vector = db.getForecast(cityName);
        adapter.refresh(vector);
        GetService service  = RetrofitClient.getClient().create(GetService.class);
        Call<ForecastDO> callList = service.getWeatherForecastReport(  cityName,GetService.appId);
        callList.enqueue(new Callback<ForecastDO>() {
            @Override
            public void onResponse(final Response<ForecastDO> response, Retrofit retrofit) {
                Log.e("Response", response.body()+"");
                if(response.body()!=null && response.body().getVector()!=null) {
                    vector.clear();
                    for (int i = 0; i < response.body().getVector().size(); i++) {
                        WeatherDO weatherDO = response.body().getVector().get(i);
                        CityDO cityDO = new CityDO();
                        if (weatherDO.getMain() != null) {
                            Main main = weatherDO.getMain();
                            cityDO.cityTemp = main.getTemp() + "";
                            cityDO.humidity = main.getHumidity() + "";
                            cityDO.pressure = main.getPressure() + "";
                        }
                        if (weatherDO.getWeather() != null && weatherDO.getWeather().size() > 0) {
                            cityDO.cloudiness = weatherDO.getWeather().get(0).getMain() + "";
                            cityDO.description = weatherDO.getWeather().get(0).getDescription() + "";
                        }
                        if (weatherDO.getClouds() != null) {
                            cityDO.cloudiness = weatherDO.getClouds().getAll() + "";
                        }
                        cityDO.cityName = weatherDO.getName() + "";
                        cityDO.date = weatherDO.getDate() + "";
                        cityDO.cityName = cityName;
                        vector.add(cityDO);
                    }

                }

                db.addForecast(vector);
                adapter.refresh(vector);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public void moveToBackScreen(View view) {
        finish();
    }

}
