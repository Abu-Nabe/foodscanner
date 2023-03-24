package com.example.unifood.Main.Fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unifood.Main.Adapter.FoodListAdapter;
import com.example.unifood.Main.Data.Database;
import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.R;

import java.text.ParseException;
import java.util.ArrayList;

public class FoodFragment extends Fragment {

    String TABLE_NAME, COLUMN_NAME_1, COLUMN_NAME_2, COLUMN_NAME_3;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_food,
                container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        TABLE_NAME = getResources().getString(R.string.TABLE_NAME);
        COLUMN_NAME_1 = getResources().getString(R.string.COLUMN_NAME_1);
        COLUMN_NAME_2 = getResources().getString(R.string.COLUMN_NAME_2);
        COLUMN_NAME_3 = getResources().getString(R.string.COLUMN_NAME_3);

        configData();
        return view;
    }
    private void configData()
    {
        ArrayList<FoodModel> dataList = Retrieve.getAllData(getContext(), TABLE_NAME, COLUMN_NAME_1,
                COLUMN_NAME_2, COLUMN_NAME_3);

        // It is generally not recommended to remove data from the adapter, it should be done in the fetching process
        // Since it is a small project for uni, I decided to save some time and do it here.
        // Should change if you want to convert it to a major project

        for (int i = dataList.size() - 1; i >= 0; i--) {
            String date1 = dataList.get(i).getData();
            String date2 = dataList.get(i).getWasted();
            if (date1.equals("Consumed")) {
                dataList.remove(i);
            } else {
                try {
                    String result = DateComparison.comparisonString(date1, date2);
                    if (result.equals("expired")) {
                        dataList.remove(i);
                    }
                } catch (ParseException e) {
                    Log.d("XAS", e.toString());
                }
            }
        }

        FoodListAdapter foodListAdapter = new FoodListAdapter(getContext(), dataList);
        recyclerView.setAdapter(foodListAdapter);
        foodListAdapter.notifyDataSetChanged();
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            configData();
        }
    }
    public static FoodFragment newInstance() {

        FoodFragment f = new FoodFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
}
