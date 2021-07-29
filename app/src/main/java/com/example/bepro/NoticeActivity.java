package com.example.bepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class NoticeActivity extends Fragment {
    TableLayout tableLayout;
    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notice, container, false);
        tableSetting(v);
        return inflater.inflate(R.layout.notice, container, false);
    }

    //row 생성이 안되는 문제, mySQL 데이터 가져오기 구현(수정 필요)
    public void tableSetting(View v){
        tableLayout = (TableLayout) v.findViewById(R.id.table);
        for(int i=0;i<4;i++){
            final TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for(int j=0;j<4;j++){
                final TextView text = new TextView(getActivity());
                text.setText("data");
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.addView(text);
            }
            tableLayout.addView(tableRow);
        }
    }
}

