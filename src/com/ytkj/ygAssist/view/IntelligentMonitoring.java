package com.ytkj.ygAssist.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.main.YunGouLoginServer;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/*
 * 智能监听
 */
public class IntelligentMonitoring extends JPanel {
	private static final long serialVersionUID = 1L;
	private String textFieldString = "22704";
	private String newestCodeID = null;// 最新期数网址id
	private boolean isAutoBuy = false;// 是否开启挂机自动下注
	private boolean isBuy = true;// 是否已经购买
	private int autoBuyNum = 7;// 挂机购买次数
	private JFrameListeningInterface foreknowInterface;
	private JTextField textField;
	private CategoryPlot plot;
	private JComboBox<String> comboBox;
	private JProgressBar progressBar;
	private JLabel label_2;
	private JLabel label_4;
	private JLabel label_6;
	private JLabel label_1;
	private JLabel label_8;
	private JLabel label_9;
	private JPanel panel;
	private JPanel panel_1;
	private JButton btnNewButton;
	private JLabel label_10;
	private JSpinner spinner;
	private JTextField textField_1;
	private JTextField textField_2;
	private JSpinner spinner_1;
	private JComboBox<String> comboBox_1;
	private JButton button_1;
	private JButton button;
	private JLabel label_16;
	private JProgressBar progressBar_1;
	private JLabel label_19;
	private JLabel label_20;

