package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {

    TextInputEditText userEmail, userPassword; // text input fields
    Button logInBtn;
    FirebaseAuth mAuth;
    Button createAccountBtn, forgotPasswordBtn;
    boolean developer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Firebase user ID
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //Get user collection
        DocumentReference mainRef  = FirebaseFirestore.getInstance()
                .collection("users")
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


        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        logInBtn = findViewById(R.id.login);
        createAccountBtn = findViewById(R.id.Create_Account);
        forgotPasswordBtn = findViewById(R.id.Forgot_Password);

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Forgot_Password.class);
                startActivity(loginIntent);
                finish();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Create_Account.class);
                startActivity(loginIntent);
                finish();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(userEmail.getText());
                String password = String.valueOf(userPassword.getText());

                if (!developer) {
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(Login_Activity.this, "Enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(Login_Activity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login_Activity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(loginIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    email = "admin@spotme.com";
                    password = "Adm!n52025";

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login_Activity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(loginIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
//TODO Add persistence for notifications and dark mode