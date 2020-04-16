package com.sri.eko.Views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.sri.eko.R;


public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RelativeLayout rl_loans = root.findViewById(R.id.rl_loans);
        RelativeLayout rl_investment = root.findViewById(R.id.rl_investment);
        RelativeLayout rl_savings = root.findViewById(R.id.rl_savings);
        RelativeLayout rl_polocies = root.findViewById(R.id.rl_polocies);

        rl_loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_loans);
            }
        });
        rl_investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_investment);
            }
        });
        rl_savings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_saving);
            }
        });
        rl_polocies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_policies);
            }
        });

        return root;
    }
}