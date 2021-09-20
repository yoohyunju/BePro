package com.example.bepro;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetFridgeSettingRequest extends StringRequest {
    final static private String URL = "http://3.37.119.236:80/fridge/fridgeSetting.php";
    private Map<String, String> map;

    public GetFridgeSettingRequest(String name, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("name", name);

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
