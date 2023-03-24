package com.example.unifood.Main.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpirylistAdapter extends RecyclerView.Adapter<ExpirylistAdapter.MyViewHolder> {

    ImageView rightIcon;
    private List<FoodModel> mList;
    ArrayList<Integer> positions = new ArrayList<Integer>();
    private Context mContext;
    ArrayList<String> foodList = new ArrayList<String>();

    public ExpirylistAdapter(Context mContext ,List<FoodModel> mList, ImageView rightIcon) {
        this.mContext = mContext;
        this.mList = mList;
        this.rightIcon = rightIcon;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_expiry_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        // to surpress issue with position
        int i = position;

        holder.checkBox.setChecked(foodList.contains(mList.get(position).getName()));

        holder.name.setText(mList.get(position).getName());
        holder.data.setText(mList.get(position).getData());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()){
                    createDialog(mList.get(i).getName());
                    positions.add(i);
                }else{
                    deleteDialog(mList.get(i).getName());
                    positions.remove(i);
                }
            }
        });

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TABLE_NAME = mContext.getResources().getString(R.string.TABLE_NAME);
                String COLUMN_NAME_1 = mContext.getResources().getString(R.string.COLUMN_NAME_1);
                String COLUMN_NAME_2 = mContext.getResources().getString(R.string.COLUMN_NAME_2);
                String COLUMN_NAME_3 = mContext.getResources().getString(R.string.COLUMN_NAME_3);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(calendar.getTime());

                // Not a good method to use if there are duplicates, only using this because uni project.
                for(int pos = 0; pos < foodList.size(); pos++){
                    Retrieve.updateData(mContext, TABLE_NAME, COLUMN_NAME_1, COLUMN_NAME_2, COLUMN_NAME_3, foodList.get(pos), "Consumed", formattedDate);
                    int deletePos = positions.get(pos);
                    foodList.remove(foodList.get(pos));
                    mList.remove(deletePos);
                    ExpirylistAdapter.super.notifyDataSetChanged();
                }
                rightIcon.setVisibility(View.GONE);
            }
        });
    }

    private void deleteDialog(String foodName)
    {
        foodList.remove(foodName);

        if(foodList.size() == 0){
            rightIcon.setVisibility(View.GONE);
        }
    }

    private void createDialog(String foodName)
    {
        foodList.add(foodName);
        rightIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView name, data;
        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            name = itemView.findViewById(R.id.FoodName);
            data = itemView.findViewById(R.id.FoodData);
        }
    }
}
