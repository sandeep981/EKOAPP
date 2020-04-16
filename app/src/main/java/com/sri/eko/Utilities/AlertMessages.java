package com.sri.eko.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class AlertMessages {
    private ProgressDialog progressDialog;

    public void showProgressDialog(Context mcontext) {
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

    }

    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public void alertMsgBox(String message, Context mcontext) {
        new AlertDialog.Builder(mcontext)
                .setTitle(message)
                .setMessage(null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


}
