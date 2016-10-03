package viewer.sats.otp.com.otpviewer;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

public class OTPWatcherService extends Service {

    public static String TAG = OTPWatcherService.class.getSimpleName();
    public MySmsReciever smsReciever = null;

    public OTPWatcherService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerSMSReciever();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if(smsReciever==null){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while(true){
//                        try {
//                            Thread.sleep(10);
//                        }catch (Exception e){
//
//                        }
//                    }
//                }
//            }).start();
//        }
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }


    public void startSMSRecieverThread() {

    }

    public void registerSMSReciever() {

        Log.i(TAG, "registerSMSReciever: started");
//        IntentFilter intentFilter=new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        smsReciever = new MySmsReciever();
        registerReceiver(smsReciever, intentFilter);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        if (smsReciever != null) {
            unregisterReceiver(smsReciever);
           // Log.i(TAG, "onDestroy: UnregisterReciver");
        }
    }
}
