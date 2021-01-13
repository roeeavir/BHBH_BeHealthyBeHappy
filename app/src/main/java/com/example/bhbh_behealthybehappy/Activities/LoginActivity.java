package com.example.bhbh_behealthybehappy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 1408;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseHelper.getInstance().getUser();


        if (user != null) { // If user, open the app
            openApp();
        } else {
            startLoginMethod(); // Open built in firebase login
        }
    }

    private void startLoginMethod() {
        Log.d("pttt", "startLoginMethod");

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                        ))
                        .setLogo(R.drawable.ic_healthy_logo)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .setTheme(R.style.GreenTheme)
                        .build(),
                RC_SIGN_IN);
    }

    private void openApp() {
        Log.d("pttt", "openApp");
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
//        if (firebaseAuth.getCurrentUser().isAnonymous())
//            AuthUI.getInstance().signOut(this);
        finish();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                openApp();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    MyHelper.getInstance().toast(getText(R.string.sign_in_cancelled).toString());
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    MyHelper.getInstance().toast(getText(R.string.no_internet_connection).toString());
                    return;
                }
                MyHelper.getInstance().toast(getText(R.string.unknown_error).toString());
                Log.e("pttt", "Sign-in error: ", response.getError());
            }
        }
    }
}