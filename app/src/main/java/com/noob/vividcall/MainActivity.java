package com.noob.vividcall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;
    RecyclerView myContactsList;
    ImageView findPeopleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         navView = findViewById(R.id.nav_view);
         navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
         findPeopleBtn=findViewById(R.id.find_people_btn);
         myContactsList=findViewById(R.id.contact_list);

        myContactsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent findpeopleIntent = new Intent(MainActivity.this,FindPeopleActivity.class);
                startActivity(findpeopleIntent);


            }
        });

    }

private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
     {
         switch (menuItem.getItemId())
         {
             case R.id.navigation_home:

                 break;
             case R.id.navigation_settings:
                 Intent settingsIntent = new Intent(MainActivity.this,SettingActivity.class);
                 startActivity(settingsIntent);
                 break;
             case R.id.navigation_notifications:
                 Intent notificationIntent = new Intent(MainActivity.this,NotificationActivity.class);
                 startActivity(notificationIntent);
                 break;
             case R.id.navigation_logout:
                 FirebaseAuth.getInstance().signOut();
                 Intent logoutIntent = new Intent(MainActivity.this,Registration.class);
                 startActivity(logoutIntent);
                 finish();
                 break;

         }
        return true;
    }
};
}
