package com.fahru.ipekaku.verification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fahru.ipekaku.Login;
import com.fahru.ipekaku.MainMenu;
import com.fahru.ipekaku.R;
import com.fahru.ipekaku.database.AppVersion;
import com.fahru.ipekaku.object.AppVersionObject;
import com.fahru.ipekaku.object.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.tbuonomo.materialsquareloading.MaterialSquareLoading;

public class Verification extends AppVersion {
    FirebaseAuth.AuthStateListener authStateListener;
    MaterialSquareLoading loading;
    TextView textView;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_version);

        loading = findViewById(R.id.material_square_loading_view);
        textView = findViewById(R.id.textLoading);
        textView.setTypeface(setTypefaceClicker(this));

        loading.show();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getCurrentUser() != null) {
                    if (auth.getCurrentUser().isEmailVerified()) {
                        if (!login){
                            setUserPrefs();
                        }
                        getVersion();
                    } else {
                        startActivity(new Intent(Verification.this, VerifyEmail.class));
                    }
                } else {
                    startActivity(new Intent(Verification.this, Login.class));
                }
            }
        };

    }

    public interface UserCallback {
        void onUserDataWhereUsername(UserObject userObject);
    }

    private void setUserPrefs(){
        getUserDataWithUid(auth.getUid(), new UserCallback() {
            @Override
            public void onUserDataWhereUsername(UserObject userObject) {
                if (userObject != null){
                    SharedPreferences.Editor editor = userPrefs.edit();
                    editor.putBoolean("login", true);
                    editor.putString("name", userObject.getName());
                    editor.putString("username", userObject.getUsername());
                    editor.putString("npm", userObject.getNpm());
                    editor.putString("reqNpm", userObject.getReqnpm());
                    editor.putString("uid", userObject.getUid());
                    editor.putString("url", userObject.getUrl());
                    editor.apply();
                }
            }
        });

    }

    private void getVersion() {
        int ver;
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ver = (int) pInfo.getLongVersionCode();
                updateCheck(ver);
            } else {
                ver = pInfo.versionCode;
                updateCheck(ver);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            startActivity(new Intent(this, MainMenu.class));
        }
    }

    private void updateCheck(final int ver) {
        latestApp(new appVersion() {
            @Override
            public void appLatestVersion(AppVersionObject version) {

                if (version != null) {
                    if (ver < version.getVer_code()) {
                        if (version.getShould_code() == 1) {
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(Verification.this);
                            dialog.setTitle("UPDATE IPEKAKU");
                            dialog.setMessage(getString(R.string.update_text_caused_old_version));
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert = dialog.create();
                            alert.show();
                        }
                        if (version.getShould_code() == 0) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Verification.this);
                            dialog.setTitle("UPDATE IPEKAKU");
                            dialog.setMessage("Telah tersesia versi terbaru dengan beberapa update:\n\n" + version.getUpdate_info());
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Verification.this, MainMenu.class));
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        startActivity(new Intent(Verification.this, MainMenu.class));
                    }
                }
            }
        });
    }

    public interface appVersion {
        void appLatestVersion(AppVersionObject version);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loading.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.show();
    }
}
