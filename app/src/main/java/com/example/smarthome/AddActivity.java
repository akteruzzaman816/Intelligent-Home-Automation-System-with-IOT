package com.example.smarthome;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    Button  addButton;
    EditText name,pinNo;

    //for firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addButton=findViewById(R.id.add_button);
        name=findViewById(R.id.name);
        pinNo=findViewById(R.id.pin_no);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        userUID=FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mReference=mDatabase.getReference().child("users").child(userUID).child("Buttons");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonName=name.getText().toString();
                String pinNumber=pinNo.getText().toString().toUpperCase();

                Map<String, String> data= new HashMap<String, String>();
                data.put("buttonName",buttonName);
                data.put("buttonState","0");
                data.put("buttonPinNumber",pinNumber);

                mReference.child(pinNumber).setValue(data);

                Intent  intent=new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);


            }
        });
    }
}
