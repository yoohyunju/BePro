package com.example.bepro.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//SNS 타입 - EMAIL 존재하는지
public class snsRequest extends StringRequest {
    final static private String URL = "http://3.37.119.236:80/login/sns.php";
    private Map<String, String> map;

    public snsRequest(String snsName, String UserEmail, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("snsName", snsName);
        map.put("userEmail", UserEmail);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}