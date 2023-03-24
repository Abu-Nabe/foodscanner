package com.example.unifood.Main.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.unifood.Main.Adapter.FoodListAdapter;
import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.R;

import java.text.ParseException;
import java.util.ArrayList;

public class WastedActivity extends AppCompatActivity {

    String TABLE_NAME, COLUMN_NAME_1, COLUMN_NAME_2, COLUMN_NAME_3;
    ImageView leftImage;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);

        leftImage = findViewById(R.id.leftIcon);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        TABLE_NAME = getResources().getString(R.string.TABLE_NAME);
        COLUMN_NAME_1 = getResources().getString(R.string.COLUMN_NAME_1);
        COLUMN_NAME_2 = getResources().getString(R.string.COLUMN_NAME_2);
        COLUMN_NAME_3 = getResources().getString(R.string.COLUMN_NAME_3);

        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            configData();
        } catch (ParseException e) {
            Log.d("XAS", "error");
        }
    }

    private void configData() throws ParseException {
        ArrayList<FoodModel> dataList = Retrieve.getAllData(this, TABLE_NAME, COLUMN_NAME_1,
                COLUMN_NAME_2, COLUMN_NAME_3);

        // It is generally not recommended to remove data from the adapter, it should be done in the fetching process
        // Since it is a small project for uni, I decided to save some time and do it here.
        // Should change if want to convert it to a major project

        for (int i = dataList.size() - 1; i >= 0; i--) {
            String date1 = dataList.get(i).getData();
            String date2 = dataList.get(i).getWasted();
            // this code checks if the data is less than a week long, but we won't be using it for now
//            if(!DateComparison.checkWeek(date2)){
                if (date1.equals("Consumed")) {
                } else {
                    try {
                        String result = DateComparison.comparisonString(date1, date2);
                        if (!result.equals("expired")) {
                            dataList.remove(i);
                        }else{
                            dataList.get(i).setData("Expired");
                        }
                    } catch (ParseException e) {
                        Log.d("XAS", e.toString());
                    }
                }
//            }else{
//              dataList.remove(i);
//            }
        }

        FoodListAdapter foodListAdapter = new FoodListAdapter(WastedActivity.this, dataList);
        recyclerView.setAdapter(foodListAdapter);
        foodListAdapter.notifyDataSetChanged();
    }
}