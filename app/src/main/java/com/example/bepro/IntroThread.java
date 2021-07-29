package com.example.bepro;

import android.os.Handler;
import android.os.Message;

//로딩화면 -> 로그인 화면 전환을 위한 메세지 전달
public class IntroThread extends Thread{
    private Handler handler;

    public IntroThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        Message msg = new Message();
        try{
            //3초 후 sleep()메소드가 종료되면 msg 값 전달
            Thread.sleep(3000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
