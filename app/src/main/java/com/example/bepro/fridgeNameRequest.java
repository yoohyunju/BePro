package com.example.bepro;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class fridgeNameRequest extends StringRequest{
    final static private String URL = "http://3.37.119.236:80/fridge/fridgeName.php";
    private Map<String, String> map;

    public fridgeNameRequest(String index, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("index", index);

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