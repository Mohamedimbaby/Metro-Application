package com.example.metroapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements LocationListener{

    ArrayList<String> fstLine = new ArrayList<>();
    ArrayList<String> sndLine = new ArrayList<>();
    ArrayList<String> thirdLine = new ArrayList<>();
    int currentLine, destinationLine, isFirst;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView numbersStationText, priceStationText;
    AutoCompleteTextView currentStationAutoComplete, destinationStationAutoComplete;
    RecyclerViewAdapter rva;
    SharedPreferences preferences;
    ArrayList<ArrayList<String>> AllAvialibleRoutes = new ArrayList<>();
    LocationManager locationManager;
    String currentStation;
    String destinationStation;
    ArrayList<String> AllStations = new ArrayList<>();
    Map<String,ArrayList< Double>> AllCoordinates = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // pref = getPreferences(MODE_PRIVATE);

        currentStationAutoComplete = findViewById(R.id.currentStationAutoComplete);
        destinationStationAutoComplete = findViewById(R.id.destinationStationAutoComplete);
        numbersStationText = findViewById(R.id.StNumText);
        priceStationText = findViewById(R.id.priceText);


        recyclerView = findViewById(R.id.resultRecycle);
        progressBar = findViewById(R.id.ProgressBar);

//============== Database Statements=======
       StationDao st = new StationDao(this);

//
//        Collections.addAll(sndLine,"Shobra","Kolyet El-zera3a","El-Mazalat",
//                "El-Khalafawi","St Terssa", "Rod El-farg","Massara",
//                "El-Shohada","Attaba", "M.Naguib","Sadat",
//                "Opera","Dokki","El-Bohos","Cairo University",
//                "Faysal","Giza","Om El-Misryeen","Sakiat Mekki"
//                ,"El-Monib");
//
//        Collections.addAll(thirdLine,"Airport","Al-Ahram","Kolleyet El-Banat",
//                 "Cairo Stadium","Cairo Fairgrounds", "El-abbasya",
//                "Abdo Pasha", "El-Geish", "Bab El-Sha'rya","Attaba",
//                "Nasser","Boulaq","El-Zamalek","Kit Kat","Mohandesen","Sudan"
//                ,"Embaba");
//
        AllStations.addAll(fstLine);
        AllStations.addAll(sndLine);
        AllStations.addAll(thirdLine);
       ArrayList<Double> doubles= new ArrayList<>();
       Collections.addAll(doubles,30.152081,31.335682);
        AllCoordinates.put("Helwan",doubles);// marg
        doubles.clear();
        Collections.addAll(doubles,30.139318,31.324422);
        AllCoordinates.put("ezbet el nakhal",doubles);
        doubles.clear();
        Collections.addAll(doubles,30.131026,31.319091);
        AllCoordinates.put("ain shams",doubles);
        doubles.clear();
        Collections.addAll(doubles,29.970143,31.250608);
        AllCoordinates.put("headek el maadi",doubles);
        doubles.clear();
        Collections.addAll(doubles,29.848982,31.334231);
        AllCoordinates.put("Helwan",doubles);


      AllStations=  removeDuplicatedItems(AllStations);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      preferences= getPreferences(MODE_PRIVATE);
       ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,AllStations);

