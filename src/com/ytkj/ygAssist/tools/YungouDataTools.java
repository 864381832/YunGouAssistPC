package com.ytkj.ygAssist.tools;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.SelectAssistPublishs;

public class YungouDataTools {
	/*
	 * 获取商品ID
	 */
	public static String getGoodsID(String goodsName, String codeType) {
		ConcurrentHashMap<String, String[]> goodsInfoCacheDate = CacheData.getGoodsInfoCacheDate();
		for (String[] goodsInfo : goodsInfoCacheDate.values()) {
			if (goodsInfo[1].equals(goodsName)) {
				if (codeType.equals(goodsInfo[3])) {
					return goodsInfo[0];
				} else if (Integer.parseInt(codeType) != 0 && Integer.parseInt(goodsInfo[3]) != 0) {
					return goodsInfo[0];
				}
			}
		}
		// 获取购买信息
		// 返回值({"code":0,"count":1,"listItems":[{"goodsID":22405,"goodsSName":"","goodsPic":"","goodsTag":"0","codeID":,"codePrice":"","codePeriod":,"codeQuantity":,"codeSales":0,"goodsKeyName":"","isSale":1,"codeLimitBuy":0}]})
		List<Map<String, String>> dList = GetGoodsInfo.getGoodsID(goodsName);
		if (dList != null) {
			if (dList.size() == 1) {
				Map<String, String> map = dList.get(0);
				String codeID = GetGoodsInfo.getGoodsPeriodInfo(map.get("goodsID"), "1")[1];
				if (codeID == null) {
					codeID = map.get("codeID");
				}
				CacheData.setGoodsInfoCacheDate(map.get("goodsID"),
						new String[] { map.get("goodsID"), map.get("goodsSName"), codeID, map.get("codeLimitBuy") });
				SelectAssistPublishs.uploadGoodsInfos(new String[] { map.get("goodsID"), map.get("goodsSName"), codeID,
						map.get("codeLimitBuy"), map.get("goodsPic"), map.get("isSale") });
				return dList.get(0).get("goodsID");
			} else {
				for (Map<String, String> map : dList) {
					String codeID = GetGoodsInfo.getGoodsPeriodInfo(map.get("goodsID"), "1")[1];
					if (codeID == null) {
						codeID = map.get("codeID");
					}
					CacheData.setGoodsInfoCacheDate(map.get("goodsID"), new String[] { map.get("goodsID"),
							map.get("goodsSName"), codeID, map.get("codeLimitBuy") });
					SelectAssistPublishs.uploadGoodsInfos(new String[] { map.get("goodsID"), map.get("goodsSName"),
							codeID, map.get("codeLimitBuy"), map.get("goodsPic"), map.get("isSale") });
					if (codeType.equals(map.get("codeLimitBuy"))) {
						return map.get("goodsID");
					} else if (Integer.parseInt(codeType) != 0 && Integer.parseInt(map.get("codeLimitBuy")) != 0) {
						return map.get("goodsID");
					}
				}
			}
		}
		return null;
	}

	/*
	 * 查询缓存区数据中奖位置
	 */
	public static boolean selectBarcodernoInfoByCache(String goodsID, String codePeriod, String codeID, String codeRNO,
			int selectNum) {
		TreeMap<String, String[]> buyListMap = CacheData.getUserBuyListCacheDate(codeID);
		if (buyListMap != null) {
			int index = 0;// 记录中奖位
			int buyStartPosition = 0;// 购买起始位置
			for (String key : buyListMap.keySet()) {
				if (selectNum == 0 || index <= selectNum) {
					buyStartPosition = index;
					String[] buyCode = buyListMap.get(key)[3].split(",");
					for (int i = 0; i < buyCode.length; i++) {
						if (buyCode[i].equals(codeRNO)) {
							int price2 = CacheData.getGoodsPriceCacheDate(goodsID);
							// 中奖区间
							String buySection = (price2 - buyStartPosition - buyCode.length + 1) + "~"
									+ (price2 - buyStartPosition);
							String buyPosition = "" + (price2 - index);
							String[] text = new String[] { goodsID, codePeriod, codeRNO, buyListMap.get(key)[1],
									"" + buyCode.length, buyPosition, buySection, codeID };
							if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
								CacheData.setSelectCacheDate(goodsID, codePeriod, text);
								SelectAssistPublishs.uploadYungouPublishs(text);
								CacheData.removeUserBuyListCacheDate(codeID);
							}
							System.out.println("查到seCache了：" + selectNum + ":" + goodsID + ":" + codePeriod + ":"
									+ codeRNO + ":" + buyListMap.get(key)[1] + ":" + buyPosition);
							return true;
						}
						index++;
					}
				}
			}
		}
		return false;
	}

	/*
	 * 获取缓存购买人次
	 */
	public static int getCodeNum(String codeID) {
		TreeMap<String, String[]> buyListMap = CacheData.getUserBuyListCacheDate(codeID);
		int buyNum = 0;
		for (String key : buyListMap.keySet()) {
			buyNum = buyNum + Integer.parseInt(buyListMap.get(key)[0]);
		}
		return buyNum;
	}
}
