package com.sri.eko.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sri.eko.Models.PackageDetails;
import com.sri.eko.R;

import java.util.ArrayList;

public class SpinnerAdp extends ArrayAdapter<PackageDetails.AmountMonths> {

    private ArrayList<PackageDetails.AmountMonths> spinnerItems;
    private Context context;

    public SpinnerAdp(Context context, int resource, ArrayList<PackageDetails.AmountMonths> listItems) {
        super(context, resource, listItems);
        this.context = context;
        this.spinnerItems = listItems;
    }

    public int getCount() {
        return spinnerItems.size();
    }

    public PackageDetails.AmountMonths getItem(int position) {
        return spinnerItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //convertView = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            convertView = mInflater.inflate(R.layout.simple_spinner_item, null);
        }
        TextView label = (TextView) convertView.findViewById(android.R.id.text1);
        // label.setTextColor(context.getResources().getColor(R.color.colorWhite));

        String amount = spinnerItems.get(position).getAmount();
        label.setText(amount);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.simple_spinner_item, null);
        }
        TextView label = (TextView) convertView.findViewById(android.R.id.text1);
        label.setText(spinnerItems.get(position).getAmount());
        label.setPadding(12, 8, 12, 8);
        return convertView;
    }
}