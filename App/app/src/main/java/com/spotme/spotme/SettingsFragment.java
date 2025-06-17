package com.spotme.spotme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment
{
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.settings, container, false);

        //Show privacy policy dialog
        view.findViewById(R.id.privacyPolicyAction).setOnClickListener(v -> {
            new PrivacyPolicyDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "PrivacyPolicy");
        });

        //Show terms dialog
        view.findViewById(R.id.termsAction).setOnClickListener(v -> {
            new TermsDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Terms");
        });

        //Show cookies dialog
        view.findViewById(R.id.cookiesAction).setOnClickListener(v -> {
            new CookiesDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Cookies");
        });

        //Show contact dialog
        view.findViewById(R.id.contactAction).setOnClickListener(v -> {
            new ContactDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Contact");
        });

        //Show feedback dialog
        view.findViewById(R.id.feedbackAction).setOnClickListener(v -> {
            new FeedbackDialogFragment()
                    .show(requireActivity().getSupportFragmentManager(), "Feedback");
        });

        //Logout button
        view.findViewById(R.id.logoutAction).setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Login_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();

            Toast.makeText(requireContext(), "Logged out Successfully!", Toast.LENGTH_SHORT).show();
        });

        //Notifications switch
        Switch toggleNotification = view.findViewById(R.id.toggleNotificationId);
        toggleNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {

            //TODO: Save to the DB

            if (isChecked)
            {
                Toast.makeText(requireContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        //theme switch
        Switch themeSwitch = view.findViewById(R.id.toggleThemeId);

        // Initialize the switch based on current theme
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        themeSwitch.setChecked(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);

        // Toggle
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            //TODO: Save to the DB

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

            //Apply
            requireActivity().recreate();
        });

        return view;
    }
}
