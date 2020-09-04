package com.example.dokiemclm;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import static com.example.dokiemclm.MainActivity.textViewID;
import static com.example.dokiemclm.MainActivity.webView;

public class MyWebView extends WebViewClient {
   ConnectIOSocket connectIOSocket = new ConnectIOSocket() ;
   public  static  Boolean wstart;
   private  Boolean loaded=false;
    public static Boolean complete=false;
    int mScreenSite;

    private boolean isRedirected= true;
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(wstart){
                connectIOSocket.updateCurrentTime("updatetime1");
                complete=false;
            }

    }



    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        Log.d("CheckERR","onReceivedSslError");
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);

            view.clearHistory();


    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if(wstart) {
            if (loaded) {
                return;
            }
            complete=true;
            loaded = true;
            connectIOSocket.updateCurrentTime("ERRORLOADPAGE");
            textViewID.setText("ERROR LOADPAGE");
            connectIOSocket.updateCurrentTime("updatetime3");
            Log.d("CheckERR","onReceivedError");

        }

    }




}
