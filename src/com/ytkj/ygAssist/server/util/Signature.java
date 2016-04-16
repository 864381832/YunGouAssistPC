package com.ytkj.ygAssist.server.util;

public class Signature {
	/**
	 * 安卓端加密格式
	 */
	public static String getSignature(String userid, String password, String time, String isLogin) {
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		basestring.append("智");
		basestring.append(userid);
		basestring.append("能");
		basestring.append(password);
		basestring.append("云");
		basestring.append(time);
		basestring.append("购");
		basestring.append(isLogin);
		// 使用MD5对待签名串求签
		return MyStringUtil.getMd5Encode(basestring.toString()).substring(12, 20);
	}

	/*
	 * 服务器端加密格式
	 */
	public static String getServerSignature(String userid, long time) {
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		basestring.append("智能");
		basestring.append(userid);
		basestring.append("云购");
		basestring.append(time);
		// 使用MD5对待签名串求签
		return MyStringUtil.getMd5Encode(basestring.toString()).substring(12, 20);
	}

	/**
	 * 检查一条完整的包含签名参数其签名是否正确
	 */
	public static boolean checkUrlSignature(String userid, String password, String time, String isLogin, String sig) {
		if (sig == null || sig.length() != 8) {
			return false;
		} else {
			String signature = getSignature(userid, password, time, isLogin);
			return sig.equals(signature);
		}
	}

	/**
	 * 检查服务器返回数据签名是否正确
	 */
	public static boolean checkServerSignature(String userid, long time, String sig) {
		if (sig == null || sig.length() != 8) {
			return false;
		} else {
			String signature = getServerSignature(userid, time);
			return sig.equals(signature);
		}
	}
}
