package com.example.bepro.my_page;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest{
    final static private String URL = "http://192.168.0.17:81/delete.php";
    private Map<String, String> map;

    public DeleteRequest(String userType, String userEmail, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userType", userType);
        map.put("userEmail", userEmail);

        //데이터 전송 확인, Logcat
    /*for(String key:map.keySet()){
        String value = map.get(key).toString();
        System.out.println(key+": " + map);
    }*/
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
