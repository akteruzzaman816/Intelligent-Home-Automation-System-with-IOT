package com.example.smarthome;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    TextView login;
    EditText email,password,name;
    Button SignUp;

    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //get editText reference
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);

        SignUp=findViewById(R.id.sign_up_button);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();
        mUser=FirebaseAuth.getInstance().getCurrentUser() ;




        login=findViewById(R.id.move_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivity.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName=name.getText().toString();
                final String userEmail=email.getText().toString();
                final String userPassword=password.getText().toString();



                auth.createUserWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                createNewUser(userEmail,userPassword,fullName);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                    finish();
                                }

                            }
                        });
            }
        });
    }

    private void createNewUser(String userEmail, String userPassword, String fullName) {
        User user=new User(fullName,userEmail,userPassword);
       String userID=mUser.getUid();
        mReference.child("users").child(userID).child("userInfo").setValue(user);


    }
}
