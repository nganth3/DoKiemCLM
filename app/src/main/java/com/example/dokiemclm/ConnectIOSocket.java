package com.example.dokiemclm;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.dokiemclm.MainActivity.CASETEST;
import static com.example.dokiemclm.MainActivity.LOAI;

public class ConnectIOSocket extends AppCompatActivity {

    private static Socket mSocket;

    public void setmSocket(String URL_SERVER) {
        try {
            mSocket = IO.socket(URL_SERVER);
            mSocket.io().open();
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("disconnect",onDisconnect);
        mSocket.on("BroadcastTestCase",onRecevData);
        mSocket.on("connect",onConnect);

    }
    public void sendevent(String event, String noidung){
        JSONObject data = new JSONObject();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault()).format(new Date());
        try {
            data.put("CASE", CASETEST);
            data.put("LOAI", LOAI);
            data.put("EVENT", event);
            data.put("TIME", currentTime);
            data.put("VACHSONG", MainActivity.getInstance().getsignal());
            data.put("CHATLUONGTHOAI", noidung);
            mSocket.emit(event,data);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updateCurrentTime(String event){
        JSONObject data = new JSONObject();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault()).format(new Date());
        try {
            data.put("CASE", CASETEST);
            data.put("LOAI", LOAI);
            data.put("EVENT", event);
            data.put("TIME", currentTime);
            data.put("VACHSONG", MainActivity.getInstance().getsignal());
            mSocket.emit(event,data);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void disconect(){
        if(mSocket!=null){
            mSocket.disconnect();
            mSocket.close();
        }
    }
    public Boolean getStateConnect(){
        return mSocket.connected();

    }
    public Emitter.Listener onRecevData = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String Data = object.getString("noidung") ;
                       Log.d("onRecevData", Data);
                       JSONObject Data2 = new JSONObject(Data);
                       Log.d("onRecevData",object.toString());
                       HandlingEmit handlingemit= new HandlingEmit();
                       handlingemit.XulyLenh(Data2);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("onRecevData",e.toString());
                    }
                }
            });

        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.d("IOXXX", "onDisconnect");
                Log.d("IOXXX", "mSocket  onDisconnect " + mSocket.connected());
                mSocket.disconnect();
                mSocket.close();
                MainActivity.getInstance().changView(true);

            }
        });

    }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.emit("user-join",MainActivity.DEVICE);

                    Log.d("IOXXX", "mSocket onConnect " + mSocket.connected());
                    MainActivity.getInstance().changView(false);

                }
            });

        }
    };
}
