package com.slidenerd.domparsingexample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {
    public static void m(String message)
    {
        Log.d("vivz", message);
    }
    public static void s(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
