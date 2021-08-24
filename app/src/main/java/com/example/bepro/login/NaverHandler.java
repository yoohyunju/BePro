package com.example.bepro.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.bepro.MainActivity;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NaverHandler extends OAuthLoginHandler {

    private Context mContext;
    private LoginActivity activity;
    private OAuthLogin mOAuthLoginModule;


    NaverHandler(Context mContext, OAuthLogin mOAuthLoginModule, LoginActivity activity) {
        this.mContext = mContext;
        this.mOAuthLoginModule = mOAuthLoginModule;
        this.activity = activity;
    }

    @Override
    public void run(boolean success) {
        if (success) {
            final String accessToken = mOAuthLoginModule.getAccessToken(mContext);
            // 유저 정보
            ProfileTask task = new ProfileTask();
            // 유저 정보 가져오기
            task.execute(accessToken);
        } else {
            String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
            Toast.makeText(mContext, "errorCode:" + errorCode
                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
        }
    }

    //네이버 json 파일 접근 -> 사용자 정보 get
    class ProfileTask extends AsyncTask<String, Void, String> {
        String result;
        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            //result 값은 JSONObject 형태로 넘어옵니다.
            return result;
        }

        //가져온 데이터 activity 전송
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(result);
                if(object.getString("resultcode").equals("00")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        SharedPreferences.Editor editor = activity.userData.edit();
                        editor.putString("email", jsonObject.getString("email"));
                        editor.putString("nickname", jsonObject.getString("nickname"));
                        editor.putString("profile_image", jsonObject.getString("profile_image"));
                        editor.apply();

                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra("userImage", jsonObject.getString("profile_image"));
                        intent.putExtra("userEmail", jsonObject.getString("email"));
                        intent.putExtra("userType", "naver");
                        activity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
