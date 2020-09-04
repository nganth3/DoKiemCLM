package com.example.dokiemclm;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.json.JSONException;

import static com.example.dokiemclm.MainActivity.progressBarWebView;
import static com.example.dokiemclm.MainActivity.textViewID;
import static com.example.dokiemclm.MyWebView.wstart;

public class MyChormeWebviewClient extends WebChromeClient {
    private int mScreenSite;
    boolean blfinish;
    ConnectIOSocket connectIOSocket = new ConnectIOSocket();


    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
        Log.d("MyChormeWebviewClient", "onShowCustomView");
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (!view.getUrl().equals("about:blank")){

            if(wstart && !MyWebView.complete){
                progressBarWebView.setProgress(newProgress);
                textViewID.setText("LOADDING ... " + newProgress +"%");
                if( newProgress >= 99){
                    progressBarWebView.setProgress(100);
                    connectIOSocket.updateCurrentTime("updatetime2");
                    blfinish = true;
                    MyWebView.complete=true;
                    textViewID.setText("COMPLETE STATUS!");

                    connectIOSocket.updateCurrentTime("updatetime3");
                    textViewID.setText("FINISH STATUS!");
                    progressBarWebView.setVisibility(View.GONE);
                }
            }
        Log.d("MyChormeWebviewClient", newProgress +" " + view.getContentHeight() +" "+MainActivity.getScreenHeight());

        }

    }

}
