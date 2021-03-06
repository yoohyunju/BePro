package com.example.bepro.my_page;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//계정 삭제
public class DeleteRequest extends StringRequest{
    final static private String URL = "http://3.37.119.236:80/login/delete.php";
    private Map<String, String> map;

    public DeleteRequest(String userIDX, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userIDX", userIDX);

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
