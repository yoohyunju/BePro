package com.example.bepro.notice;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bepro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NoticeActivity extends Fragment {
    ListView noticeListView;
    NoticeAdapter noticeAdapter;
    ArrayList<NoticeItems> noticeItems = new ArrayList<>();
    private Dialog mNoticeDialog;
    TextView noticeDetailCategory, noticeDetailTitle, noticeDetailContent;
    Button mNoticeConfirmBtn;
    int noticeIdx;
    String noticeCategory, noticeTitle, noticeContent, noticeDate;

    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View noticeView = inflater.inflate(R.layout.notice, container, false);

        noticeAdapter = new NoticeAdapter(getContext(), noticeItems);
        noticeListView = noticeView.findViewById(R.id.noticeListView);
        noticeListView.setAdapter(noticeAdapter);

        noticeItemSetting();
        //noticeAdapter.addItem("점검", "점검 공지입니다.", "2021-10-07");

        //공지 상세보기기 팝업
       mNoticeDialog = new Dialog(getContext());
       mNoticeDialog.setContentView(R.layout.notice_popup);

       noticeDetailCategory = mNoticeDialog.findViewById(R.id.noticeDetailCategory);
       noticeDetailTitle = mNoticeDialog.findViewById(R.id.noticeDetailTitle);
       noticeDetailContent = mNoticeDialog.findViewById(R.id.noticeDetailContent);


        //공지 글 클릭시
        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //클릭된 공지 팝업창 뜸
                NoticeItems item = (NoticeItems) noticeAdapter.getItem(position); //아이템 클릭 시 어댑터에서 해당 아이템 객체 가져옴
                showNoticeDetailDialog(item, position);
            }
        });

        return noticeView;
    }

    //공지 데이터 셋팅 함수
    public void noticeItemSetting() {
        String URL = "http://3.37.119.236:80/notice/selectNotice.php";

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            //volley 라이브러리의 GET 방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않아 POST 방식 사용

            @Override
            public void onResponse(JSONArray response) {
                noticeItems.clear(); //반복 추가 되지 않도록 매번 clear 해야함

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        //JSON data key 값 참조한 value 값을 변수에 담음
                        //TODO: DB에 null 값 있으면 앱이 종료되는 오류 발생, null 체크 추가하기
                        noticeIdx = Integer.parseInt(jsonObject.getString("noticeIdx")); //공지 인덱스
                        noticeCategory = jsonObject.getString("noticeCategory"); //공지 종류
                        noticeTitle = jsonObject.getString("noticeTitle"); //공지 제목
                        noticeContent = jsonObject.getString("noticeContent"); //공지 내용
                        noticeDate = jsonObject.getString("noticeDate"); //공지 등록일

                        //System.out.println("로그로그: " + noticeIdx + noticeCategory + noticeTitle + noticeDate);

                        //ArrayList에 Data add
                        noticeItems.add(new NoticeItems(noticeIdx, noticeCategory, noticeTitle, noticeContent, noticeDate));
                    }
                    noticeAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"품목 출력 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "foodItemSetting() onErrorResponse 오류 메시지: " + String.valueOf(error));
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);

    }

    public void showNoticeDetailDialog(NoticeItems item, int position){
        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = mNoticeDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mNoticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //기본 백그라운드를 투명으로 변경
        mNoticeDialog.setCanceledOnTouchOutside(false); //창 바깥 부분 터치 닫기 설정 해제

        mNoticeDialog.show(); //Dialog 띄우기

        //JSON data를 각 오브젝트에 셋팅
        noticeDetailCategory.setText("[" + item.getNoticeCategory() + "]");
        noticeDetailTitle.setText(item.getNoticeTitle());
        noticeDetailContent.setText(item.getNoticeContent());

        //품목 정보 취소 버튼
        mNoticeConfirmBtn = mNoticeDialog.findViewById(R.id.noticeConfirmBtn);
        mNoticeConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoticeDialog.dismiss(); //다이얼로그 닫기
            }
        });
    }

}

