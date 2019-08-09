package com.example.weatherforecast.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.weatherforecast.model.CityDO;

import java.util.Vector;


public class DbHelper extends SQLiteOpenHelper {
    private static final Object LOCK = new Object();
    private String TAG = DbHelper.class.getSimpleName();

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
        sqLiteDatabase.execSQL("create table tblCities (rowid integer , cityName  varchar, cityTemp varchar, " +
                "cloudiness varchar, pressure varchar, humidity varchar, description VARCHAR )");
        sqLiteDatabase.execSQL("create table tblForecast (rowid integer , cityName  varchar, cityTemp varchar,   " +
                "cloudiness varchar, pressure varchar, humidity varchar, description VARCHAR ,dateT VARCHAR )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " from  " + oldVersion + " to " + newVersion);
    }




    public void addCities(Vector<CityDO> vec) {
        synchronized (DbHelper.LOCK) {
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        CityDO obj = vec.get(i);
                        if (obj != null) {

                            ContentValues values = new ContentValues();
                            values.put("cityName", obj.cityName+"");
                            values.put("cityTemp", obj.cityTemp+"");
                            values.put("cloudiness", obj.cloudiness+"");
                            values.put("pressure", obj.pressure+"");
                            values.put("humidity", obj.humidity+"");
                            values.put("description", obj.description+"");

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblCities", values, "cityName=?", new String[]{obj.cityName });
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblCities", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }




    public void addForecast(Vector<CityDO> vec) {
        synchronized (DbHelper.LOCK) {
            if (vec != null && vec.size() > 0) {
                for (int i = 0; i < vec.size(); i++)
                {
                    try {
                        CityDO obj = vec.get(i);
                        if (obj != null) {
                            ContentValues values = new ContentValues();
                            values.put("cityName", obj.cityName+"");
                            values.put("dateT", obj.date +"");
                            values.put("cityTemp", obj.cityTemp+"");
                            values.put("cloudiness", obj.cloudiness+"");
                            values.put("pressure", obj.pressure+"");
                            values.put("humidity", obj.humidity+"");
                            values.put("description", obj.description+"");

                            SQLiteDatabase db = getWritableDatabase();
                            int updatedCount = db.update("tblForecast", values, "cityName=? and dateT = ? ", new String[]{obj.cityName,obj.date });
                            if (updatedCount == 0) {
                                db.insertWithOnConflict("tblForecast", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    public Vector<CityDO> getCities( ) {
        synchronized (DbHelper.LOCK) {
            Vector<CityDO> vec = new Vector<CityDO>();
            String selectQuery= null;
            selectQuery = "SELECT  cityName , cityTemp  ,cloudiness , pressure  , humidity  , description  FROM tblCities   "  ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int i = 0;
                        CityDO obj = new CityDO();
                        obj.cityName=  cursor.getString(i++) ;
                        obj.cityTemp=  cursor.getString(i++) ;
                        obj.cloudiness=  cursor.getString(i++) ;
                        obj.pressure=  cursor.getString(i++) ;
                        obj.humidity=  cursor.getString(i++) ;
                        obj.description=  cursor.getString(i++) ;
                        vec.add(obj);

                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vec;
        }
    }
    public Vector<CityDO> getForecast(String strCity ) {
        synchronized (DbHelper.LOCK) {
            Vector<CityDO> vec = new Vector<CityDO>();
            String selectQuery= null;
            selectQuery = "SELECT   dateT, cityTemp  ,cloudiness , pressure  , humidity  , description  FROM tblForecast where  cityName='"+strCity+"' "  ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int i = 0;
                        CityDO obj = new CityDO();
                        obj.date=  cursor.getString(i++) ;
                        obj.cityTemp=  cursor.getString(i++) ;
                        obj.cloudiness=  cursor.getString(i++) ;
                        obj.pressure=  cursor.getString(i++) ;
                        obj.humidity=  cursor.getString(i++) ;
                        obj.description=  cursor.getString(i++) ;
                        vec.add(obj);

                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vec;
        }
    }
}
