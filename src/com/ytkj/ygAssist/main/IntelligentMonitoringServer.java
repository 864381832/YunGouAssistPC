package com.ytkj.ygAssist.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.client.CloseableHttpClient;

import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.GetUserBuyMonitorServer;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.MainJFrame;
import com.ytkj.ygAssist.viewControl.IntelligentMonitoringControl;

/*
 * 云购监控服务
 */
public class IntelligentMonitoringServer {
	private static IntelligentMonitoringServer yunGouRemindServer = null;
	private String goodsID = null;// 商品ID
	private String newestPeriod = null;// 最新期数
	private String EIdx = null;// 查询多少期
	private Timer timer = null;// 监听进度条事件
	private Timer timerZeroize = null;// 监听走势是否遗漏事件
	private JFrameListeningInterface foreknowInterface;
	private String codeID = null;
	private String newestCodeID = null;// 最新期数网址id
	private boolean isNews = true;// 是否开了新一期
	private CloseableHttpClient HttpClient = null;// 查询使用HTTPClient
	private ArrayList<Thread> threadList = new ArrayList<Thread>();// 线程

	public static IntelligentMonitoringServer getIntelligentMonitoringServer() {
		if (yunGouRemindServer == null) {
			yunGouRemindServer = new IntelligentMonitoringServer();
		}
		return yunGouRemindServer;
	}

	public void setRemindServer(String goodsID, String EIdx, JFrameListeningInterface foreknowInterface) {
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
		this.EIdx = EIdx;
		this.foreknowInterface = foreknowInterface;
		this.goodsID = goodsID;
		this.HttpClient = HttpGetUtil.createHttpClient();
		codeID = CacheData.getGoodsInfoCacheDate(goodsID)[2];
		String text[] = GetGoodsInfo.shopCartNew(codeID, HttpClient);
		newestPeriod = text[1];
		newestCodeID = text[0];
		foreknowInterface.setFrameListeningText("1", text);
		try {
			CacheData.setGoodsPriceCacheDate(goodsID, Integer.parseInt(text[3]));
		} catch (Exception e) {
		}
		setChart(goodsID);
		runRemindServer();
		runZeroizeServer();
	}

	private void setChart(String goodsID) {// 设置曲线图
		boolean isHavaData = true;
		try {
			int newPeriod = Integer.parseInt(newestPeriod);
			for (int i = 1; i < Integer.parseInt(EIdx); i++) {
				if (CacheData.getSelectCacheDate(goodsID, "" + (newPeriod - i)) == null) {
					isHavaData = false;
				} else {
					foreknowInterface.setFrameListeningText("3",
							CacheData.getSelectCacheDate(goodsID, "" + (newPeriod - i)));
				}
			}
		} catch (Exception e) {
		}
		if (!isHavaData) {
			new Thread(new Runnable() {
				public void run() {
					SelectAssistPublishs.getYungouPublishs(goodsID, "0", EIdx);
					List<Map<String, String>> contentList = GetGoodsInfo.getBarcodeRaffListByGoodsID(goodsID, EIdx);
					for (int selectIndex = 0; selectIndex < contentList.size(); selectIndex++) {
						int selectIndex1 = selectIndex;
						Map<String, String> map = contentList.get(selectIndex1);
						if (CacheData.getSelectCacheDate(goodsID, map.get("codePeriod")) == null) {
							Thread thread = new Thread(new Runnable() {
								public void run() {
									if (map.get("codeState").equals("1")) {
									} else {
										if (map.get("codeRNO").equals("")) {
											GetUserBuyServer
													.getUserBuyServer(foreknowInterface, "3", map.get("codeID"), 1)
													.GetGoodsPeriodInfo(goodsID, map.get("codePeriod"),
															map.get("codeID"), false);
										} else {
											GetUserBuyServer getUserBuyServer = GetUserBuyServer
													.getUserBuyServer(foreknowInterface, "3", map.get("codeID"), 1);
											getUserBuyServer.setBarcodernoInfo(map.get("codeRNO"), map.get("userName"),
													map.get("userWeb"));
											getUserBuyServer.GetGoodsPeriodInfo(goodsID, map.get("codePeriod"),
													map.get("codeID"), false);
										}
									}
								}
							});
							thread.start();
							threadList.add(thread);
						} else {
							foreknowInterface.setFrameListeningText("3",
									CacheData.getSelectCacheDate(goodsID, map.get("codePeriod")));
						}
					}
				}
			}).start();
		}

	}

	/*
	 * 监控商品
	 */
	private void runRemindServer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String text[] = GetGoodsInfo.shopCartNew(codeID, HttpClient);
				if (text != null) {
					foreknowInterface.setFrameListeningText("2", text);
					if (!text[1].equals(newestPeriod)) {
						if (isNews) {
							isNews = false;
							foreknowInterface.setFrameText("newestCodeID", text[0]);
							foreknowInterface.setFrameText("newestPeriod", text[1]);
							String periodString = newestPeriod;
							String codeIDstring = newestCodeID;
							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {
									GetUserBuyServer.getUserBuyServer(foreknowInterface, "3", codeIDstring, 1)
											.GetGoodsPeriodInfo(goodsID, periodString, codeIDstring, false);
									// GetUserBuyMonitorServer.selectBarcodernoInfo(new
									// JFrameListeningInterface() {
									// }, "1", goodsID, periodString,
									// codeIDstring).getBarcodernoInfo();
								}
							}, 2000);
							newestPeriod = text[1];
							newestCodeID = text[0];
							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {
									GetUserBuyMonitorServer.selectBarcodernoInfo(goodsID, newestPeriod, newestCodeID);
								}
							}, 4000);
							isNews = true;
						}
					}
				}
			}
		}, 128, 128);

		GetUserBuyMonitorServer.selectBarcodernoInfo(goodsID, newestPeriod, newestCodeID);
	}

	private void runZeroizeServer() {
		timerZeroize = new Timer();
		timerZeroize.schedule(new TimerTask() {
			@Override
			public void run() {
				IntelligentMonitoringControl intelligentMonitoring = (IntelligentMonitoringControl) MainJFrame
						.getMainJFrame().getTabbedPaneSelected(1);
				ArrayList<String> values = intelligentMonitoring.getNotData();
				for (String period : values) {
					System.out.println("检测到为零" + period);
					SelectAssistPublishs.getYungouPublishs(goodsID, period, "1");
					if (CacheData.getSelectCacheDate(goodsID, period) == null) {
						String[] content = GetGoodsInfo.getGoodsPeriodInfo(goodsID, period);
						try {
							if (content[0].equals("2")) {
								GetUserBuyServer.getUserBuyServer(foreknowInterface, "3", content[1], 1)
										.GetGoodsPeriodInfo(goodsID, period, content[1], false);
							} else if (content[0].equals("3")) {
								GetUserBuyServer.getUserBuyServer(foreknowInterface, "3", content[1], 1)
										.GetGoodsPeriodInfo(goodsID, period, content[1], true);
							}
						} catch (Exception e) {
						}
					} else {
						foreknowInterface.setFrameListeningText("3", CacheData.getSelectCacheDate(goodsID, period));
					}
				}
			}
		}, 10000, 5000);
	}

	public void stopRemindServer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerZeroize != null) {
			timerZeroize.cancel();
			timerZeroize = null;
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				for (Thread thread : threadList) {
					thread.stop();
					thread = null;
				}
				threadList.removeAll(threadList);
				HttpGetUtil.CloseHttpClient(HttpClient);
				foreknowInterface.setFrameText("stopRemind", null);
			}
		}, 1000);
	}
}
