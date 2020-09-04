package com.example.dokiemclm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.core.content.PermissionChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.CALL_PHONE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static com.example.dokiemclm.MainActivity.CASETEST;
import static com.example.dokiemclm.MainActivity.LOAI;
import static com.example.dokiemclm.MainActivity.progressBarWebView;
import static com.example.dokiemclm.MainActivity.textViewID;
import static com.example.dokiemclm.MainActivity.webView;
import static com.example.dokiemclm.MyWebView.wstart;


public class HandlingEmit {
ConnectIOSocket connectIOSocket = new ConnectIOSocket();

    public void XulyLenh(JSONObject strLenh){


        try {
            if(strLenh.getString("CASE").equals(CASETEST)){
                MainActivity.textViewRecieve.setText(strLenh.getString("CASE"));

               if(CASETEST.substring(3,6).equals("CAL")&&LOAI.equals("SEND")){
                   MainActivity.getInstance().makeCall(strLenh.getString("THUEBAONHAN"));
               }else if(CASETEST.substring(3,6).equals("SMS") && LOAI.equals("SEND")){
                   SmsManager smgr = SmsManager.getDefault();
                   connectIOSocket.updateCurrentTime("updatetime1");
                   smgr.sendTextMessage(strLenh.getString("THUEBAONHAN"),null,"TESTSMSCLM".toString(),null,null);


                }else if(CASETEST.substring(3,6).equals("WEB")){
                 //  webView.setWebChromeClient(new MyChormeWebviewClient());

                   wstart=false;
                   webView.loadUrl("about:blank");
                   MainActivity.textViewID.setText("READY");
                   webView.clearCache(true);
                   webView.clearHistory();

                   webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                   webView.getSettings().setAppCacheEnabled(false);
                   webView.clearCache(true);
                   progressBarWebView.setProgress(0);

                   CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
                       public void onTick(long millisUntilFinished) {
                           MainActivity.textViewID.setText("READY   " + millisUntilFinished / 1000 );
                       }

                       @Override
                       public void onFinish() {
                               wstart=true;


                           try {
                               progressBarWebView.setVisibility(View.VISIBLE);
                               webView.loadUrl(strLenh.getString("URL"));
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }


                       }
                   }.start();

               }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
