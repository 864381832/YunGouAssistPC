package com.ytkj.ygAssist.main;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.client.CloseableHttpClient;

import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.GetUserBuyMonitorServer;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;

/*
 * 云购监控服务
 */
public class YunGouRemindServer {
	private static YunGouRemindServer[] yunGouRemindServer = new YunGouRemindServer[4];
	private Timer timer = null;
	private JFrameListeningInterface foreknowInterface;
	private int listenerIndex;// 监听位置
	private String codeID;
	private String goodsID = null;// 商品ID
	private CloseableHttpClient HttpClient = null;// 查询使用HTTPClient
	private boolean isRemind = false;

	private boolean isNews = true;// 是否开了新一期
	private String newestPeriod = null;// 最新期数
	private String newestCodeID = null;// 最新期数网址id

	public static YunGouRemindServer getYunGouRemindServer(int remindIndex) {
		if (yunGouRemindServer[remindIndex] == null) {
			yunGouRemindServer[remindIndex] = new YunGouRemindServer();
		}
		return yunGouRemindServer[remindIndex];
	}

	public void setRemindServer(String goodsID, String listenerIndex, JFrameListeningInterface foreknowInterface) {
		this.goodsID = goodsID;
		if (CacheData.getGoodsNameCacheDate(goodsID) == null) {
			if (GetGoodsInfo.getGoodsInfoByGoodsID(goodsID)) {
				foreknowInterface.setFrameText("setGoodsName", CacheData.getGoodsNameCacheDate(goodsID));
			} else {
				foreknowInterface.setFrameText("setGoodsNameError", null);
				return;
			}
		} else {
			foreknowInterface.setFrameText("setGoodsName", CacheData.getGoodsNameCacheDate(goodsID));
		}
		try {
			this.listenerIndex = Integer.parseInt(listenerIndex);
		} catch (Exception e) {
			this.listenerIndex = -1;
		}
		this.foreknowInterface = foreknowInterface;
		this.HttpClient = HttpGetUtil.createHttpClient();
		codeID = CacheData.getGoodsInfoCacheDate(goodsID)[2];
		String text[] = GetGoodsInfo.shopCartNew(codeID, this.HttpClient);
		newestPeriod = text[1];
		newestCodeID = text[0];
		try {
			Integer.parseInt(text[3]);
		} catch (Exception e) {
			foreknowInterface.setFrameText("goodsoldOut", null);
			return;
		}
		foreknowInterface.setFrameListeningText("1", text);
		runRemindServer();
	}

	/*
	 * 监控商品
	 */
	public void runRemindServer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String text[] = GetGoodsInfo.shopCartNew(codeID, HttpClient);
				foreknowInterface.setFrameListeningText("2", text);
				if (Integer.parseInt(text[2]) < listenerIndex) {
					isRemind = true;
				} else {
					if (isRemind) {
						foreknowInterface.setFrameText("playRemind", null);
					}
					isRemind = false;
				}

				if (!text[1].equals(newestPeriod)) {
					if (isNews) {
						isNews = false;
						String periodString = newestPeriod;
						String codeIDstring = newestCodeID;
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {
								new GetUserBuyServer(new JFrameListeningInterface() {
								}, "").GetGoodsPeriodInfo(goodsID, periodString, codeIDstring, false);
								// GetUserBuyMonitorServer.selectBarcodernoInfo(new
								// JFrameListeningInterface() {
								// }, "1", goodsID, periodString,
								// codeIDstring).getBarcodernoInfo();
							}
						}, 6000);
						newestPeriod = text[1];
						newestCodeID = text[0];
						GetUserBuyMonitorServer.selectBarcodernoInfo(goodsID, newestPeriod, newestCodeID);
						isNews = true;
					}
				}
			}
		}, 0, 256);

		GetUserBuyMonitorServer.selectBarcodernoInfo(goodsID, newestPeriod, newestCodeID);
	}

	public void stopRemindServer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				HttpGetUtil.CloseHttpClient(HttpClient);
				foreknowInterface.setFrameText("stopRemind", null);
			}
		}, 1000);
	}
}
