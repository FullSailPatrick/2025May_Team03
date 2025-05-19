package com.spotme.spotme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class createaccount extends AppCompatActivity {
    TextInputEditText userEmail, userPassword;
    Button signUpBtn = findViewById(R.id.signup);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createaccount);
        userEmail = findViewById(R.id.new_email);
        userPassword = findViewById(R.id.create_password);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email, password;
                email = String.valueOf(userEmail.getText());
                password = String.valueOf(userPassword.getText());
            }
        });
    }
}