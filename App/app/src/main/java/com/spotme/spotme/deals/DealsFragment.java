package com.spotme.spotme.deals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.spotme.spotme.R;

import java.util.ArrayList;
import java.util.List;

import com.spotme.spotme.CookiesDialogFragment;
import com.spotme.spotme.R;

public class DealsFragment extends Fragment
{
    private RecyclerView recyclerView;
    private DealsAdapter dealsAdapter;
    private FrameLayout frameLayout;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.deals, container, false);

        // Initialize tab layout
        tabLayout = view.findViewById(R.id.tab_Layout);
        frameLayout = view.findViewById(R.id.frame_Layout);

        // Dynamically add RecyclerView into the FrameLayout
        recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        frameLayout.addView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dealsAdapter = new DealsAdapter();
        recyclerView.setAdapter(dealsAdapter);

        // Default to loans
        dealsAdapter.updateDeals(getLoanDeals());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    dealsAdapter.updateDeals(getLoanDeals());
                } else {
                    dealsAdapter.updateDeals(getDebtDeals());
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private List<Deal> getLoanDeals() {
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal("Chris Johnson", "Hospital Bills", "$20,000", "1 yr."));
        deals.add(new Deal("Samantha Lee", "Car Repair", "$5,500", "6 mo."));

        deals.add(new Deal("Chris Johnson", "Hospital Bills", "$20,000", "1 yr."));
        deals.add(new Deal("Samantha Lee", "Car Repair", "$5,500", "6 mo."));

        deals.add(new Deal("Chris Johnson", "Hospital Bills", "$20,000", "1 yr."));
        deals.add(new Deal("Samantha Lee", "Car Repair", "$5,500", "6 mo."));

        deals.add(new Deal("Chris Johnson", "Hospital Bills", "$20,000", "1 yr."));
        deals.add(new Deal("Samantha Lee", "Car Repair", "$5,500", "6 mo."));
        return deals;
    }

    private List<Deal> getDebtDeals() {
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal("Daniel Reyes", "Credit Card", "$2,000", "3 mo."));
        deals.add(new Deal("Nina Patel", "Rent", "$1,800", "1 mo."));

        deals.add(new Deal("Daniel Reyes", "Credit Card", "$2,000", "3 mo."));
        deals.add(new Deal("Nina Patel", "Rent", "$1,800", "1 mo."));

        deals.add(new Deal("Daniel Reyes", "Credit Card", "$2,000", "3 mo."));
        deals.add(new Deal("Nina Patel", "Rent", "$1,800", "1 mo."));

        deals.add(new Deal("Daniel Reyes", "Credit Card", "$2,000", "3 mo."));
        deals.add(new Deal("Nina Patel", "Rent", "$1,800", "1 mo."));
        return deals;
    }
}