package com.example.bepro;

import android.view.View;

public interface OnFoodItemClickListener {
    public void onItemClick(FoodAdapter.ViewHolder holder, View view, int position);
}
