package com.example.unifood.Main.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Model.FoodModel;
import com.example.unifood.R;

import java.text.ParseException;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder> {

    private List<FoodModel> mList;
    private Context mContext;


    public FoodListAdapter(Context mContext ,List<FoodModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.name.setText(mList.get(position).getName());
        holder.data.setText(mList.get(position).getData());

        if(mList.get(position).getData().equals("Consumed")){
            holder.data.setTextColor(mContext.getResources().getColor(R.color.green));
        }else if(mList.get(position).getData().equals("Expired")){
            holder.data.setTextColor(mContext.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, data;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.FoodName);
            data = itemView.findViewById(R.id.FoodData);
        }
    }
}
