package com.example.bepro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaSession2;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MyPageActivity extends Fragment implements View.OnClickListener {
    LinearLayout mPwcLayout, mEditLayout, mLogoutLayout, mDeleteLayout;
    Button mEditBtn, mLogoutBtn, mDeleteBtn, mPwcOkBtn, mEditOkBtn, mLgyBtn, mLgnBtn, mDlyBtn, mDlnBtn;
    CheckBox autologin;
    String[] items = {"1일전", "2일전", "3일전"};
    Switch mSwitch;
    Spinner mSpinner;
    EditText mEditTxt;
    ImageView image;
    TextView id, nick;
    private ArrayAdapter<String> adapter;
    ViewGroup rootView;
    OAuthLogin mOAuthLoginModule; //네이버 로그인 토큰 관리
    public String imageUrl;
    public String userEmail;
    public String userNick;

    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.my_page, container, false);
        super.onCreate(savedInstanceState);

        PrefsHelper.init(getActivity());

        //네이버 로그인 개발자 접속
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                getActivity()
                ,"MDUjLCrrrlJEQ45AffDw"
                ,"5T0Mo1V63V"
                ,"냉장고 안에 무엇이"
        );

        if (setObjectView()) {  // 오브젝트 찾기
            if (setObjectMethod()) {    // 오브젝트 함수 연결
                if (setInit()) {    // 데이터 초기화

                }
            }
        }

        image = (ImageView) rootView.findViewById(R.id.userImage);
        if(imageUrl == null) image.setImageResource(R.drawable.ic_baseline_person_outline_24);
        else new DownloadFilesTask().execute(imageUrl);


        id = (TextView) rootView.findViewById(R.id.Id);
        nick = (TextView) rootView.findViewById(R.id.Nick);
        id.setText(userEmail);
        nick.setText(userNick);


        mEditTxt = (EditText) rootView.findViewById(R.id.pwcPw);
        mEditTxt = (EditText) rootView.findViewById(R.id.editPw);
        mEditTxt = (EditText) rootView.findViewById(R.id.editNickname);
        mSwitch = rootView.findViewById(R.id.swNs);
        mSpinner = rootView.findViewById(R.id.spDay);
        autologin = (CheckBox) rootView.findViewById(R.id.autologin);
        mSwitch.setChecked(true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSpinner.setEnabled(true);
                } else {
                    mSpinner.setEnabled(false);
                }
            }
        });


        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(2);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            image.setImageBitmap(result);
        }
    }

    public boolean setInit() {
        boolean bSuc = false;

        try {
            bSuc = true;
        } catch (NullPointerException e) {
            bSuc = false;
        } finally {

        }
        return bSuc;
    }

    public boolean setObjectView() {
        boolean bSuc = false;

        try {
            mPwcLayout = (LinearLayout) rootView.findViewById(R.id.pwcLayout);
            mEditLayout = (LinearLayout) rootView.findViewById(R.id.editLayout);
            mLogoutLayout = (LinearLayout) rootView.findViewById(R.id.logoutLayout);
            mDeleteLayout = (LinearLayout) rootView.findViewById(R.id.deleteLayout);
            mEditBtn = (Button) rootView.findViewById(R.id.editBtn);
            mLogoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
            mDeleteBtn = (Button) rootView.findViewById(R.id.deleteBtn);
            mPwcOkBtn = (Button) rootView.findViewById(R.id.pwcOkBtn);
            mEditOkBtn = (Button) rootView.findViewById(R.id.editOkBtn);
            mLgyBtn = (Button) rootView.findViewById(R.id.lgyBtn);
            mLgnBtn = (Button) rootView.findViewById(R.id.lgnBtn);
            mDlyBtn = (Button) rootView.findViewById(R.id.dlyBtn);
            mDlnBtn = (Button) rootView.findViewById(R.id.dlnBtn);

            bSuc = true;
        } catch (NullPointerException e) {
            bSuc = false;
        } finally {

        }
        return bSuc;
    }

    public boolean setObjectMethod() {
        boolean bSuc = false;

        try {
            mPwcLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { return true; }
            });
            mEditLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { return true; }
            });
            mLogoutLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { return true; }
            });
            mDeleteLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { return true; }
            });
            mEditBtn.setOnClickListener(this);
            mLogoutBtn.setOnClickListener(this);
            mDeleteBtn.setOnClickListener(this);
            mPwcOkBtn.setOnClickListener(this);
            mEditOkBtn.setOnClickListener(this);
            mLgyBtn.setOnClickListener(this);
            mLgnBtn.setOnClickListener(this);
            mDlyBtn.setOnClickListener(this);
            mDlnBtn.setOnClickListener(this);

            bSuc = true;
        } catch (NullPointerException e) {
            bSuc = false;


        } finally {

        }
        return bSuc;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.editBtn:
                mPwcLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.pwcOkBtn:
                mPwcLayout.setVisibility(View.GONE);
                mEditLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.editOkBtn:
                mEditLayout.setVisibility(View.GONE);
                break;
            case R.id.logoutBtn:
                //로그아웃
                mLogoutLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.lgyBtn:
                //로그아웃 (예)
                mLogoutLayout.setVisibility(View.GONE);
                //자동로그인 단말 파일 삭제
                PrefsHelper.remove("userEmail");
                PrefsHelper.remove("userPassword");

                //카카오톡 단말 토큰 삭제 (테스트용) -> 로그아웃 구현 시 사용
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.i("KAKAO_API", "로그아웃 완료");
                            }
                        });

                //네이버 토큰 삭제(테스트용) -> 로그아웃 구현 시 사용
                mOAuthLoginModule.logout(getActivity());

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.lgnBtn:
                mLogoutLayout.setVisibility(View.GONE);
                break;
            case R.id.deleteBtn:
                //계정삭제
                mDeleteLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.dlyBtn:
                //계정삭제 (예)
                mDeleteLayout.setVisibility(View.GONE);

                PrefsHelper.clear();
                //카카오톡 단말 토큰 삭제 (테스트용) -> 로그아웃 구현 시 사용
                UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.i("KAKAO_API", "로그아웃 완료");
                    }
                });

                //네이버 토큰 삭제(테스트용) -> 로그아웃 구현 시 사용
                mOAuthLoginModule.logout(getActivity());

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getActivity(), "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                /*
                DB 데이터 삭제 Request
                DeleteRequest deleteRequest = new DeleteRequest(userType, userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(deleteRequest);

                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                */
                break;
            case R.id.dlnBtn:
                mDeleteLayout.setVisibility(View.GONE);

        }
    }
}
