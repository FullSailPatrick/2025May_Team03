package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {

    TextInputEditText userEmail, userPassword; // text input fields
    Button logInBtn;
    FirebaseAuth mAuth;
    Button createAccountBtn, forgotPasswordBtn;
    boolean developer = true;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        userEmail = view.findViewById(R.id.username);
        userPassword = view.findViewById(R.id.password);
        logInBtn = view.findViewById(R.id.login);
        createAccountBtn = view.findViewById(R.id.Create_Account);
        forgotPasswordBtn = view.findViewById(R.id.Forgot_Password);

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(requireActivity(), Forgot_Password.class);
                startActivity(loginIntent);
                requireActivity().finish();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(requireActivity(), Create_Account.class);
                startActivity(loginIntent);
                requireActivity().finish();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(userEmail.getText());
                String password = String.valueOf(userPassword.getText());

                if (!developer) {
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(requireContext(), "Enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(requireContext(), "Enter a password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireContext(), "Authentication Success.", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(requireActivity(), MainActivity.class);
                                        startActivity(loginIntent);
                                        requireActivity().finish();
                                    } else {
                                        Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    email = "test12345@spotme.com";
                    password = "T3$t1234";

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String currentUser;
                                        if(user !=null){
                                            currentUser=user.getUid();
                                            userRef = FirebaseFirestore.getInstance()
                                                    .collection("Users")
                                                    .document(currentUser);
                                        }
                                        userRef.get().addOnSuccessListener(doc ->{
                                            Boolean mfaEnabled =doc.getBoolean("User Information.MFAEnabled");
                                            String userFirstName = doc.getString("User Information.FirstName");
                                            if(mfaEnabled == null){
                                                mfaEnrollPopup(userFirstName,userRef);
                                            } else if (mfaEnabled) {
                                                requireActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.loginView, new MfaFragment())
                                                        .commit();

                                            }
                                            else{
                                                launchMain();
                                            }
                                        } );
                                        Toast.makeText(requireContext(), "Authentication Success.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        return view;
    }

    public void launchMain(){
        Intent loginIntent = new Intent(requireActivity(), MainActivity.class);
        startActivity(loginIntent);

    }


    public void mfaEnrollPopup(String username, DocumentReference userData){
        String message =  "Welcome, "+ username +" Would you like to enable MFA?";

        new AlertDialog.Builder(requireActivity())
                .setTitle("Enable MFA Option")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    userData.update("User Information.MFAEnabled", true);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.loginView, new MfaFragment())
                            .commit();
                }).setNegativeButton("No", (dialog, which) -> {
                    userData.update("User Information.MFAEnabled", false);
                    launchMain();
                })
                .setCancelable(false) // Prevent dismissing by tapping outside
                .show();

    }
}