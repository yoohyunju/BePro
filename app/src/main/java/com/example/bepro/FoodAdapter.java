package com.example.bepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    ArrayList<FoodItems> items = new ArrayList<FoodItems>();

    @NonNull
    @NotNull
    @Override
    // 뷰홀더가 새로 만들어지는 시점에 호출, 정의된 XML 레이아웃으로 뷰 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.food_items, viewGroup, false); //인플레이션으로 뷰객체 생성

        return new ViewHolder(itemView); //뷰객체 생성과 동시에 뷰객체 전달 후 반환
    }

    @Override
    // 뷰홀더가 재사용 될 시 호출, 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull @NotNull FoodAdapter.ViewHolder holder, int position) {
        FoodItems foodItem = items.get(position);
        holder.setItem(foodItem);

    }

    @Override
    public int getItemCount() { //item 개수 반환
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        TextView textView3;

        public ViewHolder(@NonNull @NotNull View itemView) { //뷰홀더 생성자로 뷰객체 전달
            super(itemView);

            //뷰객체에 들어있는 텍스트뷰 참조
            textView = itemView.findViewById(R.id.foodName);
            textView2 = itemView.findViewById(R.id.foodExpiryDate);
            textView3 = itemView.findViewById(R.id.remainDate);
        }

        public void setItem(FoodItems item){
            textView.setText(item.getFoodName());
            textView2.setText(item.getFoodExpiryDate());
            textView3.setText(item.getRemainDate());
        }

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
}
