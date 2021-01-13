package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private static FirebaseHelper instance;
    private Context appContext;


    private MediaPlayer mp;

    public static FirebaseHelper getInstance() {
        return instance;
    }

    private FirebaseHelper(Context appContext) {
        this.appContext = appContext.getApplicationContext();
    }

    // Singleton
    public static void init(Context appContext) {
        if (instance == null) {
            instance = new FirebaseHelper(appContext);
        }
    }

    public FirebaseUser getUser(){
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
    }

    public DatabaseReference getDatabaseReference(String reference){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        return firebaseDatabase.getReference(reference);
    }
}
