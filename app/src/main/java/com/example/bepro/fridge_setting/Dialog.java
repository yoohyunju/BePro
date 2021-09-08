package com.example.bepro.fridge_setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.bepro.R;

//public class Dialog {
//    Context context;
//    Dialog(Context context){
//        this.context=context;
//    }
//    void showDialog(String title,String message,String positve){
//        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton("예", new DialogInterface.OnClickListener() { //사용자의 허락
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i("test","예 클릭");
//                        Toast.makeText(context,positve,Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("아니오", new DialogInterface.OnClickListener() { //사용자의 불허
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i("test","아니오 클릭");
//
//                    }
//                });
//        msgBuilder.show();
//    }
//}

public class Dialog extends DialogFragment {
    Context context;
    Dialog(Context context){
        this.context=context;
    }

    public AlertDialog onCreateDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("메세지")
                .setPositiveButton("R.string.fire", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("R.string.cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
