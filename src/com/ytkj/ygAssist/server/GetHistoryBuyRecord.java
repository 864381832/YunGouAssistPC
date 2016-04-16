package com.ytkj.ygAssist.server;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytkj.ygAssist.server.util.DateUtil;
import com.ytkj.ygAssist.server.util.HttpGetUtil;

public class GetHistoryBuyRecord {

	private int index = 0;// 计数
	private BigInteger sumTotal = new BigInteger("0");// 时间总和
	private int selectNum = 0;// 查询次数

	public static String getCodeRNO(String buyTime, String price) {
		return new GetHistoryBuyRecord().GetBuyGoodsCode(buyTime, price);
	}

	/*
	 * 获取幸运云购码
	 */
	public String GetBuyGoodsCode(String buyTime, String price) {
		// System.out.println("正在查中奖码："+buyTime);
		String[] buyTimeDate = DateUtil.getBuyTime(buyTime);
		int FIdx = 1;
		while (index < 100 && selectNum < 15) {
			// System.out.println("正在查中奖码2："+buyTime);
			selectNum++;
			try {
				String buyRecord = GetBuyRecord(FIdx, buyTimeDate[0], buyTimeDate[1]);
				// System.out.println("获取历史记录"+buyRecord);
				formatData(buyRecord, buyTime);
				FIdx += 20;
				// System.out.println("正在查中奖码4："+buyTime);
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (Exception e2) {
				}
				FIdx = 1;
				index = 0;
				sumTotal = new BigInteger("0");
			}
		}
		if (index >= 100) {
			String string = sumTotal.remainder(new BigInteger(price)).add(new BigInteger("10000001")).toString();
			// System.out.println("计算中奖码：" + string);
			return string;
		}
		return null;
	}

	public String GetBuyRecord(int FIdx, String BTime, String ETime) {
		StringBuffer getUrl = new StringBuffer();
		getUrl.append("http://api.1yyg.com/JPData?action=getHistoryBuyRecord&FIdx=").append(FIdx);
		getUrl.append("&EIdx=").append(FIdx + 19);
		getUrl.append("&BTime=").append(BTime);
		getUrl.append("&ETime=").append(ETime);
		getUrl.append("&isCount=0");
		return HttpGetUtil.getHttpData(getUrl.toString(), "http://www.1yyg.com/HistoryBuyRecords.html");
	}

	/*
	 * 输出获取到的信息
	 */
	public void formatData(String content, String buyTime) {
		String data = content.substring(content.indexOf("'data':") + 7, content.length() - 2);
		List<Map<String, String>> dataList = new Gson().fromJson(data, new TypeToken<List<Map<String, String>>>() {
		}.getType());
		for (Map<String, String> maps : dataList) {
			if (index > 0 || maps.get("buyTime").equals(buyTime)) {
				if (index >= 100) {
					break;
				}
				sumTotal = sumTotal.add(new BigInteger(DateUtil.getDateFormatStringToInt(maps.get("buyTime"))));
				++index;
			}
		}
	}

}
