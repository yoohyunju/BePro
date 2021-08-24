package com.example.bepro.home;

import android.view.View;

import com.example.bepro.home.FoodAdapter;

public interface OnFoodItemClickListener {
    public void onItemClick(FoodAdapter.ViewHolder holder, View view, int position);
}
