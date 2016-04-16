package com.ytkj.ygAssist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.YungouDataTools;

public class BackgroundMonitorServer {
	private static BackgroundMonitorServer backgroundMonitor = null;
	private Thread backgroundMonitorServerThread = null;

	public static BackgroundMonitorServer getBackgroundMonitor() {
		if (backgroundMonitor == null) {
			backgroundMonitor = new BackgroundMonitorServer();
		}
		return backgroundMonitor;
	}

	/*
	 * 用户监听操作
	 */
	public void startServer() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
						Object[] objects = GetGoodsInfo.getStartRaffleAllList("0");
						List<Map<String, String>> dList = (List<Map<String, String>>) objects[1];
						for (int i = dList.size() - 1; i >= 0; i--) {
							Map<String, String> map = dList.get(i);
							int price = Integer.parseInt(map.get("codeQuantity"));
							String goodsID = YungouDataTools.getGoodsID(map.get("goodsSName"), map.get("codeType"));
							CacheData.setGoodsPriceCacheDate(goodsID, price);
							selectZeroize(goodsID, map.get("period"), price, new JFrameListeningInterface() {
							}, true);
							Thread.sleep(3 * 60 * 1000);
						}
					} catch (Exception e) {
					}
				}
			}
		}).start();
	}

	/*
	 * 监听商品 goodsPriceType 0小件，1大件
	 */
	public void startBackgroundMonitorServerThread(JFrameListeningInterface foreknowInterface, int goodsPriceType) {
		if (backgroundMonitorServerThread == null) {
			backgroundMonitorServerThread = new Thread(new Runnable() {
				public void run() {
					String maxSeconds = "0";
					while (true) {
						try {
							Object[] objects = GetGoodsInfo.getStartRaffleAllList(maxSeconds);
							maxSeconds = (String) objects[0];
							List<Map<String, String>> dList = (List<Map<String, String>>) objects[1];
							for (int i = dList.size() - 1; i >= 0; i--) {
								Map<String, String> map = dList.get(i);
								int price = Integer.parseInt(map.get("codeQuantity"));
								if ((price < 6000 && goodsPriceType == 0)
										|| (price >= 6000 && price <= 30000 && goodsPriceType == 1)) {
									String goodsID = YungouDataTools.getGoodsID(map.get("goodsSName"),
											map.get("codeType"));
									CacheData.setGoodsPriceCacheDate(goodsID, price);
									System.out.println("准备：" + goodsID + dList.get(i).get("goodsSName"));
									if (goodsID != null
											&& !SelectAssistPublishs.getYungouPublishs(goodsID, map.get("period"))) {
										if (price <= 7000) {
											new GetUserBuyServer(foreknowInterface, "0").GetGoodsPeriodInfo(goodsID,
													map.get("period"), map.get("codeID"), true);
										} else {
											new GetUserBuyServerBigGoods(foreknowInterface, "0")
													.GetGoodsPeriodInfo(goodsID, map.get("period"), map.get("codeID"));
										}
										Thread.sleep(5000);
									}
								}
								if (price >= 30000) {
									String goodsID = YungouDataTools.getGoodsID(map.get("goodsSName"),
											map.get("codeType"));
									System.out.println("超级大件：" + goodsID + map.get("goodsSName"));
									if (goodsID != null
											&& !SelectAssistPublishs.getYungouPublishs(goodsID, map.get("period"))) {
										CacheData.setGoodsPriceCacheDate(goodsID, price);
										new Timer().schedule(new TimerTask() {
											@Override
											public void run() {
												new GetUserBuyServerBigGoods(foreknowInterface, "0").GetGoodsPeriodInfo(
														goodsID, map.get("period"), map.get("codeID"));
											}
										}, 31000);
										Thread.sleep(2000);
									}
								}
							}
							Thread.sleep(1000);
						} catch (Exception e) {
						}
					}
				}
			});
			backgroundMonitorServerThread.start();
		}
	}

	/*
	 * 监听超级大件
	 */
	public void startBackgroundMonitorServerSuperBigThread(JFrameListeningInterface foreknowInterface) {
		if (backgroundMonitorServerThread == null) {
			backgroundMonitorServerThread = new Thread(new Runnable() {
				public void run() {
					String maxSeconds = "0";
					while (true) {
						try {
							Object[] objects = GetGoodsInfo.getStartRaffleAllList(maxSeconds);
							maxSeconds = (String) objects[0];
							List<Map<String, String>> dList = (List<Map<String, String>>) objects[1];
							for (int i = dList.size() - 1; i >= 0; i--) {
								int i1 = i;
								int price = Integer.parseInt(dList.get(i).get("codeQuantity"));
								if (price >= 30000) {
									String goodsID = YungouDataTools.getGoodsID(dList.get(i).get("goodsSName"),
											dList.get(i).get("codeType"));
									System.out.println("超级大件：" + goodsID + dList.get(i).get("goodsSName"));
									if (goodsID != null && !SelectAssistPublishs.getYungouPublishs(goodsID,
											dList.get(i).get("period"))) {
										CacheData.setGoodsPriceCacheDate(goodsID, price);
										new Timer().schedule(new TimerTask() {
											@Override
											public void run() {
												new GetUserBuyServerBigGoods(foreknowInterface, "0").GetGoodsPeriodInfo(
														goodsID, dList.get(i1).get("period"),
														dList.get(i1).get("codeID"));
											}
										}, 31000);
										Thread.sleep(2000);
									}
								}
								Thread.sleep(2000);
							}
							Thread.sleep(2000);
						} catch (Exception e) {
						}
					}
				}
			});
			backgroundMonitorServerThread.start();
		}
	}

	/*
	 * 监听补零 goodsPriceType 0小件，1大件，超大件
	 */
	public void startBackgroundMonitorServerThreadZeroize(JFrameListeningInterface foreknowInterface,
			int goodsPriceType) {
		if (backgroundMonitorServerThread == null) {
			backgroundMonitorServerThread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							List<Map<String, String>> sList = GetGoodsInfo.getGoodsList();
							for (Map<String, String> map : sList) {
								int price = Integer.parseInt(map.get("codeQuantity"));
								if ((price < 4000 && goodsPriceType == 0)
										|| (price >= 4000 && price < 30000 && goodsPriceType == 1)
										|| (price >= 30000 && goodsPriceType == 2)) {
									String goodsID = map.get("goodsID");
									CacheData.setGoodsPriceCacheDate(goodsID, price);
									selectZeroize(goodsID, map.get("codePeriod"), price, foreknowInterface, false);
								}
							}
						} catch (Exception e) {
						}
					}
				}
			});
			backgroundMonitorServerThread.start();
		}

	}

	/*
	 * 补零操作
	 */
	private void selectZeroize(String goodsID, String codePeriod, int price, JFrameListeningInterface foreknowInterface,
			boolean isUserQuery) {
		ArrayList<Integer> periodArray = SelectAssistPublishs.getYungouPublishsNotData(goodsID, codePeriod);
		// System.out.println("准备在查：" + goodsID + ":" + codePeriod);
		try {
			if (isUserQuery || periodArray.size() > 1) {
				List<Map<String, String>> codeList = GetGoodsInfo.getBarcodeRaffListByGoodsID(goodsID, "50");
				for (Map<String, String> codeMap : codeList) {
					for (Integer period : periodArray) {
						if (codeMap.get("codePeriod").equals("" + period)) {
							if (!codeMap.get("codeRNO").equals("") || isUserQuery) {
								System.out.println("正在查：" + goodsID + ":" + period + ":" + codeMap.get("codeRNO"));
								if (price < 30000) {
									GetUserBuyServer getUserBuyServer = new GetUserBuyServer(foreknowInterface, "0");
									getUserBuyServer.setBarcodernoInfo(codeMap.get("codeRNO"), codeMap.get("userName"),
											codeMap.get("userWeb"));
									getUserBuyServer.GetGoodsPeriodInfo(goodsID, "" + period, codeMap.get("codeID"),
											false);
									if (price < 200) {
										Thread.sleep(3000);
									} else {
										Thread.sleep(10 * 1000);
									}
								} else {
									GetUserBuyServerBigGoods getUserBuyServer = new GetUserBuyServerBigGoods(
											foreknowInterface, "0");
									getUserBuyServer.GetGoodsPeriodInfo(goodsID, "" + period, codeMap.get("codeID"));
									Thread.sleep(30000);
								}
							}
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stopBackgroundMonitorServerThread() {
		if (backgroundMonitorServerThread != null) {
			backgroundMonitorServerThread.stop();
			backgroundMonitorServerThread = null;
		}
	}
}
