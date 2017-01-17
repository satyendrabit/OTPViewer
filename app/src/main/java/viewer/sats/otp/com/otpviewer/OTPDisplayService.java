package viewer.sats.otp.com.otpviewer;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class OTPDisplayService extends Service implements OnTouchListener {
    public static final int NOTIFICATION_ID = 1;
    public static String NOTIFCATION_ID = "NOTIFICATIONID";
    public static String OTP_VIEWER_DISMISS = "OTP_VIEWER_DISMISS";
    public static String TAG = OTPDisplayService.class.getSimpleName();
    Button mButton;
    OTPView otpView = null;
    // OTPWindow otpWindow=null;
    WindowManager wm = null;
    String message = null;

    @Override
    public IBinder
    onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //mView = new HUDView(this);
        //  mButton = new Button(this);
        //  otpWindow=new OTPWindow(this);
        // otpWindow=new OTPWindow(this);


        otpView = new OTPView(this);
//        mButton.setText("OTP Message");
        // mButton.setOnTouchListener(this);
        otpView.setOnTouchListener(this);

        if (checkPermission()) {
            buildNotificationForPermission();
            Toast.makeText(this, "Need permissions to work otpViewer, Please grant permission by tapping in NotifcationBar", Toast.LENGTH_SHORT).show();
            stopSelf();/// No need to go further you dont have permission to draw window

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // mButton.setBackgroundColor(getColor(R.color.otpbackground_color));
            // mButton.setTextColor(getColor(R.color.otpTextColor));
            // otpView.setOTPMessage();
        } else {
            //  mButton.setBackgroundColor(getResources().getColor(R.color.otpbackground_color));
            //  mButton.setTextColor(getResources().getColor(R.color.otpTextColor));
        }


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.CENTER;

        params.setTitle("Load Average");
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(otpView, params);
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String number = sharedPrefs.getString(OTP_VIEWER_DISMISS, "0");
        try {
            int val = Integer.parseInt(number);
            if (val > 0) {
                otpView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (otpView != null) {
                            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(otpView);
                            otpView = null;
                            stopSelf();
                        }
                    }
                }, val * 1000);
            }
        } catch (Exception e) {

        }

        // Log.i(TAG, "onReceive: OTPViewer is not Enabled");
        //return;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String message = intent.getStringExtra("message");
//        if(mButton!=null){
//            mButton.setText(message);
//        }
        if (otpView != null) {
            otpView.setOTPMessage(message);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(mButton != null)
//        {
//            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mButton);
//            mButton = null;
//            stopSelf();
//        }
        if (otpView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(otpView);
            otpView = null;
            stopSelf();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (otpView != null) {
            wm.removeViewImmediate(otpView);
            otpView = null;
        }
        stopSelf();
        return false;
    }

    private boolean checkPermission() {

        String permission = "android.permission.SYSTEM_ALERT_WINDOW";
        boolean alerwindowpermission = (this.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        boolean recievemessagepermission = (this.checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED);

        Log.i(TAG, "checkPermission: " + alerwindowpermission + "," + recievemessagepermission);

        return alerwindowpermission && recievemessagepermission;

    }

    public void buildNotificationForPermission() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Permission Required")
                .setContentText(" To work OTPViewer requires SMS Read Permission.")
                .setAutoCancel(true);

        Intent myintent = new Intent(this, OTPViewerSettingsActivity.class);
        myintent.putExtra(NOTIFCATION_ID, NOTIFICATION_ID);
        Intent intent[] = {myintent};

        PendingIntent resultPendingIntent = PendingIntent.getActivities(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

}
