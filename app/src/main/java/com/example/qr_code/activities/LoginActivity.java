package com.example.qr_code.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qr_code.Interface_ServerApi;
import com.example.qr_code.R;
import com.example.qr_code.apiServices.APIServices;
import com.example.qr_code.apiServices.LoginAPIService;
import com.example.qr_code.utils.AppConstants;
import com.example.qr_code.utils.CoreApp;
import com.example.qr_code.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



public class LoginActivity extends AppCompatActivity implements Interface_ServerApi {

    private Button btn_login;
    private EditText ed_login_email, ed_login_pass;
    private String st_login_email;
    private String st_login_pass;
    SharedPreferences pref;
    private String userpref_name;
    private String userpref_pass;

    private static final String URL_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_login, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_login));
        }

        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        userpref_name = pref.getString("username",null);
        userpref_pass = pref.getString("password",null);
        if(userpref_name != "" && userpref_name != "" ){
            st_login_email = userpref_name;
            st_login_pass = userpref_name;
            login_api();
        }

        btn_login = (Button)findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_login_email = (EditText)findViewById(R.id.login_email);
                ed_login_pass = (EditText)findViewById(R.id.login_pass);

                st_login_email = ed_login_email.getText().toString();
                st_login_pass = ed_login_pass.getText().toString();

                if(st_login_email.equals("")){
                    showAPIAlert("Alert", "Please input your email");
                    return;
                }

                if(st_login_pass.equals("")){
                    showAPIAlert("Alert", "Please input your password");
                    return;
                }

                login_api();
            }
        });
    }
    private void showAPIAlert(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void login_sign(View view) {

        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
    private void login_api(){

        if (CoreApp.isNetworkConnection(this)) {
            new LoginAPIService(this, st_login_email, st_login_pass, new LoginAPIService.OnResultReceived() {
                @Override
                public void onResult(String result) {
                    if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("status").equals("ok")) {
                                Log.d("LoginActivity", "Log In");
//
                            JSONObject dataObejct = object.getJSONObject("user");
                                String firstname = dataObejct.getString("username");
                                String secondname = dataObejct.getString("username");

                                AppConstants.firstname = firstname;
                                AppConstants.secondname = secondname;

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("username",st_login_email);
                                editor.putString("password",st_login_pass);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("UserName", nameStr);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            } else if (object.getString("status").equals("error")){
                                Toast.makeText(getBaseContext(), "No user || incorrect password.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            }, true).execute();
        } else {
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show();
        }





    }


    @Override
    public void post_result(String result) {

        try{

            JSONObject obj_result = new JSONObject(result);

            if(obj_result.getString("status").equals("1")){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
                showAPIAlert("Alert", "Incorrect your email or password.");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Connect error!", Toast.LENGTH_LONG).show();
        }
    }
}
