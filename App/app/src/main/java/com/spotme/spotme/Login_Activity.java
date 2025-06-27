package com.spotme.spotme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Firebase user ID
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //Get user collection
        DocumentReference mainRef  = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid);

        //apply default value
        mainRef.get().addOnSuccessListener(doc ->
        {
            Boolean darkMode = doc.getBoolean("User Settings.Dark Mode On");

            if (darkMode != null)
            {
                AppCompatDelegate.setDefaultNightMode(
                        darkMode ? AppCompatDelegate.MODE_NIGHT_YES :
                                AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.loginView, new LoginFragment())
                .commit();

    }
}