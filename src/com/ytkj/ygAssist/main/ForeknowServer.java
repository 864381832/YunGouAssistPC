package com.ytkj.ygAssist.main;

import java.util.List;
import java.util.Map;
import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;

/*
 * 提前揭晓服务
 */
public class ForeknowServer {
	public static void getForeknow(String goodsID, String period, String EIdx,
			JFrameListeningInterface foreknowInterface, int selectType) {
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
		if (period != null) {
			if (CacheData.getSelectCacheDate(goodsID, period) == null) {
				SelectAssistPublishs.getYungouPublishs(goodsID, period, "1");
				if (CacheData.getSelectCacheDate(goodsID, period) == null) {
					String[] content = GetGoodsInfo.getGoodsPeriodInfo(goodsID, period);
					if (content != null) {
						if (content[0].equals("1")) {
							String[] text = new String[] { goodsID, period, "正在云购中", "正在云购中", "正在云购中", "正在云购中",
									"正在云购中" };
							foreknowInterface.setFrameListeningText("0", text);
						} else if (content[0].equals("2")) {
							GetUserBuyServer.getUserBuyServer(foreknowInterface, "0", content[1], selectType)
									.GetGoodsPeriodInfo(goodsID, period, content[1], false);
						} else if (content[0].equals("3")) {
							GetUserBuyServer.getUserBuyServer(foreknowInterface, "0", content[1], selectType)
									.GetGoodsPeriodInfo(goodsID, period, content[1], true);
						} else {
							foreknowInterface.setFrameText("selectError", goodsID + "___" + period + "___0");
						}
					} else {
						foreknowInterface.setFrameText("selectError", goodsID + "___" + period + "___0");
					}
				} else {
					foreknowInterface.setFrameListeningText("0", CacheData.getSelectCacheDate(goodsID, period));
				}
			} else {
				foreknowInterface.setFrameListeningText("0", CacheData.getSelectCacheDate(goodsID, period));
			}
		} else {
			boolean isHavaData = true;
			String text[] = GetGoodsInfo.shopCartNew(CacheData.getGoodsInfoCacheDate(goodsID)[2],
					HttpGetUtil.getHttpClient());
			try {
				int newestPeriod = Integer.parseInt(text[1]);
				for (int i = 1; i < Integer.parseInt(EIdx) && i < newestPeriod; i++) {
					if (CacheData.getSelectCacheDate(goodsID, Integer.toString(newestPeriod - i)) == null) {
						isHavaData = false;
					} else {
						foreknowInterface.setFrameListeningText(Integer.toString(i),
								CacheData.getSelectCacheDate(goodsID, Integer.toString(newestPeriod - i)));
					}
				}
				CacheData.setGoodsPriceCacheDate(goodsID, Integer.parseInt(text[3]));
			} catch (Exception e) {
			}
			int price = Integer.parseInt(text[3]);
			CacheData.setGoodsPriceCacheDate(goodsID, price);
			if (!isHavaData) {
				SelectAssistPublishs.getYungouPublishs(goodsID, "0", EIdx);
				List<Map<String, String>> contentList = GetGoodsInfo.getBarcodeRaffListByGoodsID(goodsID, EIdx);
				if (contentList != null) {
					// for (int selectIndex = 0; selectIndex <
					// contentList.size(); selectIndex++) {
					// int selectIndex1 = selectIndex;
					// Map<String, String> map = contentList.get(selectIndex);
					int selectIndex = 0;
					for (Map<String, String> map : contentList) {
						if (CacheData.getSelectCacheDate(goodsID, map.get("codePeriod")) == null) {
							int selectIndex1 = selectIndex;
							Thread thread = new Thread(new Runnable() {
								public void run() {
									if (map.get("codeState").equals("1")) {
										String[] text = new String[] { goodsID, map.get("codePeriod"), "正在云购中", "正在云购中",
												"正在云购中", "正在云购中", "正在云购中" };
										foreknowInterface.setFrameListeningText("" + selectIndex1, text);
									} else {
										if (map.get("codeRNO").equals("")) {
											GetUserBuyServer.getUserBuyServer(foreknowInterface, "" + selectIndex1,
													map.get("codeID"), selectType).GetGoodsPeriodInfo(goodsID,
															map.get("codePeriod"), map.get("codeID"), false);
										} else {
											GetUserBuyServer getUserBuyServer = GetUserBuyServer.getUserBuyServer(
													foreknowInterface, "" + selectIndex1, map.get("codeID"),
													selectType);
											getUserBuyServer.setBarcodernoInfo(map.get("codeRNO"), map.get("userName"),
													map.get("userWeb"));
											getUserBuyServer.GetGoodsPeriodInfo(goodsID, map.get("codePeriod"),
													map.get("codeID"), false);
										}
									}
								}
							});
							thread.start();
						} else {
							foreknowInterface.setFrameListeningText("" + selectIndex,
									CacheData.getSelectCacheDate(goodsID, map.get("codePeriod")));
						}
						selectIndex++;
					}
				} else {
					foreknowInterface.setFrameText("goodsoldOut", null);
				}
			} else {
				String[] newtext = new String[] { goodsID, text[1], "正在云购中", "正在云购中", "正在云购中", "正在云购中", "正在云购中" };
				foreknowInterface.setFrameListeningText("" + 0, newtext);
			}
		}
	}
}
