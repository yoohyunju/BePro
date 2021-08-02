package com.example.bepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    //로그인 버튼 클릭 시 전환되는 로그인 화면
<<<<<<< Updated upstream
    Button Button;
    EditText EditText;
=======
    Button login, create, forget;
    EditText email, pwd;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

<<<<<<< Updated upstream
        EditText = (EditText) findViewById(R.id.loginId);
        EditText = (EditText) findViewById(R.id.loginPwd);
=======
        email = (EditText) findViewById(R.id.loginId);
        pwd = (EditText) findViewById(R.id.loginPwd);
>>>>>>> Stashed changes

        loginSetting();
        createAcSetting();
        forgetAcSetting();
    }

    public void loginSetting(){
        Button = (Button) findViewById(R.id.login);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< Updated upstream
                //회원 정보 확인, 로그인 안내(수정)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
=======
                String userEmail = email.getText().toString();
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
>>>>>>> Stashed changes
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