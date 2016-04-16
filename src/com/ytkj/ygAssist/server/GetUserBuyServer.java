package com.ytkj.ygAssist.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.client.CloseableHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.YungouDataTools;

public class GetUserBuyServer {
	public static HashMap<String, GetUserBuyServer> startThread = new HashMap<String, GetUserBuyServer>();
	public int selectType = 0;
	private boolean isSelect = false;
	private String goodsID = null;// 商品ID
	private String codePeriod = null;// 购买期数
	private String codeID = null;// 网址ID
	private Integer index = 0;// 记录中奖位
	private Integer buyStartPosition = 0;// 购买起始位置
	private String codeRNO = null;// 中奖码
	private String userName = null;// 中奖人姓名
	private String buyNum = null;// 本期中奖人购买数量
	private boolean isFind = true;// 是否结束查询
	private JFrameListeningInterface foreknowJInterface = null;
	private String selectIndex = null;
	private CloseableHttpClient HttpClient = null;// 查询使用HTTPClient
	private int selectNum = 0;// 查询次数
	private String goodsKeyUserWeb = null;// 中奖人web

	private int userBuyListCount = 0;// 记录参与人数

	public static GetUserBuyServer getUserBuyServer(JFrameListeningInterface foreknowJInterface, String selectIndex,
			String codeID, int selectType) {
		GetUserBuyServer getUserBuyServer = startThread.get(codeID + selectType);
		if (getUserBuyServer == null) {
			getUserBuyServer = new GetUserBuyServer(foreknowJInterface, selectIndex);
			getUserBuyServer.selectType = selectType;
			startThread.put(codeID + selectType, getUserBuyServer);
		} else {
			getUserBuyServer.isSelect = true;
		}
		return getUserBuyServer;
	}

	public GetUserBuyServer(JFrameListeningInterface foreknowJInterface, String selectIndex) {
		this.foreknowJInterface = foreknowJInterface;
		this.selectIndex = selectIndex;
		this.HttpClient = HttpGetUtil.createHttpClient();
	}

	public void setBarcodernoInfo(String codeRNO, String userName, String goodsKeyUserWeb) {
		this.codeRNO = codeRNO;
		this.userName = userName;
		this.goodsKeyUserWeb = goodsKeyUserWeb;
	}

	/*
	 * 查询商品期数是否存在 goodsID商品id period期数
	 */
	public void GetGoodsPeriodInfo(String goodsID, String codePeriod, String codeID, boolean codeState) {
		if (isSelect) {
			System.out.println("正在查");
			return;
		}
		this.goodsID = goodsID;
		this.codePeriod = codePeriod;
		this.codeID = codeID;
		if (CacheData.getGoodsPriceCacheDate(goodsID) == null) {
			try {
				CacheData.setGoodsPriceCacheDate(goodsID,
						Integer.parseInt(GetGoodsInfo.shopCartNew(codeID, HttpClient)[3]));
			} catch (Exception e) {
				foreknowJInterface.setFrameText("goodsoldOut", null);
				return;
			}
		}
		if (codeState) {
			try {
				Map<String, String> barcodernoInfo = GetGoodsInfo.getBarcodernoInfo(codeID);
				if (barcodernoInfo != null) {
					codeRNO = barcodernoInfo.get("codeRNO");
					userName = barcodernoInfo.get("userName");
					goodsKeyUserWeb = barcodernoInfo.get("userWeb");
				}
				// System.out.println("查询中奖码：" + codeRNO);
			} catch (Exception e) {
			}
		}
		String[] text = new String[] { goodsID, codePeriod, codeRNO == null ? "" : codeRNO,
				userName == null ? "" : userName, "", "", "", codeID };
		foreknowJInterface.setFrameListeningText(selectIndex, text);
//		System.out.println("正在查" + goodsID + ":" + codePeriod + ":" + codeRNO);
		getCodeNum();
	}

