package com.example.dokiemclm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import kotlin.collections.ArraysKt;

import static android.Manifest.permission.CALL_PHONE;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    ;
    private String URL_SERVER;
    public static String DEVICE, CASETEST, LOAI;
    public static int REQUEST_PERMISSION = 0;

    public static WebView webView;
    public  static ProgressBar progressBarWebView;
    private static MainActivity instance;
    public static ConnectIOSocket cnIOSocket;
    public static Spinner spinnerLoaiTest, spinnerKhuVuc, spinnerMang, spinnerMay;
    public static  Button buttonConnect,buttonThoaiKem,buttonThoaiTot;
    public static EditText editTextIPServer;
    public static TextView textViewID, textViewRecieve;

    //  PhoneCustomStateListener customStateListener= new PhoneCustomStateListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //   ButterKnife.bind(this);
        instance = this;
        setContentView(R.layout.activity_main);


        AnhXa();
        spinnerLoaiTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("spinnerLoaiTest", i + "");
                if (spinnerLoaiTest.getSelectedItem().toString() == "WEB") {
                    spinnerMay.setSelection(0);
                    spinnerMay.setEnabled(false);
                    webView.setVisibility(View.VISIBLE);

                } else {
                    spinnerMay.setEnabled(true);
                    webView.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // makeCall();
                //Toast.makeText(MainActivity.this,buttonConnect.getText(),Toast.LENGTH_SHORT).show();;

                textViewID.setText("");
                textViewRecieve.setText("");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PREFS_URL", editTextIPServer.getText().toString()).apply();


                if (buttonConnect.getText().equals("Connect")) {
                    buttonConnect.setText("Connecting...");
                    DEVICE = spinnerKhuVuc.getSelectedItem().toString() + spinnerLoaiTest.getSelectedItem().toString() + spinnerMang.getSelectedItem().toString() + spinnerMay.getSelectedItem().toString();
                    CASETEST = spinnerKhuVuc.getSelectedItem().toString() + spinnerLoaiTest.getSelectedItem().toString() + spinnerMang.getSelectedItem().toString();
                    LOAI = spinnerMay.getSelectedItem().toString();

                    URL_SERVER = "http://" + editTextIPServer.getText().toString();
                    cnIOSocket.setmSocket(URL_SERVER);



                } else if (buttonConnect.getText().equals("Disconnect")) {
                    cnIOSocket.disconect();
                }



               // getsignal();
            }
        });
        buttonThoaiKem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnIOSocket.sendevent("CHATLUONGTHOAI","NOK");
                Toast.makeText(MainActivity.this,"ĐÃ CẬP NHẬT CHẤT LƯỢNG THOẠI KÉM",Toast.LENGTH_LONG).show();;
            }
        });
        buttonThoaiTot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnIOSocket.sendevent("CHATLUONGTHOAI","OK");
                Toast.makeText(MainActivity.this,"ĐÃ CẬP NHẬT CHẤT LƯỢNG THOẠI TỐT",Toast.LENGTH_LONG).show();;
            }
        });

    }

    void AnhXa() {


        cnIOSocket = new ConnectIOSocket();
        spinnerLoaiTest = findViewById(R.id.spinner_loaitest);
        spinnerKhuVuc = findViewById(R.id.spinner_khuvuc);
        spinnerMang = findViewById(R.id.spinner_mang);
        spinnerMay = findViewById(R.id.spinner_may);
        buttonConnect = findViewById(R.id.button_Connect);
        editTextIPServer = findViewById(R.id.editTextTextPersonName);
        sharedPreferences = getSharedPreferences("PREFS_URL", Context.MODE_PRIVATE);

        editTextIPServer.setText(sharedPreferences.getString("PREFS_URL", "192.168.1.111:3000"));
        buttonThoaiKem= findViewById(R.id.buttonThoaiKem);
        buttonThoaiTot=findViewById(R.id.buttonThoaiTot);

        webView = findViewById(R.id.WebView1);
        progressBarWebView=findViewById(R.id.progressBarWebView);
        progressBarWebView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        webView.setWebViewClient(new MyWebView() );
        webView.setWebChromeClient(new MyChormeWebviewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(false);
        webView.clearCache(true);



        buttonThoaiKem.setVisibility(View.GONE);
        buttonThoaiTot.setVisibility(View.GONE);
        // webView.loadUrl("https://vnexpress.net/");
        textViewID = findViewById(R.id.textView_id);
        textViewRecieve = findViewById(R.id.textView_recieve);
        ArrayList<String> arrayListLoaiTest = new ArrayList<String>();

        arrayListLoaiTest.add("SMS");
        arrayListLoaiTest.add("CALL");
        arrayListLoaiTest.add("WEB");

        ArrayAdapter arrayAdapterLoaiTest = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListLoaiTest);
        spinnerLoaiTest.setAdapter(arrayAdapterLoaiTest);

        ArrayList<String> arrayListKhVuc = new ArrayList<String>();

        arrayListKhVuc.add("KV3");
        arrayListKhVuc.add("KV2");
        arrayListKhVuc.add("KV1");

        ArrayAdapter arrayAdapterKhuVuc = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListKhVuc);
        spinnerKhuVuc.setAdapter(arrayAdapterKhuVuc);

        ArrayList<String> arrayListMang = new ArrayList<String>();
        arrayListMang.add("VIETTEL");
        arrayListMang.add("MOBI");
        arrayListMang.add("VINA");
        ArrayAdapter arrayAdapterMang = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListMang);
        spinnerMang.setAdapter(arrayAdapterMang);

        ArrayList<String> arrayListMay = new ArrayList<String>();
        arrayListMay.add("SEND");
        arrayListMay.add("RECEIVE");
        ArrayAdapter arrayAdapterMay = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListMay);
        spinnerMay.setAdapter(arrayAdapterMay);

    }

    public void makeCall(String phonenumber) {
        // String phonenumber="0974101418";

        if (PermissionChecker.checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED) {
            Uri uri = Uri.parse("tel:" + phonenumber.trim());
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        }
    }

    public String getsignal() {
       return getSignalStrength(this);
   }
    private static String getSignalStrength(Context context) throws SecurityException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String strength = null;
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            strength = String.valueOf(telephonyManager.getSignalStrength().getLevel());
        }else {
            if (cellInfos != null) {
                for (int i = 0; i < cellInfos.size(); i++) {
                    if (cellInfos.get(i).isRegistered()) {
                        Log.d("signal", String.valueOf(cellInfos.get(i)));
                        if (cellInfos.get(i) instanceof CellInfoWcdma) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfos.get(i);
                            CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                            strength =  String.valueOf(cellSignalStrengthWcdma.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                            CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfos.get(i);
                            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                            strength = String.valueOf(cellSignalStrengthGsm.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfos.get(i);
                            CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                            strength =String.valueOf(cellSignalStrengthLte.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfos.get(i);
                            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                            strength =String.valueOf(cellSignalStrengthCdma.getLevel());
                        }
                        break;
                    }
                }

            }
        }
        return strength;
    }
    @Override
    protected void onStart() {
        super.onStart();
        offerReplacingDefaultDialer();
    }

    public static MainActivity getInstance(){        return instance;    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && ArraysKt.contains(grantResults, PERMISSION_GRANTED)) {

        }
    }

    private void offerReplacingDefaultDialer() {
        //XIN QUYENG CAI DAT MAC DINH
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

        if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
            Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }
    }
   public void changView(Boolean value){

        spinnerKhuVuc.setEnabled(value);
        spinnerLoaiTest.setEnabled(value);
        spinnerMang.setEnabled(value);
        spinnerMay.setEnabled(value);
        editTextIPServer.setEnabled(value);
        if (value) {
            buttonConnect.setText("Connect");
        }else{
            buttonConnect.setText("Disconnect");


        }
    }

}