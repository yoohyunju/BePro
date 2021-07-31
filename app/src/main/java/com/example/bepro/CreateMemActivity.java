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

public class CreateMemActivity extends AppCompatActivity {
    EditText id, pwd, email;
    Button register;
    //sns 연동, db 연동
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mem_page);

        id = findViewById(R.id.memId);
        pwd = findViewById(R.id.memPwd);
        email = findViewById(R.id.memEmail);
        register = findViewById(R.id.createMembtn);

        buttonSetting();
    }

    public void buttonSetting(){
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userID = id.getText().toString();
                String userPWD = pwd.getText().toString();
                String userEMAIL = email.getText().toString();

                //중복, 필수 입력 검사, 이메일 인증? 추가(수정)
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "회원 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateMemActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "회원 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userID, userPWD, userEMAIL, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateMemActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}