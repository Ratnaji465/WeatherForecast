package com.example.weatherforecast.api;

import com.example.weatherforecast.model.ForecastDO;
import com.example.weatherforecast.model.WeatherDO;

import java.util.Vector;

import javax.xml.transform.Result;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GetService {
    String appId = "80cbed998b2ac6d1495dbb97905a561c";
    @GET("/data/2.5/weather")
    Call<WeatherDO> getWeatherReport(@Query("q") String city , @Query("APPID") String apiKey );
    @GET("/data/2.5/forecast")
    Call<ForecastDO> getWeatherForecastReport(@Query("q") String city , @Query("APPID") String apiKey );
}
