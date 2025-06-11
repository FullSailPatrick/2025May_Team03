package com.spotme.spotme;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class User_Database {

    private final FirebaseFirestore userInfo = FirebaseFirestore.getInstance();

    public void addUser(String userID, Map<String,Object> userData){
        userInfo.collection("Users").document(userID).set(userData).addOnSuccessListener(aVoid -> {
        }).addOnFailureListener(e -> {
        });
    }
}
