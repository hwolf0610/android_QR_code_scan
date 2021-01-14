package com.example.qr_code.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.qr_code.utils.AppConstants;
import com.example.qr_code.utils.LoadingDialog;

public class LoginAPIService extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private String username;
    private String password;
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;

    public interface OnResultReceived {
        void onResult(String result);
    }

    public LoginAPIService(Context mContext, String username, String password, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;
        this.username = username;
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
        String response;
        try {
            String url = AppConstants.LOGIN ;
            Log.e("URL", url);
            response = APIServices.POSTLoginwithformdataUrl(url, username,password);
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
