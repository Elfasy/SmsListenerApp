package com.example.smslistenerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    final String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                if (pdusObj != null){
                    for (Object dataPdusObj : pdusObj){
                        SmsMessage currentMessage = getIncomingMessage(dataPdusObj,bundle);
                        String senderNum = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();
                        Log.d(TAG, "senderNum: " + senderNum + "; message: " + message);
                        Intent intent1 = new Intent(context,SmsReceiverActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, senderNum);
                        intent1.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE,message);
                        context.startActivity(intent1);
                    }
                }else{
                    Log.d(TAG,"onReceive: SMS is null");
                }
            }
        }catch (Exception e){
            Log.d(TAG,"Exception smsReceiver" + e);
        }
    }

    private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage currentSms;
        if (Build.VERSION.SDK_INT >= 23) {
            String format = bundle.getString("format");
            currentSms = SmsMessage.createFromPdu((byte[]) object, format);
        } else {
            currentSms = SmsMessage.createFromPdu((byte[]) object);
        }

        return currentSms;
    }
}
