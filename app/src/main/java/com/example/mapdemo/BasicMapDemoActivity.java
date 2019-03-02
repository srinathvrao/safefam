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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

    private LatLng coordinate;
    private Geocoder geo;
    private Marker destin = null;
    private GoogleMap mymap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        final View rllay = findViewById(R.id.rellay);

        geo = new Geocoder(getApplicationContext(),Locale.getDefault());

        Button searchbutt = (Button) findViewById(R.id.searchbutt);
        final EditText edt = (EditText) findViewById(R.id.enterloc);

        searchbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    edt.setFocusableInTouchMode(false);
                    edt.setFocusable(false);
                    edt.setFocusableInTouchMode(true);
                    edt.setFocusable(true);

                    hideKeyboard(BasicMapDemoActivity.this);
                    String loc = edt.getEditableText().toString();
                    if(loc.equals(""))
                        return;
                    StringBuilder sb = new StringBuilder();

                    List addressList = geo.getFromLocationName(loc, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        double x1 = address.getLatitude();
                        double y1 = address.getLongitude();
                        double x2 = coordinate.latitude;
                        double y2 = coordinate.longitude;
                        sb.append(address.getLatitude()).append(" ");
                        sb.append(address.getLongitude()).append("\n");

                        LatLng coord = new LatLng(address.getLatitude(),address.getLongitude());
                        LatLng coord2 = new LatLng((x1+x2)/2,(y1+y2)/2);
                        Location a = new Location("dummyprovider");
                        Location b = new Location("dummyprovider");
                        a.setLatitude(coord.latitude);
                        a.setLongitude(coord.longitude);
                        b.setLatitude(coordinate.latitude);
                        b.setLongitude(coordinate.latitude);

                        Log.e("location testing",a.distanceTo(b)+"");


                        int zoom=0;
                        if(a.distanceTo(b)<=5301720)zoom=13;
                        else if(a.distanceTo(b)<=7301720) zoom = 12;
                        else if(a.distanceTo(b)<=8301720) zoom = 10;
                        else zoom = 8;

                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                coord2, zoom);
                        mymap.animateCamera(location);
                        if(destin==null);
                        else
                            destin.remove();
                        destin = mymap.addMarker(new MarkerOptions().position(coord).title("Destination"));
//                        DrawRouteMaps.getInstance(getApplicationContext())
//                                .draw(coord,coordinate,mymap);
//                        LatLngBounds bounds = new LatLngBounds.Builder()
//                                .include(coord)
//                                .include(coordinate).build();
//                        Point displaySize = new Point();
//                        getWindowManager().getDefaultDisplay().getSize(displaySize);
                     //   mymap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,displaySize.x,250,30));
                        String res = sb.toString();
//                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                        Log.e("geoloc",res);
                    }
                } catch (IOException e) {
                    Log.e("geoloc", "Unable to connect to Geocoder", e);
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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        coordinate, 15);
                map.animateCamera(location);
            }
        });

        for(int n=0;n<3;n++){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GPSTracker gps = new GPSTracker(getApplicationContext());
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    coordinate = new LatLng(latitude, longitude); //Store these lat lng values somewhere. These should be constant.
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            coordinate, 15);
                    map.animateCamera(location);
                    map.addMarker(new MarkerOptions().position(coordinate).title("You're here"));
                }
            }, 2500);

        }

    }





}
