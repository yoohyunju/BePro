package com.example.bepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends Fragment {

    //private TextView mTextView;
    private Spinner mSpinner;
    private RecyclerView recyclerView;

    String[] items = {"유통기한 짧은 순", "등록 오래된 순", "등록 최신순"};

    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup homeView = (ViewGroup) inflater.inflate(R.layout.home, container, false);

        //mTextView = homeView.findViewById(R.id.textView);
        mSpinner = homeView.findViewById(R.id.spinner);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                homeView.getContext(), android.R.layout.simple_spinner_item, items);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mTextView.setText(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mTextView.setText("");
            }
        });

        //리사이클러 뷰
        recyclerView = homeView.findViewById(R.id.recyclerView);

        //리사이클러 뷰에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        FoodAdapter adapter = new FoodAdapter();

        adapter.addItem(new FoodItems("사과", "2021-07-23까지", "유통기한: 7일 남음"));
        adapter.addItem(new FoodItems("양배추", "2021-07-14까지", "유통기한: 2일 지남"));
        adapter.addItem(new FoodItems("우유", "2021-07-25까지", "유통기한: 9일 남음"));
        recyclerView.setAdapter(adapter);

        return homeView;
    }




}
