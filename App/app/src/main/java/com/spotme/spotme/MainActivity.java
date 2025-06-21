package com.spotme.spotme;

//Imports

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity
{
    FrameLayout viewContainer;
    LayoutInflater inflater;
    Button myLoansButton;
    Button myDebtsButton;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle instance)
    {
        super.onCreate(instance);
        setContentView(R.layout.main);

        //Hide title bar
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }

        inflater = LayoutInflater.from(this);
        viewContainer = findViewById(R.id.view_container);

        //navbar title settings
        bottomNav = findViewById(R.id.main_navigation);
        bottomNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        
        // Check if we should show specific screen (coming back from other activities)
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra("show_home", false)) {
                bottomNav.setSelectedItemId(R.id.nav_home);
                switchView(R.layout.home);
            } else if (intent.getBooleanExtra("show_deals", false)) {
                bottomNav.setSelectedItemId(R.id.nav_deals);
                switchView(R.layout.deals);
            } else if (intent.getBooleanExtra("show_settings", false)) {
                bottomNav.setSelectedItemId(R.id.nav_settings);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new SettingsFragment())
                        .commit();
            } else {
                // Default behavior
                switchView(R.layout.home);
            }
        } else {
            // Default behavior
            switchView(R.layout.home);
        }

        bottomNav.setOnItemSelectedListener(item ->
        {
            int id = item.getItemId();

            if (id == R.id.nav_home)
            {
                switchView(R.layout.home);
            }
            else if (id == R.id.nav_deals)
            {
                switchView(R.layout.deals);
                myDebtsButton = findViewById(R.id.debtButton);
                myDebtsButton.setOnClickListener(view ->
                {
                    switchView(R.layout.mydebts);
                    myLoansButton = findViewById(R.id.loanButton);
                    myLoansButton.setOnClickListener(View ->
                    {
                        switchView(R.layout.deals);
                    });
                });
            }
            else if (id == R.id.nav_borrow)
            {
                // FIXED: Launch BorrowActivity instead of just switching view
                Intent borrowIntent = new Intent(MainActivity.this, BorrowActivity.class);
                startActivity(borrowIntent);
            }
            else if (id == R.id.nav_lend)
            {
                // OPTION 1: If you want LendActivity to be a separate activity
                Intent lendIntent = new Intent(MainActivity.this, LendActivity.class);
                startActivity(lendIntent);

                // OPTION 2: If you want to keep the old layout switching behavior
                // switchView(R.layout.lend);
            }
            else if (id == R.id.nav_settings)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new SettingsFragment())
                        .commit();
            }

            return true;
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        
        // Handle the intent if coming back from other activities
        if (intent != null) {
            if (intent.getBooleanExtra("show_home", false)) {
                bottomNav.setSelectedItemId(R.id.nav_home);
                switchView(R.layout.home);
            } else if (intent.getBooleanExtra("show_deals", false)) {
                bottomNav.setSelectedItemId(R.id.nav_deals);
                switchView(R.layout.deals);
            } else if (intent.getBooleanExtra("show_settings", false)) {
                bottomNav.setSelectedItemId(R.id.nav_settings);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new SettingsFragment())
                        .commit();
            }
        }
    }


    //Switch view function based on a layout ID.
    private void switchView(int layoutResId)
    {
        viewContainer.removeAllViews();

        View view = inflater.inflate(layoutResId, viewContainer, false);

        viewContainer.addView(view);
    }
}