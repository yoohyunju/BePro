package com.example.bepro.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//이메일 존재하는지
public class ValidateRequest  extends StringRequest {
    final static private String URL = "http://192.168.0.17:81/validate.php";
    private Map<String, String> map;

    public ValidateRequest(String UserEmail, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userEmail", UserEmail);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
