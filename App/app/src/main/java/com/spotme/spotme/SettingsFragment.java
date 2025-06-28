package com.spotme.spotme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsFragment extends Fragment
{
    private DocumentReference settingsRef;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.settings, container, false);

        // Get Firebase user ID and Firestorm settings reference
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        settingsRef = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid);

        //Show privacy policy dialog
        view.findViewById(R.id.privacyPolicyAction).setOnClickListener(v ->
        {
            new PrivacyPolicyDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "PrivacyPolicy");
        });

        //Show terms dialog
        view.findViewById(R.id.termsAction).setOnClickListener(v ->
        {
            new TermsDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Terms");
        });

        //Show cookies dialog
        view.findViewById(R.id.cookiesAction).setOnClickListener(v ->
        {
            new CookiesDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Cookies");
        });

        //Show contact dialog
        view.findViewById(R.id.contactAction).setOnClickListener(v ->
        {
            new ContactDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Contact");
        });

        //Show feedback dialog
        view.findViewById(R.id.feedbackAction).setOnClickListener(v ->
        {
            new FeedbackDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Feedback");
        });

        //Listen to the logoutAction click and redirect the user to the login screen.
        view.findViewById(R.id.logoutAction).setOnClickListener(v ->
        {
            Intent intent = new Intent(requireActivity(), Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();

            //Little message
            Toast.makeText(requireContext(), "Logged out Successfully!", Toast.LENGTH_SHORT).show();
        });

        //Notifications switch
        Switch toggleNotification = view.findViewById(R.id.toggleNotificationId);

        //Theme switch
        Switch themeSwitch = view.findViewById(R.id.toggleThemeId);

        // Load initial settings from Firebase
        settingsRef.get().addOnSuccessListener(doc ->
        {
            //Just in case the DB is deleted
            /*if (!doc.exists())
            {
                Map<String, Object> defaultSettings = new HashMap<>();
                Map<String, Object> userSettings = new HashMap<>();
                userSettings.put("Dark Mode On", false);
                userSettings.put("Notifications On", true);
                defaultSettings.put("User Settings", userSettings);

                // Apply defaults immediately
                themeSwitch.setChecked(false);
                toggleNotification.setChecked(true);
                return;
            }*/

            // Read "User Settings" map
            Boolean darkMode = doc.getBoolean("User Settings.Dark Mode On");
            Boolean notificationsOn = doc.getBoolean("User Settings.Notifications On");

            if (darkMode != null)
            {
                themeSwitch.setChecked(darkMode);
                AppCompatDelegate.setDefaultNightMode(
                        darkMode ? AppCompatDelegate.MODE_NIGHT_YES :
                                AppCompatDelegate.MODE_NIGHT_NO);
            }

            if (notificationsOn != null)
            {
                toggleNotification.setChecked(notificationsOn);
            }

        });

        //Notifications toggle listener
        toggleNotification.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            //Save to the DB
            settingsRef.update("User Settings.Notifications On", isChecked);

            if (isChecked)
            {
                Toast.makeText(requireContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        //theme toggle listener
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            //Save to the DB
            settingsRef.update("User Settings.Dark Mode On", isChecked);

            if (isChecked)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(requireContext(), "Dark mode enabled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(requireContext(), "Light mode enabled", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
