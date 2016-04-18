package com.ytkj.ygAssist.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jna.FromNativeContext;
import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.GetHistoryBuyRecord;
import com.ytkj.ygAssist.server.GetUserBuyMonitorServer;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.YungouDataTools;

public class YunGouTest {
	private static int iInt = 0;
	private static CloseableHttpClient closeableHttpClient[] = new CloseableHttpClient[4];
	private static PoolingHttpClientConnectionManager cm = null;

	public static void main(String[] args) {
		long beginTime = System.currentTimeMillis();
		// System.out.println(GetHistoryBuyRecord.getCodeRNO("2016-04-15
		// 00:47:41.067", "5188"));
		// TestUrl();
		GetGoodsInfo.getBarcodeRaffListByGoodsID("22504", "50");
		System.out.println("执行耗时 : " + (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
	}

	public class g{
		int i = 0;
		private void sum() {

		}
	}
	
	public static String TestUrl() {
		String url = null;
		String refererUrl = null;
		String content = null;
		CloseableHttpClient HttpClient = HttpClients.createDefault();
		boolean isTrue = true;
		String codeID = null;
		// while (isTrue) {
		// String[] goodsInfo = GetGoodsInfo.shopCartNew("4391140",
		// HttpClient);
		// GetUserBuyMonitorServer.selectBarcodernoInfo(new
		// JFrameListeningInterface() {
		// }, "0", "21929", goodsInfo[1], goodsInfo[0]);
		// content = GetGoodsInfo.getUserBuyList("4422749", 1, HttpClient);
		// System.out.println(content);
		// content = GetGoodsInfo.getUserBuyList("4422749", 4969, HttpClient);
		// System.out.println(content);
		// String data = content.substring(content.indexOf("Count\":") + 7,
		// content.indexOf(",\"Data"));
		// System.out.print(Integer.parseInt(data)/10);
		// }
		// 登录
		// 返回值{"state":1, "num":-1}
		// state登录状态 0登录成功 1登录失败 2用户名格式错误 3失败次数超限，被冻结5分钟
		// num登录失败原因 -1登录密码错误 -2此账号不存在 -3此账号已被冻结 -4此账号未激活-5密码被系统锁定
		// url =
		// "https://passport.1yyg.com/JPData?action=userlogin&name=1599294563@qq.com&pwd=1yyg3775";
		// refererUrl = "https://passport.1yyg.com/login.html?forward=rego";
		// HttpGetUtil.getHttpData(url, refererUrl);

		// 获取登录信息
		// 返回值{"code":1, "userID":10694202, "username" :
		// "1599294563@qq.com","userPhoto":"","userWeb":"1011654081","hasMobile":"0"}
		// url = "http://api.1yyg.com/JPData?action=logininfo";
		// refererUrl = "http://member.1yyg.com/UserBalance.do";

		// 获取用户信息
		// 返回值{'code':0,'userWeb':'1011654081','userPhoto':'','userName':'','grade':'01','gradeName':'','addr':'','sign':'','isFriend':0}
		// url =
		// "http://u.1yyg.com/JPData?action=getUserInfo&userWeb=1013597990";
		// refererUrl = "http://www.1yyg.com/";

		// content = HttpGetUtil.getHttpData(url, refererUrl);
		// System.out.println("内容：" + content);

		// url =
		// "http://api.1yyg.com/JPData?action=getGoodsList&sortID=0&brandID=0&orderFlag=31&FIdx=1&EIdx=100&isCount=1";
		// url =
		// "http://api.1yyg.com/JPData?action=GetUserBuyListByCode&codeID=4613772&FIdx=11&EIdx=20&isCount=1&sort=0";
		// refererUrl = "http://www.1yyg.com/lottery/4613772.html";
		// url =
		// "http://weixin.1yyg.com/JPData?action=addShopCart&shopNum=1&codeID=4613772";
		// refererUrl = "http://weixin.1yyg.com/";
		url = "http://api.1yyg.com/JPData?action=getHistoryBuyRecord&FIdx=21&EIdx=40&BTime=2016-04-14%2021:54:00.222&ETime=2016-04-14%2022:54:59.125&isCount=0";
		refererUrl = "http://www.1yyg.com/HistoryBuyRecords.html";
		content = HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
		System.out.println("内容：" + content);
		// url =
		// "http://api.1yyg.com/JPData?action=GetUserBuyListByCode&codeID=4515014&FIdx=11&EIdx=20&isCount=1&sort=0";
		// refererUrl = "http://www.1yyg.com";
		// content = HttpGetUtil.getHttpData(url, refererUrl);
		// if (!string.equals(content)) {
		// string = content;
		// System.out.println("内容：" + content);
		// }

		// 获取商品最近中奖信息
		// url =
		// "http://weixin.1yyg.com/JPData?action=getBarcodeRaffListByGoodsID&goodsID=22597&period=0&FIdx=1&EIdx=18&isCount=1";

		// 获取购买信息
		// 返回值{"code":0,"state":1,"data":[{"buyTime":"","buyNum":1,"codeID":,"codePeriod":2618,"goodsName":"","codeList":[],"goodsPic":"","buyID":}],"faildata":[]}
		// url =
		// "http://api.1yyg.com/JPData?action=getshopresult&id=160114172922235628";
		// refererUrl = "http://www.1yyg.com/";

		// 查询购买商品状态
		// 返回值{'code':0}
		// code0正常 1商品已过期 3
		// url =
		// "http://cart.1yyg.com/JPData?action=selectGoodsState&codeStr=1577839,2793775,1577839&limitStr=";
		// refererUrl = "http://cart.1yyg.com/CartList.do";

		// 提交购物车 codeStr为需要排除的商品
		// url =
		// "http://cart.1yyg.com/JPData?action=updateCartState&codeStr=1577839,2793791";
		// refererUrl = "http://cart.1yyg.com/CartList.do";

		// 提交购物车 TrueCode需要购买的商品codeID
		// url =
		// "http://cart.1yyg.com/JPData?action=updateGoodsState&FalseCode=&TrueCode=2767467&BuyNum=";
		// refererUrl = "http://cart.1yyg.com/CartList.do";

		// 购买
		// 返回值{"state":0,"str":"160107214553561185"}
		// state 0购买成功 1余额不足
		// url =
		// "http://trade.1yyg.com/JPData/API.ashx?action=UserPay&device=0&integral=0&isBalance=1&payMoney=1&checkSN=";
		// refererUrl = "http://cart.1yyg.com/payment.do";

		// 获取购物车商品
		// 返回值{'code':0,'count':3,'money':4,'listItems':[{'codeID':1577839,'goodsPic':'','goodsName':'','shopNum':1,'goodsID':22515,'codeQuantity':2280,'codeSurplus':2252,'codeType':0,'codeLimitBuy':0,'myLimitSales':0}]}
		// url = "http://cart.1yyg.com/JPData?action=cartlabel";
		// refererUrl = "http://www.1yyg.com/";

		// 我的账户
		// url = "http://member.1yyg.com/UserBalance.do";
		// refererUrl = "http://member.1yyg.com/Index.do";

		// 购物车
		// url = "http://cart.1yyg.com/CartList.do";
		// refererUrl = "http://www.1yyg.com/";
		// content = HttpGetUtil.getHttpData(url, refererUrl);
		// System.out.println("内容：" + content);

		// 查询商品id
		// url = "http://www.1yyg.com/lottery/3949985.html";
		// refererUrl = "http://www.1yyg.com/";
		// content = HttpGetUtil.getHttpData(url, refererUrl);
		// content = content
		// .substring(content.indexOf("id=\"hidGoodsID\"") + 23,
		// content.indexOf("id=\"hidGoodsID\"") + 36)
		// .split("\"")[0];
		// System.out.println("内容：" + content);

		return content;
	}

	// 获取购物车数量
	// 返回值{'code':0, 'num' : 2,'money':3}
	// url="http://cart.1yyg.com/JPData?action=cartnum";
	// refererUrl="http://www.1yyg.com/";

	// 检查是否登录
	// 返回值{"code":1} code0已登录 1未登录
	// url = "http://cart.1yyg.com/JPData?action=checkLogin";

	// 更新购买数量
	// url =
	// "http://cart.1yyg.com/JPData?action=updateCartNum&codeID=2863812&num=11";
	// refererUrl = "http://cart.1yyg.com/";

	// 清除该商品的购物车
	// url = "http://cart.1yyg.com/JPData?action=deleteCart&codeID=2863812";
	// refererUrl = "http://www.1yyg.com/";

	// 获取参与记录
	// 返回值{"Code":0,"Count":55,"Data":{"Tables":{"BuyList":{"Rows":[{"userName":"","userPhoto":"","userWeb":"","buyNum":"","buyIP":"","buyIPAddr":"","buyTime":"","buyDevice":"0","buyID":"318702471"}
	// url =
	// "http://api.1yyg.com/JPData?action=GetUserBuyListByCodeEnd&codeID=2720884&FIdx=1&EIdx=10&isCount=0";
	// refererUrl = "http://www.1yyg.com/lottery/2720884.html";

	// 获取云够码
	// 返回值{'code':0,'data':[{'rnoNum':10003203}]}
	// url =
	// "http://api.1yyg.com/JPData?action=GetUserBuyCodeByBuyid&codeid=2720884&buyid=318702471";
	// refererUrl = "http://www.1yyg.com/lottery/2654407.html";

	// 最新揭晓
	// 返回值{"errorCode":0,"maxSeconds":8299665184.414,"listItems":[{"goodsPic":"","goodsSName":"","seconds":"179","codeID":2777802,"price":"399.00","period":703,"codeQuantity":399,"codeSales":399,"codeType":0}]}
	// url = "http://api.1yyg.com/JPData?action=GetStartRaffleAllList&time=0";
	// refererUrl = "http://www.1yyg.com/";

	// 获取最新购买记录
	// 返回值{'code':0,'listItems':[{'userName':'','goodsID':21877,'','goodsPic':'','buyID':319911394,'userWeb':'','userPhoto':''}]})
	// url =
	// "http://api.1yyg.com/JPData?action=GetUserBuyNewList&buyID=319815943";
	// refererUrl = "http://www.1yyg.com/";

	// 检查商品是否存在
	// 返回值{"code":10} code 10商品存在 -1不存在
	// url =
	// "http://api.1yyg.com/JPData?action=checkCollectGoods&goodsID=22504";
	// refererUrl = "http://www.1yyg.com/products/22504.html";

	// 获取商品最新购买记录（加入购物车）
	// 返回值{"code":1,"num":0,"str":"2799538|28521|28|5188|5160|0"}
	// url =
	// "http://cart.1yyg.com/JPData?action=shopCartNew&codeID=2745603&num=1";
	// refererUrl = "http://www.1yyg.com/";

	// 查询商品期数是否存在 goodsID商品id period期数
	// 返回值{"code":0,"codeID":2654637,"codeState":3}
	// url =
	// "http://api.1yyg.com/JPData?action=getGoodsPeriodInfo&goodsID=22504&period=26124";
	// refererUrl = "http://www.1yyg.com/";

	// 获取最近云购期数 goodsID商品id
	// 返回值{"codePeriod":24698,"codeID":2654546,"codeState":1}
	// codePeriod商品期数 codeID商品网址id codeState揭晓状态：1进行中、2等待揭晓、3已经揭晓
	// url =
	// "http://api.1yyg.com/JPData?action=getGoodsPeriodPage&goodsID=22504&FIdx=1&EIdx=5&IsCount=0";
	// refererUrl = "http://www.1yyg.com/products/22504.html";

	// 搜索商品
	// url =
	// "http://api.1yyg.com/JPData?action=getSearchList&key=苹果（Apple）iPhone%206s%20Plus%20128G版%204G手机&orderFlag=10&FIdx=1&EIdx=40&isCount=1";
	// refererUrl = "http://www.1yyg.com/";

	// 获取本期中奖详情
	// 返回值{"code":0,"codePeriod":25043,"codeRNO":10003451,"codeRTime":"","buyTime":"","price":"","buyCount":3488,"userName":"","userNC":"","ipAddr":"","goodsName":"","goodsPic":"","userPhoto":"","userWeb":"","codeType":0})
	// codeRTime揭晓时间 buyTime云购时间 price价格 buyCount购买数量
	// url =
	// "http://api.1yyg.com/JPData?action=GetBarcodernoInfo&codeID=2710128";
	// refererUrl = "http://www.1yyg.com/";

	// 获取本期中奖云购码 codeID商品网址id
	// 返回值{"buyTime":"2015-12-27 19:44:45.645","rnoNum":"10004140,10000061"}
	// buyTime云购时间 rnoNum云购码
	// url =
	// "http://api.1yyg.com/JPData?action=getUserBuyGoodsCodeInfo&codeID=2654407";
	// refererUrl = "http://www.1yyg.com/";

	// 获取商品最后购买时间100条购买记录
	// 返回值{'code':0,'recordEnd1':[{"buyTime":"","buyName":"","userWeb":"","buyID":,"buyCode":2663993,"buyNum":1,"goodsPeriod":15,"goodsName":""}],
	// 'recordEnd2':[{"buyTime":"","timeCodeVal":"","buyName":"","userWeb":"","buyID":318702471,"buyCode":2654407,"buyNum":2764,"goodsPeriod":24683,"goodsName":""}],
	// 'recordEnd3':[{"buyTime":"","buyName":"","userWeb":"","buyID":,"buyCode":2608228,"buyNum":32,"goodsPeriod":545,"goodsName":""}
	// recordEnd1未知码 recordEnd2最后前95条数据 recordEnd3最后前5条数据
	// url =
	// "http://api.1yyg.com/JPData?action=getLotteryRecords&codeId=2665165";
	// refererUrl = "http://www.1yyg.com/lottery/2665165.html";

	// 首页推荐
	// url = "http://poster.1yyg.com/JPData?action=getbysortid&ID=2";

	// 热门推荐
	// 返回值({'code':0,'listItems':[{'goodsID':22504,'goodsName':'','goodsPic':'','goodsTag':'0','goodsRecDesc':'','codeID':,'codePrice':'5188.00','codeQuantity':5188,'codeSales':238,'goodsNameEx':'','codePeriod':41413}]})
	// url =
	// "http://api.1yyg.com/JPData?action=getRecGoodsList&goodsLabel=12&quantity=8";
	// refererUrl = "http://www.1yyg.com/";

	// 即将揭晓
	// 返回值({"Code":0,"Count":0,"Data":{"Tables":{"Table1":{"Rows":[{"rowID":0,"goodsID":22552,"goodsSName":"","goodsPic":"","codeID":3209376,"codePrice":"8090.00","codeQuantity":8090,"codeSales":7788,"codePeriod":19429,"codeType":0,"goodsTag":"0","codeLimitBuy":"0"}]}}}})
	// url =
	// "http://api.1yyg.com/JPData?action=getGoodsList&sortID=0&brandID=0&orderFlag=0&FIdx=1&EIdx=24&isCount=0";
	// refererUrl = "http://www.1yyg.com/";

	public static void hegoubaTest() {
		String url = null;
		String refererUrl = null;
		String content = null;
		url = "http://help.hego8.com/userLogin.action?userModel.email=cMan&userModel.password=8a60473693bce316b54082d9768dee68&flag=1";
		// url = "http://help.hego8.com/userLogin.action";

		refererUrl = "http://help.hego8.com/tologin.action";
		content = HttpGetUtil.getHttpData(url, refererUrl);
		System.out.println("内容1：" + content);

		url = "http://help.hego8.com/goodsById.action?goodsId=21877";
		content = HttpGetUtil.getHttpData(url, refererUrl);
		System.out.println("内容2：" + content);
		String codePeriod = content.substring(content.indexOf("codePeriod") + 12, content.indexOf("codePrice") - 2);

		url = "http://help.hego8.com/market/getChartData.action?goodsId=22817&beginPeriod=100&endPeriod=101";
		// refererUrl = "http://help.hego8.com/market.action?chartType=0";
		String goodsInfo = HttpGetUtil.getHttpData(url, refererUrl);
		System.out.println("内容3：" + goodsInfo);
		String goodsId = goodsInfo.substring(6, 11);
		goodsInfo = goodsInfo.substring(goodsInfo.indexOf("chartPointList\":[") + 16,
				goodsInfo.indexOf("],\"codeId") + 1);
		List<Map<String, String>> dList = new Gson().fromJson(goodsInfo, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		for (Map<String, String> map : dList) {
			int y = Integer.parseInt(map.get("y"));
			int num = Integer.parseInt(map.get("buyCount"));
			String buySection = "" + (y - 1) + "~" + (y + num);
			String[] SelectAssistText = new String[] { goodsId, map.get("x"), map.get("codeId"), map.get("luckyNum"),
					map.get("buyerName"), map.get("buyCount"), map.get("y"), buySection };
			System.out.println(SelectAssistText[0] + ":" + SelectAssistText[1] + ":" + SelectAssistText[2] + ":"
					+ SelectAssistText[3] + ":" + SelectAssistText[4] + ":" + SelectAssistText[5] + ":"
					+ SelectAssistText[6] + ":" + SelectAssistText[7]);
			// SelectAssistPublishs.uploadYungouPublishs(SelectAssistText);
		}

		// CloseableHttpClient HttpClient = HttpClients.createDefault();
		// try {
		// HttpPost httpost = new HttpPost(url);
		// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// nvps.add(new BasicNameValuePair("userModel.email", "cMan"));
		// nvps.add(new BasicNameValuePair("userModel.password",
		// "8a60473693bce316b54082d9768dee68"));
		// nvps.add(new BasicNameValuePair("flag", "1"));
		//
		// httpost.addHeader("Referer", refererUrl);
		// httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		// HttpResponse httpResponse = HttpClient.execute(httpost);
		// // 获取响应实体
		// HttpEntity httpEntity = httpResponse.getEntity();
		// if (httpEntity != null) {
		// content = EntityUtils.toString(httpEntity);
		// }
		// // content = HttpGetUtil.getHttpData(url, refererUrl);
		// System.out.println("内容：" + content);
		// url =
		// "http://help.hego8.com/market/getChartData.action?goodsId=22817&beginPeriod=100&endPeriod=200";
		// refererUrl = "http://help.hego8.com/market.action?chartType=0";
		// String content2 = HttpGetUtil.getHttpData(url, refererUrl,
		// HttpClient);
		// System.out.println("内容2：" + content2);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// }
	}
}
