package com.ytkj.ygAssist.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;

public class HeGouBaServer {
	private CloseableHttpClient HttpClient = null;// 查询使用HTTPClient
	private static HeGouBaServer heGouBaServer = null;
	private String refererUrl = "http://help.hego8.com/tologin.action";

	public static HeGouBaServer getHeGouBaServer() {
		if (heGouBaServer == null) {
			heGouBaServer = new HeGouBaServer();
		}
		return heGouBaServer;
	}

	public static void stopHeGouBaServer() {
		if (heGouBaServer != null) {
			heGouBaServer = null;
		}
	}

	private HeGouBaServer() {
		HttpClient = HttpGetUtil.createHttpClient();
		// String user = ("" + new Date().getTime()).substring(3);
		// String password = ("" + new Date().getTime()).substring(4);
		// // 注册
		// StringBuffer getUrl = new StringBuffer();
		// getUrl.append("http://help.hego8.com/userLogin.action/userRegister.action?userModel.name=").append(user);
		// getUrl.append("&userModel.email=").append(user + "@qq.com");
		// getUrl.append("&userModel.password=").append(password);
		// getUrl.append("&userModel.qq=").append(user);
		// HttpGetUtil.getHttpData(getUrl.toString(), refererUrl);
		// System.out.println("注册" + getUrl.toString());
		// try {
		// Thread.sleep(2000);
		// } catch (Exception e) {
		// }
		// // 登录
		// String url =
		// "http://help.hego8.com/userLogin.action?userModel.email=" + user +
		// "@qq.com&userModel.password="
		// + MyStringUtil.getMd5Encode(password) + "&flag=1";
		// // url = "http://help.hego8.com/userLogin.action";
		// HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
		// System.out.println("登录" + getUrl.toString());
	}

	public void getBigGoodsInfo(JFrameListeningInterface foreknowInterface, int FIdx) {
		// 返回值({"Code":0,"Count":370,"Data":{"Tables":{"Table1":{"Rows":[{"rowID":0,"goodsID":22850,"goodsSName":"","goodsPic":"","codeID":3252006,"codePrice":"428888.00","codeQuantity":428888,"codeSales":25887,"codePeriod":126,"codeType":0,"goodsTag":"0","codeLimitBuy":"0"}]}}}})
		String url = "http://api.1yyg.com/JPData?action=getGoodsList&sortID=0&brandID=0&orderFlag=30&FIdx=" + FIdx
				+ "&EIdx=" + (FIdx + 39) + "&isCount=1";
		String refererUrl = "http://www.1yyg.com/";
		String content = HttpGetUtil.getHttpData(url, refererUrl);
		// System.out.println("内容：" + content);
		String data = content.substring(content.indexOf(":{\"Rows\":[") + 9, content.length() - 5);
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		for (Map<String, String> maps : dList) {
			System.out.println(maps.get("goodsSName"));
			getHegoubaData(maps.get("goodsID"), maps.get("codePeriod"), foreknowInterface);
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
			}
		}
	}

	public void getHegoubaData(String goodsID, JFrameListeningInterface foreknowInterface) {
		String url1 = "http://help.hego8.com/goodsById.action?goodsId=" + goodsID;
		String content = HttpGetUtil.getHttpData(url1, refererUrl, HttpClient);
		// System.out.println("内容2：" + content);
		String codePeriod = content.substring(content.indexOf("codePeriod") + 12, content.indexOf("codePrice") - 2);
		getHegoubaData(goodsID, codePeriod, foreknowInterface);
	}

	/*
	 * 获取合购吧数据
	 */
	public void getHegoubaData(String goodsID, String codePeriod, JFrameListeningInterface foreknowInterface) {
		StringBuffer url = new StringBuffer();
		url.append("http://help.hego8.com/market/getChartData.action?goodsId=").append(goodsID);
		int beginPeriod = Integer.parseInt(codePeriod) - 5;
		if (beginPeriod > 0) {
			url.append("&beginPeriod=").append("" + beginPeriod);
		} else {
			url.append("&beginPeriod=").append("1");
		}
		url.append("&endPeriod=").append(codePeriod);
		// refererUrl = "http://help.hego8.com/market.action?chartType=0";
		String goodsInfo = HttpGetUtil.getHttpData(url.toString(), refererUrl, HttpClient);
		readHegoubaData(goodsInfo, foreknowInterface);
	}

	/*
	 * 从文本中导入合购吧数据
	 */
	public void readHeGouBaData(JFrameListeningInterface foreknowInterface) {
		File file = new File("jre\\hegouba.ini");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String goodsInfo = br.readLine();
			readHegoubaData(goodsInfo, foreknowInterface);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 解析数据到数据库
	 */
	public static void readHegoubaData(String goodsInfo, JFrameListeningInterface foreknowInterface) {
		try {
			String goodsId = goodsInfo.substring(6, 11);
			goodsInfo = goodsInfo.substring(goodsInfo.indexOf("chartPointList\":[") + 16,
					goodsInfo.indexOf("],\"codeId") + 1);
			List<Map<String, String>> dList = new Gson().fromJson(goodsInfo,
					new TypeToken<List<Map<String, String>>>() {
					}.getType());
			for (Map<String, String> map : dList) {
				int y = Integer.parseInt(map.get("y"));
				int num = Integer.parseInt(map.get("buyCount"));
				String buySection = "" + (y - 1) + "~" + (y + num);

				String[] SelectAssistText = new String[] { goodsId, map.get("x"), map.get("luckyNum"),
						map.get("buyerName"), map.get("buyCount"), map.get("y"), buySection, map.get("codeId") };

				foreknowInterface.setFrameListeningText("", SelectAssistText);
				SelectAssistPublishs.uploadYungouPublishs(SelectAssistText);
			}
		} catch (Exception e) {
		}
	}

}
