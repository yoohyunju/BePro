package com.example.bepro;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

/*
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends Fragment {

    private Spinner mSpinner;
    private RecyclerView mHomeRecyclerView;
    private Dialog mDetailDialog;
    private Button mDetailCancelBtn;
    FoodAdapter foodAdapter;
    ArrayList<FoodItems> foodItems = new ArrayList<>();

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

        //홈 리사이클러 뷰
        mHomeRecyclerView = homeView.findViewById(R.id.homeRecyclerView);

        //리사이클러 뷰에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeView.getContext(), LinearLayoutManager.VERTICAL, false);
        mHomeRecyclerView.setLayoutManager(layoutManager);

        foodAdapter = new FoodAdapter(getContext(), foodItems); //어댑터 생성자 호출

        //foodAdapter.addItem(new FoodItems("사과", "2021-07-23까지", "유통기한: 7일 남음"));
        //foodAdapter.addItem(new FoodItems("양배추", "2021-07-14까지", "유통기한: 2일 지남"));
        //foodAdapter.addItem(new FoodItems("우유", "2021-07-25까지", "유통기한: 9일 남음"));

        mHomeRecyclerView.setAdapter(foodAdapter);

        foodAdapter.setOnItemClickListener(new OnFoodItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder, View view, int position) {
                FoodItems item = foodAdapter.getItem(position); //아이템 클릭 시 어댑터에서 해당 아이템 객체 가져옴
                showDetailDialog();
            }
        });

        //품목 상세 정보 팝업
        mDetailDialog = new Dialog(getContext()); //dialog 초기화
        mDetailDialog.setContentView(R.layout.detail_item_popup);

        //foodItemSetting(); //품목 데이터 셋팅 함수 (임시로 막아둠)

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

    /*
    public String getDate(){
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyymmdd");
        long mNowTime = System.currentTimeMillis(); //현재 시간 가져옴
        Date mDate = new Date(mNowTime);

        return mFormat.format(mDate);
    }


    public void foodItemSetting(){
        String URL = "http://10.0.2.2/beProTest/selectFood.php"; //local 경로
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            //volley 라이브러리의 GET 방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않아 POST 방식 사용

            @Override
            public void onResponse(JSONArray response) {
                foodItems.clear();
                foodAdapter.notifyDataSetChanged();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String foodName = jsonObject.getString("foodName");
                        String foodExp = jsonObject.getString("foodExp");
                        //TODO: foodRemainDate 추가

                        foodItems.add(new FoodItems(foodName, foodExp));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"품목 출력 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);

    }

    */

}
