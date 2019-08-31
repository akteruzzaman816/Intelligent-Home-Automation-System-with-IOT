package com.example.smarthome.fragment;


import android.os.Bundle;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    DatabaseReference databaseReference;
    String userUID;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    TextView warningText,keyText;

    ArrayList<ButtonModel> list;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar= rootView.findViewById(R.id.progressBar_home);
        WanderingCubes cube=new WanderingCubes();
        progressBar.setIndeterminateDrawable(cube);

        warningText=rootView.findViewById(R.id.no_data);

        recyclerView=rootView.findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("Buttons");


       // Toast.makeText(getActivity(), strtext, Toast.LENGTH_SHORT).show();

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
    }


}
