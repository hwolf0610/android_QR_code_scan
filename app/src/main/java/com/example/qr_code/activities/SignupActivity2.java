package com.example.qr_code.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qr_code.Interface_ServerApi;
import com.example.qr_code.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class SignupActivity2 extends AppCompatActivity implements Interface_ServerApi {

    private EditText etPhoneNumber, etAddress, etCity, etZipcode;
    private Button btn_sign, btn_skip;
    String stPhoneNumber, stAddress, stCity, stZipcode;
    String stFirstName = "", stLastName = "", stEmail = "", stPass = "";

    private static final String URL_SIGN_USER = "";
    private static final String URL_SIGN_VENDOR = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_login, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_login));
        }

        setContentView(R.layout.activity_signup2);

        initView();

    }

    public void initView(){

        Intent intent = getIntent();

        stFirstName = intent.getStringExtra("FirstName");
        stLastName = intent.getStringExtra("LastName");
        stEmail = intent.getStringExtra("Email");
        stPass = intent.getStringExtra("Password");

        btn_sign = (Button)findViewById(R.id.signup_btn);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stPhoneNumber = "";
                stAddress = "";
                stCity = "";
                stZipcode = "";

                etPhoneNumber = (EditText)findViewById(R.id.signup_phoneNumber);
                etAddress = (EditText)findViewById(R.id.signup_address);
                etCity = (EditText)findViewById(R.id.signup_city);
                etZipcode = (EditText)findViewById(R.id.signup_zipcode);

                stPhoneNumber = stPhoneNumber + etPhoneNumber.getText().toString();
                stAddress = stAddress + etAddress.getText().toString();
                stCity = stCity + etCity.getText().toString();
                stZipcode = stZipcode + etZipcode.getText().toString();

                if(stPhoneNumber.isEmpty()){
                    showAPIAlert("Alert", "please input your phone number!");
                }else if(stAddress.isEmpty()){
                    showAPIAlert("Alert", "please input your Street Address!");
                }else if(stCity.isEmpty()){
                    showAPIAlert("Alert", "please input your City!");
                }else if(stZipcode.isEmpty()){
                    showAPIAlert("Alert", "please input your zipcode!");
                }else{
                  //  sign_api_vendor();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

        btn_skip = (Button)findViewById(R.id.signup_skip);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   sign_api_user();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }


    private void sign_api_vendor(){


    }

    private void sign_api_user(){
    }


    @Override
    public void post_result(String result) {

        try{
            JSONObject obj_result = new JSONObject(result);
            if(obj_result.getString("status").equals("1")){
                Toast.makeText(SignupActivity2.this, "Sign is successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                showAPIAlert("Alert", "Sign is failed");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(SignupActivity2.this, "Connect error!", Toast.LENGTH_LONG).show();
        }
    }

    public void Sign_in(View view){

        Intent intent = new Intent(SignupActivity2.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
}
