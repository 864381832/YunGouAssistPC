package com.ytkj.ygAssist.view.myView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.tools.ViewTools;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.JComponent;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class GeneralizeRecord extends JDialog {
	private JPanel contentPane;
	private JTable table;
	private JPanel panel;
	private JScrollPane scrollPane;
	private String[] generalizeUserName = null;

	public static void startUserLogin() {
		GeneralizeRecord vIPBuyDialog = new GeneralizeRecord();
		vIPBuyDialog.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public GeneralizeRecord() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GeneralizeRecord.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 750, 489);
		setModal(true);
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 3, this.getWidth(),
				this.getHeight());
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.drawImage(
						Toolkit.getDefaultToolkit()
								.getImage(GeneralizeRecord.class.getResource("/images/userLogin/tp1.png")),
						0, 0, getWidth(), getHeight() / 2, this);
				g2d.setColor(new Color(111, 17, 17));
				g2d.fillRoundRect(0, getHeight() / 2, getWidth(), getHeight(), 0, 0);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton button_1 = new JButton();
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(GeneralizeRecord.class.getResource("/images/userLogin/tc.png")).getImage(), 0,
						0, c.getWidth(), c.getHeight(), c);
			}
		});
		button_1.setBorderPainted(false);
		button_1.setBounds(712, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GeneralizeRecord.this.setVisible(false);
			}
		});

		JLabel label_4 = new JLabel("我 的 推 广 记 录");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label_4.setBounds(312, 10, 156, 28);
		contentPane.add(label_4);

		panel = new JPanel();
		panel.setBounds(143, 110, 450, 211);
		panel.setBackground(new Color(0, 0, 0, 0));
		panel.setLayout(null);

		JLabel label = new JLabel("暂无推广记录，赶紧去推广吧！");
		label.setBounds(96, 0, 278, 24);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("微软雅黑", Font.BOLD, 18));
		panel.add(label);

		JLabel label_1 = new JLabel("每成功邀请到一位好友您和您的好友都能获得三天的使用时间");
		label_1.setForeground(Color.YELLOW);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_1.setBounds(6, 58, 441, 18);
		panel.add(label_1);

		JButton button = new JButton("推广邀请");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // 获得系统剪贴板
				clipboard.setContents(
						new StringSelection(
								AssistServer.getGeneralizeMessage() + "我的邀请码：" + AssistServer.getUserGeneralize()),
						null);
				HintDialog.startExpiringHint("推广信息已复制，您可以推广给您的好友使用！\n每成功邀请到一位好友您和您的好友都能获得三天的使用时间");
			}
		});
		button.setForeground(Color.WHITE);
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setFocusPainted(false);
		button.setBorder(null);
		button.setBackground(Color.RED);
		button.setBounds(158, 124, 102, 28);
		panel.add(button);

		// 定义表格列名数组
		String[] columnNames = { "账户", "推广时间", "是否领奖", "领奖时间" };
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
				if (arg0.getClickCount() == 2) {
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					String userid = generalizeUserName[table.getSelectedRow()];
					// String userid = (String)
					// tableModel.getValueAt(table.getSelectedRow(), 0);
					String isGet = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
					if ("未领取（双击可领取）".equals(isGet) || "领取失败，请稍后重试".equals(isGet)) {
						if (AssistServer.updateIsGetRewards(userid)) {
							tableModel.setValueAt("已领取", table.getSelectedRow(), 2);
							tableModel.setValueAt(new Date().toLocaleString(), table.getSelectedRow(), 3);
						} else {
							tableModel.setValueAt("领取失败，请稍后重试", table.getSelectedRow(), 2);
						}
					}
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

		// 设置table表头居中
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		// 设置table内容居中
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);
		scrollPane = new JScrollPane(table);
		scrollPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		scrollPane.setBorder(null);
		scrollPane.setBounds(25, 50, 699, 418);

		setTableData();
	}

	private void setTableData() {
		ArrayList<String[]> arrayList = AssistServer.getUserGeneralizes();
		generalizeUserName = new String[arrayList.size()];
		if (arrayList.size() > 0) {
			contentPane.add(scrollPane);
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			for (String[] strings : arrayList) {
				String isGet = "已领取";
				String time = "";
				if ("0".equals(strings[2])) {
					isGet = "未领取（双击可领取）";
				} else {
					time = new Date(Long.parseLong(strings[3])).toLocaleString();
				}
				generalizeUserName[tableModel.getRowCount()] = strings[0];
				String userid = strings[0].substring(0, 2)
						+ "**************************".substring(0, strings[0].length() - 6)
						+ strings[0].substring(strings[0].length() - 4, strings[0].length());
				String[] strs = new String[] { userid, new Date(Long.parseLong(strings[1])).toLocaleString(), isGet,
						time };
				tableModel.addRow(strs);
			}
		} else {
			contentPane.add(panel);
		}
	}
}
