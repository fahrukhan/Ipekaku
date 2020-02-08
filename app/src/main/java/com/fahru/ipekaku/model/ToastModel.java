package com.fahru.ipekaku.model;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fahru.ipekaku.R;

import es.dmoral.toasty.Toasty;

public class ToastModel extends AppCompatActivity {

    public ToastModel(){}

    public void toastSuccess(Context context, String msg){
        Toasty.custom(context, msg,
                ContextCompat.getDrawable(context, R.drawable.owl_success_toast),
                ContextCompat.getColor(context, R.color.toastSuccess),
                ContextCompat.getColor(context, android.R.color.white),
                Toasty.LENGTH_SHORT, true, true).show();
    }

    public void toastWarning(Context context, String msg){
        Toasty.custom(context, msg,
                ContextCompat.getDrawable(context, R.drawable.owl_warning_toast),
                ContextCompat.getColor(context, R.color.toastWarning),
                ContextCompat.getColor(context, R.color.colorPrimary),
                Toasty.LENGTH_SHORT, true, true).show();
    }

    public void toastError(Context context, String msg){
        Toasty.custom(context, msg,
                ContextCompat.getDrawable(context, R.drawable.owl_error_toast),
                ContextCompat.getColor(context, R.color.toastError),
                ContextCompat.getColor(context, android.R.color.white),
                Toasty.LENGTH_SHORT, true, true).show();
    }


}
