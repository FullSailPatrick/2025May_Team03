package com.spotme.spotme;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContactDialogFragment extends DialogFragment
{
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputMessage;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.contact_modal, container, false);

        //actions
        inputName = view.findViewById(R.id.inputName);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputMessage = view.findViewById(R.id.inputMessage);
        Button submitButton = view.findViewById(R.id.submitButton);
        ImageButton closeButton = view.findViewById(R.id.closeButton);

        //dismiss
        closeButton.setOnClickListener(v -> dismiss());

        //Submit event listener
        submitButton.setOnClickListener(v -> sendContactMessage());

        return view;
    }

    private void sendContactMessage()
    {
        //Get values
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String message = inputMessage.getText().toString().trim();

        //Validate values
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Map the values
        Map<String, Object> contactData = new HashMap<>();
        contactData.put("name", name);
        contactData.put("email", email);
        contactData.put("message", message);
        contactData.put("timestamp", new Timestamp(new Date()));

        //Create the collection in firebase
        FirebaseFirestore.getInstance()
                .collection("contact_messages")
                .add(contactData)
                .addOnSuccessListener(doc ->
                {
                    Toast.makeText(getContext(), "Message sent. Thank you!", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener(e ->
                {
                    Toast.makeText(getContext(), "Failed to send message.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialogTheme;
    }
}
