package com.example.bepro;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends Fragment {

    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private Dialog mDetailDialog;
    private Button mDetailCancelBtn;

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

                /*TODO 스피너 정렬기준 선택 시 품목 정렬*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mTextView.setText("");
            }
        });

        //리사이클러 뷰
        mRecyclerView = homeView.findViewById(R.id.recyclerView);

        //리사이클러 뷰에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeView.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        FoodAdapter adapter = new FoodAdapter();

        adapter.addItem(new FoodItems("사과", "2021-07-23까지", "유통기한: 7일 남음"));
        adapter.addItem(new FoodItems("양배추", "2021-07-14까지", "유통기한: 2일 지남"));
        adapter.addItem(new FoodItems("우유", "2021-07-25까지", "유통기한: 9일 남음"));

        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnFoodItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder, View view, int position) {
                FoodItems item = adapter.getItem(position); //아이템 클릭 시 어댑터에서 해당 아이템 객체 가져옴
                showDetailDialog();
            }
        });

        //품목 상세 정보 팝업
        mDetailDialog = new Dialog(getContext()); //dialog 초기화
        mDetailDialog.setContentView(R.layout.detail_item_popup);


        return homeView;
    }

    //품목 정보 팝업창
    public void showDetailDialog() {
        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mDetailDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mDetailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //기본 백그라운드를 투명으로 변경
        mDetailDialog.setCanceledOnTouchOutside(false); //창 바깥 부분 터치 닫기 설정 해제

        mDetailDialog.show(); //Dialog 띄우기

        mDetailCancelBtn = mDetailDialog.findViewById(R.id.detailCancelBtn); //품목 추가 취소 버튼
        mDetailCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailDialog.dismiss(); //다이얼로그 닫기
            }
        });
    }



}
