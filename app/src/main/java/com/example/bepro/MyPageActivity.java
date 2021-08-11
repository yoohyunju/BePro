package com.example.bepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyPageActivity extends Fragment implements View.OnClickListener {
    LinearLayout mPwcLayout, mEditLayout, mLogoutLayout, mDeleteLayout;
    Button mEditBtn, mLogoutBtn, mDeleteBtn, mPwcOkBtn, mEditOkBtn, mLgyBtn, mLgnBtn, mDlyBtn, mDlnBtn;
    String[] items = {"1일전", "2일전", "3일전"};
    Switch mSwitch;
    Spinner mSpinner;
    EditText mEditTxt;
    private ArrayAdapter<String> adapter;
    ViewGroup rootView;

    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.my_page, container, false);
        super.onCreate(savedInstanceState);

        if (setObjectView()) {  // 오브젝트 찾기
            if (setObjectMethod()) {    // 오브젝트 함수 연결
                if (setInit()) {    // 데이터 초기화

                }
            }
        }
        mEditTxt = (EditText) rootView.findViewById(R.id.pwcPw);
        mEditTxt = (EditText) rootView.findViewById(R.id.editPw);
        mEditTxt = (EditText) rootView.findViewById(R.id.editNickname);
        mSwitch = rootView.findViewById(R.id.swNs);
        mSpinner = rootView.findViewById(R.id.spDay);
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
                mLogoutLayout.setVisibility(View.VISIBLE);
                break;
            /*case R.id.lgyBtn:
                mLogoutLayout.setVisibility(View.GONE);
                break;*/
            case R.id.lgnBtn:
                mLogoutLayout.setVisibility(View.GONE);
                break;
            case R.id.deleteBtn:
                mDeleteLayout.setVisibility(View.VISIBLE);
                break;
            /*case R.id.dlyBtn:
                mDeleteLayout.setVisibility(View.GONE);
                break;*/
            case R.id.dlnBtn:
                mDeleteLayout.setVisibility(View.GONE);

        }
    }

}
