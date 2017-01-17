package viewer.sats.otp.com.otpviewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySmsReciever extends BroadcastReceiver {
    public static String OTP_ENABLED = "otp_viewer_switch";
    public static String OTP_FILTER = "OTP_FILTER_TEXT";
    public static String TAG = "MYSMSRECIEVER";

    public MySmsReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (!sharedPrefs.getBoolean(OTP_ENABLED, false)) {
            Log.i(TAG, "onReceive: OTPViewer is not Enabled");
            return;
        }


        String filterstring = "OTP,Password,Dynamic Access Code,Access code,Onetime,one time password";
        filterstring += sharedPrefs.getString(OTP_FILTER, "");


        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                strMessage += messages[i].getOriginatingAddress() + "\n";
                // strMessage += " : ";
                strMessage += messages[i].getMessageBody();
                strMessage += "\n";
            }

            Log.e("SMS", strMessage);
            if (isOTPMessage(strMessage, filterstring)) {
                Intent intent1 = new Intent(context.getApplicationContext(), OTPDisplayService.class);
                intent1.putExtra("message", strMessage + "\n Tap To Close OTPViewer");
                context.startService(intent1);
                //   Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public boolean isOTPMessage(String message, String filterstring) {

        message = message.toLowerCase();
        filterstring = filterstring.toLowerCase();
        String filter[] = filterstring.split(",");
        for (String str : filter) {
            if (message.contains(str)) {
                return true;
            }
        }
        filter = filterstring.split(" ");
        for (String str : filter) {
            if (message.contains(str)) {
                return true;
            }
        }
        return false;
    }
}
