package com.example.qr_code.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.qr_code.utils.AppConstants;
import com.example.qr_code.utils.LoadingDialog;

import org.json.JSONObject;

public class SignupAPIService  extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private String firstname;
    private String secondname;
    private String email;
    private String password;
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;
    private String nonce;
    private String url;

    public interface OnResultReceived {
        void onResult(String result);
    }

    public SignupAPIService(Context mContext, String firstname, String secondname, String email, String password, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;
        this.firstname = firstname;
        this.secondname = secondname;
        this.email = email;
        this.password = password;
        this.mListner = mListner;
        this.flagProgress = flagProgress;
    }

    public void show() {
        if (flagProgress) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
    }

    private void hide() {
        if (flagProgress) {
            loadingDialog.hide();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... unsued) {
        String response,response2;
        try {
            this.url = AppConstants.SIGNUP ;

            response2 = APIServices.GETWithNonceURL(AppConstants.GET_NONCE);
            JSONObject object = new JSONObject(response2);
            this.nonce = object.getString("nonce");

            Log.e("URL", url);
            response = APIServices.POSTSignupformUrl(url, firstname, secondname, email, password, nonce);
            Log.e(TAG, " Response: " + response);
        } catch (Exception e) {
            Log.e(TAG, "Error:... " + e.getMessage());
            response = APIServices.RESPONSE_UNWANTED;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        hide();
        if (mListner != null) mListner.onResult(result);
    }
}