package com.example.bepro.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//아이디 - 비번 존재하는지
public class LoginRequest extends StringRequest {
    final static private String URL = "http://3.37.119.236:80/login/login.php";
    private Map<String, String> map;

    public LoginRequest(String userEmail, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userEmail", userEmail);
        map.put("userPassword", userPassword);

        //데이터 전송 확인, Logcat
        /*for(String key:map.keySet()){
            String value = map.get(key).toString();
            System.out.println(key+": " + map);
        }*/
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
