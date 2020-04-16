package com.sri.eko.Views.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sri.eko.R;

public class TermsConditionsFragment extends Fragment {

    private String Description_str;
    private String TermsConditions_str;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Description_str = getArguments().getString("description", "");
            TermsConditions_str = getArguments().getString("terms", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_terms_conditions, container, false);
        TextView txt_desc = rootView.findViewById(R.id.txt_desc);
        TextView txt_terms_conditions = rootView.findViewById(R.id.txt_terms);

        txt_desc.setText(Description_str);
        txt_terms_conditions.setText(TermsConditions_str);

        return rootView;
    }

}
