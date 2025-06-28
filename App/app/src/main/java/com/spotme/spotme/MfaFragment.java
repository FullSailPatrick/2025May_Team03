package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;
// Need to add comments
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorAssertion;
import com.google.firebase.auth.MultiFactorSession;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneMultiFactorGenerator;
import com.google.firebase.firestore.DocumentReference;

import java.util.concurrent.TimeUnit;


public class MfaFragment extends Fragment {

    Button submitBtn;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String phoneNumber;


    private PhoneAuthCredential credential;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mfa, container, false);
        submitBtn = view.findViewById(R.id.MFA_signup);
        final EditText phoneInput = view.findViewById(R.id.MFA_phone_number);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phoneInput.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                   Toast.makeText(requireContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(currentUser !=null) {

                     callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential _credential) {
                                    credential = _credential;
                                }
                                @Override
                                public void onVerificationFailed(FirebaseException e) {

                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(requireContext(), "Phone number is in valid, try again", Toast.LENGTH_SHORT).show();
                                    } else if (e instanceof FirebaseTooManyRequestsException) {

                                        Toast.makeText(requireContext(), "Number of retries has been exceeded please try again later", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                @Override
                                public void onCodeSent(
                                        String _verificationId, PhoneAuthProvider.ForceResendingToken _token) {
                                    verificationId = _verificationId;
                                    forceResendingToken = _token;
                                    mfaVerificationPopup(currentUser.getDisplayName());
                                }
                            };

                    currentUser.getMultiFactor().getSession()
                            .addOnCompleteListener(
                                    new OnCompleteListener<MultiFactorSession>() {
                                        @Override
                                        public void onComplete(@NonNull Task<MultiFactorSession> task) {
                                            if (task.isSuccessful()) {
                                                MultiFactorSession multiFactorSession = task.getResult();
                                                PhoneAuthOptions phoneAuthOptions =
                                                        PhoneAuthOptions.newBuilder()
                                                                .setPhoneNumber("+1 234-567-8901")
                                                                .setTimeout(30L, TimeUnit.SECONDS)
                                                                .setMultiFactorSession(multiFactorSession)
                                                                .setCallbacks(callbacks)
                                                                .build();
                                                PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
                                            }
                                        }
                                    });
                }

            }
            });

        return view;
    }
    public void mfaVerificationPopup(String username){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Enter Verification Code");

// Create an EditText and set it as the dialog view
        final EditText input = new EditText(requireContext());
        input.setHint("Enter code");
        builder.setView(input);


        builder.setPositiveButton("Submit", (dialog, which) -> {
            String verificationCode = input.getText().toString().trim();
            credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
            MultiFactorAssertion multiFactorAssertion = PhoneMultiFactorGenerator.getAssertion(credential);
// Complete enrollment.
            FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getMultiFactor()
                    .enroll(multiFactorAssertion, "My personal phone number")
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent loginIntent = new Intent(requireActivity(), MainActivity.class);
                                        startActivity(loginIntent);

                                    }else
                                    {
                                        Toast.makeText(requireContext(), "Login Was unsuccessful", Toast.LENGTH_SHORT).show();
                                        requireActivity().getSupportFragmentManager().popBackStack(null,
                                                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        requireActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.loginView, new LoginFragment())
                                                .commit();
                                    }
                                }
                            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }
}