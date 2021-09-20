package com.example.bepro.home;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelfAddItemRequest extends StringRequest {
    final static private String URL = "http://10.0.2.2/beProTest/insertFood.php";
    private Map<String, String> map;

    public SelfAddItemRequest(String foodName, String foodTotal, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("foodName", foodName);
        map.put("foodTotal", foodTotal);


        //데이터 전송 확인, Logcat
        /*
        for(String key:map.keySet()){
            String value = map.get(key).toString(); //TODO: NPE
            System.out.println(key+": " + map);
        }
         */
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
