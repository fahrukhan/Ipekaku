package com.fahru.ipekaku.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fahru.ipekaku.object.AppVersionObject;
import com.fahru.ipekaku.verification.Verification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class AppVersion extends DbHandler {

    public void latestApp(final Verification.appVersion versionCallback){
        db.collection("app").document("version").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null){
                        AppVersionObject appVersionObject = doc.toObject(AppVersionObject.class);
                        versionCallback.appLatestVersion(appVersionObject);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Error:", e.getMessage());
            }
        });
    }


}
