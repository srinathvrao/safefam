/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

//import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.sqrt;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class BasicMapDemoActivity extends AppCompatActivity implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        OnMapReadyCallback {

    private ProgressDialog progressDialog;
    private int noofpoly=0,i;
    private LatLng coord;
    GeoApiContext context;
    private String desti = "";
    private ArrayList<LatLng> goodpoints = new ArrayList<>();
    private ArrayList<LatLng> badpoints = new ArrayList<>();


    List<LatLng> path = new ArrayList();
    private LatLng coordinate;
    private ArrayList<LatLng> todraw;
    private Geocoder geo;
    private Marker destin = null;
    private GoogleMap mymap;
    private Boolean routeDrawn = false;
    int prog=0;
    private EditText edt;
    private FloatingActionButton fab;
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void fillgoodpoints(){

//        goodpoints.add(new LatLng(13.034932,80.250702));
//        goodpoints.add(new LatLng(13.046530 ,80.253690));
//        goodpoints.add(new LatLng(13.045553 , 80.256394));
//        goodpoints.add(new LatLng(13.025240 , 80.251545));
        goodpoints.add(new LatLng(13.031985 ,80.251844));
//        goodpoints.add(new LatLng(13.039627 , 80.257989));
//        goodpoints.add(new LatLng(13.042413 ,80.256283));
//        goodpoints.add(new LatLng(13.033535 ,80.251135));
//        goodpoints.add(new LatLng(13.033513 ,80.252933));
//        goodpoints.add(new LatLng(13.031517, 80.247604));
//        goodpoints.add(new LatLng(13.031353, 80.249381));
//        goodpoints.add(new LatLng(13.033101, 80.246885));
        goodpoints.add(new LatLng(13.032960, 80.249530));
        goodpoints.add(new LatLng(13.031533, 80.247497));
        goodpoints.add(new LatLng(13.033109, 80.246893));
        goodpoints.add(new LatLng(13.033004, 80.248792));

    }

    public void fillbadpoints(){
        badpoints.add(new LatLng(13.033689, 80.251474));
        badpoints.add(new LatLng(13.033720, 80.249311));
        badpoints.add(new LatLng(13.034075, 80.247838));
        badpoints.add(new LatLng(13.033743, 80.249718));
        badpoints.add(new LatLng(13.032489, 80.247980));









    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
        progressDialog = new ProgressDialog(BasicMapDemoActivity.this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        final View rllay = findViewById(R.id.rellay);

        geo = new Geocoder(getApplicationContext(),Locale.getDefault());

        fillgoodpoints();
        fillbadpoints();
        context = new GeoApiContext.Builder()
                .apiKey("Your_api_key_here")
                .build();
        edt = (EditText) findViewById(R.id.enterloc);



        Button searchbutt = (Button) findViewById(R.id.searchbutt);

        searchbutt.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              desti = edt.getEditableText().toString();
                                              if(desti.equals(""));
                                              else{
                                                  hideKeyboard(BasicMapDemoActivity.this);
                                                  if(routeDrawn){
                                                      Intent temp = new Intent(BasicMapDemoActivity.this,TempActivity.class);
                                                      temp.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                      temp.putExtra("desti",desti);
                                                      finish();
                                                      startActivity(temp);
                                                  }
                                                  else{
                                                      try{
                                                          List addressList = geo.getFromLocationName(desti, 1);
                                                          if (addressList != null && addressList.size() > 0) {
                                                              Address address = (Address) addressList.get(0);
                                                              double x1 = address.getLatitude();
                                                              double y1 = address.getLongitude();
                                                              coord = new LatLng(x1,y1);
                                                          }
                                                      }
                                                      catch (Exception e){
                                                          Log.e("GETTING_ROUTE","geocoding "+e.getMessage());
                                                      }
                                                      connectPoints();
                                                  }



                                                  //

//

                                              }



                                          }
                                      });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Snackbar.make(rllay,"Location permission is required.",Snackbar.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();



    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */


    public void drawRoute(LatLng source, LatLng destination){
        // draws a route with coordinate as starting
        // coord is destination
        try {


            if (desti.equals(""))
                return;
            //progressDialog.show();
            StringBuilder sb = new StringBuilder();

            //List addressList = geo.getFromLocationName(desti, 1);
            //if (addressList != null && addressList.size() > 0) {
//                Address address = (Address) addressList.get(0);
//                double x1 = address.getLatitude();
//                double y1 = address.getLongitude();
//                double x2 = coordinate.latitude;
//                double y2 = coordinate.longitude;
//                sb.append(address.getLatitude()).append(" ");
//                sb.append(address.getLongitude()).append("\n");


//                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
//                                coord2, zoom);
//                        mymap.animateCamera(location);
                if (destin == null) ;
                else
                    destin.remove();
                destin = mymap.addMarker(new MarkerOptions().position(coord).title("Destination"));
                Log.e("GETTING_ROUTE", source + "");
                Log.e("GETTING_ROUTE", destination + "");

//                        new AsyncTask<Void, Void, Integer>() {
//                            @Override
//                            protected Integer doInBackground(Void... params) {


                                                      /*

                                                                GETTING DIRECTIONS FROM API STARTING HERE <<<<<<<<<<<<<<<<<<<<<<<<<<

                                                       */
                try {
                    DirectionsApiRequest req = DirectionsApi.getDirections(context, source.latitude + "," + source.longitude, destination.latitude + "," + destination.longitude);
                    DirectionsResult res = req.await();
                    //Loop through legs and steps to get encoded polylines of each step
                    if (res.routes != null && res.routes.length > 0) {
                        DirectionsRoute route = res.routes[0];

                        if (route.legs != null) {
                            for (int i = 0; i < route.legs.length; i++) {
                                DirectionsLeg leg = route.legs[i];
                                if (leg.steps != null) {
                                    for (int j = 0; j < leg.steps.length; j++) {
                                        DirectionsStep step = leg.steps[j];
                                        if (step.steps != null && step.steps.length > 0) {
                                            for (int k = 0; k < step.steps.length; k++) {
                                                DirectionsStep step1 = step.steps[k];
                                                EncodedPolyline points1 = step1.polyline;
                                                if (points1 != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                    for (com.google.maps.model.LatLng coord1 : coords1) {
                                                        path.add(new LatLng(coord1.lat, coord1.lng));
                                                    }
                                                }

                                            }
                                        } else {
                                            EncodedPolyline points = step.polyline;
                                            if (points != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                for (com.google.maps.model.LatLng coo : coords) {
                                                    path.add(new LatLng(coo.lat, coo.lng));
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    Log.e("GETTING_ROUTE", ex.getLocalizedMessage());
                }

                //Draw the polyline
                if (path.size() > 0) {
                    try {
                        Log.e("GETTING_ROUTE", "HERE!!!!!!! path size >0 " + path.size());
                        //if(noofpoly<1) {
                            mymap.addPolyline(new PolylineOptions().addAll(path).color(Color.BLUE).width(7));
                            routeDrawn = true;
                        //}
                        //noofpoly++;
                        Log.e("GETTING_ROUTE",noofpoly+"");
                        //mymap.addPolyline(opts);
                    }
                    catch (Exception e){
                        Log.e("GETTING_ROUTE",e.getMessage());
                    }
                }
                mymap.getUiSettings().setZoomControlsEnabled(true);
                //mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 14));

                //progressDialog.setProgress(prog);
                //progressDialog.hide();
                                                      /*

                                                               GETTING DIRECTIONS FROM API ENDING HERE >>>>>>>>>>>>>>>>>>>>>>>>>>>>

                                                       */

                //mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 14));
                //return 1;

//                      Log.e("geoloc",res);

        } catch (Exception e) {
            Log.e("geoloc", "Unable to connect to Geocoder", e);
        }
    }

    public ArrayList<LatLng> findWayPoints(ArrayList<LatLng> gp,ArrayList<LatLng> bp,LatLng source , LatLng dest ){

        LatLng midLatLng ;
        ArrayList<LatLng> wayPoints ;
        wayPoints = new ArrayList<LatLng>(100);

        double temp;
        int i;
        Location a,b;
        a = new Location("");
        b = new Location("");

        double threshold;
        double threshold1=50.0;
        ArrayList<LatLng> candidate=new ArrayList<LatLng>();
        double minDist;
        LatLng actDist=new LatLng(0,0);
        boolean loop = true;
        while(loop){
            candidate.clear();
            //removing source from good points arraylist
            for(i = gp.size() - 1;i>=0;i--){
                if(LatLngIsEqual(gp.get(i),source)){
                    gp.remove(i);
                    break;
                }
            }

            midLatLng = new LatLng((source.latitude + dest.latitude)/2,(source.longitude + dest.longitude)/2);

            a.setLatitude(midLatLng.latitude);
            a.setLongitude(midLatLng.longitude);

            b.setLatitude(source.latitude);
            b.setLongitude(source.longitude);

            Log.e("GETTING_ROUTES",a.distanceTo(b)+"");
            threshold = a.distanceTo(b) + 50;

            //Toast.makeText(BasicMapDemoActivity.this,"distance : " + Double.toString(threshold),Toast.LENGTH_SHORT).show();

            for(i=gp.size()- 1; i >=0 ;i--){


                b.setLongitude(gp.get(i).longitude);
                b.setLatitude(gp.get(i).latitude);

                //retaining all points inside the circle of life ;)
                temp = a.distanceTo(b);
                if(temp > threshold){
                    gp.remove(i);

                }



            }
            //Toast.makeText(BasicMapDemoActivity.this,"size : "+gp.size(),Toast.LENGTH_SHORT).show();

            for(i=bp.size()- 1; i >=0 ;i--){

                b.setLongitude(bp.get(i).longitude);
                b.setLatitude(bp.get(i).latitude);

                temp = a.distanceTo(b);
                if(temp > threshold){
                    bp.remove(i);

                }
            }
            if(gp.size()==0) break;
            minDist=100000;

            for( i=0;i<gp.size();i++){
                if(getMinBPDistance(source,gp.get(i),bp,threshold)>threshold1){
                    candidate.add(gp.get(i));
                }
            }
            for( i=0;i<candidate.size();i++){
                if(findDist(source,candidate.get(i))<minDist){
                    minDist=findDist(source,candidate.get(i));
                    actDist=candidate.get(i);
                }
            }
            source=actDist;
            //Log.e("hello","coordinates : " + Double.toString(source.latitude) + " , " + Double.toString(source.longitude));
            wayPoints.add(source);
            setMarkerAt(source);
        }
        //Toast.makeText(BasicMapDemoActivity.this,"waypoint size : "+ wayPoints.size(),Toast.LENGTH_SHORT).show();

        return wayPoints;
    }

    public void setMarkerAt(LatLng latLng){
        // Creating MarkerOptions
        mymap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    }

    boolean LatLngIsEqual(LatLng a,LatLng b){
        if(a.latitude == b.latitude && a.longitude == b.longitude)
            return true;
        else
            return false;
    }

    double min(Double a,Double b){
        if(a<b) return a;
        else return b;
    }


    double findDist(LatLng a,LatLng b){
        Location x=new Location("");
        Location y=new Location("");
        x.setLatitude(a.latitude); x.setLongitude(a.longitude);
        y.setLatitude(b.latitude); y.setLongitude(b.longitude);
        return x.distanceTo(y);
    }


    double getMinBPDistance(LatLng source , LatLng gp_curr , ArrayList<LatLng> bp , double radius){

        int i;
        double minDist;
        double X,Y;
        double dX,dY;
        X=gp_curr.latitude;
        Y=gp_curr.longitude;
        dX=100*(gp_curr.latitude-source.latitude);
        dY=100*(gp_curr.longitude-source.longitude);
        minDist = 2.0 * radius;
        //while(X<=gp_curr.latitude && Y<=gp_curr.longitude){
        for(i=0 ; i < bp.size();i++){
            minDist=min(findDist(new LatLng(X,Y),bp.get(i)),minDist);
        }
        //X=(X*1000+dX)/1000;
        //Y=(Y*1000+dY)/1000;
        //}
        return minDist;
    }

    public void connectPoints(){

//        CameraPosition cp = new CameraPosition.Builder().target(new LatLng(13.035041, 80.250399)).bearing(0).tilt(40).zoom(20).build();
//        mymap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

//        ArrayList<LatLng> c,d;
//        c = new ArrayList<LatLng>();
//        c.add(new LatLng(13.034932,80.250702));
//        c.add(new LatLng(13.033535,80.251135));
//        c.add(new LatLng(13.033513,80.252933));
//        d = new ArrayList<LatLng>();



        //Displaying all danger points using BLUE marker

        for(i=0;i<badpoints.size();i++){
            mymap.addMarker(new MarkerOptions()
                    .position(badpoints.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        //getMinBPDistance(new LatLng(13.035095,80.249762),new LatLng(13.034932,80.250702))

//        progressDialog.setTitle("Drawing Route....");
//        progressDialog.setMessage("Please wait");
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setMax(todraw.size()+2);


        try {
            todraw = findWayPoints(goodpoints,badpoints,coordinate,coord);

            int siz = todraw.size(), xyz;
            //progressDialog.show();
            if(siz==0)
                drawRoute(coordinate,coord);
            else {
                drawRoute(coordinate, todraw.get(0));
                for (xyz = 0; xyz < siz - 1; xyz++) {
                    drawRoute(todraw.get(xyz), todraw.get(xyz + 1));
                    progressDialog.setProgress(xyz + 1);
                }
                drawRoute(todraw.get(xyz), coord);
            }
        }
        catch (Exception e){
            Log.e("GETTING_ROUTE","drawing new route "+e.getMessage());
        }
        progressDialog.hide();
    }

    @Override
    public void onCameraMove(){
        Toast.makeText(getApplicationContext(), "camera is moving", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Toast.makeText(this, "The user gestured on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Toast.makeText(this, "The user tapped something on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Toast.makeText(this, "The app moved the camera.",
                    Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onMapReady(final GoogleMap map) {


        mymap = map;

        double latitude =  13.034074;
        double longitude = 80.247492;
        coordinate = new LatLng(latitude, longitude); //Store these lat lng values somewhere. These should be constant.
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, 18);
        map.moveCamera(location);
        map.addMarker(new MarkerOptions().position(coordinate).title("You're here"));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        coordinate, 15);
                map.animateCamera(location);
            }
        });

                    Intent i = getIntent();
                    try{
                        if(!i.getStringExtra("desti").equals("")){
                            desti = i.getStringExtra("desti");
                            edt.setText(desti);
                            try{
                                List addressList = geo.getFromLocationName(desti, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = (Address) addressList.get(0);
                                    double x1 = address.getLatitude();
                                    double y1 = address.getLongitude();
                                    coord = new LatLng(x1,y1);
                                }
                            }
                            catch (Exception e){
                                Log.e("GETTING_ROUTE","geocoding "+e.getMessage());
                            }
                            Log.e("GETTING_ROUTE","geocoded "+i.getStringExtra("desti"));
                            Log.e("GETTING_ROUTE","geocoded 1234 "+coord+" "+coordinate);
                            connectPoints();
                        }
                    }
                    catch (Exception e){
                        Log.e("GETTING_ROUTE","this is fine///// ......"+e.getMessage());
                    }

    }





}


/*

                        try {
                                    Log.e("GETTING_ROUTE","HERE!!!!!!!HELLO WORLD");
                                    DirectionsApiRequest req = DirectionsApi.getDirections(context, coord.latitude + "," + coord.longitude, coordinate.latitude + "," + coordinate.longitude);
                                    Log.e("GETTING_ROUTE","HERE!!!!!!!HELLO WORLD54353");

                                    Log.e("GETTING_ROUTE","HERE!!!!!!!");
                                    DirectionsResult res = req.await();
                                    if (res.routes == null)
                                        Log.e("GETTING_ROUTE", "NULLLLL");
                                    //Loop through legs and steps to get encoded polylines of each step
                                    if (res.routes != null && res.routes.length > 0) {
                                        DirectionsRoute route = res.routes[0];
                                        Log.e("GETTING_ROUTE","HERE!!!!!!!123");

                                        if (route.legs != null) {
                                            Log.e("GETTING_ROUTE","HERE!!!!!!!4353");
                                            for (int i = 0; i < route.legs.length; i++) {
                                                DirectionsLeg leg = route.legs[i];
                                                if (leg.steps != null) {
                                                    Log.e("GETTING_ROUTE","HERE!!!!!!!6445");
                                                    for (int j = 0; j < leg.steps.length; j++) {
                                                        DirectionsStep step = leg.steps[j];
                                                        if (step.steps != null && step.steps.length > 0) {
                                                            Log.e("GETTING_ROUTE","HERE!!!!!!!adczdzdz");
                                                            for (int k = 0; k < step.steps.length; k++) {
                                                                DirectionsStep step1 = step.steps[k];
                                                                EncodedPolyline points1 = step1.polyline;
                                                                Log.e("GETTING_ROUTE","HERE!!!!!!!9567575");
                                                                if (points1 != null) {
                                                                    //Decode polyline and add points to list of route coordinates
                                                                    List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                                    for (com.google.maps.model.LatLng coord1 : coords1) {
                                                                        path.add(new LatLng(coord1.lat, coord1.lng));
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            EncodedPolyline points = step.polyline;
                                                            if (points != null) {
                                                                //Decode polyline and add points to list of route coordinates
                                                                List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                                for (com.google.maps.model.LatLng coo : coords) {
                                                                    path.add(new LatLng(coo.lat, coo.lng));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    Log.e("Routing", ex.getLocalizedMessage());
                                }

                                //Draw the polyline
                                if (path.size() > 0) {
                                    Log.e("GETTING_ROUTE","HERE!!!!!!! path size >0");
                                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                                    mymap.addPolyline(opts);
                                }
                                else
                                    Log.e("GETTING_ROUTE","HERE!!!!!!! path size <0");
                                mymap.getUiSettings().setZoomControlsEnabled(true);

 */
