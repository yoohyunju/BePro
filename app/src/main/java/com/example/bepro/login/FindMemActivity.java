package com.example.bepro.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.bepro.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

//비밀번호 찾기
public class FindMemActivity extends AppCompatActivity {
    EditText inputEmail;
    Button sendEmail;

    private AlertDialog dialog; //알림
    static int value; //이메일 전송
    int mailSend = 0; //이메일 전송
    MainHandler mainHandler; //이메일 전송
    String password; //이메일로 전송될 사용자 비번
    String GmailCode; //사용자 입력 인증 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_mem_page);

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        sendEmail = (Button) findViewById(R.id.sendEmail);

        sendEmailSetting();
    }

    //이메일 전송 버튼
    public void sendEmailSetting() {
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = inputEmail.getText().toString();

                //입력을 안했을 경우
                if (userEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindMemActivity.this);
                    dialog = builder.setMessage("이메일을 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            //이메일 중복검사 & 형식 검사 조건문
                            if (!success) {
                                if (isValidEmail(userEmail)) {
                                    MailThread mailThread = new MailThread();
                                    mailThread.start();
                                    Toast.makeText(getApplicationContext(), "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    password = jsonObject.getString("userPassword");
                                    mainHandler = new MainHandler();

                                    Intent intent = new Intent(FindMemActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                    if (mailSend == 0) {
                                        value = 180;
                                        BackgroundThread backgroundThread = new BackgroundThread();
                                        backgroundThread.start();
                                        mailSend += 1;
                                    } else {
                                        value = 180;
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FindMemActivity.this);
                                    dialog = builder.setMessage("유효하지 않은 이메일 형식입니다.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    inputEmail.setText(null);
                                    return;
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindMemActivity.this);
                                dialog = builder.setMessage("존재하지 않은 이메일입니다. 회원가입을 해주세요!").setNegativeButton("확인", null).create();
                                dialog.show();
                                inputEmail.setText(null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindMemActivity.this);
                queue.add(validateRequest);
            }
        });
    }

    //이메일 형식 확인
    public boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    //메일 스레드 (전송)
    class MailThread extends Thread {
        public void run() {
            GMailSender gMailSender = new GMailSender("bepro.emailac@gmail.com", "bowaaaougekvqjhh");
            try {
                gMailSender.sendMail("냉장고엔 무엇이? 비밀번호 찾기", "많이 놀라셨죠? 회원님의 비밀번호는 " + password + " 입니당!", inputEmail.getText().toString());
            } catch (SendFailedException e) {

            } catch (MessagingException e) {
                System.out.println("인터넷 문제");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //메일 스레드
    class BackgroundThread extends Thread {
        public void run() {
            while (true) {
                value = 1;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                Message message = mainHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                message.setData(bundle);

                mainHandler.sendMessage(message);
                if (value <= 0) {
                    GmailCode = "";
                    break;
                }
            }
        }
    }

    //메일 핸들러(분/초 카운트 - 인증코드 입력)
    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message massage) {
            super.handleMessage(massage);
            Bundle bundle = massage.getData();
        }
    }
}