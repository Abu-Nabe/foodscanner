package com.example.unifood.Main.Main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Fragments.CameraFragment;
import com.example.unifood.Main.Fragments.ExpiryFragment;
import com.example.unifood.Main.Fragments.FoodFragment;
import com.example.unifood.Main.Fragments.WasteFragment;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.Main.PushNotification.NotificationBroadcastReceiver;
import com.example.unifood.Main.Utils.PreferencesUtils;
import com.example.unifood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    ViewPager pager;
    BottomNavigationView bottomNavigationView;

    String DesiredFragment;

    public static final int QR_ICON_ID = R.id.qr_icon;
    public static final int FOOD_ICON_ID = R.id.food_icon;
    public static final int EXPIRY_ICON_ID = R.id.expiry_icon;
    public static final int WASTE_ICON_ID = R.id.waste_icon;

    String TABLE_NAME, COLUMN_NAME_1, COLUMN_NAME_2, COLUMN_NAME_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        firebaseMessaging.setAutoInitEnabled(true);

        firebaseMessaging.getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                PreferencesUtils.saveToken(token, MainActivity.this);
                configNotification();
            } else {
                Log.e("XAS", "Failed to get Firebase token", task.getException());
                if(PreferencesUtils.getToken(this) != null) configNotification();
            }
        });

        TABLE_NAME = getResources().getString(R.string.TABLE_NAME);
        COLUMN_NAME_1 = getResources().getString(R.string.COLUMN_NAME_1);
        COLUMN_NAME_2 = getResources().getString(R.string.COLUMN_NAME_2);
        COLUMN_NAME_3 = getResources().getString(R.string.COLUMN_NAME_3);

        pager = findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(4);

        bottomNavigationView = findViewById(R.id.BottomNavigation);

        DesiredFragment = getIntent().getStringExtra("fragment");

        //Set Selected
        //Perform itemSelectedListener
        // Usually switch statement, but due to error had to use if/else
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == QR_ICON_ID) {
                    pager.setCurrentItem(0, false);
                    return true;
                } else if (id == FOOD_ICON_ID) {
                    pager.setCurrentItem(1, false);
                    return true;
                } else if (id == EXPIRY_ICON_ID) {
                    pager.setCurrentItem(2, false);
                    return true;
                } else if (id == WASTE_ICON_ID) {
                    pager.setCurrentItem(3, false);
                    return true;
                }else{
                    pager.setCurrentItem(0, false);
                    return true;
                }
            }
        });

        config();
    }

    private void config()
    {
        // Data always resets so uncheck this when you want data to show,
        // you can edit the dates to suit your timeframe

        ArrayList<FoodModel> dataList = Retrieve.getAllData(MainActivity.this, TABLE_NAME, COLUMN_NAME_1,
                COLUMN_NAME_2, COLUMN_NAME_3);

        if(dataList.size() == 0){
//            food lists
            Retrieve.insertRow(MainActivity.this, "KitKat", "20/03/2023", "20/04/2023");
            Retrieve.insertRow(MainActivity.this, "Milk", "20/03/2023", "30/04/2023");
            Retrieve.insertRow(MainActivity.this, "Nutella", "20/03/2023", "25/04/2023");

//         expired lists
            Retrieve.insertRow(MainActivity.this, "Beef", "20/03/2023", "20/02/2023");
            Retrieve.insertRow(MainActivity.this, "Brocolli", "20/03/2023", "20/02/2023");

//         consumed lists
            Retrieve.insertRow(MainActivity.this, "Chicken", "Consumed", "20/05/2023");
            Retrieve.insertRow(MainActivity.this, "Hot Chocolate Latte", "Consumed", "20/05/2023");
            Retrieve.insertRow(MainActivity.this, "IceCream", "Consumed", "20/05/2023");

        }
    }

    private void configNotification()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to start at 12 PM
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Schedule a repeating alarm that triggers every day at 12 PM
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return CameraFragment.newInstance();
                case 1: return FoodFragment.newInstance();
                case 2: return ExpiryFragment.newInstance();
                case 3: return WasteFragment.newInstance();
                default: return CameraFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }
}