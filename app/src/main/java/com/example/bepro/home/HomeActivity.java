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
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bepro.FridgeData;
import com.example.bepro.FridgeSettingData;
import com.example.bepro.GetFridgeSettingRequest;
import com.example.bepro.MainActivity;
import com.example.bepro.R;
import com.example.bepro.recipe.RecipeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends Fragment {

    private Spinner mSpinner;
    public RecyclerView mHomeRecyclerView;
    private Dialog mDetailDialog;
    private Button mItemDetailCancelBtn, mItemUpdateBtn, mItemDetailRecipeBtn;
    private TextView mItemDeleteBtn, detailFrigeName, detailFoodRemainDate, detailFoodExp, detailFoodDate, detailFoodRegistrant;
    private EditText detailFoodName, detailFoodNum, detailFoodMemo;
    private SearchView mSearchView;

    public FoodAdapter foodAdapter;
    ArrayList<FoodItems> foodItems = new ArrayList<>();
    String[] items = {"유통기한 짧은 순", "등록 오래된 순", "등록 최신순"};
    String updateFoodIdx, updateFoodName, updateFoodNum, updateFoodMemo, updateFoodExp, updateFoodRemainDate;
    String formatMonth, formatDay;

    MainActivity mMain = new MainActivity();
    //FridgeSettingData settingData = new FridgeSettingData();

    FridgeData fridgeData;
    int friIdx, foodIdx, foodNum;
    String foodName, foodRegistrant, foodExp, foodDate, foodRemainDate, foodMemo, selfAddFoodName, selfAddFoodNum, selfAddFoodExp;

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

    Bundle bundle;
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup homeView = (ViewGroup) inflater.inflate(R.layout.home, container, false);

        mHomeRecyclerView = homeView.findViewById(R.id.homeRecyclerView);

        //리사이클러 뷰에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeView.getContext(), LinearLayoutManager.VERTICAL, false);
        mHomeRecyclerView.setLayoutManager(layoutManager);

        //어댑터 생성자 호출
        foodAdapter = new FoodAdapter(getContext(), foodItems);
        mHomeRecyclerView.setAdapter(foodAdapter);

        //TODO: 다빈아 여기야!!ㅠㅠ help me~
//        bundle = this.getArguments();
//
//        if (bundle != null) {
//            bundle = getArguments();
//            homeFriIdx = bundle.getString("homeFriIdx");
//            foodItemSetting(homeFriIdx);
//            Log.i("test","번들"+homeFriIdx);
//        }
        //foodItemSetting(fridgeData.getFriIdx());

        //품목 검색창
        mSearchView = homeView.findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //문자열 입력 완료 시 문자열 반환
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //문자열이 변할 때 마다 문자열 반환 (실시간 검색)
                if(newText.isEmpty()){
                    System.out.println("선택된 냉장고 인덱스: " + fridgeData.getFriIdx());
                    foodItemSetting(fridgeData.getFriIdx()); //검색창 비어있을 경우 전체 품목 출력
                }else {
                    foodAdapter.getFilter().filter(newText.toString());
                }
                return false;
            }
        });

        //품목 정렬 스피너
        mSpinner = homeView.findViewById(R.id.spinner);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                homeView.getContext(), android.R.layout.simple_spinner_item, items);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(mAdapter);


        //foodAdapter.addItem(new FoodItems("banana", "2021-07-23", "7"));
        //foodAdapter.addItem(new FoodItems("apple", "2021-07-14", "2"));
        //foodAdapter.addItem(new FoodItems("egg", "2021-07-25", "10"));

        //품목 클릭 이벤트 리스너
        foodAdapter.setOnItemClickListener(new OnFoodItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder, View view, int position) {
                FoodItems item = foodAdapter.getItem(position); //아이템 클릭 시 어댑터에서 해당 아이템 객체 가져옴
                showDetailDialog(item,position);

            }
        });

        //품목 상세 정보 팝업
        mDetailDialog = new Dialog(getContext()); //dialog 초기화
        mDetailDialog.setContentView(R.layout.detail_item_popup);

        detailFrigeName = mDetailDialog.findViewById(R.id.detailItemFrigeName); //냉장고명
        detailFoodName = mDetailDialog.findViewById(R.id.detailItemFoodName); //품목명
        detailFoodNum = mDetailDialog.findViewById(R.id.detailItemFoodNum); //품목 개수
        detailFoodRemainDate = mDetailDialog.findViewById(R.id.itemDetailRemainDate); //품목 남은 기한
        detailFoodExp = mDetailDialog.findViewById(R.id.detailFoodExpDate); //품목 유통기한
        detailFoodDate = mDetailDialog.findViewById(R.id.detailItemFoodDate); //등록일
        detailFoodRegistrant = mDetailDialog.findViewById(R.id.detailItemFoodRegistrant); //등록인
        detailFoodMemo = mDetailDialog.findViewById(R.id.detailItemFoodMemo); //메모

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSortFunc(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return homeView;
    }

    public void setHomeData(){
        bundle = getArguments();

        if (bundle != null) {
            fridgeData = (FridgeData)bundle.getSerializable("fridgeData");
            foodItemSetting(fridgeData.getFriIdx());
            Log.i("test","번들"+fridgeData.toString());
        }
    }
    public void itemSortFunc(int position){
        switch (position){
            case 0: //유통기한 짧은 순
                Comparator<FoodItems> shortExp = new Comparator<FoodItems>() {
                    @Override
                    public int compare(FoodItems item1, FoodItems item2) {
                        //문자 오름차순 정렬
                        return item1.getFoodExpiryDate().compareTo(item2.getFoodExpiryDate());
                    }
                };

                Collections.sort(foodItems, shortExp);
                foodAdapter.notifyDataSetChanged();

                break;

            case 1: //등록 오래된 순
                Comparator<FoodItems> oldResister = new Comparator<FoodItems>() {
                    @Override
                    public int compare(FoodItems item1, FoodItems item2) {
                        //정수 오름차순 정렬
                        int ret = 0;

                        if (item1.getFoodIdx() < item2.getFoodIdx())
                            ret = -1;
                        else if (item1.getFoodIdx() == item2.getFoodIdx())
                            ret = 0;
                        else ret = 1;

                        return ret;
                    }
                };

                Collections.sort(foodItems, oldResister);
                foodAdapter.notifyDataSetChanged();

                break;

            case 2: //등록 최신순
                Comparator<FoodItems> newResister = new Comparator<FoodItems>() {
                    @Override
                    public int compare(FoodItems item1, FoodItems item2) {
                        //정수 내림차순 정렬
                        int ret = 0;

                        if (item1.getFoodIdx() < item2.getFoodIdx())
                            ret = 1;
                        else if (item1.getFoodIdx() == item2.getFoodIdx())
                            ret = 0;
                        else ret = -1;

                        return ret;
                    }
                };

                Collections.sort(foodItems, newResister);
                foodAdapter.notifyDataSetChanged();

                break;
        }
    }

    //품목 직접 추가 시 UI에 즉시 품목 추가 함수
    public void selfAddItemFunc() throws ParseException {
        //bundle로 data 가져오기
        Bundle bundle = this.getArguments();

        if (bundle != null){
            bundle = getArguments();
            selfAddFoodName = bundle.getString("foodName");
            selfAddFoodNum = bundle.getString("foodNum");
            selfAddFoodExp = bundle.getString("foodExp");

            System.out.println("직접 입력 식품명: " + selfAddFoodName);
            System.out.println("직접 입력 남은기한: " + selfAddFoodExp);

            //TODO: NPE 해결
            foodItems.add(new FoodItems(selfAddFoodName, selfAddFoodExp, getRemainDate(selfAddFoodExp)));

        }
    }

    //품목 상세 정보 팝업창
    public void showDetailDialog(FoodItems item,int position) {

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

        //JSON data를 각 오브젝트에 셋팅
        detailFrigeName.setText(String.valueOf(item.getFriIdx()));
        detailFoodName.setText(item.getFoodName());
        detailFoodNum.setText(String.valueOf(item.getFoodNumber()));
        detailFoodRemainDate.setText(item.getFoodRemainDate() + "일");

        String foodDate = item.getFoodDate();
        String[] foodDateArray = foodDate.split("\\s"); //공백 기준으로 문자열 잘라 등록일 날짜 추출
        detailFoodDate.setText(foodDateArray[0]);

        detailFoodRegistrant.setText(item.getFoodRegistrant());

        if(item.getFoodMemo().equals("null")){
            detailFoodMemo.setText("");
        }else
            detailFoodMemo.setText(item.getFoodMemo());

        //datePicker : 디데이 날짜 입력 버튼, 클릭시 DatePickerDialog 띄움
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), endDateSetListener, currentYear, currentMonth, currentDay).show();
            }
        });

        //품목 정보 취소 버튼
        mItemDetailCancelBtn = mDetailDialog.findViewById(R.id.itemDetailCancelBtn);
        mItemDetailCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailDialog.dismiss(); //다이얼로그 닫기
            }
        });

        //품목 정보 확인(수정) 버튼
        mItemUpdateBtn = mDetailDialog.findViewById(R.id.itemUpdateBtn);
        mItemUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFoodIdx = String.valueOf(item.getFoodIdx());
                updateFoodName = detailFoodName.getText().toString();
                updateFoodNum = detailFoodNum.getText().toString();
                updateFoodMemo = detailFoodMemo.getText().toString();
                updateFoodExp = detailFoodExp.getText().toString();
                updateFoodRemainDate = detailFoodRemainDate.getText().toString();

                foodItemUpdate(updateFoodIdx, updateFoodName, updateFoodNum, updateFoodMemo, updateFoodExp);

                TextView homeFoodName = mHomeRecyclerView.findViewById(R.id.foodName);
                TextView homeFoodExpDate = mHomeRecyclerView.findViewById(R.id.foodExpiryDate);
                TextView homeFoodRemainDate = mHomeRecyclerView.findViewById(R.id.foodRemainDate);

                //수정 사항 UI 즉시 반영
                homeFoodName.setText(updateFoodName);
                homeFoodExpDate.setText(updateFoodExp);
                homeFoodRemainDate.setText(updateFoodRemainDate);

                foodItemSetting(fridgeData.getFriIdx()); //수정 후 목록 재설정
                itemSortFunc(mSpinner.getSelectedItemPosition()); // 품목 재정렬

                //foodAdapter.notifyDataSetChanged();
                mDetailDialog.dismiss(); //다이얼로그 닫기
            }
        });

        //품목 삭제 버튼
        mItemDeleteBtn = mDetailDialog.findViewById(R.id.itemDeleteBtn);
        mItemDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodIdx = String.valueOf(item.getFoodIdx());
                foodItemDelete(foodIdx);
                foodAdapter.removeItem(position);
                foodAdapter.notifyDataSetChanged();

                mDetailDialog.dismiss(); //다이얼로그 닫기
            }
        });

        //추천 레시피 버튼
        mItemDetailRecipeBtn = mDetailDialog.findViewById(R.id.itemDetailRecipeBtn);
        mItemDetailRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //검색어 레시피 전달
                String menu = detailFoodName.getText().toString();
                System.out.println("레시피 식품명1: " + menu);

                mDetailDialog.dismiss(); //다이얼로그 닫기
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction(); //트랜잭션 시작
                RecipeActivity recipeFragment = new RecipeActivity(menu);
                transaction.replace(R.id.frameLayout, recipeFragment).commitAllowingStateLoss();

            }
        });

    }

    //품목 data 설정
    public void foodItemSetting(int fridgeIdx) {
        String URL = "http://3.37.119.236:80/food/selectFood.php/?fridgeIdx="+fridgeIdx;

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            //volley 라이브러리의 GET 방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않아 POST 방식 사용

            @Override
            public void onResponse(JSONArray response) {
                foodItems.clear(); //반복 추가 되지 않도록 매번 clear 해야함
                //foodAdapter.notifyDataSetChanged();
                //System.out.println("셋팅 함수에서 notifyDataSetChanged 시작");

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        //JSON data key 값 참조한 value 값을 변수에 담음
                        //TODO: DB에 null 값 있으면 앱이 종료되는 오류 발생, null 체크 추가하기
                        friIdx = Integer.parseInt(jsonObject.getString("friIdx")); //냉장고 인덱스
                        foodIdx = Integer.parseInt(jsonObject.getString("foodIdx")); //품목 인덱스
                        foodName = jsonObject.getString("foodName"); //품목 이름
                        foodNum = Integer.parseInt(jsonObject.getString("foodNum")); //품목 개수
                        foodRegistrant = jsonObject.getString("foodRegistrant"); //등록인
                        foodExp = jsonObject.getString("foodExp"); //유통기한
                        foodDate = jsonObject.getString("foodDate"); //등록일
                        foodRemainDate = getRemainDate(foodExp); //남은 기한
                        foodMemo = jsonObject.getString("foodMemo"); //메모

                        //ArrayList에 Data add
                        foodItems.add(new FoodItems(friIdx, foodIdx, foodName, foodNum, foodRegistrant, foodExp, foodDate, foodRemainDate, foodMemo));
                    }
                    foodAdapter.notifyDataSetChanged();
                    itemSortFunc(mSpinner.getSelectedItemPosition());

                }catch (JSONException | ParseException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"품목 출력 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "foodItemSetting() onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //GetFridgeSettingRequest getFridgeSetting = new GetFridgeSettingRequest(new FoodItems.getFriIdx(), responseListener);

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);
        //requestQueue.add(getFridgeSetting);

    }

    //품목 data 수정
    public void foodItemUpdate(String foodIdx, String foodName, String foodNum, String foodMemo, String foodExp){
        String URL = "http://3.37.119.236:80/food/updateFood.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "foodItemUpdate() onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> requestedParams = new HashMap<>();
                requestedParams.put("foodIdx", foodIdx);
                requestedParams.put("foodName", foodName);
                requestedParams.put("foodNum", foodNum);
                requestedParams.put("foodMemo", foodMemo);
                requestedParams.put("foodExp", foodExp);

                return requestedParams;
            }

        };

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        //요청큐에 요청 객체 생성
        requestQueue.add(stringRequest);

    }

    //품목 data 삭제
    public void foodItemDelete(String foodIdx){
        String URL = "http://3.37.119.236:80/food/deleteFood.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "foodItemDelete() onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> requestedParams = new HashMap<>();
                requestedParams.put("foodIdx", foodIdx);

                return requestedParams;
            }

        };

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        //요청큐에 요청 객체 생성
        requestQueue.add(stringRequest);

    }

    //유통기한 남은 날짜 계산 함수
    public String getRemainDate(String foodExpDate) throws ParseException {

        String remainDate = "";

        Calendar getToday = Calendar.getInstance();
        getToday.setTime(new Date()); //현재 날짜

        //사용자 입력 유통기한 데이터 yyyy-MM-dd 형식으로 저장
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(foodExpDate);
        Calendar expDate = Calendar.getInstance();
        expDate.setTime(date); //품목 유통기한

        long diffDays = expDate.getTimeInMillis()/ ONE_DAY - getToday.getTimeInMillis()/ ONE_DAY;

        if(diffDays > 0){
            remainDate = String.valueOf(diffDays);
            return remainDate;
        }else if (diffDays == 0){
            remainDate = String.valueOf(diffDays);
            return remainDate;
        }else {
            remainDate = String.valueOf(diffDays);
            return remainDate;
        }

    }

    //DatePickerDialog 팝업, 종료일 저장, 기존에 입력한 값이 있으면 해당 데이터 설정후 띄움
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dDayValue = dDayResult_int(dateEndY, dateEndM, dateEndD);

            dateResult.setText(getDday(year, month, day));

            int correctMonth = month + 1; //timezone 때문인지 모르겠으나,, 한달 오차가 나서 +1로 보정해줌

            if(correctMonth < 10){ //한 자리수 달이면 0붙여줌
                formatMonth = "0" + correctMonth;
            }else{
                formatMonth = String.valueOf(correctMonth);
            }

            if(day < 10){ //한 자리수 일이면 0붙여줌
                formatDay = "0" + day;
            }else {
                formatDay = String.valueOf(day);
            }
            detailFoodExp.setText(year + "-" + formatMonth + "-" + formatDay);

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
            //result *= -1;
            strFormat = "%d일";
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