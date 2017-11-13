package com.ronteo.foodstory.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ronteo.foodstory.HawkerActivity;
import com.ronteo.foodstory.MainActivity;
import com.ronteo.foodstory.R;
import com.ronteo.foodstory.adapter.HawkerAdapter;
import com.ronteo.foodstory.model.Dish;
import com.ronteo.foodstory.model.Hawker;
import com.ronteo.foodstory.util.RestClient;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class GMapFragment extends Fragment {

    private static GoogleMap gMap;
    private static MapView mMapView;

    private static final int REQUEST_ACCESS_FINE_LOCATION = 200;
    private boolean permisionToAccessLocationAccepted = false;

    private static ArrayList<Hawker> hawkerList;

    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

    public GMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        invokeWS();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                gMap = mMap;

                // For showing a move to my location button
                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_ACCESS_FINE_LOCATION);
                }

                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mMap.setMyLocationEnabled(true);
                }

                LatLng singapore = new LatLng(1.301, 103.837);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(singapore).zoom(12).build();
                //gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Hawker hawker = (Hawker) marker.getTag();
                        Intent i = new Intent(getActivity().getApplicationContext(), HawkerActivity.class);
                        i.putExtra("hawkerID", String.valueOf(hawker.getId()));
                        getActivity().getApplicationContext().startActivity(i);
                    }
                });
            }

        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode){
            case REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length >0){
                    permisionToAccessLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }

        if((!permisionToAccessLocationAccepted) ){
            getActivity().finish();
        }
    }

    public void invokeWS() {

        RestClient.get("hawker/all", new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

                ObjectMapper mapper = new ObjectMapper();

                if (timeline.length() != 0) {

                    try {

                        hawkerList = mapper.readValue(timeline.toString(), new TypeReference<List<Hawker>>() {
                        });

                        for (Hawker h : hawkerList) {
                            if (h.getAddLat() != 0 && h.getAddLong() != 0){
                                Marker marker = gMap.addMarker(new MarkerOptions()
                                .position(new LatLng(h.getAddLat(), h.getAddLong()))
                                .title(h.getName()));
                                marker.setTag(h);

                                if(h.isDelivery() && h.isStore()){

                                    BitmapDrawable deliveryDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.both);
                                    Bitmap b=deliveryDraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                }else if (h.isStore()){

                                    BitmapDrawable deliveryDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.store);
                                    Bitmap b=deliveryDraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                }else if (h.isDelivery()){

                                    BitmapDrawable deliveryDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.delivery);
                                    Bitmap b=deliveryDraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                }

                            }
                        }
                        gMap.addMarker(new MarkerOptions()
                                .position(new LatLng(10, 10))
                                .title("Hello world"));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            }

        });
    }

}
