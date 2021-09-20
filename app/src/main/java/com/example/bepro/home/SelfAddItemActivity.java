package com.example.bepro.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class SelfAddItemActivity extends AppCompatActivity { //어떻게 실행시킬 것인지
    /*int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000; //86400000 = 24시간 * 60분 * 60초 * 1000(=1초)

    TextView dateResult;
    LinearLayout datePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //현재 년월일 저장
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //TODO: 직접등록이 아닌 자동등록의 경우 DB 데이터 가져와서
        //'-' 기준으로 split 후 년월일 구분해서 저장하기

        datePicker = findViewById(R.id.selfAddFoodDatePicker); //달력 다이얼로그 버튼
        //edit_endDateBtn = (TextView) findViewById(R.id.tvFoodExpDate);
        dateResult = (TextView) findViewById(R.id.selfAddFoodRemainDate);

        //한국어 설정
        Locale.setDefault(Locale.KOREAN);

        // 디데이 날짜 입력
        //edit_endDateBtn.setText(currentYear + "년 " + (currentMonth + 1) + "월 " + currentDay + "일");

        //datePicker : 디데이 날짜 입력 버튼, 클릭시 DatePickerDialog 띄움
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getApplicationContext(), endDateSetListener, currentYear, currentMonth, currentDay).show();
            }
        });

    }

    //DatePickerDialog 팝업, 종료일 저장, 기존에 입력한 값이 있으면 해당 데이터 설정후 띄움
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //edit_endDateBtn.setText(year + "년 " + (month + 1) + "월 " + day + "일");

            ddayValue = dDayResult_int(dateEndY, dateEndM, dateEndD);

            dateResult.setText(getDday(year, month, day));

        }
    };

    //D-day 반환
    private String getDday(int mYear, int mMonth, int mDay) {

        //D-day 설정
        final Calendar dDayCalendar = Calendar.getInstance();
        dDayCalendar.set(mYear, mMonth, mDay);

        //millisecond 으로 환산 후 d-day 에서 today 의 차를 구함
        final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY; //사용자 선택 날짜
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY; //오늘 날짜
        long result = dDay - today;

        //출력 시 d-day 에 맞게 표시
        String strFormat;

        if (result > 0) { //남은 기한
            strFormat = "D-%d";
        } else if (result == 0) { //당일
            strFormat = "Today";
        } else { //지난 기한
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));

        return strCount;
    }


    //디데이 값 계산 함수
    public int onCalculatorDate (int dateEndY, int dateEndM, int dateEndD) {
        try {
            Calendar today = Calendar.getInstance(); //현재 날짜
            Calendar dDay = Calendar.getInstance();

            //시작일, 종료일 데이터 저장
            Calendar calendar = Calendar.getInstance();
            int cyear = calendar.get(Calendar.YEAR);
            int cmonth = (calendar.get(Calendar.MONTH) + 1);
            int cday = calendar.get(Calendar.DAY_OF_MONTH);

            today.set(cyear, cmonth, cday);
            dDay.set(dateEndY, dateEndM, dateEndD);// D-day의 날짜를 입력합니다.

            long day = dDay.getTimeInMillis() / ONE_DAY;
            long tday = today.getTimeInMillis() / ONE_DAY;
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.

            return (int) count; // 날짜는 하루 + 시켜줘야합니다.

        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }
    }


    //값 계산 결과 출력
    public int dDayResult_int(int dateEndY, int dateEndM, int dateEndD) {
        int result = 0;

        result = onCalculatorDate(dateEndY, dateEndM, dateEndD);

        return result;
    }

     */

}