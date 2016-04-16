package com.ytkj.ygAssist.view.myView;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.ytkj.ygAssist.server.BackgroundMonitorServer;
import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.GetUserBuyServer;
import com.ytkj.ygAssist.server.GetUserBuyServerBigGoods;
import com.ytkj.ygAssist.server.HeGouBaServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/*
 * 服务器后台监听
 */
public class BackgroundMonitor extends JPanel {
	private JFrameListeningInterface foreknowInterface;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel label_6;
	private JLabel hintLabel;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel lblid_1;
	private JButton button;
	private JButton button_1;
	private JButton button_2;
	private JComboBox<String> comboBox;

	public BackgroundMonitor() {
		setLayout(null);

		JLabel lblid = new JLabel("商品ID");
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		lblid.setBounds(35, 21, 49, 20);
		add(lblid);

		textField = new JTextField("22704");
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(92, 15, 100, 30);
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
		textField.add(comboBox_3);

		JLabel label = new JLabel("期数");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label.setBounds(214, 21, 33, 15);
		add(label);

		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setBounds(261, 15, 100, 30);
		add(textField_1);
		textField_1.setColumns(10);

		button = new JButton("开始监听");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(Color.RED);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("开始监听")) {
					button.setText("停止监听");
					comboBox.setEnabled(false);
					if (comboBox.getSelectedIndex() == 0) {
						BackgroundMonitorServer.getBackgroundMonitor()
								.startBackgroundMonitorServerThread(foreknowInterface, 0);
					} else if (comboBox.getSelectedIndex() == 1) {
						BackgroundMonitorServer.getBackgroundMonitor()
								.startBackgroundMonitorServerThread(foreknowInterface, 1);
					} else if (comboBox.getSelectedIndex() == 2) {
						BackgroundMonitorServer.getBackgroundMonitor()
								.startBackgroundMonitorServerSuperBigThread(foreknowInterface);
					} else if (comboBox.getSelectedIndex() == 3) {
						// BackgroundMonitorServer.getBackgroundMonitor()
						// .startBackgroundMonitorServerHeGouBaThread(foreknowInterface);
						new Thread(new Runnable() {
							public void run() {
								HeGouBaServer.readHegoubaData(textField_1.getText().trim(), foreknowInterface);
								button.setText("开始监听");
								comboBox.setEnabled(true);
							}
						}).start();
					} else if (comboBox.getSelectedIndex() == 4) {
						BackgroundMonitorServer.getBackgroundMonitor()
								.startBackgroundMonitorServerThreadZeroize(foreknowInterface, 0);
					} else if (comboBox.getSelectedIndex() == 5) {
						BackgroundMonitorServer.getBackgroundMonitor()
								.startBackgroundMonitorServerThreadZeroize(foreknowInterface, 1);
					} else if (comboBox.getSelectedIndex() == 6) {
						BackgroundMonitorServer.getBackgroundMonitor()
								.startBackgroundMonitorServerThreadZeroize(foreknowInterface, 2);
					}
				} else {
					button.setText("开始监听");
					comboBox.setEnabled(true);
					BackgroundMonitorServer.getBackgroundMonitor().stopBackgroundMonitorServerThread();
				}
			}
		});
		button.setBounds(782, 16, 91, 30);
		add(button);

		JLabel label_5 = new JLabel("查询用时:");
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_5.setBounds(526, 48, 80, 30);
		add(label_5);

		label_6 = new JLabel("请点击查询");
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_6.setForeground(Color.RED);
		label_6.setBounds(618, 48, 100, 30);
		add(label_6);

		hintLabel = new JLabel("");
		hintLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
		hintLabel.setForeground(Color.RED);
		hintLabel.setBounds(730, 48, 165, 30);
		add(hintLabel);

		// 定义表格列名数组
		String[] columnNames = { "商品ID", "期数", "幸运码", "中奖人", "购买人次", "中奖位", "购买区间" };
		// 创建指定表格列名和表格数据的表格模型类的对象
		DefaultTableModel tableModel = new DefaultTableModel(null, columnNames);

		table = new JTable(tableModel);
		table.setSelectionForeground(Color.BLACK);
		table.setSelectionBackground(Color.CYAN);
		table.setBorder(null);
		table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.setSize(838, 430);
		table.setLocation(3, 27);
		table.setForeground(Color.RED);
		add(table);

		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.setRowHeight(25);
		// 设置table表头居中
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		// 设置table内容居中
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);

		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(null);
		scrollPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		scrollPane.setBounds(35, 86, 838, 408);
		add(scrollPane);

		lblid_1 = new JLabel("请请输入商品ID");
		lblid_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		lblid_1.setForeground(Color.RED);
		lblid_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblid_1.setBounds(35, 48, 454, 30);
		add(lblid_1);

		button_1 = new JButton("查  询");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectButton();
			}
		});
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setForeground(Color.WHITE);
		button_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_1.setBackground(Color.RED);
		button_1.setBounds(373, 16, 80, 30);
		add(button_1);

		button_2 = new JButton("删除数据");
		button_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textField.getText().trim().equals("") || textField_1.getText().trim().equals("")) {
					((DefaultTableModel) table.getModel()).setRowCount(0);// 清除原有行
				} else {
					SelectAssistPublishs.deleteYungouPublish(textField.getText().trim(), textField_1.getText().trim());
				}
			}
		});
		button_2.setForeground(Color.WHITE);
		button_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_2.setBackground(Color.RED);
		button_2.setBounds(465, 16, 84, 30);
		add(button_2);

		JButton button_3 = new JButton("提前监听");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		button_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_3.setForeground(Color.WHITE);
		button_3.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_3.setBackground(Color.RED);
		button_3.setBounds(573, 16, 91, 30);
