package com.spotme.spotme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PrivacyPolicyDialogFragment extends DialogFragment
{
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.privacy_policy_modal, container, false);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());
        return view;
    }

    @Override
    public int getTheme()
    {
        return R.style.FullScreenDialogTheme;
    }
}