currentStationAutoComplete.setAdapter(adapter);
destinationStationAutoComplete.setAdapter(adapter);

    Sensey.getInstance().init(this);
    Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
        @Override
        public void onFaceDown() {

            String previous=  preferences.getString("previous","No Trips Saved");
            String PrePrevious=  preferences.getString("PrePrevious","No Trips Saved");
            StringBuilder strbuild = new StringBuilder(previous.replace("]", "").replace("[", ""));
            String number_Previous =  preferences.getString("number-previous","No Trips Saved");
            String price_Previous=  preferences.getString("price-previous","No Trips Saved");
            priceStationText.setText(price_Previous+"");
            numbersStationText.setText(number_Previous+"");
            if(isFirst==1) {

              strbuild = new StringBuilder(PrePrevious.replace("]", "").replace("[", ""));
                 number_Previous=  preferences.getString("number-PrePrevious","No Trips Saved");
                 price_Previous=  preferences.getString("price-PrePrevious","No Trips Saved");
                priceStationText.setText(price_Previous+"");
                numbersStationText.setText(number_Previous+"");

            }
            else if(isFirst>=2){
                Toast.makeText(MainActivity.this, "No other trips Stored", Toast.LENGTH_SHORT).show();
                return;
            }

            strbuild.toString().replace("[","");
            String [] arr= strbuild.toString().split(",");
            ArrayList<String> arrayList = new ArrayList<>();
            Arrays.asList(arrayList,arr);
            for (String item:arr)
            {
                arrayList.add(item);
            }
            isFirst++;

      //      Toast.makeText(MainActivity.this, ""+LinkedHashSet.size(), Toast.LENGTH_SHORT).show();
            rva = new RecyclerViewAdapter(MainActivity.this,arrayList);


          recyclerView.setAdapter(rva);

        }

        @Override
        public void onFaceUp() {

        }
    });
        Sensey.getInstance().startShakeDetection(new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {

            }

            @Override
            public void onShakeStopped() {
                if (AllAvialibleRoutes.size()>1)
                {
                    soluation++;
                    Toast.makeText(MainActivity.this, "next", Toast.LENGTH_SHORT).show();

                    AllAvialibleRoutes.get(soluation-1).clear();
                    rva = null;
                    rva = new RecyclerViewAdapter(MainActivity.this,AllAvialibleRoutes.get(soluation));
                    recyclerView.setAdapter(rva);
                    rva.notifyDataSetChanged();
                    ProgresBarShow k = new ProgresBarShow();
                    k.sd(AllAvialibleRoutes.get(soluation));

                }
                else {
                    Toast.makeText(MainActivity.this, "There is no another routes ", Toast.LENGTH_SHORT).show();
                }
            }
        });

