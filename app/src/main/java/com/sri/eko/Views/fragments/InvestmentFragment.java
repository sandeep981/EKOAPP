package com.sri.eko.Views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sri.eko.R;
import com.sri.eko.ViewModels.InvestmentViewModel;


public class InvestmentFragment extends Fragment {

    private InvestmentViewModel investmentViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        investmentViewModel =
                ViewModelProviders.of(this).get(InvestmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_investment, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        investmentViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}