	/*
	 * 获取中奖位置
	 */
	private void getCodeNum() {
		try {
			String content = getUserBuyList(1);
			userBuyListCount = getUserBuyListCount(content);
			if (isFind) {
				if (CacheData.getGoodsPriceCacheDate(goodsID) > 8000) {
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							new GetUserBuyServerBigGoods(new JFrameListeningInterface() {
							}, "0").GetGoodsPeriodInfo(goodsID, codePeriod, codeID);
						}
					}, goodsKeyUserWeb == null ? 31000 : 10);
					isFind = false;
					foreknowJInterface.setFrameText("webError",
							goodsID + "___" + codePeriod + "___" + selectIndex + "___" + codeID);
					return;
				}
				for (int i = 11; i < userBuyListCount; i += 10) {
					getBarcodeInfo();
					if (userBuyListCount != CacheData.getUserBuyListCacheDate(codeID).size()) {
						getUserBuyList(i);
					}
				}
			}
		} catch (Exception e) {
			selectNum++;
//			System.out.println("错误了" + selectNum);
			if (selectNum < 10) {
				index = 0;
				if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
					getCodeNum();
				} else {
					foreknowJInterface.setFrameListeningText(selectIndex,
							CacheData.getSelectCacheDate(goodsID, codePeriod));
				}
			} else {
				startThread.remove(codeID + selectType);
				foreknowJInterface.setFrameText("webError",
						goodsID + "___" + codePeriod + "___" + selectIndex + "___" + codeID);
			}
		}

	}

	/*
	 * 获取参与记录
	 */
	private String getUserBuyList(int FIdx) {
		String content = null;
		if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
			if (isFind) {
				content = GetGoodsInfo.getUserBuyListEnd(codeID, FIdx, HttpClient);
//				System.out.println("查询到"+content);
				formatData(content, FIdx);
			}
		} else {
			foreknowJInterface.setFrameListeningText(selectIndex, CacheData.getSelectCacheDate(goodsID, codePeriod));
			isFind = false;
		}
		return content;
	}

	/*
	 * 输出获取到的信息
	 */
	private void formatData(String content, int FIdx) {
		String data = null;
		try {
			data = content.split("Rows\":", 2)[1];
			data = data.substring(0, data.length() - 5);
		} catch (Exception e) {
			SelectAssistPublishs.getYungouPublishs(goodsID, codePeriod, "1");
			try {
				Thread.sleep(1000);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			selectNum++;
			if (selectNum < 10) {
				getUserBuyList(FIdx);
			} else {
				isFind = false;
				startThread.remove(codeID + selectType);
				foreknowJInterface.setFrameText("webError",
						goodsID + "___" + codePeriod + "___" + selectIndex + "___" + codeID);
			}
			return;
		}
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		if (FIdx == 1 && codeRNO == null) {
			getCodeRNO(dList.get(0).get("buyTime"));
		}
		for (Map<String, String> maps : dList) {
			if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
				buyNum = maps.get("buyNum");
				if (isFind) {
					if (goodsKeyUserWeb == null) {
						userName = maps.get("userName");
						String buyID = maps.get("buyID");
						String[] userBuyListCacheDate = CacheData.getUserBuyListCacheDate(codeID, buyID);
						if (userBuyListCacheDate == null) {
							String buyCode = getUserBuyCode(buyID);
							CacheData.setUserBuyListCacheDate(codeID, buyID,
									new String[] { buyNum, userName, maps.get("userWeb"), buyCode });
						} else {
							sortBuyCodeByCache(userBuyListCacheDate[3]);
						}
					} else {
						if (goodsKeyUserWeb.equals(maps.get("userWeb"))) {
							getUserBuyCode(maps.get("buyID"));
						} else {
							index = index + Integer.parseInt(buyNum);
						}
					}
				}
			} else {
				isFind = false;
				foreknowJInterface.setFrameListeningText(selectIndex,
						CacheData.getSelectCacheDate(goodsID, codePeriod));
				return;
			}
		}

		if (codeRNO != null && userBuyListCount == CacheData.getUserBuyListCacheDate(codeID).size()) {
			// if (codeRNO != null && YungouDataTools.getCodeNum(codeID) ==
			// CacheData.getGoodsPriceCacheDate(goodsID)) {
			if (YungouDataTools.selectBarcodernoInfoByCache(goodsID, codePeriod, codeID, codeRNO, 0)) {
				if (CacheData.getSelectCacheDate(goodsID, codePeriod) != null) {
					isFind = false;
					foreknowJInterface.setFrameListeningText(selectIndex,
							CacheData.getSelectCacheDate(goodsID, codePeriod));
				}
			}
		}
	}

	/*
	 * 获取总页数
	 */
	private int getUserBuyListCount(String content) {
		String data = content.split("Count\":", 2)[1];
		data = data.split(",\"Data", 2)[0];
		return Integer.parseInt(data);
	}

	/*
	 * 获取用户云够码
	 */
	private String getUserBuyCode(String buyIP) {
		try {
			String content = GetGoodsInfo.getUserBuyCode(codeID, buyIP);
			return sortBuyCode(content);
		} catch (Exception e) {
			SelectAssistPublishs.getYungouPublishs(goodsID, codePeriod, "1");
			if (selectNum < 10) {
				selectNum++;
				return getUserBuyCode(buyIP);
			} else {
				isFind = false;
				startThread.remove(codeID + selectType);
				foreknowJInterface.setFrameText("webError",
						goodsID + "___" + codePeriod + "___" + selectIndex + "___" + codeID);
			}
		}
		return null;
	}

	/*
	 * 输出云够码
	 */
	private String sortBuyCode(String content) {
		String data = content.split(",'data':", 2)[1];
		data = data.substring(0, data.length() - 2);
		List<Map<String, String>> dList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		buyStartPosition = index;
		StringBuffer sBuffer = new StringBuffer();
		for (Map<String, String> maps : dList) {
			if (maps.get("rnoNum").equals(codeRNO)) {
				isFind = false;
				int price2 = CacheData.getGoodsPriceCacheDate(goodsID);
				// 中奖区间
				String buySection = (price2 - buyStartPosition - Integer.parseInt(buyNum) + 1) + "~"
						+ (price2 - buyStartPosition);
				String buyPosition = "" + (price2 - index);
				String[] text = new String[] { goodsID, codePeriod, codeRNO, userName, buyNum, buyPosition, buySection,
						codeID };
				if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
					foreknowJInterface.setFrameListeningText(selectIndex, text);
					CacheData.setSelectCacheDate(goodsID, codePeriod, text);
					SelectAssistPublishs.uploadYungouPublishs(text);
					CacheData.removeUserBuyListCacheDate(codeID);
					startThread.remove(codeID + selectType);
				}
//				 System.out.println("查到1了："+goodsID+":"+codePeriod+userName);
				HttpGetUtil.CloseHttpClient(HttpClient);
				break;
			}
			sBuffer.append(maps.get("rnoNum")).append(",");
			index++;
		}
		String ronNum = sBuffer.toString();
		return ronNum.substring(0, ronNum.length() - 1);
	}

	/*
	 * 输出云够码
	 */
	private void sortBuyCodeByCache(String content) {
		String[] buyCode = content.split(",");
		buyStartPosition = index;
		for (int i = 0; i < buyCode.length; i++) {
			if (buyCode[i].equals(codeRNO)) {
				isFind = false;
				int price2 = CacheData.getGoodsPriceCacheDate(goodsID);
				// 中奖区间
				String buySection = (price2 - buyStartPosition - buyCode.length + 1) + "~"
						+ (price2 - buyStartPosition);
				String buyPosition = "" + (price2 - index);
				String[] text = new String[] { goodsID, codePeriod, codeRNO, userName, "" + buyCode.length, buyPosition,
						buySection, codeID };
				if (CacheData.getSelectCacheDate(goodsID, codePeriod) == null) {
					foreknowJInterface.setFrameListeningText(selectIndex, text);
					CacheData.setSelectCacheDate(goodsID, codePeriod, text);
					SelectAssistPublishs.uploadYungouPublishs(text);
					CacheData.removeUserBuyListCacheDate(codeID);
					startThread.remove(codeID + selectType);
				}
//				System.out.println("查到ByCache了：" + goodsID + ":" + codePeriod + ":" + codeRNO + ":" + userName + ":"
//						+ buyPosition);
				HttpGetUtil.CloseHttpClient(HttpClient);
				break;
			}
			index++;
		}
	}

	/*
	 * 获取中奖码
	 */
	private void getCodeRNO(String buyTime) {
//		System.out.println("时间" + goodsID + ":" + codePeriod + ":" + buyTime);
		new Thread(new Runnable() {
			public void run() {
				// while (CacheData.getSelectCodeRNOCacheDate(codeID) == null) {
				while (codeRNO == null) {
					codeRNO = GetHistoryBuyRecord.getCodeRNO(buyTime, "" + CacheData.getGoodsPriceCacheDate(goodsID));
					if (codeRNO != null) {
						// CacheData.setSelectCodeRNOCacheDate(codeID, codeRNO);
						String[] text = new String[] { goodsID, codePeriod, codeRNO, "", "", "", "", codeID };
						foreknowJInterface.setFrameListeningText(selectIndex, text);
						Integer isAll = userBuyListCount == CacheData.getUserBuyListCacheDate(codeID).size() ? 0
								: index;
						if (YungouDataTools.selectBarcodernoInfoByCache(goodsID, codePeriod, codeID, codeRNO, isAll)) {
							if (CacheData.getSelectCacheDate(goodsID, codePeriod) != null) {
								isFind = false;
								foreknowJInterface.setFrameListeningText(selectIndex,
										CacheData.getSelectCacheDate(goodsID, codePeriod));
							}
						}
//						System.out.println("中奖码" + goodsID + ":" + codePeriod + ":" + codeRNO);
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	private void getBarcodeInfo() {// 获取中奖信息
		if (goodsKeyUserWeb == null) {
			boolean isCodeRNO = (codeRNO == null);
			try {
				List<Map<String, String>> list = GetGoodsInfo.getBarcodeRaffListByGoodsID(goodsID, "5");
				for (Map<String, String> map : list) {
					if (map.get("codeID").equals(codeID) && !map.get("codeRNO").equals("")) {
						this.codeRNO = map.get("codeRNO");
						this.userName = map.get("userName");
						this.goodsKeyUserWeb = map.get("userWeb");
						return;
					}
				}
				Map<String, String> barcodernoInfo = GetGoodsInfo.getBarcodernoInfo(codeID);
				if (barcodernoInfo != null) {
					codeRNO = barcodernoInfo.get("codeRNO");
					userName = barcodernoInfo.get("userName");
					goodsKeyUserWeb = barcodernoInfo.get("userWeb");
				}
//				 System.out.println("查询中奖码：" + codeRNO);
			} catch (Exception e) {
			}
			if (goodsKeyUserWeb != null) {
				String[] text = new String[] { goodsID, codePeriod, codeRNO, userName, "", "", "", codeID };
				foreknowJInterface.setFrameListeningText(selectIndex, text);
				if (isCodeRNO) {
					Integer isAll = userBuyListCount == CacheData.getUserBuyListCacheDate(codeID).size() ? 0 : index;
					if (YungouDataTools.selectBarcodernoInfoByCache(goodsID, codePeriod, codeID, codeRNO, isAll)) {
						if (CacheData.getSelectCacheDate(goodsID, codePeriod) != null) {
							isFind = false;
							foreknowJInterface.setFrameListeningText(selectIndex,
									CacheData.getSelectCacheDate(goodsID, codePeriod));
						}
					}
				}
			}
		}
	}
}
