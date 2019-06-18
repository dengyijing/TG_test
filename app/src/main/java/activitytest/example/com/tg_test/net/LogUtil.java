package activitytest.example.com.tg_test.net;

import android.util.Log;

import java.nio.ByteBuffer;

public class LogUtil {

	public static String TAG = "LogUtil: ";

	/** 锁，是否关闭Log日志输出 */
	public static boolean LogOFF = true;
	/** 文件 */
//	public static boolean LogFile = false;
	/** 五种Log日志类型 */
	/** 调试日志类型 */
	public static final int DEBUG = 111;
	/** 错误日志类型 */
	
	public static final int ERROR = 112;
	/** 信息日志类型 */
	public static final int INFO = 113;
	/** 详细信息日志类型 */
	public static final int VERBOSE = 114;
	/** 警告日志类型 */
	public static final int WARN = 115;

	/** 显示，打印日志 */
	public static void LogShow(String Message, int Style) {
//		if (LogFile) {
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//			String date = df.format(new Date());
//			UMFileHelper.fileAppend(null, "umyoushi.log",
//					(date + Message).getBytes());
//			return;
//		}
		if (LogOFF) {
			switch (Style) {
			case DEBUG: {
				Log.d(TAG, Message);
			}
				break;
			case ERROR: {
				Log.e(TAG, Message);
			}
				break;
			case INFO: {
				Log.i(TAG, Message);
			}
				break;
			case VERBOSE: {
				Log.v(TAG, Message);
			}
				break;
			case WARN: {
				Log.w(TAG, Message);
			}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 打印错误基本的信息
	 * @param str 要打印的信息
	 */
	public static void e(String str){
		if(LogOFF){
			Log.e(TAG,str);
		}
	}
	/**
	 * 打印警告基本的信息
	 * @param str 要打印的信息
	 */
	public static void w(String str){
		if(LogOFF){
			Log.w(TAG,str);
		}
	}
	/**
	 * 打印调试基本的信息
	 * @param str 要打印的信息
	 */
	public static void d(String str){
		if(LogOFF){
			Log.d(TAG,str);
		}
	}

	/**
	 * 打印时间参数
	 * @param str 要打印的信息
	 */
	public static void t(String str){
		if(LogOFF && false){
			Log.d(TAG + "Time",str + ":" + System.currentTimeMillis());
		}
	}

	/**
	 * 
	 * @param sPacket
	 */
	public static String packetToHexString(ByteBuffer sPacket) {
		byte cTemp;
		String sRet = "";
		String sTemp = "";
		for (int i = 0; i < sPacket.position(); i++) {
			sTemp = "";
			cTemp = sPacket.get(i);
			sTemp = Integer.toHexString(cTemp);
			if (sTemp.length() < 2) {
				sTemp = "0" + sTemp;
			}
			sTemp = sTemp.substring(sTemp.length() - 2, sTemp.length());
			sRet += "   " + sTemp;
		}
		return sRet.toUpperCase();
	}
}
