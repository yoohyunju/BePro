package com.example.bepro.fridge_setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.bepro.R;

public class Dialog {
    Context context;
    boolean result;
    Dialog(Context context){
        this.context=context;
    }
    boolean showDialog(String title,String message,String positve){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("예", new DialogInterface.OnClickListener() { //사용자의 허락
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("test","예 클릭");
                        Toast.makeText(context,positve,Toast.LENGTH_SHORT).show();
                        result = true;
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() { //사용자의 불허
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("test","아니오 클릭");
                        result = false;
                    }
                });
        msgBuilder.show();
        return result;
    }
}