package com.fahru.ipekaku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.fahru.ipekaku.adapter.AnnouncementAdapter;
import com.fahru.ipekaku.database.DbHandler;
import com.fahru.ipekaku.database.NpmCallback;
import com.fahru.ipekaku.object.AnnouncementObject;
import com.fahru.ipekaku.object.NpmObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends DbHandler implements View.OnClickListener {
    LinearLayout menuSetting, menuAnnouncement;
    TextView annText, greetingTtext, userText;
    WormDotsIndicator dotsIndicator;
    ViewPager2 viewPager;
    RecyclerView.Adapter adapter;
    ArrayList<AnnouncementObject> list;
    int currentPos, timeOfDay;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    Calendar c;
    String Tag, path, npm, reqNpm;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        c = Calendar.getInstance();
        timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        dotsIndicator = findViewById(R.id.dots_indicator);
        viewPager = findViewById(R.id.mainMenuViewPager);
        annText = findViewById(R.id.mainMenuAnnText);
        greetingTtext = findViewById(R.id.mainMenuGreetingText);
        userText = findViewById(R.id.mainMenuUserName);
        menuSetting = findViewById(R.id.menuSettingLinlay);
        menuAnnouncement = findViewById(R.id.menuAnnouncementLinlay);
        setViewPagerPos();
        menuSetting.setOnClickListener(this);
        menuAnnouncement.setOnClickListener(this);
        list = new ArrayList<>();
        npm = userPrefs.getString("npm", "");
        reqNpm = userPrefs.getString("reqNpm", "");

        if (!npm.equals("")){
            setPath(npm);
        }else {
            setPath(reqNpm);
        }

//        String[] title = {"Announcement 1", "Announcement 2", "Announcement 3"};
//        String[] msg = {"<font color=\"#FF0000\"><b>Announcement</b></font> k oojpoj \n09ujiojlk hjkhkj iu  j lkh \n<h2>kg jg kjg \nkhl kj l;lkj jh k\njgh jg hjgjh jh  h\nlk lkj ljkjk kjhkjh khk\nh klhljl lj;k by awaludin firmansyah iasa ajshd oiahd  dsiha ioad as aod isadh ajd  asdga daio asidh aiudg asads jgcgi  uf oiiu h iopgliv", "Announcement by amin fahrudin", "Announcement by Faisal Ediansyah"};
//        final int[] img = {4,12,1};

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPos = position;
            }
        });

        /*After setting the adapter use the timer */

        userText.setText(userPrefs.getString("name", "name not set"));
        userText.setTypeface(setTypefaceClicker(this));
        setGreetings();


    }

    private void setPath(String npm) {
        getNpmDetailsWithNpm(npm, new NpmCallback() {
            @Override
            public void onNpmDetails(NpmObject npmObject) {
                path = npmObject.getMajor()+npmObject.getGeneration();
                setAnnouncement(path);
            }
        });
    }

    public void setAnnouncement(String path){
        CollectionReference loadAnn = db.collection("major").document(path).collection("announcement");
        loadAnn.orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.e(Tag, "onEvent: Listed failed", e);
                    return;
                }
                if (queryDocumentSnapshots.size() > 0){
                    list.clear();
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String id = documentSnapshot.getId();
                        String title = documentSnapshot.getString("title");
                        String msg = documentSnapshot.getString("msg");
                        long img = documentSnapshot.getLong("img");
                        String author = documentSnapshot.getString("author");
                        Timestamp time = documentSnapshot.getTimestamp("time");
                        int im = (int) img;

                        list.add(new AnnouncementObject(id, title, msg, im, author, time));
                        adapter = new AnnouncementAdapter(list);
                        viewPager.setAdapter(adapter);
                        dotsIndicator.setViewPager2(viewPager);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setViewPagerPos() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPos == list.size()) {
                    currentPos = 0;
                }
                viewPager.setCurrentItem(currentPos++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void setGreetings(){
        greetingTtext.setTypeface(setTypefaceClicker(this));
        if(timeOfDay >= 0 && timeOfDay < 11){
            greetingTtext.setText("Selamat Pagi,");
        }else if(timeOfDay >= 11 && timeOfDay < 15){
            greetingTtext.setText("Selamat Siang,");
        }else if(timeOfDay >= 15 && timeOfDay < 18){
            greetingTtext.setText("Selamat Sore,");
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            greetingTtext.setText("Selamat Malam,");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menuSettingLinlay:
                startActivity(new Intent(MainMenu.this, Settings.class));
                break;
            case R.id.menuAnnouncementLinlay:
                startActivity(new Intent(MainMenu.this, Announcement.class));
                break;
        }
    }



}
