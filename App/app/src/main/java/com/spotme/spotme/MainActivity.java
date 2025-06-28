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
import android.widget.Switch;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.tabs.TabLayout;
import com.spotme.spotme.deals.DealsFragment;
import com.spotme.spotme.deals.Debts;
import com.spotme.spotme.deals.Loans;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.tabs.TabLayout;
import com.spotme.spotme.deals.Debts;
import com.spotme.spotme.deals.Loans;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FrameLayout viewContainer;
    LayoutInflater inflater;
    private boolean isLoggedIn = false;
    Button myLoansButton;
    Button myDebtsButton;
    BottomNavigationView bottomNav;

    FrameLayout frameLayout;
    TabLayout tabLayout;

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
            } else if (id == R.id.nav_deals)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new DealsFragment())
                        .commit();

//                //switchView(R.layout.deals);
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout, new Loans())
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .commit();
//
//
//                frameLayout = (FrameLayout) findViewById(R.id.frame_Layout);
//                tabLayout = (TabLayout) findViewById(R.id.tab_Layout);
//
//                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                    @Override
//                    public void onTabSelected(TabLayout.Tab tab) {
//                        Fragment fragment = null;
//                        switch (tab.getPosition()){
//                            case 0:
//                                fragment = new Loans();
//                                break;
//                            case 1:
//                                fragment = new Debts();
//                                break;
//
//                        }
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout, fragment)
//                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .commit();
//                    }
//
//                    @Override
//                    public void onTabUnselected(TabLayout.Tab tab) {
//
//                    }
//
//                    @Override
//                    public void onTabReselected(TabLayout.Tab tab) {
//
//                    }
//                });
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
                    startActivity(new Intent(this, LendActivity.class)); // ✅ Launch LendActivity as a full screen
                }
                return true;

            } else if (id == R.id.nav_settings) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_container, new SettingsFragment())
                        .commit();
            }

            return true;
        });

        //Open Lend directly if coming from LenderApplicationActivity
        if (getIntent().getBooleanExtra("open_lend", false)) {
            bottomNav.setSelectedItemId(R.id.nav_lend); // triggers the listener
        } else if (getIntent().getBooleanExtra("open_settings", false)) {
            bottomNav.setSelectedItemId(R.id.nav_settings);
        }else if (getIntent().getBooleanExtra("open_deals", false)) {
            bottomNav.setSelectedItemId(R.id.nav_deals);
        }
    }


    //Switch view function based on a layout ID.
    private void switchView(int layoutResId) {
        viewContainer.removeAllViews();
        View view = inflater.inflate(layoutResId, viewContainer, false);
        viewContainer.addView(view);
    }
}
