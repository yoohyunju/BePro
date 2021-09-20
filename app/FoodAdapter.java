package com.example.bepro.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepro.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements OnFoodItemClickListener, Filterable {
    Context context;
    ArrayList<FoodItems> items = new ArrayList<FoodItems>();
    OnFoodItemClickListener listener;

    public FoodAdapter(Context context, ArrayList<FoodItems> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    // 뷰홀더가 새로 만들어지는 시점에 호출, 정의된 XML 레이아웃으로 뷰 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.food_items, viewGroup, false); //인플레이션으로 뷰객체 생성 (카드뷰 품목 아이템)

        return new ViewHolder(itemView, this); //뷰객체 생성과 동시에 뷰객체, 리스너 전달 후 반환
    }

    @Override
    // 뷰홀더가 재사용 될 시 호출, 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull @NotNull FoodAdapter.ViewHolder holder, int position) {
        FoodItems foodItem = items.get(position);
        //holder.setItem(foodItem);

        //JSON data를 DTO에 저장 후 DTO에서 가져와 출력
        holder.tvFoodName.setText(foodItem.getFoodName());
        holder.tvFoodExp.setText(foodItem.getFoodExpiryDate());

        long remainDate = Integer.parseInt(foodItem.getFoodRemainDate());

        if(remainDate > 0){
            holder.tvFoodRemain.setText(foodItem.getFoodRemainDate() + "일 남음");
        }else if(remainDate == 0){
            holder.tvFoodRemain.setText("오늘까지");
        }else{
            holder.tvFoodRemain.setText(foodItem.getFoodRemainDate() + "일 지남");
        }


    }

    @Override
    public int getItemCount() { //item 개수 반환
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

    public void removeItem(int position) { items.remove(position); }

    public void setOnItemClickListener(OnFoodItemClickListener listener){ //리스너 설정 메소드 추가
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) { //인터페이스 메소드 구현
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }

    }

    //Filterable implement (품목 검색 로직)
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FoodItems> itemFilterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) { //검색어가 없으면
                itemFilterList.addAll(items); //모든 아이템 추가
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(FoodItems foodItems : items) {
                    if (foodItems.getFoodName().toLowerCase().contains(filterPattern)) { //검색 패턴을 가지고 있는 품목명 일 경우
                        itemFilterList.add(foodItems);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = itemFilterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //품목 리스트 초기화 후 검색 결과 출력
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

    //아이템 뷰를 저장하는 뷰홀더 클래스
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFoodName;
        TextView tvFoodExp;
        TextView tvFoodRemain;

        public ViewHolder(@NonNull @NotNull View itemView, final OnFoodItemClickListener listener) { //뷰홀더 생성자로 뷰객체 전달
            super(itemView);

            //뷰홀더가 만들어지는 시점에 클릭 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() { //item 클릭 이벤트
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition(); //item 위치 저장
                    //ArrayList<FoodItems> items = new ArrayList<FoodItems>();
                    /*
                    notifyDataSetChanged()에 의해 리사이클러뷰가 아이템뷰를 갱신하는 과정에서,
                    뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 getAdapterPosition() 메서드는 NO_POSITION 리턴
                    if(pos != RecyclerView.NO_POSITION) {
                        //FoodItems foodItems = items.get(pos);
                    }
                     */

                    if(listener != null){ //이벤트 존재 검사
                        listener.onItemClick(ViewHolder.this, v, pos); //아이템 클릭 시 리스너 메소드 호출
                    }
                }
            });

            //뷰객체에 들어있는 텍스트뷰 참조
            tvFoodName = itemView.findViewById(R.id.foodName);
            tvFoodExp = itemView.findViewById(R.id.foodExpiryDate);
            tvFoodRemain = itemView.findViewById(R.id.foodRemainDate);
        }

        public void setItem(FoodItems item){
            tvFoodName.setText(item.getFoodName());
            tvFoodExp.setText(item.getFoodExpiryDate());
            tvFoodRemain.setText(item.getFoodRemainDate());
        }

    }

}