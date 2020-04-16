package com.sri.eko.app;


import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class CustomJsonObjectBody extends JsonRequest<JSONObject> {


    private String jsonRequest;
    public CustomJsonObjectBody(int method, String url, JSONObject jsonRequest, Listener<JSONObject> reponseListener, ErrorListener errorListener) {

        super(method, url,
                (jsonRequest == null) ? null : jsonRequest.toString(),
                reponseListener,
                errorListener);

        this.jsonRequest = (jsonRequest == null) ? null : jsonRequest.toString();
        setShouldCache(false); //This solution disables caching for each requests individually
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public byte[] getBody() {
        return jsonRequest.getBytes(StandardCharsets.UTF_8);
    }


}
