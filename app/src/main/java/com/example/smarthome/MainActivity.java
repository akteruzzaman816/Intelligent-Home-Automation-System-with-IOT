package com.example.smarthome;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.smarthome.fragment.HomeFragment;
import com.example.smarthome.fragment.MapFragment;
import com.example.smarthome.fragment.ProfileFragment;
import com.example.smarthome.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ImageView addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        addButton=findViewById(R.id.iv_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));

            }
        });


        BottomNavigationView navigationView=findViewById(R.id.bottom_navigation_main);
        navigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout,fragment)
                    .commit();

            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment=null;

        switch (menuItem.getItemId()){
            case R.id.home:
                fragment=new HomeFragment();
                break;

            case R.id.map:
                fragment=new MapFragment();
                break;
            case R.id.profile:
                fragment=new ProfileFragment();
                break;
            case R.id.setting:
                fragment=new SettingFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
