package com.sri.eko.app;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CustomJsonObjectRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;


    public CustomJsonObjectRequest(int method, String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        // Log.d("Request", "Request: "+url+"/"+params.toString());
    }


    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //Log.d(TAG, "sri_parseNetworkResponse: " + jsonString);
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    public RetryPolicy getRetryPolicy() {
        // TODO Auto-generated method stub
        return new DefaultRetryPolicy(9000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

}
