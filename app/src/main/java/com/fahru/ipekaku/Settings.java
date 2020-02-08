package com.fahru.ipekaku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fahru.ipekaku.database.DbHandler;
import com.google.android.material.button.MaterialButton;

public class Settings extends DbHandler {
    MaterialButton logoutBtn;
    TextView settingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logoutBtn = findViewById(R.id.settingLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(Settings.this, Login.class));
                userPrefs.edit().clear().apply();
            }
        });

    }
}
