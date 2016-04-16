package com.ytkj.ygAssist.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.TextAnchor;

import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;
import javax.swing.JTextPane;

/*
 * 走势图分析
 */
public class VipAnalyze extends JPanel {
	private static final long serialVersionUID = 1L;
	private int selectNum = 0;
	private long beginTime = 0;
	private String newestPeriod = null;// 最新期数
	private Thread thread;
	private String textFieldString = "22704";
	private JFrameListeningInterface foreknowInterface;
	private JTextField textField;
	private CategoryPlot plot;
	private JComboBox<String> comboBox;
	private JLabel lblid_1;
	private JButton button;
	private JLabel label_2;
	private JLabel label_1;
	private JTextPane textPane;
	private IntervalMarker inter = null;
	private JComboBox<String> comboBox_1;
	private JLabel label_3;
	private JLabel label_4;

	public VipAnalyze() {
		setLayout(null);
		JLabel lblid = new JLabel("商品ID：");
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		lblid.setForeground(Color.BLACK);
		lblid.setBounds(160, 6, 61, 30);
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
		textField.setBounds(216, 8, 98, 28);
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
				g.drawImage(new ImageIcon(VipAnalyze.class.getResource("/images/mainJFrame/xialaanniu.png")).getImage(),
						0, 2, c.getWidth() - 2, c.getHeight() - 4, c);
			}
		});
		textField.add(comboBox_3);

		JLabel label = new JLabel("查询最近多少期：");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label.setForeground(Color.BLACK);
		label.setBounds(360, 6, 112, 30);
		add(label);

		comboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] { "5", "10" }));
		comboBox.setFont(new Font("楷体", Font.PLAIN, 14));
		comboBox.setBackground(Color.RED);
		comboBox.setMaximumRowCount(10);
		comboBox.setBounds(469, 10, 48, 24);
		comboBox.setBorder(null);
		// comboBox.setFocusPainted(false);
		comboBox.setFocusable(false);
		add(comboBox);

		button = new JButton("预  测");
		button.setForeground(Color.WHITE);
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setFocusPainted(false);
		button.setBackground(Color.RED);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("预  测")) {
					if (Config.isAssistOverdue) {
						ShowMyMenu.ShowAssistOverdue();
						return;
					}
					if (textField.getText().trim().equals("")) {
						lblid_1.setText("请输入监控内容");
					} else {
						lblid_1.setText("正在查询，请稍等...");
						textField.setEnabled(false);
						comboBox.setEnabled(false);
						comboBox_1.setEnabled(false);
						inter.setStartValue(0);
						inter.setEndValue(0);
						textPane.setText("");
						button.setText("停止查询");
						thread = new Thread(new Runnable() {
							public void run() {
								try {
									beginTime = System.currentTimeMillis();
									selectNum = 0;
									YunGouServer.setTrendChart(textField.getText().trim(),
											"" + (comboBox.getSelectedIndex() * 5 + 6), foreknowInterface);

								} catch (Exception e) {
									lblid_1.setText("该商品已下架或网络异常");
									textField.setEnabled(true);
									comboBox.setEnabled(true);
									comboBox_1.setEnabled(true);
									button.setText("预  测");
								}
							}
						});
						thread.start();
					}
				} else {
					lblid_1.setText("");
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					comboBox_1.setEnabled(true);
					button.setText("预  测");
					thread.stop();
					thread = null;
				}
				VipAnalyze.this.repaint();
			}
		});
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBounds(561, 7, 87, 28);
		button.setBorder(null);
		add(button);

		lblid_1 = new JLabel("请输入商品ID");
		lblid_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblid_1.setForeground(Color.BLACK);
		lblid_1.setBounds(676, 12, 204, 18);
		add(lblid_1);

		ChartPanel dChartPanel = createJFreeChart();
		dChartPanel.setBounds(10, 77, 771, 420);
		add(dChartPanel);

		label_2 = new JLabel();
		label_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textField.getText().equals("")) {
					HttpGetUtil.openBrowseURL("http://www.1yyg.com/products/" + textField.getText() + ".html");
				}
			}
		});
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(Color.RED);
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_2.setBounds(50, 44, 404, 18);
		add(label_2);

		comboBox_1 = new JComboBox<String>(
				new DefaultComboBoxModel<String>(new String[] { "方案1", "方案2", "方案3", "方案4", "方案5" }));
		comboBox_1.setMaximumRowCount(10);
		comboBox_1.setFont(new Font("楷体", Font.PLAIN, 14));
		comboBox_1.setFocusable(false);
		comboBox_1.setBorder(null);
		comboBox_1.setBackground(Color.RED);
		comboBox_1.setBounds(564, 40, 68, 28);
		// add(comboBox_1);

		label_1 = new JLabel("选择预测方案：");
		label_1.setForeground(Color.BLACK);
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_1.setBounds(469, 44, 104, 18);
		// add(label_1);
		double ran = Math.random();
		if (ran < 0.2) {
			comboBox_1.setSelectedIndex(0);
		} else if (ran < 0.4) {
			comboBox_1.setSelectedIndex(1);
		} else if (ran < 0.6) {
			comboBox_1.setSelectedIndex(2);
		} else if (ran < 0.8) {
			comboBox_1.setSelectedIndex(3);
		} else {
			comboBox_1.setSelectedIndex(4);
		}

		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(new Font("微软雅黑", Font.BOLD, 16));
		textPane.setBounds(793, 77, 87, 420);
		add(textPane);

		label_3 = new JLabel(Config.heziQQqun);
		label_3.setForeground(Color.RED);
		label_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_3.setBounds(520, 44, 182, 18);
		add(label_3);

		if (!Config.hezuoQQqun.equals(" ")) {
			label_4 = new JLabel(Config.hezuoQQqun);
			label_4.setForeground(Color.RED);
			label_4.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(Config.hezuoQQqunUrl);
				}
			});
			label_4.setFont(new Font("微软雅黑", Font.BOLD, 14));
			label_4.setBounds(710, 44, 187, 18);
			add(label_4);
		}

		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (FrameName.equals("1")) {
					setLinedataset(text[1], text[3]);
					newestPeriod = text[1];
				} else if (FrameName.equals("2")) {
					if (text[0].equals(textField.getText().trim()) && button.getText().equals("停止查询")
							&& !text[5].equals("")) {
						setDataset(text[1], text[5]);
						selectNum++;
						int queryNum = comboBox.getSelectedIndex() * 5 + 5;
						if (selectNum >= queryNum) {
							lblid_1.setText("查询用时:" + (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
							textField.setEnabled(true);
							comboBox.setEnabled(true);
							comboBox_1.setEnabled(true);
							button.setText("预  测");
							forecastPosition();
						}
					}
				}
			}

			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("setGoodsName")) {
					label_2.setText(text);
				} else if (FrameName.equals("setGoodsNameError")) {
					label_2.setText("无法找到该商品或网络异常");
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					comboBox_1.setEnabled(true);
					button.setText("预  测");
				} else if (FrameName.equals("goodsoldOut")) {
					label_2.setText("该商品已经下降了");
					textField.setEnabled(true);
					comboBox.setEnabled(true);
					comboBox_1.setEnabled(true);
					button.setText("预  测");
				} else if (FrameName.equals("selectError") || FrameName.equals("webError")) {
					selectNum++;
					int queryNum = comboBox.getSelectedIndex() * 5 + 5;
					if (selectNum >= queryNum) {
						lblid_1.setText("查询用时:" + (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
						textField.setEnabled(true);
						comboBox.setEnabled(true);
						comboBox_1.setEnabled(true);
						button.setText("预  测");
					}
				}
			}
		};
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
	public ChartPanel createJFreeChart() {
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
				new ImageIcon(VipAnalyze.class.getResource("/images/mainJFrame/watermark.png")).getImage());
		// plot.setBackgroundImage(new ImageIcon("jre\\logo.png").getImage());
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

		plot.setRenderer(renderer);

		// 添加色域
		inter = new IntervalMarker(0, 0);
		inter.setLabelOffsetType(LengthAdjustmentType.EXPAND);
		inter.setPaint(Color.decode("#66FFCC"));// 域顏色
		plot.addRangeMarker(inter, Layer.BACKGROUND);

		ChartPanel dChartPanel = new ChartPanel(chart);
		dChartPanel.setPopupMenu(null);// 不显示弹出菜单
		dChartPanel.setMouseZoomable(false);// 禁止缩放

		return dChartPanel;
	}

	// 预测位置
	private void forecastPosition() {
		int max = CacheData.getGoodsPriceCacheDate(textField.getText().trim());
		DefaultCategoryDataset linedataset = (DefaultCategoryDataset) plot.getDataset();
		int newestPeriodInt = Integer.parseInt(newestPeriod);
		int value[] = new int[5];
		for (int i = 1; i < 6; i++) {
			value[i - 1] = linedataset.getValue("", "" + (newestPeriodInt - i)).intValue();
		}
		int lowpress1 = value[comboBox_1.getSelectedIndex()];
		int uperpress = 0;
		int downerpress = 0;
		uperpress = max / 6 + Math.abs(value[1] + value[2] - max) % (max / 6);
		downerpress = max / 6 + Math.abs(value[3] + value[4] - max) % (max / 6);
		// if ((uperpress + downerpress) > max / 2) {
		// uperpress = uperpress - max / 4;
		// downerpress = downerpress - max / 4;
		// }
		// if (comboBox_1.getSelectedIndex() == 0) {
		// } else if (comboBox_1.getSelectedIndex() == 1) {
		// } else if (comboBox_1.getSelectedIndex() == 2) {
		// } else if (comboBox_1.getSelectedIndex() == 3) {
		// } else if (comboBox_1.getSelectedIndex() == 4) {
		// }
		int StartValue = lowpress1 - uperpress;
		int EndValue = lowpress1 + downerpress;
		StartValue = StartValue > 0 ? StartValue : 8;
		EndValue = EndValue < max ? EndValue : max - 8;
		inter.setStartValue(StartValue);
		inter.setEndValue(EndValue);
		textPane.setText("第" + newestPeriod + "期预测范围为：" + StartValue + "-" + EndValue + " 预测仅供参考！建议您，根据当前趋势分析后再下注！");
	}

	public void setTextFieldContent(String goodsID) {
		textField.setText(goodsID);
	}
}
