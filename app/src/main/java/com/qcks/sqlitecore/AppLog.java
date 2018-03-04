package com.qcks.sqlitecore;


//import com.tencent.mars.xlog.Log;

import android.util.Log;

/**
 * Wrapper API for sending log output.
 */
public class AppLog {
	protected static final String TAG = "com.qckiss.mybaseapp";
	private static final boolean DEBUG = true;

	private AppLog() {
	}

	/**
	 * Send a VERBOSE log message.
	 *
	 * @param objects
	 *            The message you would like logged.
	 */
	public static void v(Object... objects) {
		if (DEBUG){
			Log.v(TAG, toString(objects));
		}
	}

	/**
	 * Send a DEBUG log message.
	 *
	 * @param objects
	 */
	public static void d(Object... objects) {
		if (DEBUG) {
			Log.d(TAG, toString(objects));
		}
	}

	/**
	 * Send a INFO log message.
	 *
	 * @param objects
	 */
	public static void i(Object... objects) {
		if (DEBUG) {
			Log.i(TAG, toString(objects));
		}
	}

	/**
	 * Send a WARN log message.
	 *
	 * @param objects
	 */
	public static void w(Object... objects) {
		if (DEBUG) {
			Log.w(TAG, toString(objects));
		}
	}

	/**
	 * Send a ERROR log message.
	 *
	 * @param objects
	 */
	public static void e(Object... objects) {
		if (DEBUG) {
			Log.e(TAG, toString(objects));
		}
	}

	private static String toString(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object o : objects) {
			sb.append(o);
			sb.append("  ");
		}
		return sb.toString();
	}
}
