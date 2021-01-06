package com.example.myfirstapp;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyMenuAdapter extends RecyclerView.Adapter<MyMenuAdapter.myMenuViewHolder> {

    private ArrayList<meal> mMenuList;
    private OnItemClickListener mListener;
    private   Boolean flag_layout =false;


    public Boolean getFlag_layout() {
        return flag_layout;
    }

    public void setFlag_layout(Boolean flag_layout) {
        this.flag_layout = flag_layout;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public static class myMenuViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView txtVwTitle,txtVwCalories,txtVwDesc,txtVwIngredients;
        public ImageView img_delete_meal;

        public myMenuViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img_myMeal);
            txtVwTitle = itemView.findViewById(R.id.txtVwTitle);
            txtVwDesc =itemView.findViewById(R.id.txtVwDesc);
            txtVwIngredients=itemView.findViewById(R.id.txtVwIngredients);
            txtVwCalories = itemView.findViewById(R.id.txtVwCalories);
            img_delete_meal = itemView.findViewById(R.id.img_delete_meal);
            img_delete_meal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }
    }
    public MyMenuAdapter(ArrayList<meal> menuList) { mMenuList = menuList; }
    @NonNull
    @Override
    public myMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//  the RecyclerView on mymeal_layout
        if (getFlag_layout().equals(false)) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymeal_layout, parent, false);
            return new myMenuViewHolder(v, mListener);
        }
        // else the RecyclerView on mymealmore_layout
        else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymealmore_layout, parent, false);
            return new myMenuViewHolder(v, mListener);
        }



    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myMenuViewHolder holder, int position) {
        //show  data for all the meals in RecyclerView for the specific layout
        if(flag_layout.equals(false)) {
            meal currentMeal = mMenuList.get(position);
            holder.mImageView.setImageBitmap(currentMeal.getImage());
            holder.txtVwTitle.setText(currentMeal.getNameMeal());
            holder.txtVwCalories.setText("Calories:"+currentMeal.getCalories());
            holder.txtVwIngredients.setText("Ingredients:"+currentMeal.sizeIngredients_counts());
            holder.txtVwDesc.setText("");
        }

        //show  data meal more the selected meal (on click the layout change )
        else if(flag_layout.equals(true)) {

            meal currentMeal = mMenuList.get(position);
            holder.mImageView.setImageBitmap(currentMeal.getImage());
            holder.txtVwTitle.setText(currentMeal.getNameMeal());
            holder.txtVwCalories.setText("Calories: "+currentMeal.getCalories());
            holder.txtVwIngredients.setText(""+currentMeal.toStringIngredients());
            holder.txtVwDesc.setText(currentMeal.getDescription());
        }

    }
    @Override
    public int getItemCount() {
        return mMenuList.size();
    }
}


