package com.example.dokiemclm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED ="android.provider.Telephony.SMS_RECEIVED";
    ConnectIOSocket connectIOSocket = new ConnectIOSocket();
    String msg, phoneNo ="";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        if (intent.getAction() == SMS_RECEIVED) {
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[mypdu.length];
                for (int i = 0; i < mypdu.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String fortmat = dataBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], fortmat);

                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }
                    msg = "";
                    for (int j = 0; j <= i; j++) {
                        msg = msg + messages[j].getMessageBody();
                    }
                    phoneNo = messages[i].getOriginatingAddress();
                }
                Log.d("msg",phoneNo +"___"+ msg);
                if(msg.equals("TESTSMSCLM")) {
                    connectIOSocket.updateCurrentTime("updatetime2");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            connectIOSocket.updateCurrentTime("updatetime3");
                        }
                    }, 3000);

                }
            }


        }
    }
}
