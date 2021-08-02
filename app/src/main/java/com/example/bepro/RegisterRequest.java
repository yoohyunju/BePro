package com.example.bepro;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //onResponse에 못들어갈 경우 IPv4 주소로 URL 변경하기, 서버 주소로 변경(수정)
    final static private String URL = "http://192.168.0.17:81/register.php";
    private Map<String, String> map;

    public RegisterRequest(String userNick, String userEmail, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userNick", userNick);
        map.put("userEmail", userEmail);
        map.put("userPassword", userPassword);

        //데이터 전송 확인, Logcat
        for(String key:map.keySet()){
            String value = map.get(key).toString();
            System.out.println(key+": " + map);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
