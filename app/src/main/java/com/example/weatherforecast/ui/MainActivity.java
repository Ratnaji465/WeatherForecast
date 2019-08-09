package com.example.weatherforecast.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapter.CitiesAdapter;
import com.example.weatherforecast.adapter.OnItemClickListener;
import com.example.weatherforecast.api.GetService;
import com.example.weatherforecast.api.RetrofitClient;
import com.example.weatherforecast.db.DbHelper;
import com.example.weatherforecast.model.CityDO;
import com.example.weatherforecast.model.Main;
import com.example.weatherforecast.model.WeatherDO;

import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView rvCitites;
    private TextView tvHumidity,tvPressure,tvCloudiness;
    Vector<CityDO> vector = new Vector<>();
    private CitiesAdapter adapter;
    private String arrCities[] = new String []{"New york,US", "London,GB", "New delhi,IN", "Beijing,CN", "Sydney,AU", "Paris,FR"};
    private String selectedCityName;
    private DbHelper db;
    private View btnMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this,"forecasting.db",null,1);
        rvCitites = (RecyclerView)findViewById(R.id.rvCitites);
        tvHumidity = (TextView)findViewById(R.id.tvHumidity);
        btnMore =  findViewById(R.id.btnMore);
        tvPressure = (TextView)findViewById(R.id.tvPressure);
        tvCloudiness = (TextView)findViewById(R.id.tvCloudiness);

        rvCitites.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        adapter = new CitiesAdapter(vector,this);
        rvCitites.setAdapter(adapter);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(40);
        drawable.setColor(Color.BLACK);
        btnMore.setBackground(drawable);
        GetService service  = RetrofitClient.getClient().create(GetService.class);
        vector = db.getCities();
        adapter.refresh(vector);
        for (int  i = 0 ;i<arrCities.length;i++ )
        {
            Call<WeatherDO> callList = service.getWeatherReport(  arrCities[i],GetService.appId);
            callList.enqueue(new Callback<WeatherDO>() {
                @Override
                public void onResponse(final Response<WeatherDO> response, Retrofit retrofit) {

                    Log.e("Response", response.body()+"");
                    CityDO cityDO=new CityDO();
                    if(response.body()!=null) {
                        WeatherDO weatherDO = response.body();
                        if (weatherDO.getMain() != null) {
                            Main main = weatherDO.getMain();
                            cityDO.cityTemp = main.getTemp() + "";
                            cityDO.humidity = main.getHumidity() + "";
                            cityDO.pressure = main.getPressure() + "";
                        }
                        if (weatherDO.getWeather() != null && weatherDO.getWeather().size() > 0) {
                            cityDO.cloudiness = weatherDO.getWeather().get(0).getMain() + "";
                        }
                        cityDO.cityName = weatherDO.getName() + "";
                        vector.add(cityDO);
                        db.addCities(vector);
                    }
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            adapter.refresh(vector);
                        }
                    };
                    new Handler(getMainLooper()).post(runnable);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();

                }
            });

        }

    }



    @Override
    public void onItemClick(Object object, int position) {
        CityDO cityDO = (CityDO) object;
        selectedCityName  = cityDO.cityName;
        tvPressure.setText(cityDO.pressure+" hpa");
        tvHumidity.setText(cityDO.humidity+" %");
        tvCloudiness.setText(cityDO.cloudiness);

    }

    public void moveToForeCastActivity(View view) {
        Intent intent = new Intent(this, ForecastActivity.class);
        intent.putExtra("city",selectedCityName);
        startActivity(intent);

    }
}
