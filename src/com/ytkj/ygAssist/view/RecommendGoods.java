package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

/*
 * 热门推荐
 */
public class RecommendGoods extends JPanel {
	private static final long serialVersionUID = 1L;
	private JFrameListeningInterface foreknowInterface = null;
	private JTable table;
	private JTextField textField;
	private JButton button;
	private JLabel label_1;
	private JButton button_1;
	private JLabel label_2;

	public RecommendGoods() {
		setLayout(null);

		JLabel label = new JLabel("输入商品名称，例如：苹果、小米");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label.setBounds(25, 16, 240, 20);
		add(label);

		textField = new JTextField("小米");
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}
		});
		textField.setFont(new Font("楷体", Font.BOLD, 14));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setColumns(10);
		textField.setBounds(275, 13, 371, 30);
		add(textField);

		button = new JButton("搜  索");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setBackground(Color.RED);
		button.setBounds(658, 13, 100, 30);
		add(button);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("搜  索")) {
					if (textField.getText().trim().equals("")) {
						label_1.setText("请输入需要搜索的内容。");
					} else {
						button.setText("停止搜索");
						label_1.setText("正在搜索");
						new Thread(new Runnable() {
							public void run() {
								List<Map<String, String>> dList = GetGoodsInfo.getGoodsID(textField.getText().trim());
								if (dList == null) {
									label_1.setText("搜索超时，请检查网络（请在云购网能正常使用的情况下使用本软件，谢谢）");
								} else {
									setTableData(dList);
								}
								label_1.setText("以下为搜索结果(点击想要查询的商品，再右击查看更多功能。)");
								button.setText("搜  索");
							}
						}).start();
					}
				} else {
					label_1.setText("云购商品推荐(点击想要查询的商品，再右击查看更多功能。)");
					button.setText("搜  索");
				}
			}
		});

		// 定义表格列名数组
		String[] columnNames = { "商品ID", "期数", "商品名", "价格", "已参与人次", "剩余人次" };
		// 创建指定表格列名和表格数据的表格模型类的对象
		DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					// System.out.println("点击了"+table.getSelectedRow());
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					String goodsID = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
					String goodsName = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
					String period = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
					ShowMyMenu.ShowGoodsRightClickMenu(table, goodsID, goodsName, period, arg0.getX(), arg0.getY());
				}
			}
		});
		table.setSelectionForeground(Color.BLACK);
		table.setSelectionBackground(Color.CYAN);
		table.setSize(838, 430);
		table.setRowHeight(25);
		table.setForeground(Color.RED);
		table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.setBorder(null);

		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(300);
		table.getColumnModel().getColumn(3).setPreferredWidth(30);
		table.getColumnModel().getColumn(4).setPreferredWidth(30);
		table.getColumnModel().getColumn(5).setPreferredWidth(30);

		// table.setCellEditor(null);

		// 设置table表头居中
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		// 设置table内容居中
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		scrollPane.setBorder(null);
		scrollPane.setBounds(25, 78, 850, 408);
		add(scrollPane);

		label_1 = new JLabel("云购商品推荐(点击想要查询的商品，再右击查看更多功能。)");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_1.setBounds(25, 42, 477, 30);
		add(label_1);

		button_1 = new JButton("刷新商品");
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (button_1.getText().equals("刷新商品")) {
					button_1.setText("正在刷新");
					setTableData();
				} else {
					label_1.setText("云购商品推荐(点击想要查询的商品，再右击查看更多功能。)");
					button_1.setText("刷新商品");
				}
			}
		});
		button_1.setForeground(Color.WHITE);
		button_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_1.setBackground(Color.RED);
		button_1.setBounds(770, 13, 100, 30);
		add(button_1);

		label_2 = new JLabel(Config.heziQQqun);
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_2.setBounds(520, 50, 186, 18);
		label_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		add(label_2);

		if (!Config.hezuoQQqun.equals(" ")) {
			JLabel label_3 = new JLabel(Config.hezuoQQqun);
			label_3.setFont(new Font("微软雅黑", Font.BOLD, 14));
			label_3.setBounds(710, 50, 186, 18);
			label_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(Config.hezuoQQqunUrl);
				}
			});
			add(label_3);
		}

		setListening();

		setTableData();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (!"搜索超时".equals(text[2]) && !"".equals(text[5])) {
					DefaultTableModel tableModel1 = (DefaultTableModel) table.getModel();
					tableModel1.addRow(text);
				}
			}
		};
	}

	private void setTableData() {
		label_1.setText("云购商品推荐(点击想要查询的商品，再右击查看更多功能。)");
		new Thread(new Runnable() {
			public void run() {
				List<Map<String, String>> dList = GetGoodsInfo.getRecGoodsList();
				setTableData(dList);
				button_1.setText("刷新商品");
			}
		}).start();
	}

	private void setTableData(List<Map<String, String>> dList) {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);// 清除原有行
		for (Map<String, String> map : dList) {
			String goodsSName = map.get("goodsSName");
			if (!"0".equals(map.get("codeLimitBuy"))) {
				goodsSName = "(限购)" + map.get("goodsSName");
			}
			String isSale = null;
			if ("0".equals(map.get("isSale"))) {
				isSale = "已下架";
			} else {
				isSale = "" + (Integer.parseInt(map.get("codeQuantity")) - Integer.parseInt(map.get("codeSales")));
			}
			String goodsID = map.get("goodsID");
			String[] text = new String[] { goodsID, map.get("codePeriod"), goodsSName, map.get("codePrice"),
					map.get("codeSales"), isSale };
			tableModel.addRow(text);
			if (CacheData.getGoodsInfoCacheDate(goodsID) == null) {
				String codeID = GetGoodsInfo.getGoodsPeriodInfo(map.get("goodsID"), "1")[1];
				if (codeID == null) {
					codeID = map.get("codeID");
				}
				CacheData.setGoodsInfoCacheDate(goodsID,
						new String[] { goodsID, map.get("goodsSName"), codeID, map.get("codeLimitBuy") });
				SelectAssistPublishs.uploadGoodsInfos(new String[] { goodsID, map.get("goodsSName"), codeID,
						map.get("codeLimitBuy"), map.get("goodsPic"), map.get("isSale") });
			}
		}
	}
}
