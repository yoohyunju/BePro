package com.example.bepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.ViewHolder> implements OnFridgeClickListener {
    ArrayList<FridgeData> items = new ArrayList<FridgeData>();
    OnFridgeClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.show_fridge_list, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FridgeData item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FridgeData item){
        items.add(item);
    }

    public void setItems(ArrayList<FridgeData> items){
        this.items = items;
    }

    public FridgeData getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, FridgeData item){
        items.set(position, item);
    }

    public void setOnFridgeClickListener(OnFridgeClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onFridgeClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onFridgeClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
       TextView textView;

       public ViewHolder(View itemView, OnFridgeClickListener listener){
           super(itemView);
           textView = itemView.findViewById(R.id.fridgeItem);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position = getAdapterPosition();

                   if(listener != null){
                       listener.onFridgeClick(ViewHolder.this, v, position);
                   }
               }
           });
       }

       public void setItem(FridgeData item){
           textView.setText(item.getName());
       }
   }
}
