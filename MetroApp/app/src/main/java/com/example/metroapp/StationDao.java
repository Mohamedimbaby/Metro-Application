package com.example.metroapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StationDao {
    MyDatabase Engine;
    static SQLiteDatabase sqLiteDatabase;

    public StationDao(Context context) {
        Engine = new MyDatabase(context);
        Log.i("!!!!", Engine.toString());
        sqLiteDatabase = Engine.getWritableDatabase();

    }

    public static boolean insert(Station station) {
        ContentValues container = new ContentValues();

        container.put("name", station.getName());
        container.put("latitude", station.getLatitude());
        container.put("longitude", station.getLongitude());
        container.put("line_num", station.getLine_num());
        container.put("isCommon", station.getiSCommon());

        long l = 0;
        if (sqLiteDatabase != null) {

            l = sqLiteDatabase.insert("station", null, container);
        }
        return l > 0;
    }


    public static ArrayList<Station> GetAllStatioins() {
        ArrayList<Station> Stations = new ArrayList<>();
        String Query = "select * from station";
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Station station = new Station();
            station.set_id(cursor.getInt(0));
            station.setName(cursor.getString(1));
            station.setLatitude(cursor.getDouble(2));
            station.setLongitude(cursor.getDouble(3));
            station.setLine_num(cursor.getInt(4));
            station.setiSCommon(cursor.getInt(5));
            Stations.add(station);
        }

        return Stations;
    }

    public static Map<String, ArrayList<Integer>> GetCommonStations() {
        Map<String, ArrayList<Integer>> map = new LinkedHashMap<>();
        String Query = "select _id,name,line_num from station where isCommon=1";
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            ArrayList<Integer> line_numbers = new ArrayList<>();
            line_numbers.add(cursor.getInt(2));
            cursor.moveToNext();
            line_numbers.add(cursor.getInt(2));
            cursor.moveToPrevious();
            map.put(cursor.getString(1), line_numbers);
        }

        return map;
    }

    public static Map<String, Integer> GetAllStationsByLine() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String Query = "select name,line_num from station";
        if (sqLiteDatabase != null) {
            Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                map.put(cursor.getString(0), cursor.getInt(1));

            }

            return map;
        }
        return null;
    }

    public static  int[] GetStationsByLine(String stationName) {
        String Query = "select line_num,isCommon from station where name =" + stationName;
            Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
            int [] lines ;
            if (cursor.getCount()>0) {
                cursor.moveToNext();

                if(cursor.getInt(1)==1)
                {
                    lines= new int[2];
                    lines[0]=cursor.getInt(0);
                    cursor.moveToNext();
                    lines[1]=cursor.getInt(1);
                     return lines;
                }
                else
                {

                   lines = new int[1];
                    lines[0]=cursor.getInt(0);
                    return lines;
                }

            }
     return null;
    }
    public static int  getStationId(String StationName,int num_line) {

        String Query = "select _id from station where name ="+StationName +"and line_num="+num_line;
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

            cursor.moveToNext();
          return cursor.getInt(0);



    }

    public static ArrayList<String> getAllStationsByLimit(int current_id, int destination_id) {
        ArrayList<String> stationsName = new ArrayList<>();
        String Query;

        if(current_id>destination_id)
        {
            Query = "select name from station where _id >="+destination_id +"and _id<"+current_id;

            stationsName=getPath(Query);
            Collections.reverse(stationsName);

        }
        else
            {
                Query = "select name from station where _id <"+destination_id +"and _id >="+current_id;
                stationsName=getPath(Query);

            }

     return    stationsName;
    }
   private static ArrayList<String> getPath(String Query)
    {
        ArrayList<String> stationsName = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            stationsName.add(cursor.getString(0));

        }
return stationsName;
    }
    public static ArrayList<String> GetCommonStation(int line1, int line2) {

        String Query = "select name from station where isCommon =1"+"and line_num="+line1;
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        ArrayList<String> stations = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            stations.add(cursor.getString(0));
          }
        ArrayList<String> finalStations = new ArrayList<>();

        for (String station:stations)
        {
            Query = "select name from station where isCommon =1"+"and line_num="+line2 +"and name ="+station;
             cursor = sqLiteDatabase.rawQuery(Query, null);
             if (cursor.getCount()>0)
             {
                 for (int i = 0; i < cursor.getCount(); i++) {

                     cursor.moveToNext();
                     finalStations.add(cursor.getString(0));
                 }
             }

   }

        return finalStations;
    }
}