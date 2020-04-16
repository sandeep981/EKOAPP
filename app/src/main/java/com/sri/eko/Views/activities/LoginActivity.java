package com.sri.eko.Views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sri.eko.R;
import com.sri.eko.app.ApIInterface;
import com.sri.eko.app.ApiClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = getClass().getSimpleName();
    TextInputEditText tie_username, tie_password;
    TextInputLayout til_username, til_password;
    ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();


        til_username = findViewById(R.id.til_username);
        til_password = findViewById(R.id.til_password);

        tie_username = findViewById(R.id.user_name);
        tie_password = findViewById(R.id.password);


        Button btn_login = findViewById(R.id.btn_login);
        Button btn_signup = findViewById(R.id.btn_signup);


        TextView txt_forgetpassword = findViewById(R.id.txt_forgetpassword);

        btn_login.setOnClickListener(this);

        btn_signup.setOnClickListener(this);
        txt_forgetpassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            if (check()) {
                sendDataRetrofit();
            }
        } else if (id == R.id.btn_signup) {
            Intent i1 = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i1);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (id == R.id.txt_forgetpassword) {
            if (tie_username.getText().toString().isEmpty()) {
                til_username.setError("This Field is mandatory");
            } else if (tie_username.getText().toString().length() < 10) {
                til_username.setError("Please enter valid number");
            } else {
                forgetPasword();
            }
        }

    }




    private boolean check() {
        boolean status = true;
        if (tie_username.getText().toString().isEmpty()) {
            til_username.setError("This Field is mandatory");
            status = false;
        } else if (tie_username.getText().toString().length() < 10) {
            til_username.setError("Please enter valid number");
            status = false;
        } else if (tie_password.getText().toString().isEmpty()) {
            til_password.setError("This Field is mandatory");
            status = false;
        }
        return status;
    }


    private void sendDataRetrofit() {
        verifyOtp();
    }


    private void forgetPasword() {
        verifyOtp();
    }


    

    private void verifyOtp() {
        Intent i1 = new Intent(LoginActivity.this, OtpActivity.class);
        i1.putExtra("mobile", tie_username.getText().toString());
        startActivity(i1);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


}
