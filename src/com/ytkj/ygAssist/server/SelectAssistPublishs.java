package com.ytkj.ygAssist.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.CacheData;

/*
 * 从本地服务器获取数据
 */
public class SelectAssistPublishs {

	/*
	 * 从本地服务器中获取云购揭晓记录数据
	 */
	public static void getYungouPublishs(String goodsID, String codePeriod, String selectNum) {
		StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("yungouPublishAjax_getYungouPublishs"));
		getUrl.append("&goodsID=").append(goodsID);
		getUrl.append("&codePeriod=").append(codePeriod);
		getUrl.append("&selectNum=").append(selectNum);
		try {
			String content = HttpGetUtil.getHttpData(getUrl.toString(), null);
			content = MyStringUtil.changeCharset(content, "ISO-8859-1", "UTF-8");
			ArrayList<String[]> stringList = new Gson().fromJson(content, new TypeToken<ArrayList<String[]>>() {
			}.getType());
			for (String[] str : stringList) {
				if (CacheData.getSelectCacheDate(str[0], str[1]) == null) {
					CacheData.setSelectCacheDate(str[0], str[1], str);
				}
			}
		} catch (Exception e) {
		}
	}

	/*
	 * 检查服务器是否存在该数据
	 */
	public static boolean getYungouPublishs(String goodsID, String codePeriod) {
		StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("yungouPublishAjax_getYungouPublishs"));
		getUrl.append("&goodsID=").append(goodsID);
		getUrl.append("&codePeriod=").append(codePeriod);
		getUrl.append("&selectNum=1");
		try {
			String content = HttpGetUtil.getHttpData(getUrl.toString(), null);
			content = MyStringUtil.changeCharset(content, "ISO-8859-1", "UTF-8");
			ArrayList<String[]> stringList = new Gson().fromJson(content, new TypeToken<ArrayList<String[]>>() {
			}.getType());
			if (stringList.size() == 1) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/*
	 * 删除服务器数据
	 */
	public static void deleteYungouPublish(String goodsID, String codePeriod) {
		StringBuffer getUrl = new StringBuffer("yungouPublishAjax_deleteYungouPublish");
		getUrl.append("&goodsID=").append(goodsID);
		getUrl.append("&codePeriod=").append(codePeriod);
		try {
			HttpGetUtil.getHttpData(getUrl.toString(), null);
		} catch (Exception e) {
		}
	}

	/*
	 * 获取未查询到的数据
	 */
	public static ArrayList<Integer> getYungouPublishsNotData(String goodsID, String codePeriod) {
		StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("yungouPublishAjax_getYungouPublishsNotData"));
		getUrl.append("&goodsID=").append(goodsID);
		getUrl.append("&codePeriod=").append(codePeriod);
		try {
			String content = HttpGetUtil.getHttpData(getUrl.toString(), null);
			ArrayList<Integer> stringList = new Gson().fromJson(content, new TypeToken<ArrayList<Integer>>() {
			}.getType());
			return stringList;
		} catch (Exception e) {
		}
		return new ArrayList<Integer>();
	}

	/*
	 * 上传云购揭晓记录数据到本地服务器中
	 */
	public static void uploadYungouPublishs(String[] text) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("yungouPublishAjax_addYungouPublishs"));
				getUrl.append("&goodsID=").append(text[0]);
				getUrl.append("&codePeriod=").append(text[1]);
				getUrl.append("&codeRNO=").append(text[2]);
				getUrl.append("&userName=").append(text[3]);
				getUrl.append("&buyCount=").append(text[4]);
				getUrl.append("&awardPosition=").append(text[5]);
				getUrl.append("&buyStartPosition=").append(text[6]);
				getUrl.append("&codeID=").append(text[7]);
				HttpGetUtil.getHttpData(getUrl.toString(), null);
			}
		}).start();
	}

	/*
	 * 从本地服务器中获取商品信息
	 */
	public static void getGoodsInfos() {
		getGoodsInfos("0");
	}

	/*
	 * 从本地服务器中获取商品信息
	 */
	public static void getGoodsInfos(String goodsID) {
		StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("yungouPublishAjax_getGoodsInfos"));
		getUrl.append("&goodsID=").append(goodsID);
		try {
			String content = HttpGetUtil.getHttpData(getUrl.toString(), null);
			content = MyStringUtil.changeCharset(content, "ISO-8859-1", "UTF-8");
			ArrayList<String[]> stringList = new Gson().fromJson(content, new TypeToken<ArrayList<String[]>>() {
			}.getType());
			for (String[] str : stringList) {
				CacheData.setGoodsInfoCacheDate(str[0], str);
			}
		} catch (Exception e) {
		}
	}

	public static void uploadGoodsInfos(String[] text) {// 上传商品信息到服务器
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("yungouPublishAjax_addGoodsInfos"));
				String userInfo = text[0] + "__" + text[1] + "__" + text[2] + "__" + text[3] + "__" + text[4] + "__"
						+ text[5];
				try {
					getUrl.append("&userInfo=").append(URLEncoder.encode(userInfo, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				HttpGetUtil.getHttpData(getUrl.toString(), null);
			}
		}).start();
	}

}
