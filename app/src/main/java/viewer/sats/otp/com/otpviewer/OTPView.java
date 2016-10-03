package viewer.sats.otp.com.otpviewer;

import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by inssingh35 on 5/9/2016.
 */
public class OTPView extends LinearLayout {
    private static String TAG = "OTPVIEW";
    LayoutInflater inflater = null;
    private Context context = null;
    private TextView textViewMessage = null;
    private TextView textViewOTP = null;

    public OTPView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.context = context;
        init();
    }

    public void init() {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        View view = inflater.inflate(R.layout.otviewer_layout, this);

        textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        textViewOTP = (TextView) view.findViewById(R.id.textViewOTP);
    }

    public void setOTPMessage(String textmessage) {
        //  textViewMessage.setText(textmessage);
        Pattern pattern = Pattern.compile("\\s{1}[0-9]{4,6}");
        Matcher matcher = pattern.matcher(textmessage);
        Spannable spantext = Spannable.Factory.getInstance().newSpannable(textmessage);
        int start = -1;
        if (matcher.find()) {
            String str = matcher.group();
           // Log.i(TAG, "setOTPMessage is: " + str);
            start = textmessage.indexOf(str);
            textViewOTP.setText("OTP:" + str);
        }
        BackgroundColorSpan bckSpan = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bckSpan = new BackgroundColorSpan(getResources().getColor(R.color.colorotphighlighter, null));
        } else {
            bckSpan = new BackgroundColorSpan(getResources().getColor(R.color.colorotphighlighter));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (start > 0) {
                spantext.setSpan(
                        bckSpan,
                        start, start + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //spantext.setSpan(   ); BackgroundColorSpan(null));
            /// textViewMessage.setText(spantext);
        } else {
            if (start > 0) {
                spantext.setSpan(
                        bckSpan,
                        start, start + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textViewMessage.setText(spantext);
    }


}
