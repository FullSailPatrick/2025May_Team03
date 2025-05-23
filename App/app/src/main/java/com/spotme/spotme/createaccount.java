package com.spotme.spotme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class createaccount extends AppCompatActivity {
    TextInputEditText userEmail, userPassword,userConfirmPassword;
    Button signUpBtn;
    Button backToLoginButton;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createaccount);
        mAuth= FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.new_email);
        userPassword = findViewById(R.id.create_password);
        userConfirmPassword = findViewById(R.id.confirm_password);
        signUpBtn = findViewById(R.id.signup);
        backToLoginButton = findViewById(R.id.return_to_login);
        backToLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               Intent  loginIntent = new Intent(getApplicationContext(), login.class);
               startActivity(loginIntent);
               finish();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email, password, confirmPassword;
                email = String.valueOf(userEmail.getText());
                password = String.valueOf(userPassword.getText());
                confirmPassword = String.valueOf(userConfirmPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(createaccount.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(createaccount.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)|| !password.equals(confirmPassword)){
                    Toast.makeText(createaccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(createaccount.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();
                                Intent  loginIntent = new Intent(getApplicationContext(), login.class);
                                startActivity(loginIntent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(createaccount.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }
}