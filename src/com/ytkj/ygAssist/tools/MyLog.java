package com.ytkj.ygAssist.tools;

public class MyLog {
	public static void outLog(String name, Object... value) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < value.length; i++) {
			stringBuffer.append(":").append(value[i]);
		}
		System.out.println(name + stringBuffer.toString());
	}
}
