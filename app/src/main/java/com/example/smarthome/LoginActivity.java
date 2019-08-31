package com.example.smarthome;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    TextView signUp;
    EditText email,password;
    Button LogIn;


    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //references
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        LogIn=findViewById(R.id.login_button);

         progressBar= (ProgressBar)findViewById(R.id.progressBar);
         FoldingCube cube=new FoldingCube();
         progressBar.setIndeterminateDrawable(cube);

                //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }

        signUp=findViewById(R.id.move_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String userEmail=email.getText().toString();
                final String userPassword=password.getText().toString();

                auth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    // there was an error

                                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });


            }
        });

    }
}
