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
import com.sri.eko.ViewModels.PoliciesViewModel;


public class PoliciesFragment extends Fragment {

    private PoliciesViewModel policiesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        policiesViewModel = ViewModelProviders.of(this).get(PoliciesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_policies, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        policiesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}