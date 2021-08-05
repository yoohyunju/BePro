package com.example.bepro;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class CreateMemActivity extends AppCompatActivity {
    EditText nick, pwd, checkpwd, email, code;
    Button check, register, codecheck, nickCheck;

    String GmailCode;
    static int value;
    int mailSend = 0;
    MainHandler mainHandler;

    private boolean Nickvalidate = false; //별명 중복 확인
    private boolean Emailvalidate = false; //이메일 중복 체크
    private boolean EmailCheck = false; //회원가입 완료

    private AlertDialog dialog;
    private CountDownTimer countDownTimer;
    private int time = 59;
    int min = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mem_page);

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
        nickCheck = findViewById(R.id.nickCheck);

        nickSetting();
        inputSetting();
        buttonSetting();
    }

    //별명 중복 확인 버튼
    public void nickSetting(){
        nickCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNick = nick.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "사용 가능한 별명 입니다.", Toast.LENGTH_SHORT).show();
                                nick.setEnabled(false);
                                Nickvalidate = true;
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                                dialog = builder.setMessage("이미 등록된 별명입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                                nick.setText(null);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                NickRequest nickRequest = new NickRequest(userNick, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateMemActivity.this);
                queue.add(nickRequest);
            }
        });
    }

    //이메일 중복 체크 버튼
    public void inputSetting(){
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();

                //입력을 안했을 경우
                if(userEmail.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                    dialog = builder.setMessage("이메일을 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            //이메일 중복검사 & 형식 검사 조건문
                            if(success){
                                if(isValidEmail(userEmail)){
                                    Toast.makeText(getApplicationContext(), "사용할 수 있는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                    email.setEnabled(false);
                                    Emailvalidate = true;
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

                                    Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                                    code.setVisibility(View.VISIBLE);
                                    codecheck.setVisibility(View.VISIBLE);
                                    mainHandler = new MainHandler();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                                    dialog = builder.setMessage("유효하지 않은 이메일 형식입니다.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    email.setText(null);
                                    return;
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                                dialog = builder.setMessage("이미 존재하는 이메일입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                                email.setText(null);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateMemActivity.this);
                queue.add(validateRequest);

            }
        });

        codecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().equals(GmailCode)){
                    Toast.makeText(getApplicationContext(), "인증되었습니다.", Toast.LENGTH_SHORT).show();
                    EmailCheck = true;
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                    dialog = builder.setMessage("인증번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                }
            }
        });
    }

    //회원가입 완료 버튼
    public void buttonSetting(){
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userNICK = nick.getText().toString();
                String userPWD = pwd.getText().toString();
                String userEMAIL = email.getText().toString();
                String PWDCheck = checkpwd.getText().toString();

                if(!Nickvalidate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                    dialog = builder.setMessage("별명 입력을 확인해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                if(!Emailvalidate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                    dialog = builder.setMessage("이메일 입력을 확인해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                if(!EmailCheck){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                    dialog = builder.setMessage("인증코드 입력을 확인해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if(userEMAIL.equals("") || userPWD.equals("") || userNICK.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //중복, 필수 입력 검사, 이메일 인증? 추가(수정)
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(userPWD.equals(PWDCheck)){
                                if(success){
                                    Toast.makeText(getApplicationContext(), "회원 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateMemActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "회원 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateMemActivity.this);
                                dialog = builder.setMessage("비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                                return;
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
    }

    //이메일 형식 확인
    public static boolean isValidEmail(String email) {
        boolean err = false; String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) { err = true; }
        return err;
    }

    //메일 스레드 (전송)
    class MailThread extends Thread{
        public void run(){
            GMailSender gMailSender = new GMailSender("bepro.emailac@gmail.com", "bowaaaougekvqjhh");

            GmailCode = gMailSender.getEmailCode();
            try{
                gMailSender.sendMail("냉장고엔 무엇이? 회원가입 이메일 인증", "환영합니다! 인증 코드는 "+GmailCode+" 입니당!", email.getText().toString());
            }catch (SendFailedException e){

            }catch (MessagingException e){
                System.out.println("인터넷 문제");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //메일 스레드
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

    //메일 핸들러(분/초 카운트 - 인증코드 입력)
    class MainHandler extends Handler{
        @Override
        public void handleMessage(Message massage){
            super.handleMessage(massage);
            Bundle bundle = massage.getData();

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if(time<10){
                        code.setHint("0" + min +" : 0" + time);
                        time--;
                    }else{
                        code.setHint("0" + min +" : " + time);
                        time--;
                    }
                    if(time < 0){
                        min--;
                        time = 59;
                    }
                    if(min==0 && time==0){
                        CreateMemActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(CreateMemActivity.this, "인증번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateMemActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            };
            timer.schedule(task, 0000);
        }
    }
}