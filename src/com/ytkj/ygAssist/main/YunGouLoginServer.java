package com.ytkj.ygAssist.main;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.impl.client.CloseableHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;

public class YunGouLoginServer {
	private boolean isLogin = false;
	private static YunGouLoginServer userServer = null;
	private CloseableHttpClient HttpClient = null;// 用户使用HTTPClient
	private Timer loginTimer = null;

	public static YunGouLoginServer getUserServer() {
		if (userServer == null) {
			userServer = new YunGouLoginServer();
		}
		return userServer;
	}

	private YunGouLoginServer() {
		HttpClient = HttpGetUtil.createHttpClient();
	}

	// 用户登录
	public boolean userLogin(String name, String pwd) {
		try {
			StringBuffer getUrl = new StringBuffer();
			// 返回值{"state":1, "num":-1}
			// state登录状态 0登录成功 1登录失败 2用户名格式错误 3失败次数超限，被冻结5分钟
			// num登录失败原因 -1登录密码错误 -2此账号不存在 -3此账号已被冻结 -4此账号未激活-5密码被系统锁定
			getUrl.append("https://passport.1yyg.com/JPData?action=userlogin&name=").append(name);
			getUrl.append("&pwd=").append(URLEncoder.encode(pwd, "UTF-8"));
			String refererUrl = "https://passport.1yyg.com/login.html?forward=rego";
			String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpClient);
			Map<String, String> contentMap = new Gson().fromJson(content, new TypeToken<Map<String, String>>() {
			}.getType());
			if ("0".equals(contentMap.get("state"))) {
				isLogin = true;
				loginTimer = new Timer();
				loginTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						String url = "http://api.1yyg.com/JPData?action=logininfo";
						String refererUrl = "http://www.1yyg.com/";
						HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
//						String content = HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
						// System.out.println("登录了：" + content);
					}
				}, 10000, 10000);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public String getUserBalance() {
		// 获取账户余额
		String url = "http://member.1yyg.com/UserBalance.do";
		String refererUrl = "http://member.1yyg.com/Index.do";
		String content = HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
		String string = content.substring(content.indexOf("id=\"hidAmount\"") + 22,
				content.indexOf("id=\"hidAmount\"") + 40);
		return string.split("\"")[0];
	}

	// 退出登录
	public void userLogout() {
		if (loginTimer != null) {
			loginTimer.cancel();
			loginTimer = null;
		}
		String getUrl = "https://passport.1yyg.com/Logout.html";
		String refererUrl = "http://www.1yyg.com/";
		HttpGetUtil.getHttpData(getUrl, refererUrl, HttpClient);
		isLogin = false;
	}

	// 购买商品
	public void buyGoods(String codeID, String buyNum, JFrameListeningInterface foreknowInterface) {
		try {
			boolean isBuySucceed = false;
			// 返回值{"code":0,"num":2,"str":"1114.00"}
			StringBuffer getUrl = new StringBuffer();
			getUrl.append("http://cart.1yyg.com/JPData?action=shopCartNew&codeID=").append(codeID);
			getUrl.append("&num=").append(buyNum);
			String refererUrl = "http://www.1yyg.com/";
			refererUrl = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpClient);
			// System.out.println("准备购买：" + refererUrl);
			// 购买
			// 返回值{"state":0,"str":"160107214553561185"}
			// state 0购买成功 1余额不足
			String url = "http://trade.1yyg.com/JPData/API.ashx?action=UserPay&device=0&integral=0&isBalance=1&payMoney=1&checkSN=";
			refererUrl = "http://cart.1yyg.com/payment.do";
			refererUrl = HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
			// System.out.println("购买完成：" + refererUrl);
			System.out.println("购买"+refererUrl);
			Map<String, String> dMapBuy = new Gson().fromJson(refererUrl, new TypeToken<Map<String, String>>() {
			}.getType());
			if (dMapBuy.get("state").equals("0")) {
				foreknowInterface.setFrameText("buySucceed", "购买成功,正在获取云购码，请稍等...");
				isBuySucceed = true;
				try {
					StringBuffer stringBuffer = new StringBuffer();
					String idStr = refererUrl.split("str\":\"", 2)[1].split("\"}", 2)[0];

					Map<String, String> dMapInfo = GetGoodsInfo.getshopresult(idStr, HttpClient);
					stringBuffer.append("购买时间：").append(dMapInfo.get("buyTime"));
					stringBuffer.append("\n购买数量：").append(dMapInfo.get("buyNum"));
					stringBuffer.append("\n云购码：");
					String userBuyCode = GetGoodsInfo.getUserBuyCode(codeID, dMapInfo.get("buyID"));
					String data = userBuyCode.split(",'data':", 2)[1];
					data = data.substring(0, data.length() - 2);
					List<Map<String, String>> dList = new Gson().fromJson(data,
							new TypeToken<List<Map<String, String>>>() {
							}.getType());
					for (int i = 0; i < dList.size(); i++) {
						stringBuffer.append(dList.get(i).get("rnoNum") + ",");
						if (i % 10 == 9) {
							stringBuffer.append("\n");
						}
					}
					foreknowInterface.setFrameText("buySucceedCode", stringBuffer.toString());
				} catch (Exception e) {
					if (isBuySucceed) {
						foreknowInterface.setFrameText("buySucceedCode", "已经购买成功，但未能成功获取云购码，请到云购网站查看您的购买码，谢谢");
					}
				}
			} else {
				foreknowInterface.setFrameText("buyError", null);
			}
		} catch (Exception e) {
			foreknowInterface.setFrameText("buyError", null);
		}
	}

	public boolean getUserBuyDetail(String codeID, String codeRNO) {
		String url = "http://member.1yyg.com/UserBuyDetail-" + codeID + ".do";
		String refererUrl = "http://member.1yyg.com/Index.do";
		String content = HttpGetUtil.getHttpData(url, refererUrl, HttpClient);
		boolean isWinning = false;
		try {
			String string = content.substring(content.indexOf("</em></p><span>") + 15,
					content.indexOf("</span> </dd>"));
			String[] strs = string.split("</span><span>");
			for (int i = 0; i < strs.length; i++) {
				if (codeRNO.equals(strs[i])) {
					isWinning = true;
					break;
				}
				System.out.println(strs[i]);
			}
		} catch (Exception e) {
		}
		return isWinning;
	}

	public boolean getIsLogin() {
		return isLogin;
	}

	// 保存登录信息
	public static void saveUserLoginInfo(String userid, String password) {
		FilesUtil.saveUserInfo("\\YunGouUserInfo.log", userid, password);
	}

	// 读取登录信息
	public static String[] readerUserLoginInfo() {
		return FilesUtil.readerUserInfo("\\YunGouUserInfo.log");
	}
}
