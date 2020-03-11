package com.example.smarthome.fragment;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smarthome.AdapterClass;
import com.example.smarthome.ButtonModel;
import com.example.smarthome.R;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {


    DatabaseReference databaseReference;
    String userUID;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    TextView warningText,keyText;

    ArrayList<ButtonModel> list;
    private ProgressBar progressBar;
    DatabaseReference tempReference;
    TextView temperature;
    private DatabaseReference reference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);




        temperature=rootView.findViewById(R.id.temp);

        progressBar= rootView.findViewById(R.id.progressBar_home);
        WanderingCubes cube=new WanderingCubes();
        progressBar.setIndeterminateDrawable(cube);



        warningText=rootView.findViewById(R.id.no_data);

        recyclerView=rootView.findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("Buttons");
        tempReference= FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("temperature");
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("Locations");

       // Toast.makeText(getActivity(), strtext, Toast.LENGTH_SHORT).show();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                tempReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("celsius").exists())
                        {
                            int value = dataSnapshot.child("celsius").getValue(Integer.class);
                            temperature.setText(String.valueOf(value)+" °C");

                            if (value>40)
                            {
                                temperature.setTextColor(Color.parseColor("#FF0000"));

                            }else {
                                temperature.setTextColor(Color.parseColor("#000000"));
                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        },500,1000);

        return rootView;


    }

    @Override
    public void onStart() {
        super.onStart();

        if (databaseReference != null)
        {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.exists())
                    {
                        warningText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        list=new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            list.add(ds.getValue(ButtonModel.class));
                        }
                        AdapterClass adapterClass=new AdapterClass(list,getActivity());
                        recyclerView.setAdapter(adapterClass);
                        progressBar.setVisibility(View.GONE);




                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }







        // retrive location data  from firebase..................................
        reference.child("home").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("latitude").exists())
                {
                    final float latitude,longitude;
                    latitude=dataSnapshot.child("latitude").getValue(Float.class);
                    longitude=dataSnapshot.child("longitude").getValue(Float.class);

                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putFloat("latitude",latitude);
                    editor.putFloat("longitude",longitude);
                    editor.apply();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