//========================== Maps =========================================
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String [] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this,permission,1);
        }
        else {
            locationManager.requestSingleUpdate(locationManager.NETWORK_PROVIDER,this, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED )
            {
                try {
                    locationManager.requestSingleUpdate(locationManager.NETWORK_PROVIDER,this, null);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        Sensey.getInstance().stop();
        super.onStop();
    }





    //called when button has been pressed
    public void getRoad(View view) {

        AllAvialibleRoutes.clear();
        rva=null;
        recyclerView.setAdapter(rva);
        currentStation = currentStationAutoComplete.getText().toString();
        destinationStation = destinationStationAutoComplete.getText().toString();


        if(currentStationAutoComplete.isInEditMode()||destinationStationAutoComplete.isInEditMode()||currentStation.equals("")||destinationStation.equals("")){
          Toast.makeText(this, "Please write the Current and destination station ... ", Toast.LENGTH_SHORT).show();
      }
      else if(currentStation.equals(destinationStation)){ Toast.makeText(this, "it is same  station ... ", Toast.LENGTH_SHORT).show();}
      else {
            ArrayList<String>FullPath =new ArrayList<>();


          int []currentLine= StationDao.GetStationsByLine(currentStation);
          int []destinationLine= StationDao.GetStationsByLine(destinationStation);
       int openLine=0;
         if(currentLine.length>1&&destinationLine.length>1)
         {
             for (int i = 0; i < currentLine.length; i++) {
                 for (int j = 0; j < currentLine.length; j++) {
                     if(currentLine[i]==destinationLine[j])
                     {
                      openLine=currentLine[i];
                      break;
                     }
                 }
             }
         }
         else if(currentLine.length>1)
         {
             for (int i = 0; i < currentLine.length; i++) {
                 if(currentLine[i]==destinationLine[0])
                 {
                     openLine=currentLine[i];
                     break;
                 }
             }
         }
         else if(destinationLine.length>1) {


             for (int i = 0; i < destinationLine.length; i++) {
                 if (currentLine[0] == destinationLine[i]) {
                     openLine = destinationLine[i];
                     break;
                 }
             }
         }
         else
             {
                 int current_id= StationDao.getStationId(currentStation,currentLine[0]);
                 int destination_id= StationDao.getStationId(destinationStation,destinationLine[0]);

                 if(currentLine[0]==destinationLine[0])
                    {
                      Log.i(  "!!!!!",StationDao.getAllStationsByLimit(current_id,destination_id).toString());
                    }
                else {
                    ArrayList<String> commonStation = StationDao.GetCommonStation(currentLine[0], destinationLine[0]);
                    for (String stationCommon:commonStation)
                    {
                        int common_id_current =StationDao.getStationId(stationCommon,currentLine[0]);
                        FullPath.addAll(StationDao.getAllStationsByLimit(current_id,common_id_current));

                        int common_id_destination=StationDao.getStationId(stationCommon,destinationLine[0]);
                        FullPath.addAll(StationDao.getAllStationsByLimit(common_id_destination,destination_id));

                    }
                }


            }
         if(openLine!=0)
         {
             int current_id= StationDao.getStationId(currentStation,openLine);
             int destination_id= StationDao.getStationId(destinationStation,openLine);
             Log.i(  "!!!!!",StationDao.getAllStationsByLimit(current_id,destination_id).toString());

         }
else {

     }
Log.i("asasas",FullPath.toString());
          // get location of both stations



          //get the short path



          new ProgresBarShow().execute();


      }
    }

    private byte getLineNumber(String station) {
        if(fstLine.contains(station))
        {
            return 1;
        }
        else if(sndLine.contains(station))
        {
            return 2;
        }
        else if(thirdLine.contains(station))
            {
               return 3;
            }
        return -1;

    }

    private ArrayList<String> getPath(int startStation, int endStation,int thisLine) {

        ArrayList<String>path= new ArrayList<>();
        if(thisLine==1){
            if(endStation>startStation)
            {
                path.add("** Helwan Direction **");
                for (int i = startStation+1; i <=endStation ; i++) {

                path.add(fstLine.get(i));
            }
            }
            else {

                path.add("** Marg Direction **");
                for (int i = startStation-1; i >=endStation ; i--) {
                    path.add(fstLine.get(i));
                }
            }


        }
        else  if(thisLine==3){
            if(endStation>startStation)
            {
                path.add("** Embaba Direction **");
                for (int i = startStation+1; i <=endStation ; i++) {
                path.add(thirdLine.get(i));
            }
            }
            else {

                path.add("** AirPort Direction **");
                for (int i = startStation-1; i >=endStation ; i--) {
                    path.add(thirdLine.get(i));
                }
            }


        }
        else  if(thisLine==2){
            if(endStation>startStation)
            {
                path.add("* El-Monib Direction **");
                for (int i = startStation+1; i <=endStation ; i++) {
                path.add(sndLine.get(i));
            }
            }
            else {

                path.add("** Shobra Direction **");
                for (int i = startStation-1; i >=endStation ; i--) {
                    path.add(sndLine.get(i));
                }
            }


        }
    return path;}


    int getLocationOfStation(String Station,boolean iSCurrent)
    {
    if(iSCurrent)
    {
        switch (currentLine)
        {
            case 1:
                return fstLine.indexOf(Station);

            case 2:
                return sndLine.indexOf(Station);

            case 3:
                return thirdLine.indexOf(Station);

        }
    }
    else {
        switch (destinationLine)
        {
            case 1:
                return fstLine.indexOf(Station);

            case 2:
                return sndLine.indexOf(Station);

            case 3:
                return thirdLine.indexOf(Station);

        }
    }

    return -1;
    }

    ArrayList<String> getCommonStations()
    {

        ArrayList<String>commonStations=new ArrayList<>();
if (((currentLine==2&&destinationLine==1))||(currentLine==1&&destinationLine==2))
{    
    for (String station : fstLine )
    {

      if(sndLine.contains(station))
      {
          commonStations.add(station);
      }
    }
}
else if (((currentLine==1&&destinationLine==3))||(currentLine==3&&destinationLine==1))
{
 

    for (String station : fstLine )
    {

        if(thirdLine.contains(station))
        {
            commonStations.add(station);
        }
    }
}
else if (((currentLine==2&&destinationLine==3))||(currentLine==3&&destinationLine==2)) {
        for (String station : thirdLine) {

            if (sndLine.contains(station)) {
                commonStations.add(station);
            }
        }

    }


        return commonStations;
    }

    boolean CheckIfStationAtSameLine()
    {
        if(currentLine==destinationLine)
        {
         return true;
        }
        else

        return false;
    }
   int getIndexOfCommonStationAtLine(String station,boolean IsCurrent)
   {
       return getLocationOfStation(station,IsCurrent);
   }
   int soluation =0;
public <T> ArrayList<T> removeDuplicatedItems(ArrayList<T> list)
{
    ArrayList<T> newList = new ArrayList<>();
    for (T element : list)
    {
        if (!newList.contains(element))
        {
            newList.add(element);
        }
    }
    return newList;
}









String beinSports(Location location)
{
    String minIndex="";
    float min=1000000000.0f;
    for (Map.Entry<String, ArrayList<Double>> entry : AllCoordinates.entrySet()) {

        Location l = new Location("");
        l.setLatitude(entry.getValue().get(0));
        l.setLongitude(entry.getValue().get(1));
        float distanceTo = location.distanceTo(l);

        Toast.makeText(this, ""+distanceTo, Toast.LENGTH_SHORT).show();
        // ...
        if(distanceTo<min)
        {
            min=distanceTo;
            minIndex=entry.getKey();

        }
    }return minIndex;
}
    double currentlongitude;
    double currentlatitude;

    double longitude;
    double latitude;
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
String Station = beinSports(location);
        currentStationAutoComplete.setText(Station);
       ArrayList<Double>doubles= AllCoordinates.get(Station);
        currentlatitude=doubles.get(0);
        currentlongitude=doubles.get(1);

        progressBar.setVisibility(View.GONE);



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getroute(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+currentlatitude+","+currentlongitude));

        startActivity(intent);

    }


    class ProgresBarShow extends AsyncTask<ArrayList<String>,Void,ArrayList<String>>{
    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... arrayLists) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }



    @Override
    protected void onPostExecute(ArrayList<String> aVoid) {
        super.onPostExecute(aVoid);
        progressBar.setVisibility(View.GONE);
        rva = new RecyclerViewAdapter(MainActivity.this,AllAvialibleRoutes.get(0));
  sd(AllAvialibleRoutes.get(0));
        recyclerView.setAdapter(rva);
    }

void sd(ArrayList<String> arrayList)
{
    int LinesCount=0;
    for (String state : arrayList) {
        if (state.charAt(0)=='*')
        {
            LinesCount++;
        }
    }
    int  StationsNumbers= arrayList.size()-LinesCount;
    numbersStationText.setText(StationsNumbers+"");
    byte ticketPrice=0;
    if((StationsNumbers<9))
    {
        ticketPrice=3;
    }
    else if((StationsNumbers<16))
    {
        ticketPrice=5;
    }
    else  ticketPrice=7;

    priceStationText.setText(ticketPrice+"");
    SharedPreferences.Editor editor = preferences.edit();

    String number_Previous=  preferences.getString("number-previous","No Trips Saved");
    String price_Previous=  preferences.getString("price-previous","No Trips Saved");
    editor.putString("number-PrePrevious",number_Previous+"");
    editor.putString("price-PrePrevious",price_Previous+"");

    editor.putString("number-previous",StationsNumbers+"");
    editor.putString("price-previous",ticketPrice+"");

    String previous=  preferences.getString("previous","No Trips Saved");
    editor.putString("PrePrevious", previous);
    editor.putString("previous", arrayList.toString());

    editor.commit();
}
}
}
