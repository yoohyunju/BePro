package com.example.bepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    //로그인 버튼 클릭 시 전환되는 로그인 화면
    Button login, create, forget;
    EditText id, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        id = (EditText) findViewById(R.id.loginId);
        pwd = (EditText) findViewById(R.id.loginPwd);

        loginSetting();
        createAcSetting();
        forgetAcSetting();
    }

    public void loginSetting(){
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = id.getText().toString();
                String userPassword = pwd.getText().toString();

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
                                id.setText(null);
                                pwd.setText(null);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    public void createAcSetting(){
        create = (Button) findViewById(R.id.createMembtn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입
                Intent intent = new Intent(LoginActivity.this, CreateMemActivity.class);
                startActivity(intent);
            }
        });
    }

    public void forgetAcSetting(){
        forget = (Button) findViewById(R.id.findMembtn);
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