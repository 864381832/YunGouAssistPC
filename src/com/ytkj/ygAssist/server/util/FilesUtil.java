package com.ytkj.ygAssist.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.filechooser.FileSystemView;

import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;

public class FilesUtil {
	/*
	 * 获取缓冲目录
	 */
	public static String getDefaultDirectory() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		return fsv.getDefaultDirectory().toString() + "\\YunGouAssist";
	}

	/**
	 * 读取文件的内容，并将文件内容以字符串的形式返回。
	 * 
	 */
	public static File createFile(String fileName) {
		getDefaultDirectory();
		try {
			File file = new File(getDefaultDirectory());
			if (!file.isDirectory()) {
				file.mkdir();
			}
			file = new File(getDefaultDirectory() + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			return file;
		} catch (Exception e) {
		}
		return null;
	}

	// 保存信息
	public static void saveUserInfo(String infoName, String userid, String password) {
		try {
			File file = FilesUtil.createFile(infoName);
			FileOutputStream out = new FileOutputStream(file, false);
			out.write(userid.getBytes("utf-8"));
			out.write("\n".getBytes("utf-8"));
			out.write(password.getBytes("utf-8"));
			out.close();
		} catch (Exception e) {
		}
	}

	// 读取信息
	public static String[] readerUserInfo(String infoName) {
		try {
			File file = FilesUtil.createFile(infoName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String userInfo[] = new String[2];
			userInfo[0] = br.readLine();
			userInfo[1] = br.readLine();
			// temp=br.readLine();
			// while(temp!=null){
			// sb.append(temp+" ");
			// temp=br.readLine();
			// }
			br.close();
			return userInfo;
		} catch (Exception e) {
		}
		return new String[] { "", "" };
	}

	// 保存文本信息
	public static void saveFileInfo(String infoName, String info) {
		try {
			File file = FilesUtil.createFile(infoName);
			FileOutputStream out = new FileOutputStream(file, false);
			out.write(info.getBytes("utf-8"));
			out.close();
		} catch (Exception e) {
		}
	}

	// 读取信息
	public static String readerFileInfo(String infoName) {
		try {
			File file = FilesUtil.createFile(infoName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String userInfo = br.readLine();
			br.close();
			return userInfo;
		} catch (Exception e) {
		}
		return "";
	}

	// 保存商品信息
	public static void saveGoodsInfoFile(String info) {
		try {
			File file = FilesUtil.createFile("\\goodsInfo.log");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8"));
			out.write(info);
			out.write("\n");
			out.close();
		} catch (Exception e) {
		}
	}

	// 读取商品名称信息
	public static void readerGoodsInfoFile() {
		try {
			File file = FilesUtil.createFile("\\goodsInfo.log");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String goodsInfo = br.readLine();
			while (goodsInfo != null) {
				String[] goods = goodsInfo.split("___");
				CacheData.setGoodsNameCacheDate(goods[0], goods[1]);
				CacheData.setGoodsInfoCacheDate(goods[0], new String[] { goods[0], goods[1], goods[2], "0" });
				goodsInfo = br.readLine();
			}
			br.close();
		} catch (Exception e) {
		}
	}

	// 读取信息
	public static void readerConfigFile() {
		try {
			File file = new File("jre\\config.ini");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			Config.yunGouAssistUserInfo = br.readLine();
			Config.yunGouRegisterUrl = br.readLine();
			Config.yunGouRegisterQQqunName = br.readLine();
			Config.yunGouRegisterQQqunUrl = br.readLine();
			br.close();
		} catch (Exception e) {
		}
	}

	// 保存商品信息
	public static void editGoodsInfoFile() {
		try {
			File file = FilesUtil.createFile("\\goodsInfo.log");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "utf-8"));
			ConcurrentHashMap<String, String> goodsMap = CacheData.getGoodsNameCacheDate();
			for (Entry<String, String> goodsId : goodsMap.entrySet()) {
				String info = goodsId.getKey() + "___" + goodsId.getValue() + "___"
						+ CacheData.getGoodsInfoCacheDate(goodsId.getKey())[2];
				out.write(info);
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
		}
	}

}
