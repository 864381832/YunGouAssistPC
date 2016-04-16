package com.ytkj.ygAssist.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.CloseableHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;

public class GetGoodsInfo {
	/*
	 * 查询商品网址id goodsID商品id period期数
	 */
	public static String[] getGoodsPeriodInfo(String goodsID, String period) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://api.1yyg.com/JPData?action=getGoodsPeriodInfo&goodsID=").append(goodsID);
		getUrl.append("&period=").append(period);
		String refererUrl = "http://www.1yyg.com/";
		// 返回值{"code":0,"codeID":2654637,"codeState":3}
		// code0正常 codeState-1未开始 1正在进行 2等待开奖 3已经开奖
		String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl);
		try {
			Map<String, String> contentMap = new Gson().fromJson(content.substring(1, content.length() - 1),
					new TypeToken<Map<String, String>>() {
					}.getType());
			if (contentMap.get("code").equals("0")) {
				return new String[] { contentMap.get("codeState"), contentMap.get("codeID") };
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 获取商品最近购买期数
	 */
	public static List<Map<String, String>> getGoodsPeriodPage(String goodsID, String EIdx) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://api.1yyg.com/JPData?action=getGoodsPeriodPage&goodsID=").append(goodsID);
		getUrl.append("&FIdx=1&EIdx=").append(EIdx).append("&IsCount=1");
		String refererUrl = "http://www.1yyg.com/";
		// String refererUrl = "http://www.1yyg.com/products/" + goodsID +
		// ".html";
		// 返回值{"code":0,"totalCount":0,"listItems":[{"codePeriod":26132,"codeID":2709962,"codeState":1}]}
		// codePeriod商品期数 codeID商品网址id codeState揭晓状态：1进行中、2等待揭晓、3已经揭晓
		String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl);
		String code = content.substring(content.indexOf(":") + 1, content.indexOf(","));
		if (code.equals("0")) {
			content = content.substring(content.indexOf("s\":") + 3, content.length() - 2);
			List<Map<String, String>> contentList = new Gson().fromJson(content,
					new TypeToken<List<Map<String, String>>>() {
					}.getType());
			// for (Map<String, String> map : contentList) {
			// System.out.println(map.get("codeID"));
			// }
			return contentList;
		} else {
			return null;
		}
	}

	/*
	 * 获取商品最近中奖信息
	 */
	public static List<Map<String, String>> getBarcodeRaffListByGoodsID(String goodsID, String EIdx) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://weixin.1yyg.com/JPData?action=getBarcodeRaffListByGoodsID&goodsID=").append(goodsID);
		getUrl.append("&period=0&FIdx=1&EIdx=").append(EIdx).append("&isCount=0");
		String refererUrl = "http://weixin.1yyg.com";
		// 返回值{"code":0,"count":,"minPeriod":,"newPeriod":,"newCodeID":4613701,"listItems":[{"codeID":"","codeState":"1","userPhoto":"","userName":"","buyNum":"0","codeRNO":"","codeRTime":"","userWeb":"","codePeriod":"","codeQuantity":"","codeSales":"","goodsPic":""}],}
		// codePeriod商品期数 codeID商品网址id codeState揭晓状态：1进行中、2等待揭晓、3已经揭晓
		String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl);
		String code = content.substring(content.indexOf("code\":") + 6, content.indexOf(",\"count"));
		String newPeriod = content.substring(content.indexOf("newPeriod\":") + 11, content.indexOf(",\"newCodeID"));
		if (code.equals("0") && !newPeriod.equals("0")) {
			content = content.substring(content.indexOf("listItems\":") + 11, content.length() - 2);
			List<Map<String, String>> contentList = new Gson().fromJson(content,
					new TypeToken<List<Map<String, String>>>() {
					}.getType());
			return contentList;
		} else {
			return null;
		}
	}

	/*
	 * 获取本期中奖详情
	 */
	public static Map<String, String> getBarcodernoInfo(String codeID) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://api.1yyg.com/JPData?action=GetBarcodernoInfo&codeID=").append(codeID);
		String refererUrl = "http://www.1yyg.com/";
		// 返回值{"code":0,"codePeriod":25043,"codeRNO":10003451,"codeRTime":"","buyTime":"","price":"","buyCount":3488,"userName":"","userNC":"","ipAddr":"","goodsName":"","goodsPic":"","userPhoto":"","userWeb":"","codeType":0})
		// codeRTime揭晓时间 buyTime云购时间 price价格 buyCount购买数量
		String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl);
		content = content.substring(1, content.length() - 1);
		Map<String, String> contentMap = new Gson().fromJson(content, new TypeToken<Map<String, String>>() {
		}.getType());
		if (contentMap.get("code").equals("0")) {
			return contentMap;
		} else {
			return null;
		}
	}

	/*
	 * 根据商品ID获取商品名称
	 */
	public static boolean getGoodsInfoByGoodsID(String goodsID) {
		try {
			String[] goodsInfoCacheDate = CacheData.getGoodsInfoCacheDate(goodsID);
			if (goodsInfoCacheDate == null) {
				SelectAssistPublishs.getGoodsInfos(goodsID);
				goodsInfoCacheDate = CacheData.getGoodsInfoCacheDate(goodsID);
				if (goodsInfoCacheDate == null) {
					String codeID = GetGoodsInfo.getGoodsPeriodInfo(goodsID, "1")[1];
					Map<String, String> map = getBarcodernoInfo(codeID);
					String goodsName = map.get("goodsName");
					if (goodsName == null) {
						return false;
					}
					CacheData.setGoodsNameCacheDate(goodsID, goodsName);
					CacheData.setGoodsInfoCacheDate(goodsID, new String[] { goodsID, goodsName, codeID, "0" });
					new Thread(new Runnable() {
						public void run() {
							List<Map<String, String>> list = getGoodsID(goodsName);
							for (Map<String, String> map : list) {
								CacheData.setGoodsInfoCacheDate(map.get("goodsID"), new String[] { map.get("goodsID"),
										goodsName, codeID, map.get("codeLimitBuy") });
								SelectAssistPublishs.uploadGoodsInfos(new String[] { map.get("goodsID"), goodsName,
										codeID, map.get("codeLimitBuy"), map.get("goodsPic"), map.get("isSale") });
							}
							FilesUtil.saveGoodsInfoFile(goodsID + "___" + goodsName + "___" + codeID);
						}
					}).start();
				} else {
					CacheData.setGoodsNameCacheDate(goodsID, goodsInfoCacheDate[1]);
					FilesUtil
							.saveGoodsInfoFile(goodsID + "___" + goodsInfoCacheDate[1] + "___" + goodsInfoCacheDate[2]);
				}
			} else {
				CacheData.setGoodsNameCacheDate(goodsID, goodsInfoCacheDate[1]);
				FilesUtil.saveGoodsInfoFile(goodsID + "___" + goodsInfoCacheDate[1] + "___" + goodsInfoCacheDate[2]);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * 获取本期剩余数量 return String{codeID,期数,已购买数,总价,剩余数}
	 */
	public static String[] shopCartNew(String codeID, CloseableHttpClient HttpClient) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://cart.1yyg.com/JPData?action=shopCartNew&codeID=").append(codeID);
		getUrl.append("&num=1");
		String refererUrl = "http://www.1yyg.com/";
		// String refererUrl = "http://www.1yyg.com/products/" + goodsID +
		// ".html";
		// 返回值{"code":1,"num":0,"str":"2710463|26214|5|5188|5183|0"}
		String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpClient);
		try {
			content = content.substring(content.indexOf("str") + 6, content.length() - 3);
			return content.split("\\|");
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 获取微信获取本期剩余数量 return String{codeID,期数,已购买数,总价,剩余数}
	 */
	public static String[] shopCartNew1(String codeID, CloseableHttpClient HttpClient) {
		String getUrl = "http://weixin.1yyg.com/JPData?action=addShopCart&shopNum=1&codeID=" + codeID;
		String refererUrl = "http://weixin.1yyg.com/";
		// 返回值{"code":1,"NewCodeInfo":"4688144|602|19282|389999|370717|4"}
		String content = HttpGetUtil.getHttpData(getUrl, refererUrl, HttpClient);
		try {
			content = content.substring(content.indexOf("NewCodeInfo") + 14, content.length() - 3);
			return content.split("\\|");
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 获取云购结束后参与记录
	 */
	public static String getUserBuyListEnd(String codeID, int FIdx, CloseableHttpClient HttpClient) {
		StringBuffer getUrl = new StringBuffer();
		// 返回值{"Code":0,"Count":55,"Data":{"Tables":{"BuyList":{"Rows":[{"userName":"","userPhoto":"","userWeb":"","buyNum":"","buyIP":"","buyIPAddr":"","buyTime":"","buyDevice":"0","buyID":"318702471"}
		getUrl.append("http://api.1yyg.com/JPData?action=GetUserBuyListByCodeEnd&codeID=").append(codeID);
		getUrl.append("&FIdx=").append(FIdx);
		getUrl.append("&EIdx=").append(FIdx + 9);
		getUrl.append("&isCount=1");
		String refererUrl = "http://www.1yyg.com/lottery/" + codeID + ".html";
		return HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpClient);
	}

	/*
	 * 获取云购中参与记录
	 */
	public static String getUserBuyList(String codeID, int FIdx, CloseableHttpClient HttpClient) {
		StringBuffer getUrl = new StringBuffer();
		// 返回值{"Code":0,"Count":55,"Data":{"Tables":{"BuyList":{"Rows":[{"userName":"","userPhoto":"","userWeb":"","buyNum":"","buyIP":"","buyIPAddr":"","buyTime":"","buyDevice":"0","buyID":"318702471"}
		getUrl.append("http://api.1yyg.com/JPData?action=GetUserBuyListByCode&codeID=").append(codeID);
		getUrl.append("&FIdx=").append(FIdx);
		getUrl.append("&EIdx=").append(FIdx + 9);
		getUrl.append("&isCount=1");
		String refererUrl = "http://www.1yyg.com/lottery/" + codeID + ".html";
		return HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpClient);
	}

	/*
	 * 顺序获取云购中参与记录
	 */
	public static String getUserBuyListSort(String codeID, int FIdx, CloseableHttpClient HttpClient) {
		StringBuffer getUrl = new StringBuffer();
		// 返回值({'Code':0,'Count':57768,'Rows':[{"userName":"","userPhoto":"","userWeb":"","buyNum":"1","buyIP":"","buyIPAddr":"","buyTime":"","buyDevice":"4"}]})
		getUrl.append("http://api.1yyg.com/JPData?action=GetUserBuyListByCode&codeID=").append(codeID);
		getUrl.append("&FIdx=").append(FIdx);
		getUrl.append("&EIdx=").append(FIdx + 9);
		getUrl.append("&isCount=1&sort=1");
		String refererUrl = "http://www.1yyg.com";
		String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpClient);
		System.out.println("查到" + content);
		// if (content.equals("")) {
		// System.out.println("继续查到" + content);
		// return getUserBuyListSort(codeID, FIdx, HttpClient);
		// } else {
		return content;
		// }
	}

	/*
	 * 获取用户云够码
	 */
	public static String getUserBuyCode(String codeID, String buyID) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://api.1yyg.com/JPData?action=GetUserBuyCodeByBuyid&codeid=").append(codeID);
		getUrl.append("&buyid=").append(buyID);
		// http://api.1yyg.com/JPData?action=GetUserBuyCodeByBuyid&codeid=2549588&buyid=315549442
		// 返回值{'code':0,'data':[{'rnoNum':10003203}]}
		String refererUrl = "http://www.1yyg.com/lottery/" + codeID + ".html";
		return HttpGetUtil.getHttpData(getUrl.toString(), refererUrl);
	}

	/*
	 * 获取用户购买详情
	 */
	public static Map<String, String> getshopresult(String id, CloseableHttpClient HttpClient) {
		// 获取购买信息
		// 返回值{"code":0,"state":1,"data":[{"buyTime":"","buyNum":1,"codeID":,"codePeriod":2618,"goodsName":"","codeList":[],"goodsPic":"","buyID":}],"faildata":[]}
		String url = "http://api.1yyg.com/JPData?action=getshopresult&id=" + id;
		String refererUrl = "http://www.1yyg.com/";
		String content = HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
		String data = content.split("\"data\":\\[", 2)[1].split("\\],\"faildata", 2)[0].replace("[", "1").replace("]",
				"1");
		// System.out.println(data);
		Map<String, String> dMap = new Gson().fromJson(data, new TypeToken<Map<String, String>>() {
		}.getType());
		return dMap;
	}

	/*
	 * 获取商品ID
	 */
	public static List<Map<String, String>> getGoodsID(String goodsName) {
		// 获取购买信息
		// 返回值({"code":0,"count":1,"listItems":[{"goodsID":22405,"goodsSName":"","goodsPic":"","goodsTag":"0","codeID":,"codePrice":"","codePeriod":,"codeQuantity":,"codeSales":0,"goodsKeyName":"","isSale":1,"codeLimitBuy":0}]})
		String url = null;
		try {
			url = "http://search.1yyg.com/JPData?action=getSearchList&key=" + URLEncoder.encode(goodsName, "UTF-8")
					+ "&orderFlag=10&quantity=120";
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String refererUrl = "http://www.1yyg.com/";
		String content = HttpGetUtil.getHttpData(url, refererUrl);
		try {
			String data = content.substring(content.indexOf("listItems\":[") + 11, content.length() - 2);
			// System.out.println(data);
			return new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
			}.getType());
		} catch (Exception e) {
		}
		return null;
	}

	/*
	 * 查询热门推荐产品
	 */
	public static List<Map<String, String>> getRecGoodsList() {
		// url =
		// "http://api.1yyg.com/JPData?action=getRecGoodsList&goodsLabel=12&quantity=8";
		// refererUrl = "http://www.1yyg.com/";
		// 获取购买信息
		// 返回值({'code':0,'listItems':[{'goodsID':22504,'goodsName':'','goodsPic':'','goodsTag':'0','goodsRecDesc':'','codeID':,'codePrice':'5188.00','codeQuantity':5188,'codeSales':238,'goodsNameEx':'','codePeriod':41413}]})

		// String url =
		// "http://api.1yyg.com/JPData?action=getRecGoodsList&goodsLabel=12&quantity=8";
		String url = "http://api.1yyg.com/JPData?action=getGoodsList&sortID=0&brandID=0&orderFlag=10&FIdx=1&EIdx=100&isCount=1";
		String refererUrl = "http://www.1yyg.com/";
		String content = HttpGetUtil.getHttpData(url, refererUrl);
		// System.out.println(content);
		try {
			// String data = content.substring(content.indexOf("listItems':[") +
			// 11, content.length() - 2);
			String data = content.substring(content.indexOf(":{\"Rows\":[") + 9, content.length() - 5);
			return new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
			}.getType());
		} catch (Exception e) {
		}
		return new ArrayList<Map<String, String>>();
	}

	/*
	 * 查询所有在线商品
	 */
	public static List<Map<String, String>> getGoodsList() {
		// 返回值({'code':0,'count':400,'listItems':[{"rowID":0,"goodsID":,"goodsSName":"","goodsPic":"","codeID":,"codePrice":7,"codeQuantity":,"codeSales":5,"codePeriod":,"codeType":0,"goodsTag":0}]})
		// orderFlag 31价值从小到大,30价值从大到小,10即将揭晓,20人气,50最新
		String url = "http://m.1yyg.com/JPData?action=getGoodsPageList&sortID=0&brandID=0&orderFlag=31&FIdx=1&EIdx=600&isCount=1";
		String refererUrl = "http://m.1yyg.com/";
		String content = HttpGetUtil.getHttpData(url, refererUrl);
		try {
			String data = content.substring(content.indexOf("listItems':[") + 11, content.length() - 2);
			return new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
			}.getType());
		} catch (Exception e) {
		}
		return new ArrayList<Map<String, String>>();
	}

	/*
	 * 获取最新揭晓
	 */
	public static Object[] getStartRaffleAllList(String time) {
		// 最新揭晓
		// 返回值{"errorCode":0,"maxSeconds":8299665184.414,"listItems":[{"goodsPic":"","goodsSName":"","seconds":"179","codeID":2777802,"price":"399.00","period":703,"codeQuantity":399,"codeSales":399,"codeType":0}]}
		String maxSeconds = time;
		String url = "http://api.1yyg.com/JPData?action=GetStartRaffleAllList&time=" + maxSeconds;
		String refererUrl = "http://www.1yyg.com/";
		String content = HttpGetUtil.getHttpData(url, refererUrl);
		System.out.println("内容：" + content);
		if ("0".equals(content.substring(14, 15))) {
			maxSeconds = content.substring(29, content.indexOf(",\"listItems", 2));
			String data = content.substring(content.indexOf("listItems\":[") + 11, content.length() - 2);
			List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
			}.getType());
			return new Object[] { maxSeconds, dList };
		}
		return null;
	}

}
