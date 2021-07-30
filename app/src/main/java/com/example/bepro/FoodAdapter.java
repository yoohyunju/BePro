package com.example.bepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements OnFoodItemClickListener {

    ArrayList<FoodItems> items = new ArrayList<FoodItems>();
    OnFoodItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    // 뷰홀더가 새로 만들어지는 시점에 호출, 정의된 XML 레이아웃으로 뷰 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.food_items, viewGroup, false); //인플레이션으로 뷰객체 생성

        return new ViewHolder(itemView, this); //뷰객체 생성과 동시에 뷰객체, 리스너 전달 후 반환
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

    public void setOnItemClickListener(OnFoodItemClickListener listener){ //리스너 설정 메소드 추가
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) { //인터페이스 메소드 구현
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        TextView textView3;

        public ViewHolder(@NonNull @NotNull View itemView, final OnFoodItemClickListener listener) { //뷰홀더 생성자로 뷰객체 전달
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() { //item 클릭 이벤트
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition(); //item 위치 저장

                    if(listener != null){ //이벤트 존재 검사
                        listener.onItemClick(ViewHolder.this, v, pos); //아이템 클릭 시 리스너 메소드 호출

                    }
                }
            });

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
