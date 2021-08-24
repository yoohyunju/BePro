package com.example.bepro;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bepro.home.FoodItems;
import com.example.bepro.home.HomeActivity;
import com.example.bepro.home.SelfAddItemAdapter;
import com.example.bepro.login.snsRequest;
import com.example.bepro.my_page.MyPageActivity;
import com.example.bepro.notice.NoticeActivity;
import com.example.bepro.recipe.RecipeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager; //앱 fragment에서 작업을 추가, 삭제, 교체하고 백 스택에 추가하는 클래스
    private FragmentTransaction transaction; //fragment 변경을 위한 트랜잭션(작업단위)

    private HomeActivity mHome;
    private RecipeActivity mRecipe;
    private MyPageActivity mMyPage;
    private NoticeActivity mNotice;
    private BottomNavigationView mNavView;

    private LinearLayout mFridgeListOpenBtn, mAddFridgeBtn, mSelfAddBtn;
    private Dialog mAddItemDialog, mFridgeListDialog, mFridgeAddDialog, mSelfAddDialog;
    private Button mAddCancelBtn, mFridgeListCancelBtn, mFridgeAddCancelBtn, mSelfAddCancelBtn, mSelfItemAddBtn;

    public String image, email, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavView = findViewById(R.id.navigation);
        mNavView.setOnItemSelectedListener(new ItemSelectedListener()); //BottomNavigationView에 이벤트 리스너 연결

        //로그인 데이터 받아오기
        Intent intent = getIntent();
        image = intent.getStringExtra("userImage");
        email = intent.getStringExtra("userEmail");
        type = intent.getStringExtra("userType");

        //fragment 객체 생성
        mHome = new HomeActivity();
        mRecipe = new RecipeActivity();
        mMyPage = new MyPageActivity();
        mNotice = new NoticeActivity();

        //마이페이지에 받아온 데이터 넣기
        mMyPage.imageUrl = image;
        mMyPage.userEmail = email;
        mMyPage.userType = type;

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

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction(); //트랜잭션 시작
        transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();

    }

    //BottomNavigationView 이벤트 리스너 구현
    private class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.home:
                    transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();
                    break;

                case R.id.recipe:
                    transaction.replace(R.id.frameLayout, mRecipe).commitAllowingStateLoss();
                    break;

                case R.id.add:
                    showItemAddDialog(); //Dialog 함수 호출
                    break;

                case R.id.myPage:
                    transaction.replace(R.id.frameLayout, mMyPage).commitAllowingStateLoss();
                    break;

                case R.id.notice:
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
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mSelfAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //기본 백그라운드를 투명으로 변경
        mSelfAddDialog.setCanceledOnTouchOutside(false); //창 바깥 부분 터치 닫기 설정 해제

        mSelfAddDialog.show();


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
                //TODO: 품목 카드뷰 추가
                //리사이클러뷰에 레이아웃 매니저 설정
                RecyclerView recyclerView = mSelfAddDialog.findViewById(R.id.addFoodRecyclerView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

                SelfAddItemAdapter adapter = new SelfAddItemAdapter();
                //임시 데이터
                adapter.addItem(new FoodItems("바나나", "2", 1));
                recyclerView.setAdapter(adapter);

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
        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mFridgeAddDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mFridgeAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFridgeAddDialog.setCanceledOnTouchOutside(false);

        mFridgeAddDialog.show();

        mFridgeAddCancelBtn = mFridgeAddDialog.findViewById(R.id.fridgeAddCancelBtn);
        mFridgeAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeAddDialog.dismiss();
                showFridgeListDialog(); //이전 다이얼로그 재시작

            }
        });
    }
}