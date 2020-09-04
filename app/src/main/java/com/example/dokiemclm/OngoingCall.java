package com.example.dokiemclm;

import android.os.CountDownTimer;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import io.reactivex.subjects.BehaviorSubject;

import static com.example.dokiemclm.Constants.asString;
import static com.example.dokiemclm.MainActivity.LOAI;
import static com.example.dokiemclm.MainActivity.buttonThoaiKem;
import static com.example.dokiemclm.MainActivity.buttonThoaiTot;
import static com.example.dokiemclm.MainActivity.spinnerLoaiTest;

public class OngoingCall {



    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private static Call call;
    ConnectIOSocket connectIOSocket = new ConnectIOSocket();
    private Object callback = new Call.Callback() {

        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            state.onNext(newState);
            Log.d("STATECALL",""+ newState );
            MainActivity.textViewID.setText(asString(newState) );

            if(LOAI.equals("SEND")){
                switch (newState){
                    case 1:
                        connectIOSocket.updateCurrentTime("updatetime1");
                        break;
                    case 4:
                        connectIOSocket.updateCurrentTime("updatetime2");
                        if(spinnerLoaiTest.getSelectedItem().toString().equals("CALL") &&LOAI.equals("SEND") ){
                            buttonThoaiKem.setVisibility(View.VISIBLE);
                            buttonThoaiTot.setVisibility(View.VISIBLE);
                        }else{
                            buttonThoaiKem.setVisibility(View.GONE);
                            buttonThoaiTot.setVisibility(View.GONE);
                        }
                        break;
                    case 7:
                        connectIOSocket.updateCurrentTime("updatetime3");
                        buttonThoaiKem.setVisibility(View.GONE);
                        buttonThoaiTot.setVisibility(View.GONE);
                        break;
                }
            }
            switch (newState){

                case 4:  countDownTimer.start();      break;
                case 7:    hangup();          break;
            }

        }
    };
    public CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        public void onTick(long millisUntilFinished) {
            MainActivity.textViewID.setText("EndCall in: " + millisUntilFinished / 1000 +"s");
        }

        @Override
        public void onFinish() {
            if(MainActivity.LOAI.equals("RECEIVE")) {
                hangup();
            }

        }
    };
    public final void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback)callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    public void answer() { assert call != null;

        call.answer(VideoProfile.STATE_AUDIO_ONLY);



    }

    public void hangup() {
        assert call != null;
        call.disconnect();
        countDownTimer.cancel();
    }

}
