package com.fahru.ipekaku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fahru.ipekaku.R;
import com.fahru.ipekaku.database.DbHandler;
import com.fahru.ipekaku.database.NpmCallback;
import com.fahru.ipekaku.object.AnnouncementObject;
import com.fahru.ipekaku.object.NpmObject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Announcement extends DbHandler {
    TextInputEditText title, desc;
    MaterialButton postBtn;
    String npm, reqNpm, path, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        title = findViewById(R.id.announcementTitle);
        desc = findViewById(R.id.announcementMsg);
        postBtn = findViewById(R.id.announcementPostButton);
        npm = userPrefs.getString("npm", "");
        reqNpm = userPrefs.getString("reqNpm", "");
        username = userPrefs.getString("username", "anonymous");

        if (!npm.equals("")){
            setPath(npm);
        }else {
            setPath(reqNpm);
        }


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit = title.getText().toString();
                String des = desc.getText().toString();
                if (!tit.isEmpty() && !des.isEmpty()){
                    postAnnouncement(path, tit, des, 1, username);
                }else {
                    toastWarning(Announcement.this, "title / announcement text tidak boleh kosong");
                }
            }
        });


    }

    private void setPath(String npm) {
      getNpmDetailsWithNpm(npm, new NpmCallback() {
          @Override
          public void onNpmDetails(NpmObject npmObject) {
              path = npmObject.getMajor()+npmObject.getGeneration();
          }
      });
    }
}
