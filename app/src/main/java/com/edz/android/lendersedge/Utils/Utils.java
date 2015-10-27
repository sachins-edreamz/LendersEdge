package com.edz.android.lendersedge.Utils;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {

    public static final String INTERNET_CONNECTION = "No network connection available.";

    public static String[] strRateIndex = new String[]{"1-month LIBOR",
            "3-month LIBOR", "6-month LIBOR", "12-month LIBOR", "Prime", "CMT"};

    public static String[] strRateIndexIds = new String[]{"1MLIBOR",
            "3MLIBOR", "6MLIBOR", "12MLIBOR", "PRIME", "FWDPRM"};

    public static String strPayFreq[] = new String[]{"Monthly", "Quarterly",
            "Semi Annual", "Annual"};

    public static String[] strPayFreqIds = new String[]{"M", "Q", "S", "A"};

    public static String strIntDayCount[] = new String[]{"Actual/360",
            "Actual/365", "Actual/Actual", "365/360", "30/360"};

    public static String strIndDayCountId[] = new String[]{"A360", "A365",
            "ActAct", "365360", "30360"};

    private Context mContext;
    private Dialog mDlg;

    public Utils(Context mContext) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
    }

    public boolean isInterentConnection() {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info[] = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

        }
        return false;
    }


    public void mToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public double convertStringToDouble(String str) {
        if (str.trim().equals("")) {
            return 0;
        }
        return Double.parseDouble(str.trim());
    }
}