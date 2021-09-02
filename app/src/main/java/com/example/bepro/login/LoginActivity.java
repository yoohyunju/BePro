package com.example.bepro.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bepro.MainActivity;
import com.example.bepro.R;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

//로그인
public class LoginActivity extends AppCompatActivity {
    OAuthLoginButton naver;
    Button login, create, forget, kakao;
    EditText email, pwd;
    CheckBox autologin, saveid;

    String userEmail, userPassword; //사용자가 입력
    String id, password, idSet, pwdSet; //아이디 저장, 자동로그인
    private SessionCallback sessionCallback; //카카오 로그인 세션
    private AlertDialog dialog; //알림
    boolean kakaologin, naverlogin; //카카오 자동로그인 체크, 네이버 로그인 성공 체크
    String userNICK, userEMAIL, userType, userPWD, userImage; //DB 데이터 전송
    OAuthLogin mOAuthLoginModule; //네이버 로그인 토큰 관리
    SharedPreferences userData; //네이버 로그인 데이터 저장공간
    public String nick; //별명 넘기기 (main으로)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

<<<<<<< Updated upstream
        //카카오 로그인 세션
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

=======
>>>>>>> Stashed changes
        naver = (OAuthLoginButton) findViewById(R.id.naver);
        kakao = (Button) findViewById(R.id.kakao);
        email = (EditText) findViewById(R.id.loginId);
        pwd = (EditText) findViewById(R.id.loginPwd);
        login = (Button) findViewById(R.id.login);
        forget = (Button) findViewById(R.id.findMembtn);
        create = (Button) findViewById(R.id.createMembtn);
        autologin = (CheckBox) findViewById(R.id.autologin);
        saveid = (CheckBox) findViewById(R.id.saveid);

<<<<<<< Updated upstream
        //카카오 로그인 버튼
        kakao.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });

=======
>>>>>>> Stashed changes
        //네이버 로그인 개발자 접속
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this
                ,"MDUjLCrrrlJEQ45AffDw"
                ,"5T0Mo1V63V"
                ,"냉장고 안에 무엇이"
        );

        //네이버 JSON 에서 가져온 USER 정보
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        userNICK = userData.getString("nickname", "") + "의 네이버";
        userEMAIL = userData.getString("email", "");
        userImage = userData.getString("profile_image", "");

        //네이버 로그인
        if (mOAuthLoginModule.getAccessToken(LoginActivity.this) != null) {
            //이미 로그인 된 상태 (자동 로그인) -> 단말 토큰이 있을 경우
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userImage", userImage);
            intent.putExtra("userEmail", userEMAIL);
            intent.putExtra("userType", "naver");
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "네이버 자동 로그인", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            //로그인 한 적이 없는 경우 (DB 세팅, 로그인)
            naver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("HandlerLeak")
                    OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                        @Override
                        public void run(boolean success) {
                            if (success) {
                                //네이버 핸들러 (로그인, 사용자 데이터 호출)
                                NaverHandler naver = new NaverHandler(LoginActivity.this, mOAuthLoginModule, LoginActivity.this);
                                naver.run(true);
                                Toast.makeText(getApplicationContext(), "네이버 로그인 완료", Toast.LENGTH_SHORT).show();
                                naverLogin(); //데이터 DB 전송
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("네이버 로그인에 실패하였습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        };
                    };
                    mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
                }
            });
        }

        PrefsHelper.init(getApplicationContext());

        //단말 파일 자동 로그인
        id = PrefsHelper.read("userEmail", "");
        password = PrefsHelper.read("userPassword", "");
        nick = PrefsHelper.read("userNick", "");
        //단말 파일 아이디 저장
        idSet = PrefsHelper.read("saveID", "");
        pwdSet = PrefsHelper.read("savePWD", "");

        //자동 로그인 (checked)
        if(id != "" && password != ""){
            userEmail = id;
            userPassword = password;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userType", "normal");
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "자동 로그인되었습니다.", Toast.LENGTH_SHORT).show();
        }

        //아이디 저장 (checked)
        if(idSet != "" && pwdSet != ""){
            email.setText(idSet);
            pwd.setText(pwdSet);
        }

<<<<<<< Updated upstream
=======
        //카카오 로그인 세션
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        //카카오 로그인 버튼
        kakao.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });

