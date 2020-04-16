package com.sri.eko.Utilities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class GetContacts {

    private Context mcontext;
    private String TAG = getClass().getSimpleName();

    private SharedPreferences settingsShrepref;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "MyPrefFileShred";
    public static final String KEY_TODAY_DATE_ANDTIME = "TimeToday";
    String TodayDate_String;


    @SuppressLint("CommitPrefEdits")
    public GetContacts(Context mcontext) {
        this.mcontext = mcontext;

        //To check the time zone
        if (checkTimeZone()) Log.d(TAG, "TimeZone Matched");
        else Log.d(TAG, "TimeZone Not Matched");

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        TodayDate_String = dateFormat.format(date);


        settingsShrepref = mcontext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settingsShrepref.edit();

        String Val = settingsShrepref.getString(KEY_TODAY_DATE_ANDTIME, "");

        Log.i("Stored Val", Val);
        Log.i("Today date", TodayDate_String);

        if (!Val.equals(TodayDate_String)) {
            new FetchContacts().execute();
        }
    }

    private boolean checkTimeZone() {

        boolean check = false;
        TimeZone tz = TimeZone.getDefault();
        Log.d(TAG, "TimeZone: " + tz.getDisplayName());

        TimeZone tz1 = TimeZone.getTimeZone("Asia/Calcutta");
        Log.d(TAG, "TimeZoneIndia: " + tz1.getDisplayName());

        if (tz1.getDisplayName().equals(tz.getDisplayName())) check = true;

        return check;
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchContacts extends AsyncTask<Void, Void, ArrayList<Contact>> {

        private final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
        private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

        @SuppressLint("InlinedApi")
        private final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(mcontext);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ArrayList<Contact> doInBackground(Void... params) {
            try {

                // to get all the app and package names
                JSONArray appsArray = new JSONArray();
                ArrayList<AppInfo> appInfoArrayList = getInstalledApps(false); // false, only installed Apps
                for (AppInfo appInfo : appInfoArrayList) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("appName", appInfo.getAppName());
                    jsonObject.put("appPackage", appInfo.getPackageName());
                    jsonObject.put("appVersion", appInfo.getVersionName());
                    // jsonObject.put("appIcon", appInfo.getIcon());
                    appsArray.put(jsonObject);
                }
                Log.d(TAG, "AppsList: " + appsArray.toString());

                ArrayList<Contact> contacts = new ArrayList<>();
                ContentResolver cr = mcontext.getContentResolver();
                String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        // get the contact's information
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                        int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        // get the user's email address
                        String email = null;
                        Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (ce != null && ce.moveToFirst()) {
                            email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            ce.close();
                        }

                        // get the user's phone number
                        String phone = null;
                        if (hasPhone > 0) {
                            Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (cp != null && cp.moveToFirst()) {
                                phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                cp.close();
                            }
                        }

                        // if the user user has an email or phone then add it to contacts
                        if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                && !email.equalsIgnoreCase(name)) || (!TextUtils.isEmpty(phone))) {

                            Contact contact = new Contact();
                            contact.setName(name);
                            contact.setEmail(email);
                            contact.setPhoneNumber(phone);
                            contacts.add(contact);
                        }

                    } while (cursor.moveToNext());
                    // clean up cursor
                    cursor.close();
                }

                return contacts;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            try {
                if (contacts != null) {
                    // success
                    JSONArray array = new JSONArray();
                    for (Contact person : contacts) {
                        String name = person.getName() == null ? "No Name" : person.getName();
                        String emailId = person.getEmail() == null ? "No Email" : person.getEmail();
                        String Number = person.getPhoneNumber() != null ? person.getPhoneNumber().replace(" ", "") : person.getPhoneNumber();
                        // Log.d(TAG, person.getName() + " " + Number + " " + emailId);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", name);
                        jsonObject.put("number", Number);
                        jsonObject.put("email", emailId);
                        array.put(jsonObject);
                    }
                    //  Log.d(TAG, "contacts: " + array.toString());
                }

                Log.d("Comments", "First Time");

                editor.putString(KEY_TODAY_DATE_ANDTIME, TodayDate_String);
                editor.apply();

                String Val = settingsShrepref.getString(KEY_TODAY_DATE_ANDTIME, null);
                Log.d(TAG, "after Insertion data Date " + Val);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        }
    }


    private ArrayList<AppInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppInfo> res = new ArrayList<AppInfo>();
        List<PackageInfo> packs = mcontext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            AppInfo newInfo = new AppInfo();
            newInfo.setAppName(p.applicationInfo.loadLabel(mcontext.getPackageManager()).toString());
            newInfo.setPackageName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setIcon(p.applicationInfo.loadIcon(mcontext.getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }
}

class AppInfo {

    private String appName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private Drawable icon;

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }
}


class Contact {
    private String name;
    private String email;
    private String phoneNumber;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
