package com.example.bepro.my_page;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//이미지 수정
public class ImageChangeRequest extends StringRequest{
    final static private String URL = "http://3.37.119.236:80/login/image.php";
    private Map<String, String> map;

    public ImageChangeRequest(String userImage, String userNick, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userImage", userImage);
        map.put("userNick", userNick);

        //데이터 전송 확인, Logcat
    for(String key:map.keySet()){
        System.out.println(key+": " + map);
    }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
