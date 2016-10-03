package viewer.sats.otp.com.otpviewer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Satyendra";
    private static final int SMS_REQUEST = 10001;
    private static final int REQUEST_CODE = 10002;
    private MySmsReciever smsReciever = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // startService(new Intent(this,OTPWatcherService.class));

        //  Toast.makeText(this,"Service Started",Toast.LENGTH_LONG).show();;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
            checkDrawOverlayPermission();
        } else {
            registerSMSReciever();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {

        if (super.checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            super.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, SMS_REQUEST);

        } else {
            registerSMSReciever();
        }

    }

    public void registerSMSReciever() {

        Log.i(TAG, "registerSMSReciever: started");
//        IntentFilter intentFilter=new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        //Intent intent=new Intent(this,OTPWatcherService.class);
        //startService(intent);
//        IntentFilter intentFilter=new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
//        smsReciever=new MySmsReciever();
//        registerReceiver(smsReciever,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  unregisterReceiver(smsReciever);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case SMS_REQUEST:
                if (grantResults.length == 1) {
                    registerSMSReciever();
                } else {
                    Toast.makeText(this, "Permission requires", Toast.LENGTH_LONG).show();
                }
                return;

            default:
                return;

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {

            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
            }
        }
    }

}
