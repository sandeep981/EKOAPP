package com.sri.eko.Views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sri.eko.R;

import static com.sri.eko.Utilities.Constants.PREFS;
import static com.sri.eko.Utilities.Constants.USER_ID;
import static com.sri.eko.Utilities.Constants.USER_MOBILE;
import static com.sri.eko.Utilities.Constants.USER_NAME;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = getClass().getSimpleName();
    TextInputEditText tie_otp;
    TextInputLayout til_otp;
    String mobileNumber = "";
    boolean forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileNumber = bundle.getString("mobile");
            forgetPassword = bundle.getBoolean("forgetPassword");
        }

        til_otp = findViewById(R.id.til_otp);
        tie_otp = findViewById(R.id.tie_otp);

        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            if (tie_otp.getText().toString().isEmpty()) {
                til_otp.setError("This Field is mandatory");
            } else if (tie_otp.getText().toString().length() < 6) {
                til_otp.setError("Please enter valid number");
            } else {
                verfyOtp(tie_otp.getText().toString());
            }
        }
    }

    private void verfyOtp(String otp) {

        saveUserData();

    }

    private void saveUserData() {
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, "123");
        editor.putString(USER_NAME, "demo");
        editor.putString(USER_MOBILE, "1234567892");
        editor.apply();

        Intent i1 = new Intent(OtpActivity.this, HomeActivity.class);
        startActivity(i1);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finishAffinity();
    }

    public void hideKeyboared() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
