package com.example.bepro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button login, create, forget;
    EditText email, pwd;
    CheckBox autologin, saveid;
    String userEmail, userPassword, id, password, idSet, pwdSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        email = (EditText) findViewById(R.id.loginId);
        pwd = (EditText) findViewById(R.id.loginPwd);
        login = (Button) findViewById(R.id.login);
        forget = (Button) findViewById(R.id.findMembtn);
        create = (Button) findViewById(R.id.createMembtn);
        autologin = (CheckBox) findViewById(R.id.autologin);
        saveid = (CheckBox) findViewById(R.id.saveid);

        PrefsHelper.init(getApplicationContext());
        //PrefsHelper.clear(); // 자동로그인, 아이디 저장 파일 비우기(테스트용)

        //단말 파일 key, value 값 가져오기
        id = PrefsHelper.read("userEmail", "");
        password = PrefsHelper.read("userPassword", "");
        idSet = PrefsHelper.read("saveID", "");
        pwdSet = PrefsHelper.read("savePWD", "");

        //자동 로그인 (checked)
        if(id != "" && password != ""){
            userEmail = id;
            userPassword = password;
            Toast.makeText(getApplicationContext(), "자동 로그인되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        //아이디 저장 (checked)
        if(idSet != "" && pwdSet != ""){
            email.setText(idSet);
            pwd.setText(pwdSet);
        }

        loginSetting();
        createAcSetting();
        forgetAcSetting();
    }

    //로그인 버튼
    public void loginSetting(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPassword = pwd.getText().toString();

                //자동 로그인 체크가 되면 파일에 put
                if(autologin.isChecked()){
                    PrefsHelper.write("userEmail", userEmail);
                    PrefsHelper.write("userPassword", userPassword);
                }

                //아이디 비번 저장 체크 확인
                if(saveid.isChecked()){
                    PrefsHelper.write("saveID", userEmail);
                    PrefsHelper.write("savePWD", userPassword);
                }else if (saveid.isChecked() && PrefsHelper.read("saveID", "") == ""){
                    PrefsHelper.remove("saveID");
                    PrefsHelper.remove("savePWD");
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "로그인에 실패했습니다..", Toast.LENGTH_SHORT).show();
                                email.setText(null);
                                pwd.setText(null);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userEmail, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    //회원가입 버튼
    public void createAcSetting(){
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입
                Intent intent = new Intent(LoginActivity.this, CreateMemActivity.class);
                startActivity(intent);
            }
        });
    }

    //아이디 비번 찾기 버튼
    public void forgetAcSetting(){
        forget.setOnClickListener(new View.OnClickListener() {
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