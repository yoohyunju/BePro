package com.example.bepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager; //앱 fragment에서 작업을 추가, 삭제, 교체하고 백 스택에 추가하는 클래스
    private FragmentTransaction transaction; //fragment 변경을 위한 트랜잭션(작업단위)
    private HomeActivity mHome;
    private RecipeActivity mRecipe;
    private MyPageActivity mMyPage;
    private NoticeActivity mNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView mNavView = findViewById(R.id.navigation);
        mNavView.setOnNavigationItemSelectedListener(new ItemSelectedListener()); //BottomNavigationView에 이벤트 리스너 연결

        mHome = new HomeActivity(); //fragment 객체 생성
        mRecipe = new RecipeActivity();
        mMyPage = new MyPageActivity();
        mNotice = new NoticeActivity();

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction(); //트랜잭션 시작
        transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();


    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.home:
                    transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();
                    break;
                case R.id.recipe:
                    transaction.replace(R.id.frameLayout, mRecipe).commitAllowingStateLoss();
                    break;
                case R.id.add:
                    //기능추가
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
}