package com.example.dokiemclm;

import android.os.CountDownTimer;
import android.telecom.Call;
import android.telecom.InCallService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallService extends InCallService {

    private OngoingCall ongoingCall;

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        new OngoingCall().setCall(call);
        ongoingCall = new OngoingCall();

         if(MainActivity.LOAI.equals("RECEIVE")) {
             ongoingCall.answer();
            // countDownTimer.start();
         }

            // CallActivity.start(this, call);
    }


    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        new OngoingCall().setCall(null);
    }
}
