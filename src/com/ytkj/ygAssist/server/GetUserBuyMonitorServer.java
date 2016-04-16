package com.ytkj.ygAssist.server;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.http.impl.client.CloseableHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;

public class GetUserBuyMonitorServer {
	private Timer timer = null;
	private String goodsID = null;// 商品ID
	private String codePeriod = null;// 购买期数
	private String codeID = null;// 网址ID
	private CloseableHttpClient HttpClient = null;// 查询使用HTTPClient
	private static HashMap<String, GetUserBuyMonitorServer> getUserBuyMonitorServerMap = new HashMap<String, GetUserBuyMonitorServer>();
	private int userBuyNum;// 用户购买数

	private TreeMap<String, String[]> userBuyListMap = new TreeMap<String, String[]>(new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return Integer.parseInt(o2) - Integer.parseInt(o1);
		}
	});

	public static GetUserBuyMonitorServer selectBarcodernoInfo(String goodsID, String codePeriod, String codeID) {
		// if (CacheData.getGoodsPriceCacheDate(goodsID) > 300) {
		if (getUserBuyMonitorServerMap.get(codeID) == null) {
			GetUserBuyMonitorServer getUserBuyMonitorServer = new GetUserBuyMonitorServer(goodsID, codePeriod, codeID);
			getUserBuyMonitorServerMap.put(codeID, getUserBuyMonitorServer);
		}
		// }
		return getUserBuyMonitorServerMap.get(codeID);
	}

	public GetUserBuyMonitorServer(String goodsID, String codePeriod, String codeID) {
		this.HttpClient = HttpGetUtil.createHttpClient();
		GetGoodsPeriodInfo(goodsID, codePeriod, codeID);
	}

	/*
	 * 查询商品期数是否存在 goodsID商品id period期数
	 */
	public void GetGoodsPeriodInfo(String goodsID, String codePeriod, String codeID) {
		this.goodsID = goodsID;
		this.codePeriod = codePeriod;
		this.codeID = codeID;
		// getCodeNum();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String[] text = GetGoodsInfo.shopCartNew(CacheData.getGoodsInfoCacheDate(goodsID)[2], HttpClient);
				// String[] text = GetGoodsInfo.shopCartNew("4465406",
				// HttpClient);
				if (text != null) {
					if (codeID.equals(text[0])) {
						if (Integer.parseInt(text[2]) - userBuyNum > 10) {
							userBuyNum = Integer.parseInt(text[2]);
							getCodeNum();
						}
					} else {
						System.out.println("取消" + goodsID + ":" + codePeriod);
						// new Thread(new Runnable() {
						// public void run() {
						// String string =
						// GetGoodsInfo.getUserBuyListEnd(codeID, 1,
						// HttpClient);
						// formatData(string, 1);
						// HttpGetUtil.CloseHttpClient(HttpClient);
						// System.out.println("总价格" +
						// YungouDataTools.getCodeNum(codeID));
						// System.out.println("总大小" +
						// CacheData.getUserBuyListCacheDate(codeID).size());
						// System.out.println("缓存大小" + userBuyListMap.size());
						// }
						// }).start();
						timer.cancel();
						getUserBuyMonitorServerMap.remove(codeID);
					}
				}
			}
		}, 1000, 1000);
	}

	/*
	 * 获取中奖位置
	 */
	public void getCodeNum() {
		getUserBuyList(1);
	}

	/*
	 * 获取参与记录
	 */
	private void getUserBuyList(int FIdx) {
		try {
			String content = GetGoodsInfo.getUserBuyList(codeID, FIdx, HttpClient);
			formatData(content, FIdx);
		} catch (Exception e) {
			getUserBuyList(FIdx);
		}
	}

	/*
	 * 输出获取到的信息
	 */
	private void formatData(String content, int FIdx) {
		// data = content.split("Rows\":", 2)[1];
		String data = content.substring(content.indexOf("Rows\":") + 6, content.length() - 5);
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		boolean isHave = true;
		if (userBuyListMap.get(dList.get(dList.size() - 1).get("buyID")) != null) {
			isHave = false;
		}
		for (Map<String, String> maps : dList) {
			String buyCode = getUserBuyCode(maps.get("buyID"));
			String[] info = new String[] { maps.get("buyNum"), maps.get("userName"), maps.get("userWeb"), buyCode };
			userBuyListMap.put(maps.get("buyID"), info);
		}
		if (FIdx != 1 && dList.size() != 10) {
			isHave = false;
		}
		int listCount = getUserBuyListCount(content);
		if (userBuyListMap.size() == listCount) {
			isHave = false;
			CacheData.setUserBuyListCacheDate(codeID, userBuyListMap);
		}
		if (isHave) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			getUserBuyList(FIdx + 10);
		}
		System.out.println("大小" + goodsID + ":" + codePeriod + ":" + listCount + ":"
				+ CacheData.getUserBuyListCacheDate(codeID).size());
	}

	/*
	 * 获取用户云够码
	 */
	private String getUserBuyCode(String buyIP) {
		try {
			String content = GetGoodsInfo.getUserBuyCode(codeID, buyIP);
			return sortBuyCode(content);
		} catch (Exception e) {
			return getUserBuyCode(buyIP);
		}
	}

	/*
	 * 输出云够码
	 */
	private String sortBuyCode(String content) {
		String data = content.split(",'data':", 2)[1];
		data = data.substring(0, data.length() - 2);
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		StringBuffer sBuffer = new StringBuffer();
		for (Map<String, String> maps : dList) {
			sBuffer.append(maps.get("rnoNum")).append(",");
		}
		String ronNum = sBuffer.toString();
		return ronNum.substring(0, ronNum.length() - 1);
	}

	/*
	 * 获取总页数
	 */
	private int getUserBuyListCount(String content) {
		String data = content.substring(content.indexOf("Count\":") + 7, content.indexOf(",\"Data"));
		return Integer.parseInt(data);
	}
}
