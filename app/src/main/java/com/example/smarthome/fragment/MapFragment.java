package com.example.smarthome.fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.smarthome.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private Marker marker;
    private boolean status=false;
    private GeoQuery geoQuery;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private DatabaseReference reference;
    private GeoFire geoFire;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        removeInfo();


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buildLocationCallBack();
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mapView = getView().findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        setUpGeoFire();


    }

    private void setUpGeoFire() {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            reference= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("Locations");
            geoFire=new GeoFire(reference);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission
                    (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1); // 1 is requestCode
                return;

            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        float latitude=preferences.getFloat("latitude",0f);
        float longitude=preferences.getFloat("longitude",0f);

// adding marker

                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(latitude,longitude))
                        .radius(1000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.TRANSPARENT)
                        .strokeWidth(5.0f));

                //add circle for Home..................................

                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Smart Home");
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_icon));
                mMap.addMarker(marker);

//                Polyline line = mMap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(51.5, -0.1), new LatLng(latitude, longitude))
//                        .width(5)
//                        .color(Color.RED));


                 geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude,longitude),0.5f); //500m
                 geoQuery.setCenter(new GeoLocation(latitude,longitude));

                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        Toast.makeText(getActivity(), "Activated", Toast.LENGTH_SHORT).show();
                        status=true;
                        sendInfo();
                    }

                    @Override
                    public void onKeyExited(String key) {
                        Toast.makeText(getActivity(), "DeActivated", Toast.LENGTH_SHORT).show();
                        removeInfo();
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });









    }

    private void removeInfo() {
//        if (!status){
//            Map<String , String>info=new HashMap<>();
//            info.put("status","0");
//            reference.child("info").setValue(info);
//        }


    }

    private void sendInfo() {
        Map<String , String>info=new HashMap<>();
        info.put("status","1");
        reference.child("info").setValue(info);

    }

    @Override
    public void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }


    private void buildLocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {

                if (mMap != null) {

                    geoFire.setLocation("you", new GeoLocation(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                            if (marker != null) marker.remove();
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                            locationResult.getLastLocation().getLongitude()))
                                    .title("You"));
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(marker.getPosition(), 12.9f));

                        }
                    });



                }
            }
        };



    }
}