package com.ytkj.ygAssist.main;

import java.awt.Component;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.DateUtil;
import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.server.util.Signature;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.AddUserInfo;
import com.ytkj.ygAssist.view.myView.CheckUpdate;
import com.ytkj.ygAssist.view.myView.ExpiringHint;

public class AssistServer {
	// 1元云购用户登录
	public static boolean yunGouUserLogin(String name, String pwd) {
		try {
			StringBuffer getUrl = new StringBuffer();
			// 返回值{"state":1, "num":-1}
			// state登录状态 0登录成功 1登录失败 2用户名格式错误 3失败次数超限，被冻结5分钟
			// num登录失败原因 -1登录密码错误 -2此账号不存在 -3此账号已被冻结 -4此账号未激活-5密码被系统锁定
			getUrl.append("https://passport.1yyg.com/JPData?action=userlogin&name=").append(name);
			getUrl.append("&pwd=").append(URLEncoder.encode(pwd, "UTF-8"));
			String refererUrl = "https://passport.1yyg.com/login.html?forward=rego";
			String content = HttpGetUtil.getHttpData(getUrl.toString(), refererUrl, HttpGetUtil.createHttpClient());
			Map<String, String> contentMap = new Gson().fromJson(content, new TypeToken<Map<String, String>>() {
			}.getType());
			if ("0".equals(contentMap.get("state"))) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// 登录云购助手
	public static void loginAssist(String userid, String password, JFrameListeningInterface foreknowInterface) {
		Config.userName = userid;
		Config.userPassword = password;
		String loginInfo = getLoginAssistInfo(userid, password);
		if (loginInfo == null) {
			foreknowInterface.setFrameText("loginError", "请检查网络");
		} else {
			boolean isLoginSucceed = false;
			if ("801".equals(loginInfo)) {
				if (yunGouUserLogin(userid, password)) {
					loginInfo = registerUser(userid, password);
					if ("802".equals(loginInfo)) {
						foreknowInterface.setFrameText("loginErrorOverdue", "账户已过期");
					} else {
						isLoginSucceed = true;
						AddUserInfo.startUserLogin(loginInfo.split("___")[0]);
					}
				} else {
					foreknowInterface.setFrameText("loginError", "账户名或密码错误");
				}
			} else if ("802".equals(loginInfo)) {
				foreknowInterface.setFrameText("loginErrorOverdue", "账户已过期");
			} else if ("804".equals(loginInfo)) {
				foreknowInterface.setFrameText("loginSeriousnessError", null);
			} else {
				isLoginSucceed = true;
			}
			if (isLoginSucceed) {
				String sig = null;
				long time = 0;
				try {
					time = Long.parseLong(loginInfo.split("___")[0]);
					sig = loginInfo.split("___")[1];
				} catch (Exception e) {
				}
				boolean isSucceed = Signature.checkServerSignature(userid, time, sig);
				if (isSucceed) {
					AssistServer.getIsBuyAssist(time, userid);
					foreknowInterface.setFrameText("loginSucceed", null);
				} else {
					foreknowInterface.setFrameText("loginError", "请检查网络");
				}
			}
		}
	}

	// 登录云购助手
	public static String getLoginAssistInfo(String userid, String password) {
		String url = MyStringUtil.changeCharset(HttpGetUtil.getHttpUrl("userAjax_login"), "UTF-8", "GBK");
		String content = HttpGetUtil.getHttpData(url, null);
		return content;
	}

	// 注册助手账户
	public static String registerUser(String userid, String password) {
		StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("userAjax_registerUser"));
		getUrl.append("&userInfo=").append(Config.yunGouAssistUserInfo);
		String url = MyStringUtil.changeCharset(getUrl.toString(), "UTF-8", "GBK");
		String content = HttpGetUtil.getHttpData(url, null);
		return content;
	}

	// 获取用户剩余使用时间
	public static void getUserExpirationTime(JFrameListeningInterface foreknowInterface) {
		String url = MyStringUtil.changeCharset(HttpGetUtil.getHttpUrl("userAjax_getUserExpirationTime"), "UTF-8",
				"GBK");
		String loginInfo = HttpGetUtil.getHttpData(url, null);
		if (loginInfo != null) {
			if ("801".equals(loginInfo)) {
				foreknowInterface.setFrameText("loginError", "账户名或密码错误，请重新登录");
			} else if ("802".equals(loginInfo)) {
				foreknowInterface.setFrameText("loginErrorOverdue", "账户已过期，请找管理员续费。");
			} else if ("803".equals(loginInfo)) {// 已在其他设备登录
				foreknowInterface.setFrameText("loginError", "该账户已在其他电脑登录，请查看是否有其他人在使用此账户，谢谢");
			} else if ("804".equals(loginInfo)) {
				foreknowInterface.setFrameText("loginError", "请使用正版的云购助手，谢谢（使用盗版助手如有财务损失，本公司概不负责，谢谢）");
			} else {
				long time2 = 0;
				String sig = null;
				try {
					time2 = Long.parseLong(loginInfo.split("___")[0]);
					sig = loginInfo.split("___")[1];
				} catch (Exception e) {
				}
				boolean isSucceed = Signature.checkServerSignature(Config.userName, time2, sig);
				if (isSucceed) {
					String ExpirationTime = null;
					if (time2 > 24 * 60 * 60 * 1000) {
						ExpirationTime = "剩余：" + ((int) (time2 / (24 * 60 * 60 * 1000) + 1) + "天");
					} else if (time2 > 60 * 60 * 1000) {
						ExpirationTime = "剩余：" + ((int) (time2 / (60 * 60 * 1000)) + "小时");
					} else {
						ExpirationTime = "剩余：" + ((int) (time2 / (60 * 1000)) + "分钟");
					}
					foreknowInterface.setFrameText("ExpirationTime", ExpirationTime);
					if (DateUtil.checkIsRemainingTime(time2)) {
						ExpiringHint.startExpiringHint(0);
					}
				} else {
					foreknowInterface.setFrameText("loginError", "网络异常，请重新登录");
				}
			}
		}
	}

	// 保存登录信息
	public static void saveLoginAssistInfo(String userid, String password) {
		FilesUtil.saveUserInfo("\\AssistInfo.log", userid, password);
	}

	// 保存登录信息
	public static String[] readerLoginAssistInfo() {
		return FilesUtil.readerUserInfo("\\AssistInfo.log");
	}

	// 获取用户是否为付费用户
	public static void getIsBuyAssist(long time, String userid) {
		long t = new Date().getTime();
		if (time > (t + 24 * 24 * 60 * 60 * 1000)) {
			Config.isVip = true;
		} else {
			String url = null;
			url = Config.yunGouAssistUrl + "/userAjax_getIsBuyAssist?userid=" + userid;
			String content = HttpGetUtil.getHttpData(url, null);
			if ("true".equals(content)) {
				Config.isVip = true;
			}
		}
	}

	// 获取推广码
	public static String getUserGeneralize() {
		String url = Config.yunGouAssistUrl + "/userAjax_getUserGeneralize?userid=" + Config.userName;
		String content = HttpGetUtil.getHttpData(url, null);
		return content;
	}

	// 填写推广码
	public static String addUserGeneralize(String generalizeId) {
		String url = Config.yunGouAssistUrl + "/userAjax_addUserGeneralize?userid=" + Config.userName + "&userInfo="
				+ generalizeId;
		String content = HttpGetUtil.getHttpData(url, null);
		return content;
	}

	// 获取推广码列表
	public static ArrayList<String[]> getUserGeneralizes() {
		try {
			String content = HttpGetUtil.getHttpData(HttpGetUtil.getHttpUrl("userAjax_getUserGeneralizes"), null);
			ArrayList<String[]> stringList = new Gson().fromJson(content, new TypeToken<ArrayList<String[]>>() {
			}.getType());
			return stringList;
		} catch (Exception e) {
		}
		return null;
	}

	// 领取奖励
	public static boolean updateIsGetRewards(String userid) {
		StringBuffer getUrl = new StringBuffer(HttpGetUtil.getHttpUrl("userAjax_updateIsGetRewards"));
		getUrl.append("&userInfo=").append(userid);
		String content = HttpGetUtil.getHttpData(getUrl.toString(), null);
		return "true".equals(content);
	}

	// 获取客服QQ
	public static void getRenewQQ() {
		String content = AssistServer.getSoftInfo("renewQQ");
		if (content != null) {
			Config.renewQQ = content;
		}
	}

	// 获取推广消息
	public static String getGeneralizeMessage() {
		String content = AssistServer.getSoftInfo("generalizeMessage");
		if (content != null) {
			return content;
		}
		return "推荐一个智能云购助手，用这个看走时图，中奖率不是一般的高啊,下载安装填写我的邀请码计科免费获得3天试用时间,官网http://yg.yuntengkeji.cn/，智能云购 ③群：319643207，";
	}

	// 获取公告
	public static String getCommonality() {
		String content = AssistServer.getSoftInfo("commonality");
		if (content != null) {
			return content;
		}
		return "欢迎使用智能云购助手。";
	}

	// 获取招聘推广信息
	public static String getRecruitMessage() {
		String content = AssistServer.getSoftInfo("recruitMessage");
		if (content != null) {
			return content;
		}
		return "重大福利：\r\n1、加入封侯合资群888833可向群内侯爷助理申请3天延长试用，点击加入（点击加入为qq代码，点击即可加入）\r\n2、加入后每合资超过100元可向群内侯爷助理申请3天延长使用；\r\n3、封侯合资群另设有vip群，每天送出288-1888大红包，同时设有3-6倍倍反活动哦！\r\n\r\n另外；本软件大量招募操盘手，有意者加群514880562";
	}

	public static String getSoftInfo(String info) {
		String url = Config.yunGouAssistUrl + "/userAjax_getSoftInfo?userid=" + info;
		String content = HttpGetUtil.getHttpData(url, null);
		if (content != null) {
			content = MyStringUtil.changeCharset(content, "ISO-8859-1", "UTF-8");
		}
		return content;
	}

	public static String[] getSoftInfo2(String info) {
		String url = Config.yunGouAssistUrl + "/userAjax_getSoftInfo2?userid=" + info;
		String content = HttpGetUtil.getHttpData(url, null);
		if (content != null) {
			content = MyStringUtil.changeCharset(content, "ISO-8859-1", "UTF-8");
			String soft[] = content.split("___");
			if (soft.length == 2) {
				return soft;
			}
		}
		return null;
	}

	// 获取跳转QQ群连接
	public static void getHeziQQqunInfo() {
		String[] content = AssistServer.getSoftInfo2("gotoUrl");
		if (content != null) {
			Config.heziQQqunUrl = content[0];
			Config.heziQQqun = content[1];
		}
		String[] content2 = AssistServer.getSoftInfo2("assistQQqun");
		if (content != null) {
			Config.assistQQqunUrl = content2[0];
			Config.assistQQqun = content2[1];
		}
		String[] content3 = AssistServer.getSoftInfo2("hezuoQQqun");
		if (content != null) {
			Config.hezuoQQqunUrl = content3[0];
			Config.hezuoQQqun = content3[1];
		}
		String[] content4 = AssistServer.getSoftInfo2("shouyeText");
		if (content != null) {
			Config.shouyeText1 = content4[0];
			Config.shouyeText2 = content4[1];
		}
		String[] content5 = AssistServer.getSoftInfo2("assistTitle");
		if (content != null) {
			Config.assistTitleUrl = content5[0];
			Config.assistTitle = content5[1];
		}
		
		String content6 = AssistServer.getSoftInfo("hezuoQQ");
		if (content != null) {
			Config.hezuoQQ = content6;
		}
		
		String content7 = AssistServer.getSoftInfo("isBackgroundMonitorServer");
		if (content != null) {
			Config.isBackgroundMonitorServer = content7.equals("true");
		}
	}

	// 获取跳转QQ群二维码图片连接
	public static String getHeziQQqunEwmUrl() {
		String content = AssistServer.getSoftInfo("heziQQqunEwmUrl");
		if (content != null) {
			return content;
		}
		return "http://7xqag1.dl1.z0.glb.clouddn.com/yshzq.png";
	}

	// 检查是否有新版本
	public static void getSoftVersions(Component parentComponent) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String soft1[] = AssistServer.getSoftInfo2("explainTextUrl");
					if (soft1 != null) {
						Config.assistExplainTextUrl = soft1[0];
						Config.assistHelpVideoUrl = soft1[1];
					}
					String url = Config.yunGouAssistUrl + "/userAjax_getSoftDownloadUrl";
					String content = HttpGetUtil.getHttpData(url, null);
					if (content != null) {
						content = MyStringUtil.changeCharset(content, "ISO-8859-1", "UTF-8");
						String soft[] = content.split("___");
						if (Integer.parseInt(soft[0]) > Config.yunGouAssistVersionsInteger) {
							int value = JOptionPane.showConfirmDialog(parentComponent, "发现智能云购助手有新版本了，赶紧下载更新吧！！！",
									"是否现在去更新版本，更强大的功能等着你哟！！！", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (value == JOptionPane.YES_OPTION) {
								CheckUpdate.startUpdate(soft[1]);
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

}
