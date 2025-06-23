package com.spotme.spotme;

//Imports
import static com.spotme.spotme.R.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    FrameLayout viewContainer;
    LayoutInflater inflater;
    private boolean isLoggedIn = false;
    Button myLoansButton;
    Button myDebtsButton;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.main);

        // Hide title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        inflater = LayoutInflater.from(this);
        viewContainer = findViewById(R.id.view_container);

        bottomNav = findViewById(R.id.main_navigation);
        bottomNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        switchView(R.layout.home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                switchView(R.layout.home);
            } else if (id == R.id.nav_deals) {
                switchView(R.layout.deals);
                myDebtsButton = findViewById(R.id.debtButton);
                myDebtsButton.setOnClickListener(view -> {
                    switchView(R.layout.mydebts);
                    myLoansButton = findViewById(R.id.loanButton);
                    myLoansButton.setOnClickListener(View -> {
                        switchView(R.layout.deals);
                    });
                });
            } else if (id == R.id.nav_borrow) {
                //switchView(R.layout.borrow);
                Intent borrowIntent = new Intent(this, BorrowActivity.class);
                startActivity(borrowIntent);
            } else if (id == R.id.nav_lend) {
                SharedPreferences prefs = getSharedPreferences("spotme_prefs", MODE_PRIVATE);
                boolean hasSubmittedLenderApp = prefs.getBoolean("lender_app_done", false);

                if (!hasSubmittedLenderApp) {
                    startActivity(new Intent(this, LenderApplicationActivity.class));
                } else {
                    //switchView(R.layout.lend);
                    // COMMENTED OUT: Launch LendActivity (this was causing crashes)
                    Intent lendIntent = new Intent(this, LendActivity.class);
                    startActivity(lendIntent);
                }
            } else if (id == R.id.nav_settings) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new SettingsFragment())
                        .commit();
            }

            return true;
        });

        // Optional: Open lend layout directly if coming from LenderApplicationActivity
        if (getIntent().getBooleanExtra("open_lend", false)) {
            bottomNav.setSelectedItemId(R.id.nav_lend); // triggers the listener
        }
    }


    // Switch view function based on a layout ID.
    private void switchView(int layoutResId) {
        viewContainer.removeAllViews();
        View view = inflater.inflate(layoutResId, viewContainer, false);
        viewContainer.addView(view);
    }
}
