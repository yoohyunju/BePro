package com.example.bepro.my_page;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bepro.BuildConfig;
import com.example.bepro.MainActivity;
import com.example.bepro.login.NickRequest;
import com.example.bepro.login.PrefsHelper;
import com.example.bepro.R;
import com.example.bepro.login.LoginActivity;
import com.example.bepro.login.RegisterRequest;
import com.example.bepro.login.snsRequest;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import android.provider.ContactsContract;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MyPageActivity extends Fragment implements View.OnClickListener {
    LinearLayout mPwcLayout, mEditLayout, mLogoutLayout, mDeleteLayout;
    Button mEditBtn, mLogoutBtn, mDeleteBtn, mPwcOkBtn, pwcCancel, newPwCh, newNickCh, mEditOkBtn, mLgyBtn, mLgnBtn, mDlyBtn, mDlnBtn;
    CheckBox autologin;
    String[] items = {"1일전", "2일전", "3일전"};
    Switch mSwitch;
    Spinner mSpinner;
    EditText pwcpw, editPw, editNickname;
    ImageView image;
    TextView id, nick;
    TextView memEMAIL;
    private ArrayAdapter<String> adapter;
    ViewGroup rootView;
    OAuthLogin mOAuthLoginModule; //네이버 로그인 토큰 관리
    public String imageUrl, userEmail, userNick, userType, userPassword, newPassword, newNick, dbImage;
    Dialog dialog;
    boolean success = false;
    private static final int RESULT_CODE = 0;

    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.my_page, container, false);
        super.onCreate(savedInstanceState);
        getUser();

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

        image = rootView.findViewById(R.id.userImage);

        //이미지를 눌렀을 때
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, RESULT_CODE);
            }
        });

        id = rootView.findViewById(R.id.Id);
        nick = rootView.findViewById(R.id.Nick);
        id.setText(userEmail);

        memEMAIL = rootView.findViewById(R.id.memEMAIL);
        memEMAIL.setText(userEmail);

        pwcpw = rootView.findViewById(R.id.pwcPw);
        editPw = rootView.findViewById(R.id.editPw);
        editNickname = rootView.findViewById(R.id.editNickname);
        mSwitch = rootView.findViewById(R.id.swNs);
        mSpinner = rootView.findViewById(R.id.spDay);
        autologin = rootView.findViewById(R.id.autologin);
        mSwitch.setChecked(true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSpinner.setEnabled(isChecked);
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

    //이미지 처리
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
            mPwcLayout = rootView.findViewById(R.id.pwcLayout);
            mEditLayout = rootView.findViewById(R.id.editLayout);
            mLogoutLayout = rootView.findViewById(R.id.logoutLayout);
            mDeleteLayout = rootView.findViewById(R.id.deleteLayout);
            mEditBtn = rootView.findViewById(R.id.editBtn);
            mLogoutBtn = rootView.findViewById(R.id.logoutBtn);
            mDeleteBtn = rootView.findViewById(R.id.deleteBtn);
            mPwcOkBtn = rootView.findViewById(R.id.pwcOkBtn);
            pwcCancel = rootView.findViewById(R.id.pwcCancel);
            newPwCh = rootView.findViewById(R.id.newPwCh);
            newNickCh = rootView.findViewById(R.id.newNickCh);
            mEditOkBtn = rootView.findViewById(R.id.editOkBtn);
            mLgyBtn = rootView.findViewById(R.id.lgyBtn);
            mLgnBtn = rootView.findViewById(R.id.lgnBtn);
            mDlyBtn = rootView.findViewById(R.id.dlyBtn);
            mDlnBtn = rootView.findViewById(R.id.dlnBtn);

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
            pwcCancel.setOnClickListener(this);
            newNickCh.setOnClickListener(this);
            newPwCh.setOnClickListener(this);
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
                boolean pwdCheck = pwcpw.getText().toString().equals(userPassword);
                if (pwdCheck) {
                    mPwcLayout.setVisibility(View.GONE);
                    mEditLayout.setVisibility(View.VISIBLE);

                    pwcpw.setText(null);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                    pwcpw.setText(null);
                }
                break;
            case R.id.pwcCancel:
                mPwcLayout.setVisibility(View.GONE);
                break;
            case R.id.editOkBtn:
                editNickname.setText(null);
                editPw.setText(null);
                userNick = newNick;
                mEditLayout.setVisibility(View.GONE);
                break;
            case R.id.newNickCh:
                newNick = editNickname.getText().toString();
                getUser(newNick);
                nick.setText(newNick);
                break;
            case R.id.newPwCh:
                newPassword = editPw.getText().toString();
                Response.Listener<String> newNickR = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                };
                PwdChangeRequest pw = new PwdChangeRequest(userType, userEmail ,newPassword, newNickR);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(pw);
                userPassword = newPassword;
                Toast.makeText(getActivity(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutBtn:
                //로그아웃
                mLogoutLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.lgyBtn:
                //로그아웃 (예)
                mLogoutLayout.setVisibility(View.GONE);
                if(userType.equals("kakao")){
                    //카카오톡 단말 토큰 삭제 (테스트용) -> 로그아웃 구현 시 사용
                    UserManagement.getInstance()
                            .requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    System.out.println("카카오 로그아웃");
                                }
                            });
                    Intent logout = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(logout);
                    break;
                }else if(userType.equals("naver")){
                    System.out.println("네이버 로그아웃");
                    //네이버 토큰 삭제(테스트용) -> 로그아웃 구현 시 사용
                    mOAuthLoginModule.logout(getActivity());
                    Intent logout = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(logout);
                    break;
                }else{
                    //자동로그인 단말 파일 삭제
                    PrefsHelper.remove("userEmail");
                    PrefsHelper.remove("userPassword");
                    Intent logout = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(logout);
                    break;
                }
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

                if(userType.equals("kakao")){
                    //카카오톡 단말 토큰 삭제 (테스트용) -> 로그아웃 구현 시 사용
                    UserManagement.getInstance()
                            .requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    System.out.println("카카오 로그아웃");
                                }
                            });
                }else if(userType.equals("naver")){
                    System.out.println("네이버 로그아웃");
                    //네이버 토큰 삭제(테스트용) -> 로그아웃 구현 시 사용
                    mOAuthLoginModule.logout(getActivity());
                }else{
                    //자동로그인 단말 파일 삭제
                    PrefsHelper.clear();
                }

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

                DeleteRequest deleteRequest = new DeleteRequest(userType, userEmail, responseListener);
                RequestQueue delete = Volley.newRequestQueue(getActivity());
                delete.add(deleteRequest);

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.dlnBtn:
                mDeleteLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        Glide.with(getContext()).load(uri).into(image);
        imageUrl = uri.toString();
        System.out.println(imageUrl + userNick);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getBoolean("success");
                    if(success){
                        System.out.println("성공");
                        System.out.println(imageUrl + userNick);
                    }else{
                        System.out.println("실패");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("테스트" + imageUrl);
        ImageChangeRequest imageChangeRequest = new ImageChangeRequest(imageUrl, userNick, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(imageChangeRequest);
    }

    public void getUser(String nickname){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //false면 nick이 있다는 말
                    success = jsonObject.getBoolean("success");
                    if(!success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        dialog = builder.setMessage("이미 존재하는 별명입니다.").setNegativeButton("확인", null).create();
                        dialog.show();
                    }else{
                        Response.Listener<String> newNickR = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        System.out.println(newNick + userNick);
                        NickChangeRequest nick = new NickChangeRequest(userNick ,newNick, newNickR);
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.add(nick);

                        Toast.makeText(getActivity(), "별명이 변경되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        NickRequest snsRequest = new NickRequest(nickname, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(snsRequest);
    }

    public void getUser(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //false면 nick이 있다는 말
                    userNick = jsonObject.getString("userNickname");
                    nick.setText(userNick);
                    userPassword = jsonObject.getString("userPassword");
                    dbImage = jsonObject.getString("userImg");

                    System.out.println(dbImage + imageUrl);
                    if(dbImage != "null") Glide.with(getContext()).load(dbImage).into(image);
                    else if(imageUrl == null) image.setImageResource(R.drawable.ic_baseline_person_outline_24);
                    else new DownloadFilesTask().execute(imageUrl);

                    if(userPassword.equals(userType)) pwcpw.setText(userPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        snsRequest snsRequest = new snsRequest(userType, userEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(snsRequest);
    }
}

