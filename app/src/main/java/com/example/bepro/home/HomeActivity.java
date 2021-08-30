package com.example.bepro.home;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bepro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends Fragment {

    private Spinner mSpinner;
    private RecyclerView mHomeRecyclerView;
    private Dialog mDetailDialog;
    private Button mDetailCancelBtn, mDetailUpdateBtn;
    private TextView detailFrigeName, detailFoodExp, detailFoodDate, detailFoodRegistrant;
    private EditText detailFoodName, detailFoodNum;
    FoodAdapter foodAdapter;
    ArrayList<FoodItems> foodItems = new ArrayList<>();
    String[] items = {"유통기한 짧은 순", "등록 오래된 순", "등록 최신순"};

    //달력 관련 변수 정의
    private LinearLayout datePickerBtn;
    private TextView tvRemainDate;

    Calendar calendar;
    int dateEndY, dateEndM, dateEndD;
    int dDayValue = 0;
    int currentYear, currentMonth, currentDay;
    private final int ONE_DAY = 24 * 60 * 60 * 1000; //Millisecond 형태의 하루(24 시간), 86400000 = 24시간 * 60분 * 60초 * 1000(=1초)

    TextView dateResult;
    LinearLayout datePicker;

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
        mHomeRecyclerView.setAdapter(foodAdapter);

        //foodAdapter.addItem(new FoodItems("사과", "2021-07-23까지", "유통기한: 7일 남음"));
        //foodAdapter.addItem(new FoodItems("양배추", "2021-07-14까지", "유통기한: 2일 지남"));
        //foodAdapter.addItem(new FoodItems("우유", "2021-07-25까지", "유통기한: 9일 남음"));

        //품목 클릭 이벤트 리스너
        foodAdapter.setOnItemClickListener(new OnFoodItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder, View view, int position) {
                FoodItems item = foodAdapter.getItem(position); //아이템 클릭 시 어댑터에서 해당 아이템 객체 가져옴
                showDetailDialog(item);

            }
        });

        //품목 상세 정보 팝업
        mDetailDialog = new Dialog(getContext()); //dialog 초기화
        mDetailDialog.setContentView(R.layout.detail_item_popup);

        detailFrigeName = mDetailDialog.findViewById(R.id.detailItemFrigeName); //냉장고명
        detailFoodName = mDetailDialog.findViewById(R.id.detailItemFoodName); //품목명
        detailFoodNum = mDetailDialog.findViewById(R.id.detailItemFoodNum); //품목 개수
        //detailFoodExp = mDetailDialog.findViewById(R.id.itemDetailRemainDate); //품목 기한
        detailFoodDate = mDetailDialog.findViewById(R.id.detailItemFoodDate); //등록일
        detailFoodRegistrant = mDetailDialog.findViewById(R.id.detailItemFoodRegistrant); //등록인

        //foodItemSetting(); //PHP 연결후 JSON data 가져와서 DTO 셋팅하는 함수

        return homeView;
    }

    //품목 정보 팝업창
    public void showDetailDialog(FoodItems item) {
        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mDetailDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        mDetailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //기본 백그라운드를 투명으로 변경
        mDetailDialog.setCanceledOnTouchOutside(false); //창 바깥 부분 터치 닫기 설정 해제

        mDetailDialog.show(); //Dialog 띄우기

        datePicker = mDetailDialog.findViewById(R.id.itemDetailDatePicker); //기한 선택 달력 버튼
        dateResult = mDetailDialog.findViewById(R.id.itemDetailRemainDate); //남은 기한 출력 텍스트뷰

        //현재 년월일 저장
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //한국어 설정
        Locale.setDefault(Locale.KOREAN);

        //datePicker : 디데이 날짜 입력 버튼, 클릭시 DatePickerDialog 띄움
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), endDateSetListener, currentYear, currentMonth, currentDay).show();
            }
        });

        //품목 정보 취소 버튼
        mDetailCancelBtn = mDetailDialog.findViewById(R.id.detailCancelBtn);
        mDetailCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailDialog.dismiss(); //다이얼로그 닫기
            }
        });

        //품목 정보 확인(수정) 버튼
        mDetailUpdateBtn = mDetailDialog.findViewById(R.id.detailUpdateBtn);
        mDetailUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodIdx = String.valueOf(item.getFoodIdx());
                String foodName = detailFoodName.getText().toString();
                String foodNum = detailFoodNum.getText().toString();
                //String foodExp = detailFoodExp.getText().toString();
                //foodItemUpdate(item, foodIdx, foodName, foodNum);
                mDetailDialog.dismiss(); //다이얼로그 닫기

                //foodItemSetting(); //수정 후 목록 재설정
            }
        });
        
        //String remainDate = item.getFoodExpiryDate() - today; TODO: 유통기한 계산 로직직

        //JSO data를 각 오브젝트에 셋팅
        detailFrigeName.setText(String.valueOf(item.getFriIdx()));
        detailFoodName.setText(item.getFoodName());
        detailFoodNum.setText(String.valueOf(item.getFoodNumber()));
        //detailFoodExp.setText(item.getFoodExpiryDate());
        detailFoodDate.setText(item.getFoodDate());
        detailFoodRegistrant.setText(item.getFoodRegistrant());

    }

    /*
    public String getDate(){
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyymmdd");
        long mNowTime = System.currentTimeMillis(); //현재 시간 가져옴
        Date mDate = new Date(mNowTime);

        return mFormat.format(mDate);
    }
     */

    /*품목 data 설정 //main에 push하기 위한 임시 주석
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

                        //JSON data key 값 참조한 value 값을 변수에 담음
                        int friIdx = Integer.parseInt(jsonObject.getString("friIdx"));
                        int foodIdx = Integer.parseInt(jsonObject.getString("foodIdx"));
                        String foodName = jsonObject.getString("foodName");
                        int foodNum = Integer.parseInt(jsonObject.getString("foodNum"));
                        String foodRegistrant = jsonObject.getString("foodRegistrant");
                        String foodExp = jsonObject.getString("foodExp");
                        String foodDate = jsonObject.getString("foodDate");

                        //TODO: foodRemainDate 추가

                        foodItems.add(new FoodItems(friIdx, foodIdx, foodName, foodNum, foodRegistrant, foodExp, foodDate));
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

    //품목 data 수정
    public void foodItemUpdate(FoodItems item, String foodIdx, String foodName, String foodNum){
        String URL = "http://10.0.2.2/beProTest/updateFood.php"; //local 경로

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //foodItems.clear();
                foodAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> requestedParams = new HashMap<>();
                requestedParams.put("foodIdx", foodIdx);
                requestedParams.put("foodName", foodName);
                requestedParams.put("foodNum", foodNum);

                return requestedParams;
            }

        };

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        //요청큐에 요청 객체 생성
        requestQueue.add(stringRequest);

    }

     */
    //DatePickerDialog 팝업, 종료일 저장, 기존에 입력한 값이 있으면 해당 데이터 설정후 띄움
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //edit_endDateBtn.setText(year + "년 " + (month + 1) + "월 " + day + "일");

            dDayValue = dDayResult_int(dateEndY, dateEndM, dateEndD);

            dateResult.setText(getDday(year, month, day));
            //TODO: 지난 날짜 선택 불가하게 수정

        }
    };

    //D-day 반환 함수
    private String getDday(int mYear, int mMonth, int mDay) {

        //D-day 설정
        final Calendar dDayCalendar = Calendar.getInstance();
        dDayCalendar.set(mYear, mMonth, mDay);

        //millisecond 으로 환산 후 d-day 에서 today 의 차를 구함
        final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY; //사용자 선택 날짜
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY; //오늘 날짜
        long result = dDay - today;

        //출력 시 d-day 에 맞게 표시
        String strFormat;

        if (result > 0) { //남은 기한
            strFormat = "%d일";
        } else if (result == 0) { //당일
            strFormat = "오늘";
        } else { //지난 기한
            result *= -1;
            strFormat = "지남";
        }

        final String strCount = (String.format(strFormat, result));

        return strCount;
    }


    //D-day 값 계산 함수
    public int onCalculatorDate (int dateEndY, int dateEndM, int dateEndD) {
        try {
            Calendar today = Calendar.getInstance(); //현재 날짜
            Calendar dDay = Calendar.getInstance();

            //시작일, 종료일 데이터 저장
            Calendar calendar = Calendar.getInstance();
            int cyear = calendar.get(Calendar.YEAR);
            int cmonth = (calendar.get(Calendar.MONTH) + 1);
            int cday = calendar.get(Calendar.DAY_OF_MONTH);

            today.set(cyear, cmonth, cday);
            dDay.set(dateEndY, dateEndM, dateEndD);// D-day의 날짜를 입력

            long day = dDay.getTimeInMillis() / ONE_DAY;
            long tday = today.getTimeInMillis() / ONE_DAY;
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됨

            return (int) count; // 날짜는 하루 + 시켜줘야함

        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }
    }

    //D-day 값 계산 결과 출력 함수
    public int dDayResult_int(int dateEndY, int dateEndM, int dateEndD) {
        int result = 0;

        result = onCalculatorDate(dateEndY, dateEndM, dateEndD);

        return result;
    }

}
