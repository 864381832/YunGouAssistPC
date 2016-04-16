package com.ytkj.ygAssist.tools;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/*
 * 缓存数据
 */
public class CacheData {
	// 缓存查询到的数据
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, String[]>> selectCacheDate = new ConcurrentHashMap<String, ConcurrentHashMap<String, String[]>>();
	private static ConcurrentHashMap<String, String> goodsNameCacheDate = new ConcurrentHashMap<String, String>();// 缓存商品名称
	private static ConcurrentHashMap<String, Integer> goodsPriceCacheDate = new ConcurrentHashMap<String, Integer>();// 缓存商品价格
	private static ConcurrentHashMap<String, String[]> goodsInfoCacheDate = new ConcurrentHashMap<String, String[]>();// 缓存商品信息
//	private static HashMap<String, String> selectCodeRNOCacheDate = new HashMap<String, String>();// 缓存中奖码

	private static ConcurrentHashMap<String, TreeMap<String, String[]>> userBuyListCacheDate = new ConcurrentHashMap<String, TreeMap<String, String[]>>();

	public static String[] getSelectCacheDate(String goodsID, String period) {
		ConcurrentHashMap<String, String[]> selectCacheDateMap = CacheData.selectCacheDate.get(goodsID);
		if (selectCacheDateMap != null) {
			String[] CacheDateStr = selectCacheDateMap.get(period);
			if (CacheDateStr != null) {
				return CacheDateStr;
			}
		}
		return null;
	}

	public static ConcurrentHashMap<String, String[]> getSelectCacheDate(String goodsID) {
		return selectCacheDate.get(goodsID);
	}

	public static void setSelectCacheDate(String goodsID, String period, String[] text) {
		ConcurrentHashMap<String, String[]> selectCacheDateMap = CacheData.selectCacheDate.get(goodsID);
		if (selectCacheDateMap == null) {
			ConcurrentHashMap<String, String[]> periodHashMap = new ConcurrentHashMap<String, String[]>();
			periodHashMap.put(period, text);
			CacheData.selectCacheDate.put(goodsID, periodHashMap);
		} else {
			if (selectCacheDateMap.get(period) == null) {
				selectCacheDateMap.put(period, text);
			}
		}
	}

	public static String getGoodsNameCacheDate(String goodsID) {
		String goodsName = goodsNameCacheDate.get(goodsID);
		if (goodsName != null && !"0".equals(CacheData.getGoodsInfoCacheDate(goodsID)[3])) {
			goodsName = "(限购)" + goodsName;
		}
		return goodsName;
	}

	public static ConcurrentHashMap<String, String> getGoodsNameCacheDate() {
		return goodsNameCacheDate;
	}

	public static void setGoodsNameCacheDate(String goodsID, String goodsName) {
		goodsNameCacheDate.put(goodsID, goodsName);
	}

	public static Integer getGoodsPriceCacheDate(String goodsID) {
		return goodsPriceCacheDate.get(goodsID);
	}

	public static void setGoodsPriceCacheDate(String goodsID, int price) {
		goodsPriceCacheDate.put(goodsID, price);
	}

	public static String[] getGoodsInfoCacheDate(String goodsID) {
		// 0商品ID,1商品名,2codeID,3限购数量
		return goodsInfoCacheDate.get(goodsID);
	}

	public static ConcurrentHashMap<String, String[]> getGoodsInfoCacheDate() {
		return goodsInfoCacheDate;
	}

	public static void setGoodsInfoCacheDate(String goodsID, String[] goodsInfo) {
		CacheData.goodsInfoCacheDate.put(goodsID, goodsInfo);
	}

	public static String[] getUserBuyListCacheDate(String codeID, String buyID) {
		// 0buyNum购买数量,1userName用户名,2userWeb用户web,3rnoNum云购码
		if (CacheData.userBuyListCacheDate.get(codeID) != null) {
			if (CacheData.userBuyListCacheDate.get(codeID).get(buyID) != null) {
				return CacheData.userBuyListCacheDate.get(codeID).get(buyID);
			}
		}
		return null;
	}

	public static TreeMap<String, String[]> getUserBuyListCacheDate(String codeID) {
		if (userBuyListCacheDate.get(codeID) == null) {
			TreeMap<String, String[]> periodHashMap = new TreeMap<String, String[]>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return Integer.parseInt(o2) - Integer.parseInt(o1);
				}
			});
			CacheData.userBuyListCacheDate.put(codeID, periodHashMap);
		}
		return userBuyListCacheDate.get(codeID);
	}

	public static void setUserBuyListCacheDate(String codeID, TreeMap<String, String[]> value) {
		userBuyListCacheDate.put(codeID, value);
	}

	public static void setUserBuyListCacheDate(String codeID, String buyID, String[] text) {
		if (CacheData.userBuyListCacheDate.get(codeID) == null) {
			TreeMap<String, String[]> periodHashMap = new TreeMap<String, String[]>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return Integer.parseInt(o2) - Integer.parseInt(o1);
				}
			});
			periodHashMap.put(buyID, text);
			CacheData.userBuyListCacheDate.put(codeID, periodHashMap);
		} else {
			if (CacheData.userBuyListCacheDate.get(codeID).get(buyID) == null) {
				CacheData.userBuyListCacheDate.get(codeID).put(buyID, text);
			}
		}
	}

	public static void removeUserBuyListCacheDate(String codeID) {
		userBuyListCacheDate.remove(codeID);
	}

//	public static String getSelectCodeRNOCacheDate(String codeID) {
//		return selectCodeRNOCacheDate.get(codeID);
//	}
//
//	public static void setSelectCodeRNOCacheDate(String codeID, String codeRNO) {
//		selectCodeRNOCacheDate.put(codeID, codeRNO);
//	}
}
