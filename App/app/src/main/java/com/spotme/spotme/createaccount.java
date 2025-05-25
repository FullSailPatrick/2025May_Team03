package com.spotme.spotme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class createaccount extends AppCompatActivity {
    TextInputEditText userEmail, userPassword,userConfirmPassword;
    Button signUpBtn, backToLoginButton;
    TextView confirmPasswordText,passwordLen,passwordCap,passwordDigit,passwordSpecial;
    FirebaseAuth mAuth;
    String specialCharRegex = ".*[!@#$%^&*()_+\\-=\\[\\];':\"\\\\|,.<>/?~*].*";

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
        passwordLen = findViewById(R.id.password_length_requirements);
        passwordCap = findViewById(R.id.password_capitol_requirements);
        passwordDigit = findViewById(R.id.password_digit_requirements);
        passwordSpecial = findViewById(R.id.password_special_requirements);
        confirmPasswordText=findViewById(R.id.confirm_password_requirements);
        int successColor = ContextCompat.getColor(this,R.color.Success_700);
        int errorColor = ContextCompat.getColor(this, R.color.Error_700);
        backToLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               Intent  loginIntent = new Intent(getApplicationContext(), login.class);
               startActivity(loginIntent);
               finish();
            }
        });

        //TODO Implement text listeners for other text inputs (email, user name, name, phone, address)
        userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(successColor,errorColor);
                validateConfirmPassword(successColor,errorColor);
            }
        });
        userConfirmPassword.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               validateConfirmPassword(successColor,errorColor);
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }
           @Override
           public void afterTextChanged(Editable s) {
               // This is where you'll put your validation logic
               validatePassword(successColor,errorColor);
               validateConfirmPassword(successColor,errorColor);
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
                if(!validatePassword(successColor,errorColor))
                {
                    Toast.makeText(createaccount.this, "Password is to weak", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(createaccount.this, "Account Created.",
                                        Toast.LENGTH_LONG).show();
                                Intent  loginIntent = new Intent(getApplicationContext(), login.class);
                                startActivity(loginIntent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(createaccount.this,
                                        "Authentication failed."+ Objects.requireNonNull(task.getException()).getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
        });
    }
    private void validateConfirmPassword(int _successColor, int _errorColor){
        String password, confirmPassword;
        password = String.valueOf(userPassword.getText());
        confirmPassword = String.valueOf(userConfirmPassword.getText());
        if(!TextUtils.isEmpty(password)&&TextUtils.isEmpty(confirmPassword)||TextUtils.isEmpty(confirmPassword))
        {
            confirmPasswordText.setTextColor(_errorColor);
        }
        else if(!password.equals(confirmPassword))
        {
            confirmPasswordText.setTextColor(_errorColor);
        }
        else
        {
            confirmPasswordText.setTextColor(_successColor);
        }
    }
    private boolean validatePassword(int _successColor, int _errorColor){
        boolean passwordOK = false;
        int passLen;
        int passDigit;
        int passCap;
        int passSpecial;
        int sum;
        String password, confirmPassword;
        password = String.valueOf(userPassword.getText());
        confirmPassword = String.valueOf(userConfirmPassword.getText());
        if(TextUtils.isEmpty(password)&&!TextUtils.isEmpty(confirmPassword))
        {
            confirmPasswordText.setTextColor(_errorColor);
        }
        if(password.length()< 7)
        {
            passwordLen.setTextColor(_errorColor);
            passLen = 0;
        }
        else {
            passwordLen.setTextColor(_successColor);
            passLen = 1;
        }
        if(!password.matches(".*[A-Z].*")){
            passwordCap.setTextColor(_errorColor);
            passCap = 0;
        }
        else{
            passwordCap.setTextColor(_successColor);
            passCap = 1;
        }
        if(!password.matches(".*\\d.*")){
            passwordDigit.setTextColor(_errorColor);
            passDigit = 0;
        }
        else {
            passwordDigit.setTextColor(_successColor);
            passDigit =1;
        }
        if(!password.matches(specialCharRegex)){
            passwordSpecial.setTextColor(_errorColor);
            passSpecial = 0;
        }
        else{
            passwordSpecial.setTextColor(_successColor);
            passSpecial= 1;
        }
        sum = passLen+passDigit+passCap+passSpecial;
        if (sum == 4){
            passwordOK = true;
        }
        return passwordOK;
    }
}