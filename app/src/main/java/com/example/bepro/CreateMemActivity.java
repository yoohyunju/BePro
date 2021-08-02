package com.example.bepro;

import android.os.Bundle;
<<<<<<< Updated upstream

import androidx.appcompat.app.AppCompatActivity;

public class CreateMemActivity extends AppCompatActivity {

=======
import android.os.Handler;
import android.os.Message;
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

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class CreateMemActivity extends AppCompatActivity {
    EditText nick, pwd, checkpwd, email, code;
    Button check, register, codecheck;
    String GmailCode;
    static int value;
    int mailSend = 0;
    MainHandler mainHandler;
>>>>>>> Stashed changes
    //sns 연동, db 연동
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mem_page);
<<<<<<< Updated upstream
=======

        nick = findViewById(R.id.memNick);
        email = findViewById(R.id.memEmail);
        check = findViewById(R.id.EmailCheck);
        codecheck = findViewById(R.id.codeCheck);
        codecheck.setVisibility(View.GONE);
        code = findViewById(R.id.code);
        code.setVisibility(View.GONE);
        pwd = findViewById(R.id.memPwd);
        checkpwd = findViewById(R.id.checkPwd);
        register = findViewById(R.id.createMembtn);

        inputSetting();
        buttonSetting();
    }

    //이메일 중복 체크 버튼
    public void inputSetting(){
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailThread mailThread = new MailThread();
                mailThread.start();

                if(mailSend == 0){
                    value = 180;
                    BackgroundThread backgroundThread = new BackgroundThread();
                    backgroundThread.start();
                    mailSend += 1;
                }else{
                    value = 180;
                }
                Toast.makeText(getApplicationContext(), "인증번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                code.setVisibility(View.VISIBLE);
                codecheck.setVisibility(View.VISIBLE);

                mainHandler = new MainHandler();
            }
        });

        codecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().equals(GmailCode)){
                    Toast.makeText(getApplicationContext(), "인증되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //입력 완료 후 회원가입 버튼 세팅
    public void buttonSetting(){
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userNICK = nick.getText().toString();
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
                RegisterRequest registerRequest = new RegisterRequest(userNICK, userEMAIL, userPWD, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateMemActivity.this);
                queue.add(registerRequest);
            }
        });
>>>>>>> Stashed changes
    }

    class MailThread extends Thread{
        public void run(){
            GMailSender gMailSender = new GMailSender("bepro.emailac@gmail.com", "bowaaaougekvqjhh");

            GmailCode = gMailSender.getEmailCode();
            try{
                gMailSender.sendMail("냉장고엔 무엇이? 회원가입 이메일 인증", "인증 코드는 "+GmailCode+" 입니다!", email.getText().toString());
            }catch (SendFailedException e){

            }catch (MessagingException e){
                System.out.println("인터넷 문제");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class BackgroundThread extends Thread{
        public void run(){
            while(true){
                value = 1;
                try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }
                Message message = mainHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                message.setData(bundle);

                mainHandler.sendMessage(message);
                if(value<=0){
                    GmailCode="";
                    break;
                }
            }
        }
    }

    class MainHandler extends Handler{
        @Override
        public void handleMessage(Message massage){
            super.handleMessage(massage);
            int min, sec;

            Bundle bundle = massage.getData();
            int value = bundle.getInt("value");

            min = value/60;
            sec = value%60;

            if(sec<10){
                code.setHint("0"+min+" : 0"+sec);
            }else{
                code.setHint("0"+min+" : 0"+sec);
            }
        }
    }
}