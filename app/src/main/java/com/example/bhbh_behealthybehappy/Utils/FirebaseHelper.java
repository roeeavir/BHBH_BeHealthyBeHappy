package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.Models.UserItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DATES_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USERS_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.WATER_GLASS;

// Helps with most firebase related functions
public class FirebaseHelper {

    // Variables
    private static FirebaseHelper instance;
    private Context appContext;


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

    public void addItem(ItemEntry item, String date) {// Adds item to user database
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);

        myRef.child(user.getUid()).child(DATES_REF).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(item.getName()).exists()) {
                    MyHelper.getInstance().toast(item.getName() + " is already in your list");
                } else {
                    if (item.getItemType() == Enums.ITEM_THEME.ACTIVITY)
                        myRef.child(user.getUid()).child(DATES_REF).child(date).child(item.getName()).setValue(15);
                    else
                        myRef.child(user.getUid()).child(DATES_REF).child(date).child(item.getName()).setValue(100);
                    MyHelper.getInstance().playAudio(R.raw.item_added);
                    MyHelper.getInstance().toast(item.getName() + " has been added to your list!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addWaterGlass(String date) {
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);
        String water = "Water";

        myRef.child(user.getUid()).child(DATES_REF).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(water).exists()) {
                    int water_glasses = snapshot.child(water).getValue(Integer.class) + 1;
                    myRef.child(user.getUid()).child(DATES_REF).child(date).child(WATER_GLASS).setValue(water_glasses);
                } else
                    myRef.child(user.getUid()).child(DATES_REF).child(date).child(WATER_GLASS).setValue(1);
                MyHelper.getInstance().playAudio(R.raw.water_added);
                MyHelper.getInstance().toast("Water glass has been added to your list!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeUserItem(UserItemEntry item, UserItemAdapter user_item_adapter, String date) {
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);

        myRef.child(user.getUid()).child(DATES_REF).child(date)
                .child(item.getItemEntry().getName()).removeValue();

        user_item_adapter.removeItem(item);

        MyHelper.getInstance().playAudio(R.raw.item_removed);

        MyHelper.getInstance().toast(item.getItemEntry().getName() + " has been removed from your list");
        Log.w("pttt", item.getItemEntry().getName() + " has been removed from your list");

    }
}
