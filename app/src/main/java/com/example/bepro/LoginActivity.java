package com.example.bepro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;

import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    OAuthLoginButton naver;
    Button login, create, forget, kakao;
    EditText email, pwd;
    CheckBox autologin, saveid;
    String userEmail, userPassword, id, password, idSet, pwdSet;
    private SessionCallback sessionCallback;
    private AlertDialog dialog;
    boolean kakaologin, naverlogin;
    String userNICK, userEMAIL, userType, userPWD;
    OAuthLogin mOAuthLoginModule;
    SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        sessionCallback = new SessionCallback(); //초기화
        Session.getCurrentSession().addCallback(sessionCallback); //현재 세션에 콜백

        naver = (OAuthLoginButton) findViewById(R.id.naver);
        kakao = (Button) findViewById(R.id.kakao);
        email = (EditText) findViewById(R.id.loginId);
        pwd = (EditText) findViewById(R.id.loginPwd);
        login = (Button) findViewById(R.id.login);
        forget = (Button) findViewById(R.id.findMembtn);
        create = (Button) findViewById(R.id.createMembtn);
        autologin = (CheckBox) findViewById(R.id.autologin);
        saveid = (CheckBox) findViewById(R.id.saveid);

        //자동 로그인 (현재 앱에 토큰이 있다면 바로 로그인)
        /*kakaologin = Session.getCurrentSession().checkAndImplicitOpen();

        if(kakaologin){
            Toast.makeText(getApplicationContext(), "카카오톡 자동 로그인", Toast.LENGTH_SHORT).show();
            Session.getCurrentSession().checkAndImplicitOpen();
        }*/

        //카카오톡 토큰 삭제 (테스트용) -> 로그아웃 구현 시 사용
        /*UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.i("KAKAO_API", "로그아웃 완료");
                    }
                });
        */
        //카카오 로그인 버튼
        kakao.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });

        //네이버 sns 로그인 개발자 인증
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this
                ,getString(R.string.naver_client_id)
                ,getString(R.string.naver_client_secret)
                ,getString(R.string.app_name)
        );

        //네이버 토큰 삭제 -> 로그아웃
        //mOAuthLoginModule.logout(LoginActivity.this);

        if (mOAuthLoginModule.getAccessToken(LoginActivity.this) != null) {
            //이미 로그인 된 상태 (자동 로그인)
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                                NaverHandler naver = new NaverHandler(LoginActivity.this, mOAuthLoginModule, LoginActivity.this);
                                naver.run(true);

                                userData = getSharedPreferences("userData", MODE_PRIVATE);
                                userNICK = userData.getString("nickname", "");
                                userEMAIL = userData.getString("email", "");
                                System.out.println("낭낭" + userNICK + userEMAIL);
                                naverlogin = true;
                                Toast.makeText(getApplicationContext(), "네이버 로그인 완료", Toast.LENGTH_SHORT).show();
                                naverLogin();
                            } else {
                                String errorCode = mOAuthLoginModule
                                        .getLastErrorCode(LoginActivity.this).getCode();
                                String errorDesc = mOAuthLoginModule.getLastErrorDesc(LoginActivity.this);
                                Toast.makeText(LoginActivity.this, "errorCode:" + errorCode
                                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                            }
                        };
                    };
                    mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
                }
            });
        }

        PrefsHelper.init(getApplicationContext());
        //PrefsHelper.clear(); // 자동로그인, 아이디 저장 파일 비우기(테스트용)

        //단말 파일 key, value 값 가져오기
        id = PrefsHelper.read("userEmail", "");
        password = PrefsHelper.read("userPassword", "");
        idSet = PrefsHelper.read("saveID", "");
        pwdSet = PrefsHelper.read("savePWD", "");

        //System.out.println("저장된 xml: " + id + password + idSet + pwdSet);
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
                Intent intent = new Intent(LoginActivity.this, com.example.bepro.FindMemActivity.class);
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

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                //로그인에 실패했을 때, 인터넷 연결이 불안정할 때때
               @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                //로그인 도중 세션이 닫혔을 때
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                //로그인에 성공했을 때, MeV2Response에 로그인한 유저의 정보를 담고 있음.
                @Override
                public void onSuccess(MeV2Response result) {
                    userNICK = result.getNickname();
                    userEMAIL = result.getKakaoAccount().getEmail();
                    userType = "kakao";
                    userPWD = "kakao";

                    if(!kakaologin){
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        System.out.println("넣겠음");
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

                        //MainActivity에서 intent로 전달된 값을 변수로 저장해 사용 or DB에 데이터를 추가해야 하는지?
                        Toast.makeText(getApplicationContext(), "카카오톡 로그인 완료", Toast.LENGTH_SHORT).show();

                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void naverLogin() {
        userType = "naver";
        userPWD = "naver";
        if (naverlogin) {
            System.out.println(naverlogin);
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            System.out.println("넣겠음");
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
    }

    //뒤로가기 버튼 막아두기
    @Override
    public void onBackPressed(){

    }
}