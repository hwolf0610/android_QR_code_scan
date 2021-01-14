package com.example.qr_code.activities;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.JsonReader;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_code.Interface_ServerApi;
import com.example.qr_code.R;
import com.example.qr_code.utils.AppConstants;
import com.example.qr_code.utils.Preferences;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Interface_ServerApi {

    private Button btn_mycode, btn_turn, btn_share;
    private SurfaceView surfaceView;
    private TextView tv_status,main_text;
    private TextView txtBarcodeValue;
    private TextView tv_username,tv_firstname;
    private TextView tv_savedEvents, tv_myFavs, tv_history, tv_myReviews, tv_settings;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final int PICK_REQUEST = 53;

    String intentData = "";
    boolean isEmail = false;
    boolean isTurn = false;
    boolean isUsercode = false;

    private FrameLayout scan_display, img_logo, img_mycode;
    private ImageView img_myqr;
    SharedPreferences pref;
//  String userName = getIntent().getStringExtra("UserName");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light));
        }
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        tv_username=(TextView)findViewById(R.id.username);
        tv_firstname=(TextView)findViewById(R.id.firstname);
        main_text = (TextView)findViewById(R.id.main_text);

        btn_mycode = (Button)findViewById(R.id.btn_mycode);
        btn_turn = (Button)findViewById(R.id.btn_turn);
        btn_share = (Button)findViewById(R.id.btn_share);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
        tv_status = (TextView)findViewById(R.id.tv_status);

        txtBarcodeValue = (TextView)findViewById(R.id.txtBarcodeValue);

        tv_savedEvents = (TextView)findViewById(R.id.tv_savedEvents);
        tv_myFavs = (TextView)findViewById(R.id.tv_myFavs);
        tv_history = (TextView)findViewById(R.id.tv_history);
        tv_myReviews = (TextView)findViewById(R.id.tv_myReviews);
        tv_settings = (TextView)findViewById(R.id.tv_settings);

        scan_display = (FrameLayout)findViewById(R.id.scan_display);
        img_logo = (FrameLayout)findViewById(R.id.img_mylogo);
        img_mycode = (FrameLayout)findViewById(R.id.img_mycode);

        img_myqr = (ImageView)findViewById(R.id.img_myqr);

        main_text.setText(Html.fromHtml(  "Scan Restaurants, Stores & Service Providers…"  +
                "<b>" + "View instantly and GET:" + "</b>" + "On the spot - Specials, Sales, Events, Money saving offers and more." + "<b>Refer customers, businesses & earn rewards and CASH.</b>"
        ));
//        try {
//            JSONObject jsonObject = new JSONObject(loginInfo);
//            String name = jsonObject.getString("name");
//            tv_username.setText(name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        tv_username.setText(userName);
            tv_firstname.setText(AppConstants.firstname );
            tv_username.setText("Logout");
            tv_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_LONG).show();
                    pref = getSharedPreferences("user_details",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        btn_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTurn = !isTurn;
                tv_status.setText("SCAN");

                if(isTurn == true){

                    btn_turn.setText("On");

                    scan_display.setVisibility(View.GONE);
                    img_logo.setVisibility(View.VISIBLE);
                    img_mycode.setVisibility(View.GONE);
                    cameraSource.stop();

                }else{

                    btn_turn.setText("Off");
                    scan_display.setVisibility(View.VISIBLE);
                    img_logo.setVisibility(View.GONE);
                    img_mycode.setVisibility(View.GONE);

                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btn_mycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isUsercode = true;
                tv_status.setText("My Person Code");

                scan_display.setVisibility(View.GONE);
                img_logo.setVisibility(View.GONE);
                img_mycode.setVisibility(View.VISIBLE);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_REQUEST);
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Comming Soon", Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.scanazon.com/share"));
                startActivity(browserIntent);

            }
        });

        tv_savedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        tv_myFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        tv_myReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void RegisterUser(View v){
        Preferences pref = new Preferences(this);
        pref.deleteDate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_REQUEST && resultCode == RESULT_OK){
            img_myqr.setImageURI(data.getData());
        }
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;

                            } else {
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;

                                //txtBarcodeValue.setText(intentData);
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));

//                                Intent intent = new Intent(MainActivity.this, WebscanActivity.class);
//                                intent.putExtra("loadUrl", intentData);
//                                startActivity(intent);

                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public void post_result(String result) {

        try{
            JSONObject obj_result = new JSONObject(result);
            Toast.makeText(MainActivity.this, obj_result.getString("message"),
                    Toast.LENGTH_LONG).show();

            if(obj_result.getString("status").equals("1")){
                showAPIAlert("Alert", "Data successfully uploaded!");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Connect Error", Toast.LENGTH_LONG).show();
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
}
