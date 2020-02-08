package com.fahru.ipekaku;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.fahru.ipekaku.database.DbHandler;
import com.fahru.ipekaku.database.NpmCallback;
import com.fahru.ipekaku.model.ToastModel;
import com.fahru.ipekaku.object.NpmObject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends DbHandler {
    TextInputLayout npmLay, userNmLay;
    TextInputEditText email, pass, username, npm;
    MaterialButton regBtn;
    ToastModel tm;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        npmLay = findViewById(R.id.regNpmInputCustom);
        userNmLay = findViewById(R.id.regUsrNmInputCustom);
        email = findViewById(R.id.regEmail);
        pass = findViewById(R.id.regPass);
        username = findViewById(R.id.regUsername);
        npm = findViewById(R.id.regNpm);
        regBtn = findViewById(R.id.regRegisterBtn);
        tm = new ToastModel();
        user = FirebaseAuth.getInstance().getCurrentUser();


        npmLay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "format NPM 8 digit,\nEx: 17111001", Toast.LENGTH_SHORT).show();
            }
        });
        userNmLay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "a-z, (_),(.),(-) max length 20", Toast.LENGTH_SHORT).show();
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldCheck()) {
                    tm.toastSuccess(getApplicationContext(), "Field tidak boleh ada yang kosong");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("invalid email");
                    email.setFocusable(true);
                } else if (username.getText().toString().length() < 3) {
                    tm.toastWarning(getApplicationContext(), "minimum length 3 character");
                } else {
                    checkUsername();
                }

            }
        });
    }

    public interface UserCallback {
        void onUserDataWhereUsername(String email);
    }

    private void checkUsername() {

        getUserDataWhereUsername(username.getText().toString(), new UserCallback() {
            @Override
            public void onUserDataWhereUsername(String email) {
                if (email.equals("not")) {
                    checkNpmRegistered();
                } else {
                    if (Build.VERSION.SDK_INT > 25) {
                        tm.toastError(getApplicationContext(), "Username telah digunakan oleh: " + getEmailCencored(email));
                    } else {
                        tm.toastError(getApplicationContext(), "NPM ini telah terhubung dengan email: " + email);
                    }
                }
            }
        });
    }


//    public interface NpmCallback {
//        void onUserDataWhereNpm(NpmObject npmObject);
//    }

    private void checkNpmRegistered() {
        getNpmDetailsWithNpm(npm.getText().toString(), new NpmCallback() {
            @Override
            public void onNpmDetails(NpmObject npmObject) {
                if (npmObject == null) {
                    tm.toastError(getApplicationContext(), "NPM tidak terdaftar pada Ipekaku\nSilahkan hubungi developer");
                } else if (npmObject.getEmail() != null && !npmObject.getEmail().isEmpty()) {
                    if (Build.VERSION.SDK_INT > 25) {
                        tm.toastError(getApplicationContext(), "NPM ini telah terhubung dengan email: " + getEmailCencored(npmObject.getEmail()));
                    } else {
                        tm.toastError(Register.this, "NPM ini telah terhubung dengan email: " + npmObject.getEmail());
                    }
                } else {
                    regBtn.setEnabled(false);
                    registerNewUser(email.getText().toString(), pass.getText().toString(), username.getText().toString(), npmObject);
                }
            }
        });
    }


    //Utility
    private boolean fieldCheck() {
        return email.getText().toString().isEmpty()
                || pass.getText().toString().isEmpty()
                || username.getText().toString().isEmpty()
                || npm.getText().toString().isEmpty();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getEmailCencored(String email) {

        String emailPattern = "(?<emailHead>[_A-Za-z0-9-\\+]{1,3})+?(?<replacementEmailPart>[_A-Za-z0-9-\\+]*)*?(?<emailTail>@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})){1}";

        Pattern p = Pattern.compile(emailPattern);

        Matcher m = p.matcher(email);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String replStr = m.group("replacementEmailPart");
            if (replStr != null) {
                replStr = replStr.replaceAll("[_A-Za-z0-9-\\+]", "*");
            } else {
                replStr = "****";
            }
            m.appendReplacement(sb, m.group("emailHead")
                    + replStr
                    + m.group("emailTail"));
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
