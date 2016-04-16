package com.ytkj.ygAssist.main;

import com.ytkj.ygAssist.tools.JFrameListeningInterface;

public class YunGouServer {
	/*
	 * 提前揭晓：根据商品ID和期数来查询
	 */
	public static void setForeknow(String goodsID, String period, String EIdx,
			JFrameListeningInterface foreknowInterface) {
		ForeknowServer.getForeknow(goodsID, period, EIdx, foreknowInterface, 2);
	}

	/*
	 * 马上开奖
	 */
	public static void setNowWinning(String goodsID, String period, String EIdx,
			JFrameListeningInterface foreknowInterface) {
		ForeknowServer.getForeknow(goodsID, period, EIdx, foreknowInterface, 3);
	}

	/*
	 * 监控商品
	 */
	public static void setYunGouRemind(boolean isListener, int remindIndex, String goodsID, String listenerIndex,
			JFrameListeningInterface foreknowInterface) {
		new Thread(new Runnable() {
			public void run() {
				YunGouRemindServer yunGouRemindServer = YunGouRemindServer.getYunGouRemindServer(remindIndex);
				if (isListener) {
					yunGouRemindServer.setRemindServer(goodsID, listenerIndex, foreknowInterface);
				} else {
					yunGouRemindServer.stopRemindServer();
				}
			}
		}).start();
	}

	/*
	 * 智能监听
	 */
	public static void setIntelligentMonitoring(boolean isListener, String goodsID, String EIdx,
			JFrameListeningInterface foreknowInterface) {
		IntelligentMonitoringServer intelligentMonitoringServer = IntelligentMonitoringServer
				.getIntelligentMonitoringServer();
		if (isListener) {
			intelligentMonitoringServer.setRemindServer(goodsID, EIdx, foreknowInterface);
		} else {
			intelligentMonitoringServer.stopRemindServer();
		}
	}

	/*
	 * 走势分析：根据商品ID和期数来查询
	 */
	public static void setTrendChart(String goodsID, String EIdx, JFrameListeningInterface foreknowInterface) {
		TrendChartServer.getTrendChart(goodsID, EIdx, foreknowInterface);
	}

	/*
	 * 云购登录
	 */
	public static void yunGouLogin(boolean isLogin, String name, String pwd,
			JFrameListeningInterface foreknowInterface) {
		new Thread(new Runnable() {
			public void run() {
				if (isLogin) {
					if (YunGouLoginServer.getUserServer().userLogin(name, pwd)) {
						foreknowInterface.setFrameText("loginSucceed", name);
						String balance = YunGouLoginServer.getUserServer().getUserBalance();
						foreknowInterface.setFrameText("loginBalance", balance);
					} else {
						foreknowInterface.setFrameText("loginError", null);
					}
				} else {
					YunGouLoginServer.getUserServer().userLogout();
					foreknowInterface.setFrameText("logout", null);
				}
			}
		}).start();
	}

	/*
	 * 购买
	 */
	public static void buyGoods(String codeID, String buyNum, JFrameListeningInterface foreknowInterface) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				YunGouLoginServer.getUserServer().buyGoods(codeID, buyNum, foreknowInterface);
			}
		}).start();
	}

	/*
	 * 登录助手
	 */
	public static void loginAssist(String userid, String password, JFrameListeningInterface foreknowInterface) {
		new Thread(new Runnable() {
			public void run() {
				AssistServer.loginAssist(userid, password, foreknowInterface);
			}
		}).start();
	}
}