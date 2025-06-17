package com.spotme.spotme;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {
    TextInputEditText resetPWEmail;
    Button toLoginButton, resetPWButton;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        resetPWEmail = findViewById(R.id.forgot_email_entry);
        toLoginButton = findViewById(R.id.to_login);
        resetPWButton = findViewById(R.id.submit_reset_btn);
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        resetPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String resetEmail;
                resetEmail = String.valueOf(resetPWEmail.getText());
                if (resetEmail.isEmpty()) {
                    Toast.makeText(Forgot_Password.this, "Valid email required", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(aVoid -> {
                    Toast.makeText(Forgot_Password.this, "If email is registered a reset link " +
                            "will be sent to your email", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(Forgot_Password.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}