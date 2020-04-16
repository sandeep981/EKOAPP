package com.sri.eko.Views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sri.eko.R;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    TextInputEditText tie_username, tie_mobile, tie_email, tie_password, tie_con_password, tie_address;
    TextInputLayout til_username, til_mobile, til_email, til_password, til_con_password, til_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();

        til_username = findViewById(R.id.til_username);
        til_mobile = findViewById(R.id.til_user_mobile);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        til_con_password = findViewById(R.id.til_con_password);
        tie_address = findViewById(R.id.tie_address);

        tie_username = findViewById(R.id.tie_username);
        tie_mobile = findViewById(R.id.tie_user_mobile);
        tie_email = findViewById(R.id.tie_email);
        tie_password = findViewById(R.id.password);
        tie_con_password = findViewById(R.id.tie_con_password);
        til_address = findViewById(R.id.til_address);

        Button btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (check()) {
                        hideKeyboared();
                        sendDataRetrofit();
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }

    private void sendDataRetrofit() {

        verifyOtp();
    }

    private boolean check() throws NullPointerException {
        boolean status = true;

        String password = Objects.requireNonNull(tie_password.getText()).toString();
        String conPassword = Objects.requireNonNull(tie_con_password.getText()).toString();

        if (String.valueOf(tie_username.getText()).isEmpty()) {
            til_username.setError("This Field is mandatory");
            status = false;
        } else if (tie_mobile.getText().toString().isEmpty()) {
            til_mobile.setError("This Field is mandatory");
            status = false;
        } else if (tie_mobile.getText().toString().length() < 10) {
            til_mobile.setError("Please enter valid number");
            status = false;
        } else if (tie_email.getText().toString().isEmpty()) {
            til_email.setError("This Field is mandatory");
            status = false;
        } else if (isValidEmail(tie_email.getText().toString())) {
            til_email.setError("Please enter valid Email Id");
            status = false;
        } else if (tie_password.getText().toString().isEmpty()) {
            til_password.setError("This Field is mandatory");
            status = false;
        } else if (tie_con_password.getText().toString().isEmpty()) {
            til_con_password.setError("This Field is mandatory");
            status = false;
        } else if (!password.equals(conPassword)) {
            til_con_password.setError("Password Mismatch");
            status = false;
        } else if (tie_address.getText().toString().isEmpty()) {
            til_address.setError("This Field is mandatory");
            status = false;
        }

        return status;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
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

    private void verifyOtp() {
        Intent i1 = new Intent(RegistrationActivity.this, OtpActivity.class);
        i1.putExtra("mobile", tie_username.getText().toString());
        startActivity(i1);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
