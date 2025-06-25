package com.spotme.spotme;

//Imports

import static com.spotme.spotme.R.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.spotme.spotme.deals.Debts;
import com.spotme.spotme.deals.Loans;

public class MainActivity extends AppCompatActivity
{
    FrameLayout viewContainer;
    FrameLayout frameLayout;
    TabLayout tabLayout;
    LayoutInflater inflater;
    private boolean isLoggedIn=false;


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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout, new Loans())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();


                frameLayout = (FrameLayout) findViewById(R.id.frame_Layout);
                tabLayout = (TabLayout) findViewById(R.id.tab_Layout);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        Fragment fragment = null;
                        switch (tab.getPosition()){
                            case 0:
                                fragment = new Loans();
                                break;
                            case 1:
                                fragment = new Debts();
                                break;

                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
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
