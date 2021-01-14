package com.example.qr_code.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.qr_code.activities.LoginActivity;
import com.example.qr_code.activities.SignupActivity;
import com.example.qr_code.utils.AppConstants;
import com.example.qr_code.utils.CoreApp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class APIServices {
    public static final String RESPONSE_UNWANTED = "UNWANTED";

    private static final String LINE_FEED = "\r\n";
    private static final String twoHyphens = "--";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String POSTWithURL(String url, String bodyStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        byte[] body = bodyStr.getBytes(StandardCharsets.UTF_8);
        outputStream.write(body, 0, body.length);
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTLoginwithformdataUrl(String url, String emailStr, String passwordStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";

        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //first parameter - email
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"username\"" + LINE_FEED + LINE_FEED
                + emailStr + LINE_FEED);

        //second parameter - passwordStr
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"password\"" + LINE_FEED + LINE_FEED
                + passwordStr + LINE_FEED);
        writer.write(LINE_FEED);
        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTSignupformUrl(String urlString, String firstname, String secondname, String email, String password, String nonce) throws IOException, JSONException{
        trustEveryone();

        JSONObject json = new JSONObject();
        json.put("username", firstname);
        json.put("email", email);
        json.put("password", password);

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";


        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        DataOutputStream os = new DataOutputStream(con.getOutputStream());
        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
        os.writeBytes(json.toString());

        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(con.getResponseCode()));
        Log.i("MSG" , con.getResponseMessage());

        con.disconnect();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String GETWithNonceURL(String url) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }


    public static String  GET_params(){
            OkHttpClient client = new OkHttpClient();
            String url = AppConstants.GET_NONCE;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        final String myResponse = response.body().string();
//                        JSONObject object = new JSONObject(myResponse);
//                        String nonce = object.getString("nonce");

                    }
                }
            });
            return "";
    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