>>>>>>> Stashed changes
        //카카오 자동 로그인 (단말 토큰_kakaologin)
        kakaologin = Session.getCurrentSession().checkAndImplicitOpen();
        if(kakaologin){
            Toast.makeText(getApplicationContext(), "카카오톡 자동 로그인", Toast.LENGTH_SHORT).show();
            Session.getCurrentSession().checkAndImplicitOpen();
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

                //아이디 비번 저장 체크
                if(saveid.isChecked()){
                    PrefsHelper.write("saveID", userEmail);
                    PrefsHelper.write("savePWD", userPassword);
                }else if (saveid.isChecked() && PrefsHelper.read("saveID", "") == ""){
                    PrefsHelper.remove("saveID");
                    PrefsHelper.remove("savePWD");
                }

                //로그인 리스너
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                nick = jsonObject.getString("userNickname");

                                //자동 로그인 체크
                                if(autologin.isChecked()){
                                    PrefsHelper.write("userEmail", userEmail);
                                    PrefsHelper.write("userPassword", userPassword);
                                    PrefsHelper.write("userNick", nick);
                                }

                                Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userEmail", userEmail);
                                intent.putExtra("userType", "normal");
                                startActivity(intent);
                            }else{
                                if(userEmail.isEmpty() || userPassword.isEmpty()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    dialog = builder.setMessage("아이디 또는 비밀번호를 입력해주세요.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    dialog = builder.setMessage("아이디 또는 비밀번호가 틀렸습니다.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    email.setText(null);
                                    pwd.setText(null);
                                }
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
                Intent intent = new Intent(LoginActivity.this, FindMemActivity.class);
                startActivity(intent);
            }
        });
    }

    //결과값 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    //다른 로그인 API를 같이 사용하는 경우 제거를 안하면 로그아웃 오류 발생
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
        //현재 액티비티 제거 시 콜백도 같이 제거
    }

    //카카오 로그인 세션
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                //로그인에 실패했을 때, 인터넷 연결이 불안정할 때때
               @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("네트워크 연결이 불안정합니다. 다시 시도해주세요.").setNegativeButton("확인", null).create();
                        dialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인 도중 오류가 발생했습니다.").setNegativeButton("확인", null).create();
                        dialog.show();
                    }
                }

                //로그인 도중 세션이 닫혔을 때
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("세션이 닫혔습니다. 다시 시도해 주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                }

                //로그인에 성공했을 때, MeV2Response에 로그인한 유저의 정보를 담고 있음.
                @Override
                public void onSuccess(MeV2Response result) {
                    userNICK = result.getNickname() + "의 카카오";
                    userEMAIL = result.getKakaoAccount().getEmail();
                    userType = "kakao";
                    userPWD = "kakao";

                    //자동로그인이 아닐 경우
                    if(!kakaologin){
                        //데이터 넣기
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                            }
                                        };
                                        RegisterRequest registerRequest1 = new RegisterRequest(userNICK, userEMAIL, userPWD, userType, responseListener1);
                                        RequestQueue queue1 = Volley.newRequestQueue(LoginActivity.this);
                                        queue1.add(registerRequest1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        snsRequest registerRequest = new snsRequest(userType, userEMAIL, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        queue.add(registerRequest);
                        Toast.makeText(getApplicationContext(), "카카오톡 로그인 완료", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userImage", result.getProfileImagePath());
                    intent.putExtra("userType", "kakao");
                    intent.putExtra("userEmail", userEMAIL);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("카카오 로그인에 실패하였습니다.").setNegativeButton("확인", null).create();
            dialog.show();
        }
    }

    //네이버 로그인 성공
    public void naverLogin() {
        userType = "naver";
        userPWD = "naver";
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        };
                        RegisterRequest registerRequest1 = new RegisterRequest(userNICK, userEMAIL, userPWD, userType, responseListener1);
                        RequestQueue queue1 = Volley.newRequestQueue(LoginActivity.this);
                        queue1.add(registerRequest1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        snsRequest registerRequest = new snsRequest(userType, userEMAIL, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(registerRequest);
    }

    //뒤로가기 버튼 막아두기
    @Override
    public void onBackPressed(){

    }
}