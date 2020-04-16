package com.sri.eko.Views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.sri.eko.R;
import com.sri.eko.Utilities.AlertMessages;
import com.sri.eko.app.AppController;
import com.sri.eko.app.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.sri.eko.Utilities.Constants.PREFS;
import static com.sri.eko.Utilities.Constants.USER_ID;

public class SplashActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST = 100;
    SharedPreferences preferences;
    private String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        user_ID = preferences.getString(USER_ID, null);



        if (requestForPermissions()) {
            if (user_ID != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                }, 2300);
            } else {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Intent i1 = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i1);
                            finish();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    private boolean requestForPermissions() {
        boolean readCameraState = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        boolean readNetWorkState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean readContacts = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED;

        if (readCameraState && readNetWorkState && readContacts) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, String.valueOf(requestCode));
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:

                boolean permissionsCount = grantResults.length > 0;
                boolean permissionOne = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionTwo = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean permissionThree = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (permissionsCount && permissionOne && permissionTwo && permissionThree) {
                    //All permissions are granted
                    if (user_ID != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }, 2300);
                    } else {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    Intent i1 = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(i1);
                                    finish();
                                }
                            }
                        };
                        thread.start();
                    }

                } else {
                    takeToPermissionSettings();
                }
                break;
        }
    }

    AlertDialog alert;
    boolean alertOnlyOnce;

    private void takeToPermissionSettings() {
        alertOnlyOnce = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("All Permissions are mandatory!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST && resultCode == RESULT_OK) {
            alertOnlyOnce = false;
            requestForPermissions();
        }
    }


    private void sendData() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(this);

        String url = "http://rntcp.dmxapp.in/V1/AppCommandCenter/getServerDateFormat";
        HashMap<String, String> params = new HashMap<>();
        params.put("", "");

        Log.d(TAG, url);
        Log.d(TAG, params.toString());

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        alertMessages.hideProgressDialog();
                        try {
                            Log.d(TAG, response.toString());
                            String result = response.getString("date");
                            alertMessages.alertMsgBox(result, SplashActivity.this);
                            if (result.equalsIgnoreCase("valid")) {

                            } else {
                                String message = "Please enter valid number";
                                alertMessages.alertMsgBox(message, SplashActivity.this);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertMessages.hideProgressDialog();
                        // TODO Auto-generated method stub
                        if (error instanceof TimeoutError) {
                            Toast.makeText(SplashActivity.this, "Request Time out error. Your internet connection is too slow to work", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(SplashActivity.this, "Connection Server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(SplashActivity.this, "Network connection error! Check your internet Setting", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(SplashActivity.this, "Parsing error", Toast.LENGTH_LONG).show();
                        }

                    }
                });
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);

    }
}
