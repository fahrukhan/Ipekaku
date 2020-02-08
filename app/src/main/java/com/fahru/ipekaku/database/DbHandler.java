package com.fahru.ipekaku.database;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fahru.ipekaku.Login;
import com.fahru.ipekaku.MainMenu;
import com.fahru.ipekaku.Register;
import com.fahru.ipekaku.model.BaseModel;
import com.fahru.ipekaku.model.ToastModel;
import com.fahru.ipekaku.object.AnnouncementObject;
import com.fahru.ipekaku.object.NpmObject;
import com.fahru.ipekaku.object.UserObject;
import com.fahru.ipekaku.verification.Verification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class DbHandler extends BaseModel {
    ToastModel tm;
    protected FirebaseFirestore db;
    protected FirebaseAuth auth;
    protected FirebaseUser user;
    protected Calendar calendar;
    protected String now;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        calendar = Calendar.getInstance();
        tm = new ToastModel();

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date cal = Calendar.getInstance().getTime();
        now = dateFormat.format(cal);
    }


    //Verification.class
    public void getUserDataWithUid(final String uid, final Verification.UserCallback dbCallback) {
        db.collection("users")
                .whereEqualTo("uid", uid)
                .limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            UserObject userObject = documentSnapshot.toObject(UserObject.class);
                            dbCallback.onUserDataWhereUsername(userObject);
                        }
                    }
                } else {
                    dbCallback.onUserDataWhereUsername(null);
                }
            }
        });
    }



    public void getUserDataWhereUsername(final String username, final Register.UserCallback dbCallback) {
        db.collection("users")
                .whereEqualTo("username", username)
                .limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            UserObject userObject = documentSnapshot.toObject(UserObject.class);
                            dbCallback.onUserDataWhereUsername(userObject.getEmail());
                        }
                    }
                } else {
                    dbCallback.onUserDataWhereUsername("not");
                }
            }
        });
    }

    //USER DATA REGISTER ACTIVITY
    public void loginWithEmail(final String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addDevice(email, "login");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tm.toastWarning(getApplicationContext(), e.getMessage());
            }
        });
    }

    public void registerNewUser(final String email, String pass, final String username, final NpmObject npm) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            final UserObject userObject = new UserObject(false, false, email, auth.getUid(), false, npm.getName(), "", npm.getNpm(), "", "", username, false);
                            addNewUserRegistered(email, userObject);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tm.toastWarning(getApplicationContext(), "Error: Cant send email verification\nPlease contact developer");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tm.toastError(getApplicationContext(), "Ooops!\nError: REGISTER FAILED\nPlease contact developer");
            }
        });
    }

    public void addNewUserRegistered(final String email, final UserObject userObject) {
        DocumentReference dbHis = db.collection("users").document(email);
        dbHis.set(userObject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    tm.toastSuccess(getApplicationContext(), "REGISTER SUCCESS\nEmail verification sent!");
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    addDevice(email, "reg");

                }
            }
        });
    }

    //Device Info
    private void addDevice(String email, String mode) {
        Map<String, Object> history = new HashMap<>();
        history.put("api-level", Build.VERSION.SDK_INT);
        history.put("device", Build.BRAND + " (" + Build.DEVICE + ")");
        history.put("model", Build.MODEL + " (" + Build.PRODUCT + ")");
        history.put("status", mode);
        history.put("time", FieldValue.serverTimestamp());
        String docPath = mode + now;
        DocumentReference dbHis = db.collection("history").document(email).collection("history").document(docPath);
        dbHis.set(history);
    }

    //NPM
    public void getNpmDetailsWithNpm(String npm, final NpmCallback npmCallback){
        db.collection("Npm").document(npm).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        NpmObject npmObject = documentSnapshot.toObject(NpmObject.class);
                        npmCallback.onNpmDetails(npmObject);
                    }
                }
            }
        });

    }

    //Announcement
    public void postAnnouncement(final String path, final String title, final String desc, final int img, final String author){
        AnnouncementObject annObject = new AnnouncementObject();
        db.collection("major").document(path).collection("announcement").add(annObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        AnnouncementObject annObject  = new AnnouncementObject(documentReference.getId(), title, desc, img, author, Timestamp.now());
                        db.collection("major").document(path).collection("announcement")
                                .document(documentReference.getId()).set(annObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                toastSuccess(getApplicationContext(), "Announcement Posted");
                                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                            }
                        });
                    }
                });
    }
}
