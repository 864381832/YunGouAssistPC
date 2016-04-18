package com.ytkj.ygAssist.viewControl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import org.jfree.data.category.DefaultCategoryDataset;
import com.ytkj.ygAssist.main.YunGouLoginServer;
import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.IntelligentMonitoring;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

public class IntelligentMonitoringControl extends IntelligentMonitoring {
	private static final long serialVersionUID = 1L;

	private int progressBarValue = 0;

	public IntelligentMonitoringControl() {
		super();
		addButtonMouseListener();
		setListening();
	}

	/*
	 * 添加按钮监听事件
	 */
	private void addButtonMouseListener() {
		startMonitorButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (startMonitorButton.getText().equals("开始监控")) {
					if (Config.isAssistOverdue) {
						ShowMyMenu.ShowAssistOverdue();
						return;
					}
					if (textField.getText().equals("")) {
						label_1.setText("请输入监控内容");
					} else {
						label_9.setText("");
						textField.setEnabled(false);
						comboBox.setEnabled(false);
						startMonitorButton.setText("停止监控");
						new Thread(new Runnable() {
							public void run() {
								try {
									YunGouServer.setIntelligentMonitoring(true, textField.getText().trim(),
											"" + (comboBox.getSelectedIndex() * 5 + 6), foreknowInterface);
								} catch (Exception e) {
									label_8.setText("该商品已下架或网络异常");
									textField.setEnabled(true);
									comboBox.setEnabled(true);
									startMonitorButton.setText("开始监控");
								}
							}
						}).start();
					}
				} else {
					startMonitorButton.setText("正在停止");
					startMonitorButton.setEnabled(false);
					YunGouServer.setIntelligentMonitoring(false, null, null, foreknowInterface);
				}
				IntelligentMonitoringControl.this.repaint();
			}
		});

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (startMonitorButton.getText().equals("开始监控")) {
					HintDialog.startExpiringHint("请先开启监控");
				} else {
					if (!YunGouLoginServer.getUserServer().getIsLogin()) {
						HintDialog.startExpiringHint(HintDialog.yungouLogin, "您还未登录1元云购账户，请先登录后再使用购买功能，谢谢！！！");
					} else {
						btnNewButton.setEnabled(false);
						if (isBuy) {
							isBuy = false;
							YunGouServer.buyGoods(newestCodeID, spinner.getValue().toString(), foreknowInterface);
						}
					}
				}
			}
		});

		guajiButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (guajiButton.getText().equals("挂机")) {
					if (textField_1.getText().trim().equals("") || textField_2.getText().trim().equals("")) {
						HintDialog.startExpiringHint("请填写挂机范围");
					} else {
						if (!YunGouLoginServer.getUserServer().getIsLogin()) {
							HintDialog.startExpiringHint(HintDialog.yungouLogin, "您还未登录1元云购账户，请先登录后再使用购买功能，谢谢！！！");
						} else {
							isAutoBuy = true;
							spinner_1.setEnabled(false);
							comboBox_1.setEnabled(false);
							textField_1.setEnabled(false);
							textField_2.setEnabled(false);
							autoBuyNum = 0;
							guajiButton.setText("停止");
						}
					}
				} else {
					isAutoBuy = false;
					autoBuyNum = 7;
					spinner_1.setEnabled(true);
					comboBox_1.setEnabled(true);
					textField_1.setEnabled(true);
					textField_2.setEnabled(true);
					guajiButton.setText("挂机");
				}
			}
		});
	}

	/*
	 * 设置查询回调事件
	 */
	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (FrameName.equals("1")) {
					progressBar.setMaximum(Integer.parseInt(text[3]));
					progressBar.setValue(Integer.parseInt(text[2]));
					progressBar_1.setVisible(true);
					label_1.setText("当前第" + text[1] + "云");
					label_16.setText("当前第" + text[1] + "云");
					label_2.setText(text[2]);
					label_4.setText(text[3]);
					label_6.setText(text[4]);
					newestCodeID = text[0];
					setLinedataset(text[1], text[3]);
				} else if (FrameName.equals("2")) {
					progressBar.setValue(Integer.parseInt(text[2]));
					label_2.setText(text[2]);
					label_6.setText(text[4]);
					progressBarValue = progressBarValue == 100 ? 0 : progressBarValue + 50;
					progressBar_1.setValue(progressBarValue);
					listeningAutoBuy(text[2]);
				} else if (FrameName.equals("3")) {
					if (text[0].equals(textField.getText().trim()) && startMonitorButton.getText().equals("停止监控")) {
						if (!text[5].equals("")) {
							setDataset(text[1], text[5]);
							try {
								String period = label_1.getText();
								if (Integer.parseInt(text[1]) + 1 == Integer
										.parseInt(period.substring(3, period.indexOf("云")))) {
									label_8.setText("第" + text[1] + "云中奖人:" + text[3] + " 买了" + text[4] + "次");
								}
							} catch (Exception e) {
							}
						} else {
							setDataset(text[1], "0");
						}
					}
				}
			}

			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("setGoodsName")) {
					label_9.setText(text);
				} else if (FrameName.equals("setGoodsNameError")) {
					label_8.setText("无法找到该商品或网络异常");
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					startMonitorButton.setText("开始监控");
				} else if (FrameName.equals("goodsoldOut")) {
					label_8.setText("该商品已经下降了");
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					startMonitorButton.setText("开始监控");
				} else if (FrameName.equals("webError")) {
					if (startMonitorButton.getText().equals("停止监控")) {
						if (CacheData.getSelectCacheDate(text.split("___")[0], text.split("___")[1]) == null) {
							new Thread(new Runnable() {
								public void run() {
									SelectAssistPublishs.getYungouPublishs(text.split("___")[0], text.split("___")[1],
											"1");
									new GetUserBuyServer(foreknowInterface, "3").GetGoodsPeriodInfo(
											text.split("___")[0], text.split("___")[1], text.split("___")[3], true);
								}
							}).start();
						} else {
							foreknowInterface.setFrameListeningText("3",
									CacheData.getSelectCacheDate(text.split("___")[0], text.split("___")[1]));
						}
						// System.out.println("搜索超时，请检查网络");
					}
				} else if (FrameName.equals("newestCodeID")) {
					newestCodeID = text;
					if (guajiButton.getText().equals("停止")) {
						isAutoBuy = true;
					}
				} else if (FrameName.equals("newestPeriod")) {
					label_1.setText("当前第" + text + "云");
					label_16.setText("当前第" + text + "云");
				} else if (FrameName.equals("stopRemind")) {
					label_1.setText("请输入监控内容");
					label_8.setText("最近一期中奖情况");
					label_9.setText("");
					label_16.setText("");
					plot.setDataset(null);// 定义图标对象
					progressBar.setValue(0);
					label_2.setText("0");
					label_4.setText("0");
					label_6.setText("0");
					newestCodeID = "0";
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					startMonitorButton.setEnabled(true);
					progressBar_1.setVisible(false);
					startMonitorButton.setText("开始监控");
					if (isAutoBuy) {
						isAutoBuy = false;
						autoBuyNum = 7;
						spinner_1.setEnabled(true);
						comboBox_1.setEnabled(true);
						textField_1.setEnabled(true);
						textField_2.setEnabled(true);
						guajiButton.setText("挂机");
					}
				} else if (FrameName.equals("buySucceed")) {
					HintDialog.startExpiringHint(HintDialog.buySucceed, text);
					btnNewButton.setEnabled(true);
					isBuy = true;
					refreshBalance();// 刷新余额
				} else if (FrameName.equals("buySucceedCode")) {
					HintDialog.startExpiringHint(text);
					refreshBalance();// 刷新余额
				} else if (FrameName.equals("buyError")) {
					HintDialog.startExpiringHint("未能购买成功,请检查账户余额，或网络问题");
					btnNewButton.setEnabled(true);
					isBuy = true;
					refreshBalance();// 刷新余额
				}
			}
		};
	}

	// 监听自动下注
	private void listeningAutoBuy(String startBuyNum) {
		if (isAutoBuy) {
			int startBuyNumInt = Integer.parseInt(startBuyNum);
			if (Integer.parseInt(textField_1.getText()) < startBuyNumInt
					&& Integer.parseInt(textField_2.getText()) > startBuyNumInt) {
				// System.out.println("购买了" + label_2.getText());
				if (isAutoBuy) {
					isAutoBuy = false;
					YunGouServer.buyGoods(newestCodeID, spinner_1.getValue().toString(), foreknowInterface);
					autoBuyNum++;
					comboBox_1.setSelectedIndex(comboBox_1.getSelectedIndex() - 1);
					if (comboBox_1.getSelectedIndex() == 0) {
						spinner_1.setEnabled(true);
						comboBox_1.setEnabled(true);
						textField_1.setEnabled(true);
						textField_2.setEnabled(true);
						comboBox_1.setSelectedIndex(1);
						guajiButton.setText("挂机");
					}
				}
			}
		}
	}

	// 初始化数据
	private void setLinedataset(String codePeriod, String upperBound) {
		DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
		int queryNum = comboBox.getSelectedIndex() * 5 + 5;
		int codePeriodInt = Integer.parseInt(codePeriod) - queryNum;
		for (int i = 0; i < queryNum; i++) {
			linedataset.addValue(0, // 值
					"", // 哪条数据线
					"" + (codePeriodInt + i)); // 对应的横轴
		}
		plot.setDataset(linedataset);// 定义图标对象
		int maxInt = Integer.parseInt(upperBound);
		plot.getRangeAxis().setUpperBound(maxInt + maxInt / 20);// 数据轴上的显示最大值
	}

	// 设置数据
	private void setDataset(String codePeriod, String buyPosition) {
		DefaultCategoryDataset linedataset = (DefaultCategoryDataset) plot.getDataset();
		if (linedataset != null) {
			try {
				if (linedataset.getValue("", codePeriod).intValue() == 0) {
					linedataset.setValue(Integer.parseInt(buyPosition), "", codePeriod);
				}
			} catch (Exception e) {
				linedataset.removeColumn(0);
				linedataset.addValue(Integer.parseInt(buyPosition), // 值
						"", // 哪条数据线
						codePeriod); // 对应的横轴
			}
		}
		plot.setDataset(linedataset);// 定义图标对象
	}

	public void setTextFieldContent(String goodsID) {
		textField.setText(goodsID);
	}

	public ArrayList<String> getNotData() {
		DefaultCategoryDataset linedataset = (DefaultCategoryDataset) plot.getDataset();
		ArrayList<String> values = new ArrayList<String>();
		if (linedataset != null) {
			try {
				for (Object key : linedataset.getColumnKeys()) {
					if (linedataset.getValue("", (String) key).intValue() == 0) {
						values.add((String) key);
					}
				}
			} catch (Exception e) {
			}
		}
		return values;
	}

	/*
	 * 刷新余额
	 */
	public void refreshBalance() {
		if (YunGouLoginServer.getUserServer().getIsLogin()) {
			new Thread(new Runnable() {
				public void run() {
					try {
						String balance = YunGouLoginServer.getUserServer().getUserBalance();
						label_20.setText("余额:" + balance);
						label_20.setVisible(true);
					} catch (Exception e2) {
					}
				}
			}).start();
		} else {
			label_20.setVisible(false);
		}
	}
}
