
package com.example.weatherforecast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Vector;

public class ForecastDO {

    @SerializedName("list")
    @Expose
    private Vector<WeatherDO> vec;

    public Vector<WeatherDO> getVector() {
        return vec;
    }


}
