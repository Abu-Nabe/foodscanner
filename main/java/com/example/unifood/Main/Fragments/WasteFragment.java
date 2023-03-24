package com.example.unifood.Main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.unifood.Main.Activity.WastedActivity;
import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.R;

import java.text.ParseException;
import java.util.ArrayList;

public class WasteFragment extends Fragment {

    String TABLE_NAME, COLUMN_NAME_1, COLUMN_NAME_2, COLUMN_NAME_3;
    TextView bottomLabel, topLabel, middleLabel, viewAll;
    RelativeLayout progressView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_waste_fragment,
                container, false);

        progressView = view.findViewById(R.id.progressView);
        topLabel = view.findViewById(R.id.topLabel);
        middleLabel = view.findViewById(R.id.middleLabel);
        bottomLabel = view.findViewById(R.id.bottomLabel);

        TABLE_NAME = getResources().getString(R.string.TABLE_NAME);
        COLUMN_NAME_1 = getResources().getString(R.string.COLUMN_NAME_1);
        COLUMN_NAME_2 = getResources().getString(R.string.COLUMN_NAME_2);
        COLUMN_NAME_3 = getResources().getString(R.string.COLUMN_NAME_3);

        config();

        bottomLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WastedActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void config()
    {
        ArrayList<FoodModel> dataList = Retrieve.getAllData(getContext(), TABLE_NAME, COLUMN_NAME_1,
                COLUMN_NAME_2, COLUMN_NAME_3);

        int consumed = 0;
        int total = 0;
        for(int i = dataList.size() - 1; i >= 0; i--){
//            if(!DateComparison.checkWeek(dataList.get(i).getWasted())) {
                if (dataList.get(i).getData().equals("Consumed")) {
                    consumed++;
                    total++;
                } else {
                    try {
                        String result = DateComparison.comparisonString(dataList.get(i).getData(), dataList.get(i).getWasted());
                        if (result.equals("expired")) {
                            total++;
                        }
                    } catch (ParseException e) {
                        Log.d("XAS", e.toString());
                    }
                }
//           }
        }

        percentageConverter(consumed, total);
    }

    private void percentageConverter(int consumed, int total){
        double percentage = ((double) consumed / total) * 100;
        int intPercentage = (int) percentage;
        String topText = String.valueOf(intPercentage) + "%";
        topLabel.setText(topText);

        if(intPercentage == 100){
            progressView.setBackgroundResource(R.drawable.green_circle_view);
            middleLabel.setText("Well done you haven't wasted anything!");
        }else if(intPercentage >= 80){
            progressView.setBackgroundResource(R.drawable.green_circle_view);
            middleLabel.setText("Good effort, but you can do better!");
        }else if(intPercentage > 50){
            progressView.setBackgroundResource(R.drawable.orange_circle_view);
            middleLabel.setText("It's not looking too good");
        }else if(intPercentage < 50 && intPercentage > 25){
            progressView.setBackgroundResource(R.drawable.orange_circle_view);
            middleLabel.setText("Bad effort this week");
        }else if(intPercentage < 25){
            progressView.setBackgroundResource(R.drawable.red_circle_view);
            middleLabel.setText("Horrendous tbh");
        }else if(total == 0){
            topLabel.setText("0%");
            progressView.setBackgroundResource(R.drawable.circle_view);
            middleLabel.setText("No food consumed/expired");
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            config();
        }
    }
    public static WasteFragment newInstance() {

        WasteFragment f = new WasteFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
}
