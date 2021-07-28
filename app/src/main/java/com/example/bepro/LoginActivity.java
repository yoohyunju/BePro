package com.example.bepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    //로그인 버튼 클릭 시 전환되는 로그인 화면
    Button Button;
    EditText EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        EditText = (EditText) findViewById(R.id.loginId);
        EditText = (EditText) findViewById(R.id.loginPwd);

        loginSetting();
        createAcSetting();
        forgetAcSetting();
    }

    public void loginSetting(){
        Button = (Button) findViewById(R.id.login);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 정보 확인, 로그인 안내(수정)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createAcSetting(){
        Button = (Button) findViewById(R.id.createMembtn);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입
                Intent intent = new Intent(LoginActivity.this, CreateMemActivity.class);
                startActivity(intent);
            }
        });
    }

    public void forgetAcSetting(){
        Button = (Button) findViewById(R.id.findMembtn);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 정보 찾기
                Intent intent = new Intent(LoginActivity.this, com.example.bepro.FindMemActivity.class);
                startActivity(intent);
            }
        });
    }

    //뒤로가기 버튼 막아두기
    @Override
    public void onBackPressed(){

    }
}