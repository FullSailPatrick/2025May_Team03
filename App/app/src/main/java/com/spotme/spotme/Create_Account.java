package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;


public class Create_Account extends AppCompatActivity {
    TextInputEditText userEmail, userPassword,userConfirmPassword,userName;
    EditText userPhone, userAddress, userFirstName, userLastName;
    Button signUpBtn, backToLoginButton;
    TextView confirmPasswordText,passwordLen,passwordCap,passwordDigit,passwordSpecial, userLen, userUnique, userChar;
    FirebaseAuth mAuth;
    String specialCharRegex = ".*[!@#$%^&*()_+\\-=\\[\\];':\"\\\\|,.<>/?~*].*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createaccount);
        int successColor = ContextCompat.getColor(this,R.color.Success_900);
        int errorColor = ContextCompat.getColor(this, R.color.Error_700);
        mAuth= FirebaseAuth.getInstance();
        User_Database userDatabase = new User_Database();

        // Button initialization
        signUpBtn = findViewById(R.id.signup);
        backToLoginButton = findViewById(R.id.return_to_login);

        // User Email  input Validation
        userEmail = findViewById(R.id.new_email); // User Email Address Entry Field
        userName = findViewById(R.id.create_username); // User Name Entry Field
        userUnique = findViewById(R.id.create_unique_username);
        userLen = findViewById(R.id.username_length_requirements);
        userChar = findViewById(R.id.username_char_requirements);

        // User Password validation formating configuration
        userPassword = findViewById(R.id.create_password);
        passwordLen = findViewById(R.id.password_length_requirements);
        passwordCap = findViewById(R.id.password_capitol_requirements);
        passwordDigit = findViewById(R.id.password_digit_requirements);
        passwordSpecial = findViewById(R.id.password_special_requirements);

        // Confirm password validation
        userConfirmPassword = findViewById(R.id.confirm_password);
        confirmPasswordText=findViewById(R.id.confirm_password_requirements);

        //User Data Fields
        userAddress = findViewById(R.id.user_postal_address);
        userPhone = findViewById(R.id.user_phone_number);
        userFirstName = findViewById(R.id.first_name);
        userLastName = findViewById(R.id.last_name);


        // Back to Login_Activity screen functionality
        backToLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               Intent  loginIntent = new Intent(getApplicationContext(), Login_Activity.class);
               startActivity(loginIntent);
               finish();
            }
        });

        //TODO Implement text listeners for other text inputs (email, user name, name, phone, address)
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                validateUsername(successColor,errorColor);
            }
        });
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
                    Toast.makeText(Create_Account.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Create_Account.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)|| !password.equals(confirmPassword)){
                    Toast.makeText(Create_Account.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validatePassword(successColor,errorColor))
                {
                    Toast.makeText(Create_Account.this, "Password is to weak", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validateUsername(successColor,errorColor))
                {
                    Toast.makeText(Create_Account.this, "Username is invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userID = task.getResult().getUser().getUid();
                                User_Database userDatabase = new User_Database();
                                userDatabase.addUser(userID, userDatabase.buildUserData(userEmail,userName,
                                        userFirstName,userLastName,userPhone,userAddress));
                                Toast.makeText(Create_Account.this, "Account Created.",
                                        Toast.LENGTH_LONG).show();
                                Intent  loginIntent = new Intent(getApplicationContext(), Login_Activity.class);
                                startActivity(loginIntent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Create_Account.this,
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
    private boolean validateUsername(int _successColor, int _errorColor){
        String email, username, emailPrefix;
        email = String.valueOf(userEmail.getText());
        username = String.valueOf(userName.getText());
        boolean usernameOK = false;
        int sum;
        int uniqueUsername;
        int usernameLengthOk;
        int usernameCharOk;

        // make sure username and email aren't blank
        if(TextUtils.isEmpty(username))
        {
            userUnique.setTextColor(_errorColor);
            userChar.setTextColor(_errorColor);
            userLen.setTextColor(_errorColor);
        }
        // compare email to username and make sure they don't match
        if(!TextUtils.isEmpty(email))
        {
            int indexOfAt = email.indexOf('@');
            emailPrefix = email.substring(0, indexOfAt);
            if(username.equals(emailPrefix)) {
                userUnique.setTextColor(_errorColor);
                uniqueUsername = 0;
            }
            else{
                userUnique.setTextColor(_successColor);
                uniqueUsername = 1;
            }
        }
        else {
            uniqueUsername = 0;
            userUnique.setTextColor(_errorColor);
        }
        if (username.matches(specialCharRegex))
        {
            userChar.setTextColor(_errorColor);
            usernameCharOk = 0;
        }
        else
        {
            userChar.setTextColor(_successColor);
            usernameCharOk = 1;
        }
        if (username.length()<8)
        {
            userLen.setTextColor(_errorColor);
            usernameLengthOk = 0;
        }
        else
        {
            userLen.setTextColor(_successColor);
            usernameLengthOk = 1;
        }
        sum = usernameCharOk + usernameLengthOk + uniqueUsername;
        if(sum == 3){
            usernameOK = true;
        }
        return usernameOK;
    }



}