package com.spotme.spotme;

//Imports

import static com.spotme.spotme.R.*;

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
    private boolean isLoggedIn=false;
    Button myLoansButton;
    Button myDebtsButton;


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
        BottomNavigationView bottomNav = findViewById(R.id.main_navigation);
        bottomNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        View loginView = inflater.inflate(R.layout.login, viewContainer, false);
        viewContainer.addView(loginView);

        Button loginButton = loginView.findViewById(R.id.login);
        loginButton.setOnClickListener(view ->
                {
                    handleLoginEvent();
                    // TODO: make an if statement to switch between login and home.
                    if (!isLoggedIn) {
                        switchView(R.layout.login);
                    } else if (isLoggedIn) {
                        switchView(R.layout.home);
                    }
                });


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
                switchView(R.layout.borrow);
            }
            else if (id == R.id.nav_lend)
            {
                switchView(R.layout.lend);
            }
            else if (id == R.id.nav_settings)
            {
                switchView(R.layout.settings);
            }

            return true;
        });

    }


    //Switch view function based on a layout ID.
    private void switchView(int layoutResId)
    {
        viewContainer.removeAllViews();

        View view = inflater.inflate(layoutResId, viewContainer, false);

        viewContainer.addView(view);
    }
    private void handleLoginEvent() {
        // Set login value to true
        isLoggedIn = true;
        // Transition to home screen
        switchView(R.layout.home);

    }
}
