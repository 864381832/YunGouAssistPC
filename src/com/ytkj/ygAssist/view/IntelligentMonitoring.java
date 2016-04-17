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
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.ui.TextAnchor;

import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
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
	public String textFieldString = "22704";
	public String newestCodeID = null;// 最新期数网址id
	public boolean isAutoBuy = false;// 是否开启挂机自动下注
	public boolean isBuy = true;// 是否已经购买
	public int autoBuyNum = 7;// 挂机购买次数
	public JFrameListeningInterface foreknowInterface;
	public JTextField textField;//商品ID编辑框
	public CategoryPlot plot;
	public JComboBox<String> comboBox;
	public JProgressBar progressBar;
	public JLabel label_2;
	public JLabel label_4;
	public JLabel label_6;
	public JLabel label_1;
	public JLabel label_8;
	public JLabel label_9;
	public JPanel panel;
	public JPanel panel_1;
	public JButton btnNewButton;
	public JLabel label_10;
	public JSpinner spinner;
	public JTextField textField_1;
	public JTextField textField_2;
	public JSpinner spinner_1;
	public JComboBox<String> comboBox_1;
	public JButton guajiButton;
	public JButton startMonitorButton;
	public JLabel label_16;
	public JProgressBar progressBar_1;
	public JLabel label_19;
	public JLabel label_20;

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

		startMonitorButton = new JButton("开始监控");
		startMonitorButton.setForeground(Color.WHITE);
		startMonitorButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		startMonitorButton.setFocusPainted(false);
		startMonitorButton.setBackground(Color.RED);
		startMonitorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		startMonitorButton.setBounds(356, 14, 87, 28);
		startMonitorButton.setBorder(null);
		add(startMonitorButton);

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

		guajiButton = new JButton("挂机");
		guajiButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		guajiButton.setForeground(Color.WHITE);
		guajiButton.setBackground(Color.RED);
		
		guajiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		guajiButton.setBounds(276, 21, 70, 28);
		panel_1.add(guajiButton);

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
}
