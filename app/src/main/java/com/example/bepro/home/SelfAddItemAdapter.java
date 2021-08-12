package com.example.bepro.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepro.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SelfAddItemAdapter extends RecyclerView.Adapter<SelfAddItemAdapter.ViewHolder> {
    ArrayList<FoodItems> items = new ArrayList<FoodItems>();

    @NonNull
    @NotNull
    @Override
    // 뷰홀더가 새로 만들어지는 시점에 호출, 정의된 XML 레이아웃으로 뷰 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        //인플레이션으로 뷰객체 생성 (카드뷰 품목 아이템)
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.add_food_items, viewGroup, false);

        return new ViewHolder(itemView); //뷰객체 생성과 동시에 뷰객체 반환
    }

    @Override
    // 뷰홀더가 재사용 될 시 호출, 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int position) {
        FoodItems foodItem = items.get(position);
        viewHolder.setItem(foodItem);
        //holder.tvFoodName.setText(foodItem.getFoodName());
        //holder.tvFoodExp.setText(foodItem.getFoodExpiryDate());
        //holder.tvFoodRemain.setText(foodItem.getRemainDate());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FoodItems item){
        items.add(item);
    }

    public void setItems(ArrayList<FoodItems> items){
        this.items = items;
    }

    public FoodItems getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, FoodItems item){
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFoodName;
        TextView tvFoodTotal;
        TextView tvFoodRemain;

        public ViewHolder(@NonNull @NotNull View itemView) { //뷰홀더 생성자로 뷰객체 전달
            super(itemView);


            //뷰객체에 들어있는 텍스트뷰 참조
            tvFoodName = itemView.findViewById(R.id.selfAddFoodName);
            tvFoodTotal = itemView.findViewById(R.id.selfAddFoodTotalCount);
            tvFoodRemain = itemView.findViewById(R.id.selfAddFoodRemainDate);
        }

        public void setItem(FoodItems item){
            tvFoodName.setText(item.getFoodName());
            tvFoodTotal.setText(item.getFoodExpiryDate());
            tvFoodRemain.setText(item.getFoodRemainDate());
        }

    }


}