//		add(button_3);

		comboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(
				new String[] { "监听小件", "监听大件", "超级大件", "合购吧", "小件补零", "大件补零", "超大件补零" }));
		comboBox.setFont(new Font("楷体", Font.BOLD, 12));
		comboBox.setBackground(Color.RED);
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(676, 16, 86, 30);
		add(comboBox);

		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (!"搜索超时".equals(text[2]) && !"".equals(text[5])) {
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					// if (text.length == 8) {
					boolean isHave = true;
					for (int i = 0; i < tableModel.getRowCount(); i++) {
						if (tableModel.getValueAt(i, 0).equals(text[0])
								&& tableModel.getValueAt(i, 1).equals(text[1])) {
							isHave = false;
						}
					}
					if (isHave) {
						String[] text2 = new String[] { text[0], text[1], text[2], text[3], text[4], text[5], text[6] };
						tableModel.addRow(text2);
					}
					if (tableModel.getRowCount() == 100) {
						tableModel.setRowCount(0);// 清除原有行
					}
				}
			}

			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("setGoodsName")) {
					lblid_1.setText(text);
				} else if (FrameName.equals("webError")) {
					String[] text2 = new String[] { text.split("___")[0], text.split("___")[1], "搜索超时", "搜索超时", "搜索超时",
							"搜索超时", "搜索超时" };
					foreknowInterface.setFrameListeningText(text.split("___")[2], text2);
				} else if (FrameName.equals("selectError")) {
					String[] text2 = new String[] { text.split("___")[0], text.split("___")[1], "搜索异常", "请检查输入商品ID",
							"或商品期数", "或检查网络", "搜索异常" };
					foreknowInterface.setFrameListeningText(text.split("___")[2], text2);
				} else if (FrameName.equals("setGoodsNameError")) {
					hintLabel.setText("未查到此商品");
				}
			}
		};
	}

	private void selectButton() {
		new Thread(new Runnable() {
			public void run() {
				try {
					hintLabel.setText("");
					if (textField.getText().equals("")) {
						hintLabel.setText("请输入商品ID");
						return;
					}
					if (textField_1.getText().equals("")) {
						hintLabel.setText("请输入查询期数");
						return;
					}
					String goodsID = textField.getText().trim();
					String period = textField_1.getText().trim();
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
					// YunGouServer.setForeknow(textField.getText().trim(),
					// textField_1.getText().trim(), null,
					// foreknowInterface);
					String[] content = GetGoodsInfo.getGoodsPeriodInfo(goodsID, period);
					if (content != null) {
						if (content[0].equals("2")) {
							new GetUserBuyServer(foreknowInterface, "0").GetGoodsPeriodInfo(goodsID, period, content[1],
									true);
						} else if (content[0].equals("3")) {
							// new GetUserBuyServerBothway(foreknowInterface,
							// "0").GetGoodsPeriodInfo(goodsID, period,
							// content[1], true);
							new GetUserBuyServerBigGoods(foreknowInterface, "0").GetGoodsPeriodInfo(goodsID, period,
									content[1]);
						}
					}
				} catch (Exception e2) {
					hintLabel.setText("该商品已下架或网络异常");
				}
			}
		}).start();
	}
}
