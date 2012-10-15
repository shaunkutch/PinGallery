package com.plastku.pingallery;

import com.plastku.pingallery.R;

import android.content.Context;
import android.widget.Toast;

public class NotificationUtil {
    public static final String TAG = NotificationUtil.class.getSimpleName();

    private NotificationUtil() {
    }

    public static void makeToast(Context context, int resId) {
	makeToast(context, context.getString(resId));
    }

    public static void makeToast(Context context, String message) {
	Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void makeServerErrorToast(Context context) {
	makeToast(context, R.string.http_error);
    }

    public static void makeServerErrorToast(Context context, String message) {
	makeToast(context, message + ", " + context.getString(R.string.http_error));
    }

    public static void makeConnectionProblemToast(Context context) {
	makeToast(context, R.string.http_exception);
    }

    public static void makeConnectionProblemToast(Context context, String message) {
	makeToast(context, message + ", " + context.getString(R.string.http_exception));
    }

    public static void makeParserErrorToast(Context context) {
	makeToast(context, R.string.parsing_exception);
    }

    public static void makeParserErrorToast(Context context, String message) {
	makeToast(context, message + ", " + context.getString(R.string.parsing_exception));
    }
}