	public IntelligentMonitoring() {
		setLayout(null);
		JLabel lblid = new JLabel("商品ID：");
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		lblid.setForeground(Color.BLACK);
		lblid.setBounds(10, 12, 61, 30);
		lblid.setToolTipText("商品ID在1元云购网正在进行的商品页网址中获得,如http://www.1yyg.com/products/22504.html,其中在商品ID为22504");
		add(lblid);

		textField = new JTextField(textFieldString);
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}
		});
		textField.setToolTipText("商品ID在1元云购网正在进行的商品页网址中获得,如http://www.1yyg.com/products/22504.html,其中在商品ID为22504");
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(66, 14, 98, 28);
		textField.setColumns(10);
		add(textField);
		JButton comboBox_3 = new JButton();
		comboBox_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textField.isEnabled()) {
					ShowMyMenu.ShowGoodsIDMenu(textField);
				}
			}
		});
		comboBox_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		comboBox_3.setBorderPainted(false);
		comboBox_3.setBounds(textField.getWidth() - 18, 0, 18, 28);
		comboBox_3.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(IntelligentMonitoring.class.getResource("/images/mainJFrame/xialaanniu.png"))
						.getImage(), 0, 2, c.getWidth() - 2, c.getHeight() - 4, c);
			}
		});
		textField.add(comboBox_3);

		JLabel label = new JLabel("监听最近多少期：");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label.setForeground(Color.BLACK);
		label.setBounds(176, 12, 112, 30);
		add(label);

		comboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(
				new String[] { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50" }));
		comboBox.setFont(new Font("楷体", Font.PLAIN, 14));
		comboBox.setBackground(Color.RED);
		comboBox.setMaximumRowCount(10);
		comboBox.setSelectedIndex(1);
		comboBox.setBounds(285, 16, 48, 24);
		comboBox.setBorder(null);
		// comboBox.setFocusPainted(false);
		comboBox.setFocusable(false);
		add(comboBox);

		button = new JButton("开始监控");
		button.setForeground(Color.WHITE);
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setFocusPainted(false);
		button.setBackground(Color.RED);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("开始监控")) {
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
						button.setText("停止监控");
						new Thread(new Runnable() {
							public void run() {
								try {
									YunGouServer.setIntelligentMonitoring(true, textField.getText().trim(),
											"" + (comboBox.getSelectedIndex() * 5 + 6), foreknowInterface);
								} catch (Exception e) {
									label_8.setText("该商品已下架或网络异常");
									textField.setEnabled(true);
									comboBox.setEnabled(true);
									button.setText("开始监控");
								}
							}
						}).start();
					}
				} else {
					button.setText("正在停止");
					button.setEnabled(false);
					YunGouServer.setIntelligentMonitoring(false, null, null, foreknowInterface);
				}
				IntelligentMonitoring.this.repaint();
			}
		});
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBounds(356, 14, 87, 28);
		button.setBorder(null);
		add(button);

		label_1 = new JLabel("请输入监控内容");
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_1.setForeground(Color.BLACK);
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(10, 68, 119, 18);
		add(label_1);

		label_2 = new JLabel("0");
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(Color.RED);
		label_2.setBounds(816, 103, 77, 18);
		add(label_2);

		JLabel label_3 = new JLabel("已参与");
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_3.setForeground(Color.BLACK);
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(816, 130, 77, 18);
		add(label_3);

		label_4 = new JLabel("0");
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setForeground(Color.RED);
		label_4.setBounds(816, 418, 77, 18);
		add(label_4);

		JLabel label_5 = new JLabel("总人次");
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_5.setForeground(Color.BLACK);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(816, 445, 77, 18);
		add(label_5);

		label_6 = new JLabel("0");
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setForeground(Color.RED);
		label_6.setBounds(815, 259, 78, 18);
		add(label_6);

		JLabel label_7 = new JLabel("剩余人次");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_7.setForeground(Color.BLACK);
		label_7.setBounds(815, 289, 78, 18);
		add(label_7);

		label_8 = new JLabel("最近一期中奖情况");
		label_8.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_8.setForeground(Color.RED);
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setBounds(10, 44, 433, 18);
		add(label_8);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setFont(new Font("微软雅黑", Font.BOLD, 14));
		progressBar.setBounds(787, 96, 32, 370);
		add(progressBar);
		progressBar.setOrientation(SwingConstants.VERTICAL);
		progressBar.setForeground(Color.RED);
		progressBar.setMaximum(5188);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBorder(null);
		tabbedPane.setForeground(Color.WHITE);
		tabbedPane.setBounds(455, 10, 439, 81);
		add(tabbedPane);

		panel = new JPanel();
		panel.setForeground(Color.WHITE);
		tabbedPane.addTab("手动购买", null, panel, null);
		panel.setLayout(null);

		btnNewButton = new JButton("购买");
		btnNewButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.RED);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (button.getText().equals("开始监控")) {
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
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNewButton.setBounds(160, 45, 90, 30);
		btnNewButton.setBorderPainted(false);
		panel.add(btnNewButton);

		label_10 = new JLabel("购买数量:");
		label_10.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_10.setBounds(10, 51, 79, 15);
		panel.add(label_10);

		spinner = new JSpinner();
		spinner.setBorder(null);
		spinner.setFont(new Font("微软雅黑", Font.BOLD, 14));
		spinner.setBackground(Color.RED);
		spinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner.setBounds(86, 44, 70, 30);
		panel.add(spinner);

		JLabel lblNewLabel = new JLabel("购买前确定有足够的余额，并清空购物车");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 24, 352, 15);
		panel.add(lblNewLabel);

		JLabel label_18 = new JLabel(Config.heziQQqun);
		label_18.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_18.setBounds(10, -4, 176, 22);
		label_18.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_18.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		panel.add(label_18);

		if (!Config.hezuoQQqun.equals(" ")) {
			label_19 = new JLabel(Config.hezuoQQqun);
			label_19.setFont(new Font("微软雅黑", Font.BOLD, 14));
			label_19.setBounds(199, -4, 176, 22);
			label_19.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_19.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(Config.hezuoQQqunUrl);
				}
			});
			panel.add(label_19);
		}

		label_20 = new JLabel("余额:900000.00");
		label_20.setHorizontalAlignment(SwingConstants.CENTER);
		label_20.setForeground(Color.RED);
		label_20.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_20.setBounds(250, 53, 124, 15);
		panel.add(label_20);
		label_20.setVisible(false);

		panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		tabbedPane.addTab("自动下注", null, panel_1, null);
		panel_1.setLayout(null);

		JLabel label_11 = new JLabel("购买次数");
		label_11.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_11.setBounds(10, 26, 72, 15);
		panel_1.add(label_11);

		spinner_1 = new JSpinner();
		spinner_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		spinner_1.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner_1.setBounds(77, 21, 50, 26);
		panel_1.add(spinner_1);

		JLabel label_12 = new JLabel("挂机期数");
		label_12.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_12.setBounds(139, 26, 62, 15);
		panel_1.add(label_12);

		comboBox_1 = new JComboBox<String>(
				new DefaultComboBoxModel<String>(new String[] { "0", "1", "2", "3", "4", "5", "6" }));
		comboBox_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		comboBox_1.setBackground(Color.RED);
		comboBox_1.setSelectedIndex(1);
		comboBox_1.setBounds(213, 21, 50, 26);
		panel_1.add(comboBox_1);

		JLabel label_13 = new JLabel("从");
		label_13.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_13.setBounds(10, 53, 22, 15);
		panel_1.add(label_13);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField_1.setBounds(32, 49, 60, 26);
		panel_1.add(textField_1);
		textField_1.setColumns(10);

		JLabel label_14 = new JLabel("开始购买,超过");
		label_14.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_14.setBounds(100, 53, 97, 15);
		panel_1.add(label_14);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField_2.setBounds(203, 49, 60, 26);
		panel_1.add(textField_2);
		textField_2.setColumns(10);

		JLabel label_15 = new JLabel("则放弃购买");
		label_15.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_15.setBounds(268, 53, 78, 15);
		panel_1.add(label_15);

		button_1 = new JButton("挂机");
		button_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_1.setForeground(Color.WHITE);
		button_1.setBackground(Color.RED);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (button_1.getText().equals("挂机")) {
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
							button_1.setText("停止");
						}
					}
				} else {
					isAutoBuy = false;
					autoBuyNum = 7;
					spinner_1.setEnabled(true);
					comboBox_1.setEnabled(true);
					textField_1.setEnabled(true);
					textField_2.setEnabled(true);
					button_1.setText("挂机");
				}
			}
		});
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setBounds(276, 21, 70, 28);
		panel_1.add(button_1);

		JLabel label_17 = new JLabel("软件不一定能抢到位置，请谨慎使用挂机功能");
		label_17.setForeground(Color.RED);
		label_17.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_17.setBounds(10, 2, 336, 15);
		panel_1.add(label_17);

		ChartPanel dChartPanel = createJFreeChart();
		dChartPanel.setBounds(10, 90, 778, 430);
		add(dChartPanel);

		label_9 = new JLabel();
		label_9.setForeground(Color.BLACK);
		label_9.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textField.getText().equals("")) {
					HttpGetUtil.openBrowseURL("http://www.1yyg.com/products/" + textField.getText() + ".html");
				}
			}
		});
		label_9.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_9.setBounds(130, 68, 385, 18);
		add(label_9);
		label_9.setHorizontalAlignment(SwingConstants.LEFT);

		label_16 = new JLabel("当前期数");
		label_16.setBounds(781, 490, 120, 18);
		add(label_16);
		label_16.setHorizontalAlignment(SwingConstants.CENTER);
		label_16.setForeground(Color.RED);
		label_16.setFont(new Font("微软雅黑", Font.BOLD, 14));

		progressBar_1 = new JProgressBar();
		progressBar_1.setBounds(797, 474, 87, 12);
		progressBar_1.setMaximum(100);
		add(progressBar_1);
		progressBar_1.setVisible(false);

		setListening();
	}

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
					listeningAutoBuy(text[2]);
					int val = progressBar_1.getValue() == 100 ? 0 : progressBar_1.getValue() + 50;
					progressBar_1.setValue(val);
				} else if (FrameName.equals("3")) {
					if (text[0].equals(textField.getText().trim()) && button.getText().equals("停止监控")) {
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
					button.setText("开始监控");
				} else if (FrameName.equals("goodsoldOut")) {
					label_8.setText("该商品已经下降了");
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					button.setText("开始监控");
				} else if (FrameName.equals("webError")) {
					if (button.getText().equals("停止监控")) {
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
					if (button_1.getText().equals("停止")) {
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
					button.setEnabled(true);
					progressBar_1.setVisible(false);
					button.setText("开始监控");
					if (isAutoBuy) {
						isAutoBuy = false;
						autoBuyNum = 7;
						spinner_1.setEnabled(true);
						comboBox_1.setEnabled(true);
						textField_1.setEnabled(true);
						textField_2.setEnabled(true);
						button_1.setText("挂机");
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
						button_1.setText("挂机");
					}
				}
			}
		}
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

	// 创建表格
	private ChartPanel createJFreeChart() {
		JFreeChart chart = ChartFactory.createLineChart(null, // 报表题目，字符串类型
				null, // 横轴
				null, // 纵轴
				null, // 获得数据集
				PlotOrientation.VERTICAL, // 图标方向垂直
				false, // 显示图例
				false, // 不用生成工具
				false // 不用生成URL地址
		);
		chart.setBackgroundPaint(new Color(214, 217, 223));
		chart.setTextAntiAlias(true);
		// 生成图形
		plot = chart.getCategoryPlot();
		plot.setBackgroundImage(
				new ImageIcon(IntelligentMonitoring.class.getResource("/images/mainJFrame/watermark.png")).getImage());
		// 图像属性部分
		plot.setBackgroundPaint(new Color(214, 217, 223));
		plot.setDomainGridlinesVisible(false); // 设置背景网格线是否可见
		plot.setRangeGridlinesVisible(true);
		// plot.setDomainGridlinePaint(Color.GRAY); // 设置背景网格线颜色
		plot.setRangeGridlinePaint(Color.GRAY);
		plot.setNoDataMessage("没有数据,请点击开始监控");// 没有数据时显示的文字说明。
		plot.setNoDataMessageFont(new Font("微软雅黑", Font.PLAIN, 30));// 字体的大小
		plot.setNoDataMessagePaint(Color.RED);// 字体颜色
		// 设置横轴标题字体
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("微软雅黑", Font.PLAIN, 14));
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		domainAxis.setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 14));
		domainAxis.setTickLabelPaint(Color.black);
		domainAxis.setLowerMargin(0.02);// 数据轴下（左）边距
		domainAxis.setUpperMargin(0.02);// 数据轴上（右）边距
		// categoryAxis.setCategoryMargin(15.0);//？格与格之间地间距
		// 设置纵轴标题字体
		ValueAxis rangeAxis = plot.getRangeAxis();
		// rangeAxis.setUpperMargin(0.1);
		// rangeAxis.setLowerMargin(0.5);
		rangeAxis.setLabelFont(new Font("微软雅黑", Font.PLAIN, 14));
		rangeAxis.setLowerBound(0);// 数据轴上的显示最小值
		rangeAxis.setUpperBound(100);// 数据轴上的显示最大值
		rangeAxis.setTickLabelPaint(Color.black);
		rangeAxis.setAutoRange(false);// 不自动分配Y轴数据
		// valueAxis.setAxisLineStroke(new BasicStroke(2.0f)); //
		// 坐标轴粗细

		// plot.setAxisOffset(new RectangleInsets(0d, 0d, 0d, 0d));
		plot.setOutlineStroke(null); // 边框粗细

		// 数据渲染部分 主要是对折线做操作
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseItemLabelsVisible(true);
		renderer.setSeriesPaint(0, Color.red); // 设置折线的颜色
		renderer.setBaseShapesFilled(true);
		renderer.setBaseShapesVisible(true); // series 点（即数据点）可见
		// renderer.setBaseLinesVisible(true);//series 点（即数据点）间有连线可见
		renderer.setBasePositiveItemLabelPosition(
				new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.BOTTOM_CENTER), true);

		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelFont(new Font("微软雅黑", Font.BOLD, 12)); // 设置提示折点数据形状
		renderer.setBaseItemLabelPaint(Color.black);
		renderer.setSeriesStroke(0, new BasicStroke(1.2f));

		renderer.addChangeListener(new RendererChangeListener() {
			@Override
			public void rendererChanged(RendererChangeEvent arg0) {
				System.out.println("点击");
			}
		});

		plot.setRenderer(renderer);

		ChartPanel dChartPanel = new ChartPanel(chart);
		dChartPanel.setPopupMenu(null);// 不显示弹出菜单
		dChartPanel.setMouseZoomable(false);// 禁止缩放

		return dChartPanel;
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
