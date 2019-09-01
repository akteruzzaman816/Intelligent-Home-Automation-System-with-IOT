package com.example.smarthome.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.smarthome.R;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    MapView mapView;
    Marker marker;

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


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        // MapsInitializer.initialize(getContext());
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

        // retrive location data  from firebase..................................
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final double latitude,longitude;
                latitude=dataSnapshot.child("lat").getValue(Double.class);
                longitude=dataSnapshot.child("lon").getValue(Double.class);


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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mMap !=null){
                    if (marker != null) marker.remove();
                    marker =mMap.addMarker(new MarkerOptions()
                     .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                             locationResult.getLastLocation().getLongitude()))
                            .title("You"));
                    mMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(marker.getPosition(),12.9f));


                }
            }
        };




    }
}