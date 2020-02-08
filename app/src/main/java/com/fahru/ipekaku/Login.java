package com.fahru.ipekaku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.fahru.ipekaku.database.DbHandler;
import com.fahru.ipekaku.model.ToastModel;
import com.fahru.ipekaku.verification.Verification;
import com.fahru.ipekaku.verification.VerifyEmail;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends DbHandler implements View.OnClickListener {
    FirebaseAuth.AuthStateListener authStateListener;
    ToastModel tm;
    MaterialButton login, reg;
    TextInputEditText email, pass;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reg = findViewById(R.id.loginRegBtn);
        login = findViewById(R.id.loginBtn);
        reg.setOnClickListener(this);
        login.setOnClickListener(this);
        email = findViewById(R.id.loginUser);
        pass = findViewById(R.id.loginPass);
        tm = new ToastModel();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    firebaseAuth.getCurrentUser().reload();
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login.this, Verification.class));
                    }else {
                        startActivity(new Intent(Login.this, VerifyEmail.class));
                    }

                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginRegBtn :
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.loginBtn :
                signInWithEmail(email.getText().toString(), pass.getText().toString());
                break;
        }
    }

    private void signInWithEmail(String email, String pass) {
        if (!email.isEmpty() && !pass.isEmpty()){
            loginWithEmail(email, pass);
        }else {
            tm.toastWarning(getApplicationContext(), "email & password tidak boleh kososng");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
