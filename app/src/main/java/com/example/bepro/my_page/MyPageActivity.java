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
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;

import com.example.bepro.FridgeAdapter;
import com.example.bepro.MainActivity;
import com.example.bepro.UserData;
import com.example.bepro.login.NickRequest;
import com.example.bepro.login.PrefsHelper;
import com.example.bepro.R;
import com.example.bepro.login.LoginActivity;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import java.net.URL;

public class MyPageActivity extends Fragment implements View.OnClickListener {
    LinearLayout mPwcLayout, mEditLayout, mLogoutLayout, mDeleteLayout, FridgeChLayout;
    Button mEditBtn, mLogoutBtn, mDeleteBtn, mPwcOkBtn, pwcCancel, newPwCh, newNickCh, mEditOkBtn, mLgyBtn, mLgnBtn, mDlyBtn, mDlnBtn, friOk, friNo;
    CheckBox autologin;
    String[] items = {"1??????", "2??????", "3??????"};
    Switch mSwitch;
    Spinner mSpinner;
    EditText pwcpw, editPw, editNickname;
    ImageView image;
    TextView id, nick, memEMAIL;
    private ArrayAdapter<String> adapter;
    ViewGroup rootView;
    OAuthLogin mOAuthLoginModule; //????????? ????????? ?????? ??????
    String newPassword, newNick;
    Dialog dialog;
    boolean success = false;
    private static final int RESULT_CODE = 0; //????????? ??????

