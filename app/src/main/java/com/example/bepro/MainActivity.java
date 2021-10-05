package com.example.bepro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bepro.fridge_setting.FridgeMemberActivity;
import com.example.bepro.home.FoodItems;
import com.example.bepro.home.HomeActivity;
import com.example.bepro.login.snsRequest;
import com.example.bepro.my_page.MyPageActivity;
import com.example.bepro.notice.NoticeActivity;
import com.example.bepro.recipe.RecipeActivity;
import com.example.bepro.home.SelfAddItemAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private FragmentManager fragmentManager; //앱 fragment에서 작업을 추가, 삭제, 교체하고 백 스택에 추가하는 클래스
    private FragmentTransaction transaction; //fragment 변경을 위한 트랜잭션(작업단위)

    private HomeActivity mHome;
    private RecipeActivity mRecipe;
    private MyPageActivity mMyPage;
    private NoticeActivity mNotice;
    private BottomNavigationView mNavView;

    private LinearLayout mFridgeListOpenBtn, mAddFridgeBtn, mSelfAddBtn, mFridgeSettingBtn, m_btnOCR;;
    private Dialog mAddItemDialog, mFridgeListDialog, mFridgeAddDialog, mSelfAddDialog;
    private Button mAddCancelBtn, mFridgeListCancelBtn, mFridgeNameAddBtn, mFridgeAddCancelBtn, mSelfAddCancelBtn, mSelfAddConfirmBtn, mSelfItemAddBtn;

    private CardView mSelfAddCardView;
    ArrayList<FoodItems> foodItems = new ArrayList<>();

    //JSON DATA 받아올 변수
    String friIdx, foodName, foodNum, foodExp, foodRegistrant, text;


    //TODO: 프로젝트 병합 후 주석 해제
    public String image, email, type, fridgeName, userIdx;
    ArrayList<Integer> fridgeIdx = new ArrayList<Integer>();
    TextView selected;
    EditText addFridge;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FridgeAdapter fridgeAdapter;
    UserData user = new UserData(); //user 객체
    FridgeSettingData settingData = new FridgeSettingData();

    Bitmap testImage;
    InputImage inputImage;
    File file;
    Uri uri;
    private static final int ALBUM_RESULT_CODE = 0; //앨범 선택
    private static final int CAMERA_RESULT_CODE = 101; //앨범 선택

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        mNavView = findViewById(R.id.navigation);
        mNavView.setOnItemSelectedListener(new ItemSelectedListener()); //BottomNavigationView에 이벤트 리스너 연결

        // TODO: 프로젝트 병합 후 주석 해제
        //로그인 데이터 받아오기
        Intent intent = getIntent();
        image = intent.getStringExtra("userImage");
        email = intent.getStringExtra("userEmail");
        type = intent.getStringExtra("userType");

        if(image != null)
            //SNS 로그인
            user.setImage(image);
        user.setEmail(email);
        user.setType(type);

        getUser();

        //fragment 객체 생성
        mHome = new HomeActivity();
        mRecipe = new RecipeActivity();
        mMyPage = new MyPageActivity();
        mNotice = new NoticeActivity();

        //dialog 초기화
        mAddItemDialog = new Dialog(MainActivity.this);
        mAddItemDialog.setContentView(R.layout.add_item_popup); //품목 추가 팝업 xml 연결

        mFridgeListDialog = new Dialog(MainActivity.this);
        mFridgeListDialog.setContentView(R.layout.fridge_list_popup); //냉장고 리스트 팝업 xml 연결

        mFridgeAddDialog = new Dialog(MainActivity.this);
        mFridgeAddDialog.setContentView(R.layout.fridge_add_popup); //냉장고 추가 팝업 xml 연결

        mSelfAddDialog = new Dialog(MainActivity.this);
        mSelfAddDialog.setContentView(R.layout.self_item_add_popup); //품목 직접 추가 팝업 xml 연결

        mFridgeListOpenBtn = findViewById(R.id.fridgeListOpenBtn);
        mFridgeListOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFridgeListDialog(); /*냉장고 리스트 팝업 함수 호출*/
            }
        });

        // TODO: 프로젝트 병합 후 주석 해제
        //냉장고 리스트
        recyclerView = mFridgeListDialog.findViewById(R.id.FridgeRc);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        fridgeAdapter = new FridgeAdapter();

        fridgeAdapter.setOnFridgeClickListener(new OnFridgeClickListener() {
            @Override
            public void onFridgeClick(FridgeAdapter.ViewHolder holder, View view, int position) {
                //선택에 따라 품목 부분 가져오기
                FridgeData name = fridgeAdapter.getItem(position);
                selected = findViewById(R.id.myFridge);
                selected.setText(name.getName());
                //fridgeSetting 을 모두 객체로 저장
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            settingData.setFridgeIndex(jsonObject.getInt("friIDX"));
                            settingData.setAuthority(jsonObject.getString("friSetAuth"));
                            settingData.setCategory(jsonObject.getInt("FriSetCategory"));
                            settingData.setPush(jsonObject.getInt("friSetPush"));
                            settingData.setSort(jsonObject.getInt("friSetSort"));
                            settingData.setView(jsonObject.getInt("FriSetView"));

                            System.out.println(
                                    settingData.getFridgeIndex()
                                    + settingData.getAuthority()
                                    + settingData.getCategory()
                                    + settingData.getPush()
                                    + settingData.getSort()
                                    + settingData.getView());
                        }catch (Exception e){

                        }
                    }
                };
                GetFridgeSettingRequest getFridgeSetting = new GetFridgeSettingRequest(name.getName(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(getFridgeSetting);

                mFridgeListDialog.cancel();
            }
        });


        //냉장고 설정 버튼
        mFridgeSettingBtn = findViewById(R.id.fridgeSettingBtn);
        mFridgeSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FridgeMemberActivity.class);
                intent.putExtra("user", (Serializable) user); //PLUS
                startActivity(intent);
            }
        });

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction(); //트랜잭션 시작
        transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, @NotNull String[] permissions) {

    }

    @Override
    public void onGranted(int requestCode, @NotNull String[] permissions) {

    }

    //BottomNavigationView 이벤트 리스너 구현
    private class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.home:
                    item.setChecked(true);
                    transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();
                    break;

                case R.id.recipe:
                    item.setChecked(true);
                    transaction.replace(R.id.frameLayout, mRecipe).commitAllowingStateLoss();
                    break;

                case R.id.add:
                    item.setChecked(true);
                    showItemAddDialog(); //Dialog 함수 호출
                    break;

                case R.id.myPage:
                    item.setChecked(true);
                    //객체 데이터 보내기
                    mMyPage.user = user;
                    mMyPage.fridgeAdapter = fridgeAdapter;

                    transaction.replace(R.id.frameLayout, mMyPage).commitAllowingStateLoss();
                    break;

                case R.id.notice:
                    item.setChecked(true);
                    transaction.replace(R.id.frameLayout, mNotice).commitAllowingStateLoss();
                    break;
            }
            return false;
        }

    }

    //뒤로가기 버튼 막아두기
    @Override
    public void onBackPressed(){

    }

    //품목 추가 팝업창
    public void showItemAddDialog(){

       //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mAddItemDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mAddItemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //기본 백그라운드를 투명으로 변경
        mAddItemDialog.setCanceledOnTouchOutside(false); //창 바깥 부분 터치 닫기 설정 해제

        mAddItemDialog.show(); //Dialog 띄우기

        m_btnOCR = mAddItemDialog.findViewById(R.id.btn_OCR);
        m_btnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ocr 실행 (text 인식 결과 - log 확인 (cropper 추가 후 품목 수정))
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("영수증 등록");
                builder.setMessage("사진을 가져올 방법을 선택해주세요!");

                builder.setPositiveButton("카메라로 전환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                });
                builder.setNegativeButton("앨범에서 가져오기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, ALBUM_RESULT_CODE);
                    }
                });
                builder.setCancelable(true);
                builder.create().show();
            }
        });

        //품목 추가 취소 버튼
        mAddCancelBtn = mAddItemDialog.findViewById(R.id.addCancelBtn);
        mAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddItemDialog.dismiss(); //Dialog 닫기
            }
        });

        //직접 추가하기 버튼
        mSelfAddBtn = mAddItemDialog.findViewById(R.id.selfAddBtn);
        mSelfAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddItemDialog.dismiss(); //이전 다이얼로그 닫기
                showSelfAddDialog();
            }
        });

    }

    //품목 직접 추가하기 팝업
    public void showSelfAddDialog(){

        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mSelfAddDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        //mSelfAddDialog.getWindow().setLayout(1000, 1500); //크기 고정

        mSelfAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //기본 백그라운드를 투명으로 변경
        mSelfAddDialog.setCanceledOnTouchOutside(false); //창 바깥 부분 터치 닫기 설정 해제

        mSelfAddDialog.show();

        //리사이클러뷰에 레이아웃 매니저 설정
        RecyclerView recyclerView = mSelfAddDialog.findViewById(R.id.addFoodRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SelfAddItemAdapter adapter = new SelfAddItemAdapter();
        recyclerView.setAdapter(adapter);

        //취소 버튼
        mSelfAddCancelBtn = mSelfAddDialog.findViewById(R.id.selfAddCancelBtn);
        mSelfAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelfAddDialog.dismiss();
                showItemAddDialog(); //이전 다이얼로그 재시작
            }
        });

        //품목 추가하기 버튼
        mSelfItemAddBtn = mSelfAddDialog.findViewById(R.id.selfItemAddBtn);
        mSelfItemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //빈 카드뷰 추가
                adapter.addItem(new FoodItems("",0,""));
                adapter.notifyDataSetChanged();

            }
        });

        //품목 추가 확인 버튼
        mSelfAddConfirmBtn = mSelfAddDialog.findViewById(R.id.selfAddConfirmBtn);
        mSelfAddConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter.getItemCount() >= 1){ //품목 아이템이 한개 이상 있을 시
                    for(int i=0; i < adapter.getItemCount(); i++){ //아이템 개수만큼 반복
                        adapter.setPosition(i);

                        //System.out.println("냉장고 인덱스: "+ friIdx + " 등록인: " + foodRegistrant);
                        EditText mFoodName = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editAddFoodName);
                        EditText mFoodTotal = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editFoodTotalCount);
                        TextView mFoodExpDate = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.selfAddFoodExpDate);

                        friIdx = String.valueOf(settingData.getFridgeIndex()); //냉장고 인덱스
                        foodName = mFoodName.getText().toString(); //사용자 입력 식품명
                        foodNum = mFoodTotal.getText().toString(); //사용자 입력 개수
                        foodExp = mFoodExpDate.getText().toString(); //사용자 입력 남은 날짜
                        foodRegistrant = user.getNickname(); //등록인

                        //TODO: 여러 품목 data를 반복적으로 삽입하려면?
                        if(TextUtils.isEmpty(foodName) != true && TextUtils.isEmpty(foodNum)!= true && TextUtils.isEmpty(foodExp) != true){ //카드뷰에 입력 데이터가 있을 시

                            //bundle로 data 넘기기 (품목 추가 시 바로 UI에 보이게 하는 용도)
                            Bundle bundle = new Bundle();
                            bundle.putString("foodName", foodName);
                            bundle.putInt("foodNum", Integer.parseInt(foodNum));
                            bundle.putString("foodExp", foodExp);

                            mHome.setArguments(bundle);
                            try {
                                mHome.selfAddItemFunc();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //품목 직접 입력 기능 함수 호출
                            selfFoodItemInsert(friIdx, foodName, foodNum, foodExp, foodRegistrant);

                            if(i == adapter.getItemCount() - 1){ //마지막 값 까지 추가되면 팝업창 닫기
                                mSelfAddDialog.dismiss();
                                mAddItemDialog.dismiss();

                                mHome.foodAdapter.notifyDataSetChanged(); // UI 바로 추가
                                //home.foodItemSetting(); //DB 품목 리스트 재설정 TODO: null 존재시 앱 종료
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "추가할 품목이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //냉장고 리스트 팝업창
    public void showFridgeListDialog(){

        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mFridgeListDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mFridgeListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFridgeListDialog.setCanceledOnTouchOutside(false);

        mFridgeListDialog.show(); //Dialog 띄우기

        mFridgeListCancelBtn = mFridgeListDialog.findViewById(R.id.fridgeListCancelBtn); //냉장고 리스트 취소 버튼
        mFridgeListCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeListDialog.dismiss(); //Dialog 닫기
            }
        });

        mAddFridgeBtn = mFridgeListDialog.findViewById(R.id.addFridgeBtn); //냉장고 추가 버튼
        mAddFridgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeListDialog.dismiss();
                showFridgeAddDialog();
            }
        });

    }

    //냉장고 추가하기 팝업창
    public void showFridgeAddDialog(){
        // TODO: 프로젝트 병합 후 주석 해제
        addFridge = mFridgeAddDialog.findViewById(R.id.fridgeName);
        addFridge.setText(null);

        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mFridgeAddDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        mFridgeAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFridgeAddDialog.setCanceledOnTouchOutside(false);

        mFridgeAddDialog.show();

        // TODO: 프로젝트 병합 후 주석 해제
        //냉장고 추가하기 확인 버튼
        mFridgeNameAddBtn = mFridgeAddDialog.findViewById(R.id.fridgeNameAddBtn);
        mFridgeNameAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addFridgeName = addFridge.getText().toString();
                fridgeAdapter.addItem(new FridgeData(addFridgeName));
                recyclerView.setAdapter(fridgeAdapter);
                //mFridgeAddDialog.dismiss();
                //showFridgeListDialog(); //이전 다이얼로그 재시작

                //추가된 냉장고 db에 넣기, 나머지 DB 부분 처리
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            FridgeIndex(userIdx);
                            mFridgeAddDialog.dismiss();
                            showFridgeListDialog(); //이전 다이얼로그 재시작
                        }catch (Exception e){

                        }
                    }
                };
                AddFridgeNameRequest addFridgeNameRequest = new AddFridgeNameRequest(addFridgeName, userIdx, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(addFridgeNameRequest);
            }
        });

        //냉장고 추가하기 취소 버튼
        mFridgeAddCancelBtn = mFridgeAddDialog.findViewById(R.id.fridgeAddCancelBtn);
        mFridgeAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeAddDialog.dismiss();
                showFridgeListDialog(); //이전 다이얼로그 재시작

            }
        });
    }

    // TODO: 프로젝트 병합 후 주석 해제
    //냉장고 세팅 인덱스 (fridge_setting)
    public void FridgeIndex(final String userIDX) {
        String URL = "http://3.37.119.236:80/fridge/fridge.php/?userIDX="+userIDX; //local 경로
        fridgeAdapter.indexClear();
        user.MyFridgeClear();
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                fridgeIdx.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        int friIdx = jsonObject.getInt("friIdx");
                        String authority = jsonObject.getString("friAuth");

                        if (authority.equals("admin")){
                            //나의 냉장고 (권한 위임 후 객체 데이터 삭제)
                            user.MyFridge(friIdx);
                        }
                        fridgeAdapter.addFridgeIDX(friIdx);
                    }
                    fridgeName(fridgeAdapter.getFridgeIDX());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "Error " + error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //냉장고 리스트 이름 가져오기
    public void fridgeName(ArrayList<Integer> fridgeIdx){
        int length = fridgeIdx.size();

        for(int i = 0; i < length; i++){
            String index = fridgeIdx.get(i).toString();
            fridgeAdapter.NameClear();
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        fridgeName = jsonObject.getString("friName");
                        System.out.println(fridgeName);
                        fridgeAdapter.addItem(new FridgeData(fridgeName));
                        recyclerView.setAdapter(fridgeAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            fridgeNameRequest fridgeName = new fridgeNameRequest(index, responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(fridgeName);
        }
    }

    public void getUser(){
        //DB에서 회원 데이터 받아오기
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //false면 nick이 있다는 말
                    user.setNickname(jsonObject.getString(("userNickname")));
                    user.setPassword(jsonObject.getString("userPassword"));
                    user.setDbImage(jsonObject.getString("userImg"));
                    user.setIndex(jsonObject.getString("userIDX"));
                    userIdx = jsonObject.getString("userIDX");
                    FridgeIndex(user.getIndex());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        snsRequest snsRequest = new snsRequest(type, email, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(snsRequest);
    }


    //품목 data 추가
    public void selfFoodItemInsert(String friIdx, String foodName, String foodNum, String foodExp, String foodRegistrant){
        String URL = "http://3.37.119.236:80/food/insertFood.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "foodItemInsert() onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> requestedParams = new HashMap<>();
                requestedParams.put("friIdx", friIdx);
                requestedParams.put("foodName", foodName);
                requestedParams.put("foodNum", foodNum);
                requestedParams.put("foodExp", foodExp);
                requestedParams.put("foodRegistrant", foodRegistrant);

                return requestedParams;
            }

        };

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        //요청큐에 요청 객체 생성
        requestQueue.add(stringRequest);

    }

    public void takePhoto(){
        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, "org.techtown.capture.intent.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_RESULT_CODE);
    }

    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(getExternalCacheDir(), filename);

        return outFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case CAMERA_RESULT_CODE:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    inputImage = InputImage.fromBitmap(bitmap, 0);
                    getText(inputImage);
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            case ALBUM_RESULT_CODE:
                Uri uri = data.getData();
                //url bitmap 으로 전환
                try {
                    testImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                inputImage = InputImage.fromBitmap(testImage, 0);
                getText(inputImage);
                break;
        }
    }

    //text에서 한글 뽑아내기
    public static String getOnlyKor(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null && str.length()!=0){
            Pattern p = Pattern.compile("[가-힣]");
            Matcher m = p.matcher(str);
            while (m.find()) {
                sb.append(m.group());
            }
        }
        return sb.toString();
    }

    public void getText(InputImage image){
        System.out.println("텍스트 인식 시작");
        TextRecognizer recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(@NonNull @NotNull Text visionText) {
                                for(Text.TextBlock block:visionText.getTextBlocks()){
                                    text = block.getText();
                                    for(Text.Line line : block.getLines()) {
                                        String lineText = line.getText();
                                        System.out.println(getOnlyKor(lineText));
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                System.out.println("이미지 인식 실패");
                            }
                        });
    }
}