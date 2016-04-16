package com.ytkj.ygAssist.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.client.CloseableHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;

public class GetUserBuySortMonitorServer {
	private Timer timer = null;
	private String goodsID = null;// 商品ID
	private String codePeriod = null;// 购买期数
	private String codeID = null;// 网址ID
	private CloseableHttpClient HttpClient = null;// 查询使用HTTPClient
	private static HashMap<String, GetUserBuySortMonitorServer> getUserBuyMonitorServerMap = new HashMap<String, GetUserBuySortMonitorServer>();
	private int userBuyNum = 0;// 用户购买数
	private ArrayList<String[]> userBuyList = new ArrayList<String[]>();
	private ArrayList<String> userBuyListString = new ArrayList<String>();

	public static GetUserBuySortMonitorServer selectBarcodernoInfo(String goodsID, String codePeriod, String codeID) {
		// if (CacheData.getGoodsPriceCacheDate(goodsID) > 300) {
		if (getUserBuyMonitorServerMap.get(codeID) == null) {
			GetUserBuySortMonitorServer getUserBuyMonitorServer = new GetUserBuySortMonitorServer(goodsID, codePeriod,
					codeID);
			getUserBuyMonitorServerMap.put(codeID, getUserBuyMonitorServer);
		}
		// }
		return getUserBuyMonitorServerMap.get(codeID);
	}

	public GetUserBuySortMonitorServer(String goodsID, String codePeriod, String codeID) {
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
				// String[] text =
				// GetGoodsInfo.shopCartNew(CacheData.getGoodsInfoCacheDate(goodsID)[2],
				// HttpClient);
				String[] text = GetGoodsInfo.shopCartNew("4465406", HttpClient);
				if (text != null) {
					if (codeID.equals(text[0])) {
						if (Integer.parseInt(text[2]) - userBuyNum > 10) {
							userBuyNum = Integer.parseInt(text[2]);
							getCodeNum();
						}
					} else {
						getCodeNum();
						System.out.println("取消" + goodsID + ":" + codePeriod);
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
		getUserBuyList(userBuyList.size() / 10 * 10 + 1);
	}

	/*
	 * 获取参与记录
	 */
	private void getUserBuyList(int FIdx) {
		try {
			String content = GetGoodsInfo.getUserBuyListSort(codeID, FIdx, HttpClient);
			formatData(content, FIdx);
			if (getUserBuyListCount(content) > userBuyList.size()) {
				Thread.sleep(1000);
				getUserBuyList(userBuyList.size() / 10 * 10 + 1);
			}
		} catch (Exception e) {
			getUserBuyList(FIdx);
		}
	}

	/*
	 * 输出获取到的信息
	 */
	private void formatData(String content, int FIdx) {
		// data = content.split("Rows\":", 2)[1];
		String data = content.substring(content.indexOf("Rows':") + 6, content.length() - 2);
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		for (Map<String, String> maps : dList) {
			String buyTime = maps.get("buyTime");
			String userWeb = maps.get("userWeb");
			if (!isAddUserBuyList(buyTime + userWeb)) {
				userBuyListString.add(buyTime + userWeb);
				String[] info = new String[] { buyTime, maps.get("buyNum"), maps.get("userName"), userWeb };
				userBuyList.add(info);
			}
		}
		System.out.println("大小" + goodsID + ":" + codePeriod + ":" + userBuyList.size() + ":"
				+ CacheData.getUserBuyListCacheDate(codeID).size());
	}

	private boolean isAddUserBuyList(String string) {// 是否已经添加
		for (String str : userBuyListString) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 输出云够码
	 */
	private int getUserBuyCode(String buyID, String codeRNO) {
		int index = 1;
		String content = GetGoodsInfo.getUserBuyCode(codeID, buyID);
		String data = content.split(",'data':", 2)[1];
		data = data.substring(0, data.length() - 2);
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		for (Map<String, String> maps : dList) {
			if (maps.get("rnoNum").equals(codeRNO)) {
				return index;
			}
			index++;
		}
		return 0;
	}

	private void getBarcodernoInfo() {
		try {
			List<Map<String, String>> list = GetGoodsInfo.getBarcodeRaffListByGoodsID(goodsID, "5");
			for (Map<String, String> map : list) {
				if (map.get("codeID").equals(codeID) && !map.get("codeRNO").equals("")) {
					String userWeb = map.get("userWeb");
					String codeRNO = map.get("codeRNO");
					String buyNum = map.get("buyNum");
					getUserBuyListEnd(userWeb, codeRNO, buyNum, true);
					return;
				}
			}
			Map<String, String> barcodernoInfo = GetGoodsInfo.getBarcodernoInfo(codeID);
			String codeRNO = barcodernoInfo.get("codeRNO");
			if (codeRNO != null && !codeRNO.equals("")) {
				getUserBuyListEnd(barcodernoInfo.get("userWeb"), codeRNO, barcodernoInfo.get("buyTime"), false);
			}
			// System.out.println("查询中奖码：" + codeRNO);
		} catch (Exception e) {
		}
	}

	private void getUserBuyListEnd(String userWeb, String codeRNO, String buyNum, boolean isBarcode) {
		int index = 1;// 记录中奖位
		int buyStartPosition = 1;// 购买起始位置
		int userIndex = 1;
		for (String[] info : userBuyList) {
			buyStartPosition = index;
			if (userWeb.equals(info[3]) && (isBarcode ? buyNum.equals(info[1]) : true)) {
				int FIdx = (userBuyList.size() - userIndex) / 10 + 1;
				String content = GetGoodsInfo.getUserBuyListEnd(codeID, FIdx, HttpClient);
				String data = content.split("Rows\":", 2)[1];
				data = data.substring(0, data.length() - 5);
				List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
				}.getType());
				for (Map<String, String> maps : dList) {
					if (userWeb.equals(maps.get("userWeb"))
							&& (isBarcode ? buyNum.equals(maps.get("buyNum")) : buyNum.equals(maps.get("buyTime")))) {
						int position = getUserBuyCode(maps.get("buyID"), codeRNO);
						if (position != 0) {
							index = index + Integer.parseInt(buyNum) - position;
							CacheData.getGoodsPriceCacheDate(goodsID);
							String buySection = buyStartPosition + "~"
									+ (buyStartPosition + Integer.parseInt(buyNum) - 1);
							String buyPosition = "" + index;
							String[] text = new String[] { goodsID, codePeriod, codeRNO, maps.get("userName"), buyNum,
									buyPosition, buySection, codeID };
							if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
								CacheData.setSelectCacheDate(goodsID, codePeriod, text);
//								SelectAssistPublishs.uploadYungouPublishs(text);
								CacheData.removeUserBuyListCacheDate(codeID);
							}
							System.out.println("查到ByCache了：" + goodsID + ":" + codePeriod + ":" + codeRNO + ":"
									+ maps.get("userName") + ":" + buyPosition+":"+buySection);
							return;
						}
					}
				}
			}
			userIndex++;
			index = index + Integer.parseInt(buyNum);
		}
	}

	/*
	 * 获取总页数
	 */
	private int getUserBuyListCount(String content) {
		String data = content.substring(content.indexOf("Count':") + 7, content.indexOf(",'Rows"));
		return Integer.parseInt(data);
	}
}
