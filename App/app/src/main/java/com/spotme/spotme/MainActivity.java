package com.spotme.spotme;

//Imports
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity
{
    FrameLayout viewContainer;
    LayoutInflater inflater;


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
        switchView(R.layout.home);

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

//        if (layoutResId == R.layout.settings)
//        {
//            // Load the SettingsFragment inside the placeholder
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.view_container, new SettingsFragment())
//                    .commit();
//
//        }
    }

}
