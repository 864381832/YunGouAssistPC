package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.main.YunGouLoginServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Cursor;

/*
 * 提前揭晓
 */
public class Foreknow extends JPanel {
	private static final long serialVersionUID = 1L;
	private int selectNum = 0;
	private long beginTime = 0;
	private Thread thread = null;
	private JFrameListeningInterface foreknowInterface;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel label_6;
	private JTable table;
	private JScrollPane scrollPane;
	private JCheckBox checkBox;
	private JComboBox<String> comboBox;
	private JLabel lblid_1;
	private JButton button;
	private JComboBox<String> comboBox_1;

	public Foreknow() {
		setLayout(null);

		JLabel lblid = new JLabel("商品ID");
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		lblid.setBounds(35, 21, 49, 20);
		add(lblid);

		textField = new JTextField("22704");
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}
		});
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
		comboBox_3.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(IntelligentMonitoring.class.getResource("/images/mainJFrame/xialaanniu.png"))
						.getImage(), 0, 2, c.getWidth() - 2, c.getHeight() - 2, c);
			}
		});
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
		textField_1.setEnabled(false);
		JButton comboBox_4 = new JButton();
		comboBox_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textField_1.isEnabled()) {
					ShowMyMenu.ShowGoodsPeriodMenu(textField_1, textField.getText());
				}
			}
		});
		comboBox_4.setCursor(new Cursor(Cursor.HAND_CURSOR));
		comboBox_4.setBorderPainted(false);
		comboBox_4.setBounds(textField.getWidth() - 18, 0, 18, 28);
		comboBox_4.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(IntelligentMonitoring.class.getResource("/images/mainJFrame/xialaanniu.png"))
						.getImage(), 0, 2, c.getWidth() - 2, c.getHeight() - 2, c);
			}
		});
		textField_1.add(comboBox_4);

		button = new JButton("查  询");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(Color.RED);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("查  询")) {
					if (Config.isAssistOverdue) {
						ShowMyMenu.ShowAssistOverdue();
						return;
					}
					selectButton();
				} else {
					setButtonEnabled(true);
					button.setText("查  询");
					label_6.setText("停止查询");
					if (thread != null) {
						thread.stop();
						thread = null;
					}
				}
			}
		});
		button.setBounds(745, 15, 100, 30);

		add(button);

		JLabel label_5 = new JLabel("查询用时:");
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_5.setBounds(488, 48, 74, 30);
		add(label_5);

		label_6 = new JLabel("请点击查询");
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_6.setForeground(Color.RED);
		label_6.setBounds(563, 48, 126, 30);
		add(label_6);

		if (!Config.hezuoQQqun.equals(" ")) {
			JLabel hintLabel = new JLabel(Config.hezuoQQqun);
			hintLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
			hintLabel.setBounds(689, 48, 184, 30);
			hintLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			hintLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
				}
			});
			add(hintLabel);
		}

		// 定义表格列名数组
		String[] columnNames = { "商品ID", "期数", "幸运码", "中奖人", "中奖时购买人次", "中奖位", "购买区间" };
		// 定义表格数据数组
		String[][] tableValues = new String[][] { {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} };
		// 创建指定表格列名和表格数据的表格模型类的对象
		DefaultTableModel tableModel = new DefaultTableModel(tableValues, columnNames) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setSelectionForeground(Color.BLACK);
		table.setSelectionBackground(Color.CYAN);
		table.setBorder(null);
		table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.setSize(838, 430);
		table.setLocation(3, 27);
		table.setForeground(Color.RED);
		add(table);
		table.setRowHeight(25);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(20);
		table.getColumnModel().getColumn(2).setPreferredWidth(30);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.getColumnModel().getColumn(5).setPreferredWidth(30);
		table.getColumnModel().getColumn(6).setPreferredWidth(30);

		// 设置table表头居中
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		// 设置table内容居中
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					try {
						DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
						String goodsID = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
						if (goodsID != null && !goodsID.equals("")) {
							String codeRNO = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
							String userName = (String) tableModel.getValueAt(table.getSelectedRow(), 3);
							String period = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
							ShowMyMenu.ShowForeknowRightClickMenu(table,
									new String[] { goodsID, codeRNO, userName, period }, arg0.getX(), arg0.getY());
						}
					} catch (Exception e) {
					}
				}
			}
		});

		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(null);
		scrollPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		scrollPane.setBounds(35, 86, 838, 408);
		add(scrollPane);

		checkBox = new JCheckBox("验证自己中奖");
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (checkBox.isSelected()) {
					if (!YunGouLoginServer.getUserServer().getIsLogin()) {
						HintDialog.startExpiringHint(HintDialog.yungouLogin, "您还未登录1元云购账户，请先登录后再使用验证中奖功能，谢谢！！！");
						checkBox.setSelected(false);
					} else {
						((DefaultTableModel) table.getModel()).addColumn("是否中奖");
					}
				} else {
					((DefaultTableModel) table.getModel()).setColumnCount(7);
				}
			}
		});
		checkBox.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		LookAndFeel.installProperty(checkBox, "opaque", Boolean.FALSE);
		checkBox.setBounds(380, 15, 120, 30);
		add(checkBox);

		comboBox_1 = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] { "最近期数", "精确查询" }));
		comboBox_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		comboBox_1.setSelectedIndex(0);
		comboBox_1.setBounds(524, 15, 90, 30);
		comboBox_1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (comboBox_1.getSelectedIndex() == 0) {
						textField_1.setEnabled(false);
						comboBox.setEnabled(true);
					} else if (comboBox_1.getSelectedIndex() == 1) {
						textField_1.setEnabled(true);
						comboBox.setEnabled(false);
					}
				}
			}
		});
		add(comboBox_1);

		comboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] { "最近2", "最近3", "最近4", "最近5",
				"最近6", "最近7", "最近8", "最近9", "最近10", "最近11", "最近12", "最近13", "最近14", "最近15" }));
		comboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		comboBox.setMaximumRowCount(14);
		comboBox.setSelectedIndex(1);
		comboBox.setBounds(632, 15, 80, 30);
		add(comboBox);

		lblid_1 = new JLabel("请请输入商品ID");
		lblid_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblid_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textField.getText().equals("")) {
					HttpGetUtil.openBrowseURL("http://www.1yyg.com/products/" + textField.getText() + ".html");
				}
			}
		});
		lblid_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		lblid_1.setForeground(Color.RED);
		lblid_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblid_1.setBounds(35, 48, 454, 30);
		add(lblid_1);

		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (button.getText().equals("停止查询") && text[0].equals(textField.getText())) {
					table.setValueAt(text[0], Integer.parseInt(FrameName), 0);
					table.setValueAt(text[1], Integer.parseInt(FrameName), 1);
					if (!"搜索超时".equals(text[2])) {
						table.setValueAt(text[2], Integer.parseInt(FrameName), 2);
						table.setValueAt(text[3], Integer.parseInt(FrameName), 3);
						table.setValueAt(text[4], Integer.parseInt(FrameName), 4);
						if (!text[2].equals("") && !text[2].equals("正在云购中")) {
							if (checkBox.isSelected() && table.getValueAt(Integer.parseInt(FrameName), 7).equals("")) {
								if (YunGouLoginServer.getUserServer().getIsLogin()) {
									new Thread(new Runnable() {
										public void run() {
											try {
												if (YunGouLoginServer.getUserServer().getUserBuyDetail(text[7],
														text[2])) {
													table.setValueAt("恭喜您中奖了", Integer.parseInt(FrameName), 7);
												} else {
													table.setValueAt("未中奖", Integer.parseInt(FrameName), 7);
												}
											} catch (Exception e) {
												table.setValueAt("验证出问题了", Integer.parseInt(FrameName), 7);
											}
										}
									}).start();
								} else {
									checkBox.setSelected(false);
									((DefaultTableModel) table.getModel()).setColumnCount(7);
								}
							}
						}
					}
					if (!"".equals(text[5]) && table.getValueAt(Integer.parseInt(FrameName), 5).equals("")) {
						if (comboBox_1.getSelectedIndex() == 1) {
							label_6.setText((System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
							button.setText("查  询");
							setButtonEnabled(true);
						} else {
							selectNum++;
							if (selectNum >= comboBox.getSelectedIndex() + 2) {
								label_6.setText((System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
								button.setText("查  询");
								setButtonEnabled(true);
							}
						}
					}
					table.setValueAt(text[5], Integer.parseInt(FrameName), 5);
					table.setValueAt(text[6], Integer.parseInt(FrameName), 6);
				}
			}

			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("setGoodsName")) {
					lblid_1.setText(text);
				} else if (FrameName.equals("webError")) {
					String[] text2 = new String[] { text.split("___")[0], text.split("___")[1], "搜索超时", "网络繁忙", "网络繁忙",
							"网络繁忙", "网络繁忙" };
					foreknowInterface.setFrameListeningText(text.split("___")[2], text2);
				}else if (FrameName.equals("isSelect")) {
					String[] text2 = new String[] { text.split("___")[0], text.split("___")[1], "搜索超时", "正在查询", "正在查询",
							"正在查询", "正在查询" };
					foreknowInterface.setFrameListeningText(text.split("___")[2], text2);
				} else if (FrameName.equals("selectError")) {
					String[] text2 = new String[] { text.split("___")[0], text.split("___")[1], "搜索异常", "请检查输入商品ID",
							"或商品期数", "或检查网络", "搜索异常" };
					foreknowInterface.setFrameListeningText(text.split("___")[2], text2);
				} else if (FrameName.equals("setGoodsNameError")) {
					button.setText("查  询");
					setButtonEnabled(true);
					label_6.setText("未查到此商品");
				} else if (FrameName.equals("goodsoldOut")) {
					button.setText("查  询");
					setButtonEnabled(true);
					label_6.setText("该商品已经下降了");
				}
			}
		};
	}

	private void selectButton() {
		if (textField.getText().equals("")) {
			label_6.setText("请输入商品ID");
			return;
		}
		if (comboBox_1.getSelectedIndex() == 1 && textField_1.getText().equals("")) {
			label_6.setText("请输入查询期数");
			return;
		}
		button.setText("停止查询");
		label_6.setText("正在查询...");
		lblid_1.setText("");
		setButtonEnabled(false);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 7; j++) {
				table.setValueAt("", i, j);
			}
			if (checkBox.isSelected()) {
				table.setValueAt("", i, 7);
			}
		}
		thread = new Thread(new Runnable() {
			public void run() {
				beginTime = System.currentTimeMillis();
				selectNum = 0;
				try {
					if (comboBox_1.getSelectedIndex() == 0) {
						YunGouServer.setForeknow(textField.getText().trim(), null,
								"" + (comboBox.getSelectedIndex() + 2), foreknowInterface);
					} else {
						YunGouServer.setForeknow(textField.getText().trim(), textField_1.getText().trim(), null,
								foreknowInterface);
					}
				} catch (Exception e2) {
					button.setText("查  询");
					label_6.setText("该商品已下架或网络异常");
					setButtonEnabled(true);
				}
			}
		});
		thread.start();

	}

	private void setButtonEnabled(boolean isEnabled) {
		textField.setEnabled(isEnabled);
		textField_1.setEnabled(isEnabled);
		comboBox.setEnabled(isEnabled);
		comboBox_1.setEnabled(isEnabled);
		checkBox.setEnabled(isEnabled);
		if (isEnabled) {
			if (comboBox_1.getSelectedIndex() == 0) {
				textField_1.setEnabled(false);
			} else {
				comboBox.setEnabled(false);
			}
		}
	}

	public void setTextFieldContent(String goodsID, String period) {
		textField.setText(goodsID);
		textField_1.setText(period);
	}
}
