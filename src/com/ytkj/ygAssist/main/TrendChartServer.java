package com.ytkj.ygAssist.main;

import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.CloseableHttpClient;

import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;

/*
 * 提前揭晓服务
 */
public class TrendChartServer {
	public static void getTrendChart(String goodsID, String EIdx, JFrameListeningInterface foreknowInterface) {
		if (CacheData.getGoodsNameCacheDate(goodsID) == null) {
			if (GetGoodsInfo.getGoodsInfoByGoodsID(goodsID)) {
				foreknowInterface.setFrameText("setGoodsName", CacheData.getGoodsNameCacheDate(goodsID));
			} else {
				foreknowInterface.setFrameText("setGoodsNameError", null);
				return;
			}
		} else {
			CacheData.setGoodsTreeListCacheDate(goodsID);
			foreknowInterface.setFrameText("setGoodsName", CacheData.getGoodsNameCacheDate(goodsID));
		}
		CloseableHttpClient HttpClient = HttpGetUtil.createHttpClient();
		String text[] = GetGoodsInfo.shopCartNew(CacheData.getGoodsInfoCacheDate(goodsID)[2], HttpClient);
		foreknowInterface.setFrameListeningText("1", text);
		boolean isHavaData = true;
		try {
			int newestPeriod = Integer.parseInt(text[1]);
			for (int i = 1; i < Integer.parseInt(EIdx); i++) {
				if (CacheData.getSelectCacheDate(goodsID, "" + (newestPeriod - i)) == null) {
					isHavaData = false;
				} else {
					foreknowInterface.setFrameListeningText("2",
							CacheData.getSelectCacheDate(goodsID, "" + (newestPeriod - i)));
				}
			}
			CacheData.setGoodsPriceCacheDate(goodsID, Integer.parseInt(text[3]));
		} catch (Exception e) {
		}
		if (!isHavaData) {
			SelectAssistPublishs.getYungouPublishs(goodsID, "0", EIdx);
			List<Map<String, String>> contentList = GetGoodsInfo.getGoodsPeriodPage(goodsID, EIdx);
			if (contentList != null) {
				for (int selectIndex = 0; selectIndex < contentList.size(); selectIndex++) {
					Map<String, String> map = contentList.get(selectIndex);
					if (CacheData.getSelectCacheDate(goodsID, map.get("codePeriod")) == null) {
						Thread thread = new Thread(new Runnable() {
							public void run() {
								if (map.get("codeState").equals("1")) {
								} else if (map.get("codeState").equals("2")) {
									new GetUserBuyServer(foreknowInterface, "2").GetGoodsPeriodInfo(goodsID,
											map.get("codePeriod"), map.get("codeID"), false);
								} else if (map.get("codeState").equals("3")) {
									new GetUserBuyServer(foreknowInterface, "2").GetGoodsPeriodInfo(goodsID,
											map.get("codePeriod"), map.get("codeID"), true);
								}
							}
						});
						thread.start();
					} else {
						foreknowInterface.setFrameListeningText("2",
								CacheData.getSelectCacheDate(goodsID, map.get("codePeriod")));
					}
				}
			} else {
				foreknowInterface.setFrameText("setGoodsNameError", null);
			}
		}
	}
}
