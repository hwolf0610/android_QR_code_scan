package com.example.qr_code.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qr_code.Interface_ServerApi;
import com.example.qr_code.R;
import com.example.qr_code.apiServices.APIServices;
import com.example.qr_code.apiServices.LoginAPIService;
import com.example.qr_code.apiServices.SignupAPIService;
import com.example.qr_code.utils.AppConstants;
import com.example.qr_code.utils.CoreApp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity implements Interface_ServerApi {

    private Button btn_sign;
    private EditText  etEmail, etPass, etFirstName, etLastName;
    private CheckBox checkBox;
    private String  stEmail, stPass, stFirstName, stLastName, stCheck;
    private String nonce;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final String URL_SIGN_USER = "";

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

        setContentView(R.layout.activity_signup);

        initView();
    }

    private void initView(){

        btn_sign = (Button)findViewById(R.id.signup_btn);
        etFirstName = (EditText)findViewById(R.id.signup_firstName);
        etLastName = (EditText)findViewById(R.id.signup_lastName);
        etEmail = (EditText)findViewById(R.id.signup_email);
        etPass = (EditText)findViewById(R.id.signup_pass);
        checkBox = (CheckBox)findViewById(R.id.signup_check);

        btn_sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stFirstName = etFirstName.getText().toString();
                stLastName = etLastName.getText().toString();
                stEmail = etEmail.getText().toString();
                stPass = etPass.getText().toString();
                nonce = "";
                if(stFirstName.isEmpty()){
                    showAPIAlert("Alert", "please input your First Name!");
                }
//                else if(stLastName.isEmpty()){
//                    showAPIAlert("Alert", "please input your Last Name!");
//                }
                else if(stEmail.isEmpty()){
                    showAPIAlert("Alert!", "please input your Email!");
                }else if(!stEmail.trim().matches(emailPattern)){
                    showAPIAlert("Alert", "Email is not correct format!");
                }else if(stPass.isEmpty()){
                    showAPIAlert("Alert", "please input your password!");
                }
                else{
                    sign_up();
                }
            }
        });
    }

    public void Sign_in(View view){

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sign_up(){
        if (CoreApp.isNetworkConnection(this)) {

            new SignupAPIService(this, stFirstName, stLastName, stEmail, stPass, new SignupAPIService.OnResultReceived() {
                @Override
                public void onResult(String result) {
                    if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("code").equals("200")) {
                                Log.d("SignupActivity", "Sign up");//
//                                JSONObject dataObejct = object.getJSONObject("data");
                                String firstname = stFirstName;
                                String secondname = stFirstName;

                                AppConstants.firstname = firstname;
                                AppConstants.secondname = secondname;

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                SignupActivity.this.finish();
                            } else {
                                Toast.makeText(SignupActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(SignupActivity.this, "json error", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            }, true).execute();
        } else {
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show();
        }

    }

    public void showAPIAlert(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void post_result(String result) {

        try{
            JSONObject obj_result = new JSONObject(result);
            if(obj_result.getString("status").equals("1")){
                Toast.makeText(SignupActivity.this, "Sign is successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                showAPIAlert("Alert", "Sign is failed");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(SignupActivity.this, "Connect error!", Toast.LENGTH_LONG).show();
        }
    }
}