    public UserData user = new UserData();
    public FridgeAdapter fridgeAdapter;
    // @NonNull : null ???????????? ??????
    // @Nullable : null ??????
    //onCreateView(): fragment??? ????????? UI??? ???????????? ?????? ??? ?????????
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.my_page, container, false);
        super.onCreate(savedInstanceState);

        //???????????? ?????????
        PrefsHelper.init(getActivity());

        //????????? ????????? ????????? ??????
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                getActivity()
                ,"MDUjLCrrrlJEQ45AffDw"
                ,"5T0Mo1V63V"
                ,"????????? ?????? ?????????"
        );

        if (setObjectView()) {  // ???????????? ??????
            if (setObjectMethod()) {    // ???????????? ?????? ??????
                if (setInit()) {    // ????????? ?????????

                }
            }
        }

        //?????? ?????? ?????????, ??????
        id = rootView.findViewById(R.id.Id);
        nick = rootView.findViewById(R.id.Nick);

        id.setText(user.getEmail());
        nick.setText(user.getNickname());

        image = rootView.findViewById(R.id.userImage);

        if(user.getDbImage() != "null") Glide.with(getContext()).load(user.getDbImage()).into(image);
        else if(user.getImage() == null) image.setImageResource(R.drawable.ic_baseline_person_outline_24);
        else new DownloadFilesTask().execute(user.getImage());

        //?????? ?????? ????????? ??????
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("????????? ??????");
                builder.setMessage("?????????????????????????");
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, RESULT_CODE);
                    }
                });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        //?????? ?????? ?????? ?????????
        memEMAIL = rootView.findViewById(R.id.memEMAIL);
        memEMAIL.setText(user.getEmail());

        pwcpw = rootView.findViewById(R.id.pwcPw);
        editPw = rootView.findViewById(R.id.editPw);
        editNickname = rootView.findViewById(R.id.editNickname);
        mSwitch = rootView.findViewById(R.id.swNs);
        mSpinner = rootView.findViewById(R.id.spDay);
        autologin = rootView.findViewById(R.id.autologin);

        //?????? ?????? ??????
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
                System.out.println(items[position] + "?????? ??????");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }


    //????????? ?????? (url)
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
            // doInBackground ?????? ????????? total ??? ?????? ??????
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
            FridgeChLayout = rootView.findViewById(R.id.FridgeChLayout);
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
            friOk = rootView.findViewById(R.id.friOk);
            friNo = rootView.findViewById(R.id.friNo);


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
            friNo.setOnClickListener(this);
            friOk.setOnClickListener(this);

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
                //?????? ?????? ?????? ??????
                mPwcLayout.setVisibility(View.VISIBLE);
                if(user.getPassword().equals(user.getType())) pwcpw.setText(user.getPassword());
                break;
            case R.id.pwcOkBtn:
                //???????????? ?????? ??????
                boolean pwdCheck = pwcpw.getText().toString().equals(user.getPassword());
                if (pwdCheck) {
                    mPwcLayout.setVisibility(View.GONE);
                    mEditLayout.setVisibility(View.VISIBLE);

                    pwcpw.setText(null);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("??????????????? ???????????? ????????????.").setNegativeButton("??????", null).create();
                    dialog.show();
                    pwcpw.setText(null);
                }
                break;
            case R.id.pwcCancel:
                //?????? ?????? ?????? ?????? ??????
                mPwcLayout.setVisibility(View.GONE);
                break;
            case R.id.editOkBtn:
                //?????? ?????? ??????
                editNickname.setText(null);
                editPw.setText(null);
                user.setNickname(newNick);
                mEditLayout.setVisibility(View.GONE);
                break;
            case R.id.newNickCh:
                //?????? ????????????
                newNick = editNickname.getText().toString();
                getUser(newNick);
                nick.setText(newNick);
                break;
            case R.id.newPwCh:
                //?????? ??????
                newPassword = editPw.getText().toString();
                Response.Listener<String> newNickR = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { }
                };
                PwdChangeRequest pw = new PwdChangeRequest(user.getType(), user.getEmail() ,newPassword, newNickR);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(pw);
                user.setPassword(newPassword);
                Toast.makeText(getActivity(), "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutBtn:
                //???????????? ??????
                mLogoutLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.lgyBtn:
                //???????????? (???)
                mLogoutLayout.setVisibility(View.GONE);
                if(user.getType().equals("kakao")){
                    //???????????? ?????? ?????? ?????? (????????????) -> ???????????? ?????? ??? ??????
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Toast.makeText(getActivity(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(user.getType().equals("naver")){
                    System.out.println("????????? ????????????");
                    //????????? ?????? ??????(????????????) -> ???????????? ?????? ??? ??????
                    mOAuthLoginModule.logout(getActivity());
                }else{
                    //??????????????? ?????? ?????? ??????
                    PrefsHelper.remove("userEmail");
                    PrefsHelper.remove("userPassword");
                }
                Intent logout = new Intent(getActivity(), LoginActivity.class);
                Toast.makeText(getActivity(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                startActivity(logout);
                break;
            case R.id.lgnBtn:
                //???????????? (?????????)
                mLogoutLayout.setVisibility(View.GONE);
                break;
            case R.id.deleteBtn:
                //????????????
                if(user.MyFridge().isEmpty()){
                    mDeleteLayout.setVisibility(View.VISIBLE);
                }else{
                    //?????? ????????? ?????? ??????
                    System.out.println("?????? ????????? ????????? ?????????." + user.MyFridge());
                    FridgeChLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.friOk:
                FridgeChLayout.setVisibility(View.GONE);
                mDeleteLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.friNo:
                //?????? ?????? ???????????? ?????? (??????)
                FridgeChLayout.setVisibility(View.GONE);
                break;
            case R.id.dlyBtn:
                //???????????? (???)
                mDeleteLayout.setVisibility(View.GONE);

                if(user.getType().equals("kakao")){
                    //???????????? ?????? ?????? ?????? (????????????) -> ???????????? ?????? ??? ??????
                    UserManagement.getInstance()
                            .requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    System.out.println("????????? ????????????");
                                }
                            });
                }else if(user.getType().equals("naver")){
                    System.out.println("????????? ????????????");
                    //????????? ?????? ??????(????????????) -> ???????????? ?????? ??? ??????
                    mOAuthLoginModule.logout(getActivity());
                }else{
                    //??????????????? ?????? ?????? ??????
                    PrefsHelper.clear();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getActivity(), "?????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(user.getIndex(), responseListener);
                RequestQueue delete = Volley.newRequestQueue(getActivity());
                delete.add(deleteRequest);
                break;

            case R.id.dlnBtn:
                //?????? ?????? (?????????)
                mDeleteLayout.setVisibility(View.GONE);
        }
    }

    //????????? ?????? ?????? (DB, url)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        Glide.with(getContext()).load(uri).into(image);
        user.setImage(uri.toString());
        user.setDbImage(uri.toString());

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { }
        };

        ImageChangeRequest imageChangeRequest = new ImageChangeRequest(user.getImage(), user.getNickname(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(imageChangeRequest);
    }

    //?????? ?????? ?????? ?????????
    public void getUser(String nickname){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //false??? nick??? ????????? ???
                    success = jsonObject.getBoolean("success");
                    if(!success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        dialog = builder.setMessage("?????? ???????????? ???????????????.").setNegativeButton("??????", null).create();
                        dialog.show();
                    }else{
                        Response.Listener<String> newNickR = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) { }
                        };
                        NickChangeRequest nick = new NickChangeRequest(user.getNickname() ,newNick, newNickR);
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.add(nick);

                        Toast.makeText(getActivity(), "????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
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
}

