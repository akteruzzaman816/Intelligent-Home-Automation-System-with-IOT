package com.example.smarthome.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;


public class ProfileFragment extends Fragment {

    TextView name,email,address,userID,password,buttonCount;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);

            name=view.findViewById(R.id.profile_name);
            email=view.findViewById(R.id.profile_email);
            address=view.findViewById(R.id.profile_address);
            userID=view.findViewById(R.id.profile_userId);
            password=view.findViewById(R.id.profile_password);
            buttonCount=view.findViewById(R.id.button_num);

            String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            userID.setText(userUid);

            userID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(userID.getText());
                    Toast.makeText(getActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            reference= FirebaseDatabase.getInstance().getReference().child("users").child(userUid);


            reference.child("userInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   name.setText(dataSnapshot.child("userName").getValue(String.class));
                   address.setText(dataSnapshot.child("address").getValue(String.class));
                   email.setText(dataSnapshot.child("userEmail").getValue(String.class));
                   password.setText(dataSnapshot.child("userPassword").getValue(String.class));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            reference.child("Buttons").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count= (int) dataSnapshot.getChildrenCount();
                   // Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_SHORT).show();
                    buttonCount.setText(String.valueOf(count));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



       return view;
    }





}
