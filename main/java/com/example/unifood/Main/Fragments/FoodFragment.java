package com.example.unifood.Main.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unifood.Main.Adapter.FoodListAdapter;
import com.example.unifood.Main.Data.Database;
import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Extension.editTextChange;
import com.example.unifood.Main.Main.MainActivity;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class FoodFragment extends Fragment {

    String TABLE_NAME, COLUMN_NAME_1, COLUMN_NAME_2, COLUMN_NAME_3;
    RecyclerView recyclerView;
    ImageView rightImage;
    String productName;
    DatePickerDialog.OnDateSetListener mDataSetListener;

    BottomSheetDialog bottomSheetDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_food,
                container, false);

        rightImage = view.findViewById(R.id.rightIcon);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        TABLE_NAME = getResources().getString(R.string.TABLE_NAME);
        COLUMN_NAME_1 = getResources().getString(R.string.COLUMN_NAME_1);
        COLUMN_NAME_2 = getResources().getString(R.string.COLUMN_NAME_2);
        COLUMN_NAME_3 = getResources().getString(R.string.COLUMN_NAME_3);

        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                month = month + 1;

                String currentDay = DateComparison.currentDay();
                String expiryDate = day + "/" + month + "/" + year;

                Retrieve.insertRow(getActivity(), productName, currentDay, expiryDate);

                configData();
                bottomSheetDialog.dismiss();
            }
        };

        configData();
        return view;
    }

    private void openBottomSheet()
    {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.activity_add_sheet);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        EditText editText;
        Button dob = bottomSheetDialog.findViewById(R.id.button);
        editText = bottomSheetDialog.findViewById(R.id.food_name);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString().trim();
                String stringWithoutSpaces = inputString.replaceAll(" ", "");

                if (!stringWithoutSpaces.isEmpty()) {
                    productName = inputString;
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getContext(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDataSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setTitle("Expiry Date");
                    dialog.show();
                }else{
                    editText.setText(null);
                    editText.setHint("Food name is empty");
                    editText.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                    editTextChange.changeTextColor(editText, getActivity());
                }

            }
        });

        bottomSheetDialog.show();
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
