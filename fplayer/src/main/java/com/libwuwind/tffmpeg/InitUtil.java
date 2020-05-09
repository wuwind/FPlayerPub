package com.libwuwind.tffmpeg;

import java.util.UUID;

public class InitUtil {
	public native static void init();
	
	public static String getUuid(){
		return UUID.randomUUID().toString();
	}
//	static{
//		System.loadLibrary("myffmpeg");
//	}
}
