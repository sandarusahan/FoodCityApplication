package com.foodcityapp.esa.foodcityapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodcityapp.esa.foodcityapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gMap;
    private int PROXIMITY_RADIUS = 1000;
    private double Latitude, Longtitude;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        Latitude= 6.921512;
        Longtitude = 79.937772;

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.setMyLocationEnabled(true);
        gMap.addMarker(new MarkerOptions().position(new LatLng(6.921512 , 79.937772)));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.921512 , 79.937772),10));
        findNearByFoodCity();
    }

    private void findNearByFoodCity() {

        gMap.clear();
        String foodCity = "Cargills Foodcity";
        String url = getUrl(Latitude, Longtitude, foodCity);
        Object dataTranfer[] = new Object[2];
        dataTranfer[0]= gMap;
        dataTranfer[1]= url;

       /// GetNearbyPlaces getNearbyPlacesData = new GetNearbyPlaces();
       // getNearbyPlacesData.execute(dataTranfer);
        Toast.makeText(getContext(), "Showing Near By Foodcity Supermarkets!", Toast.LENGTH_LONG);

    }

    private String getUrl(double latitude, double longtitude, String foodcity){

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location" + latitude + ","+ longtitude);
        googlePlaceUrl.append("&radius"+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type"+foodcity);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key"+"AIzaSyCdatWUcOIB-dxcoPnKoKwJARAEWotOJ6s");

        return googlePlaceUrl.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
