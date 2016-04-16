package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.main.YunGouLoginServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.GeneralizeRecord;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * 云购登录
 */
public class YunGouLogin extends JPanel {
	private static final long serialVersionUID = 1L;
	private JFrameListeningInterface foreknowInterface;
	private JTextField txtqqcom;
	private JPasswordField passwordField;
	private JLabel label_3;
	private JButton button;
	private JCheckBox checkBox;
	private JLabel label_4;
	private JButton button_1;
	private JTable table;

	public YunGouLogin() {
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(148, 148, 150), 2));
		panel.setToolTipText("用户登录");
		panel.setBounds(31, 24, 257, 195);
		add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("云购登录");
		label.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label.setBounds(95, 10, 75, 15);
		panel.add(label);

		JLabel label_1 = new JLabel("用户名:");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_1.setBounds(20, 35, 54, 30);
		panel.add(label_1);

		txtqqcom = new JTextField();
		txtqqcom.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		txtqqcom.setBounds(74, 35, 160, 30);
		panel.add(txtqqcom);
		txtqqcom.setColumns(10);
		txtqqcom.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(txtqqcom, arg0.getX(), arg0.getY());
				}
			}
		});
		txtqqcom.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					passwordField.requestFocus();
				}
				super.keyReleased(e);
			}
		});

		JLabel label_2 = new JLabel("密    码:");
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_2.setBounds(20, 80, 54, 30);
		panel.add(label_2);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		passwordField.setBounds(74, 80, 160, 30);
		panel.add(passwordField);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					loginButton();
				}
				super.keyReleased(e);
			}
		});

		checkBox = new JCheckBox("是否记住密码");
		checkBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		checkBox.setSelected(true);
		checkBox.setBackground(Color.LIGHT_GRAY);
		checkBox.setBounds(60, 122, 133, 23);
		panel.add(checkBox);

		button = new JButton("登录1元云购");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBackground(Color.RED);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginButton();
			}
		});
		button.setBounds(20, 157, 106, 30);
		panel.add(button);

		label_3 = new JLabel("");
		label_3.setForeground(Color.RED);
		label_3.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		label_3.setBounds(31, 230, 164, 30);
		add(label_3);

		String userInfo[] = YunGouLoginServer.readerUserLoginInfo();
		txtqqcom.setText(userInfo[0]);
		passwordField.setText(userInfo[1]);

		button_1 = new JButton("注册1元云购");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (button_1.getText().equals("注册1元云购")) {
					HttpGetUtil.openBrowseURL(Config.yunGouRegisterUrl);
				} else {
					refreshBalance();
					// button_1.setText("正在获取余额");
					// new Thread(new Runnable() {
					// public void run() {
					// try {
					// String balance =
					// YunGouLoginServer.getUserServer().getUserBalance();
					// label_4.setText("余额: " + balance);
					// } catch (Exception e2) {
					// }
					// button_1.setText("刷新余额");
					// }
					// }).start();
				}
			}
		});
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setForeground(Color.WHITE);
		button_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_1.setBackground(Color.RED);
		button_1.setBounds(138, 157, 106, 30);
		panel.add(button_1);

		label_4 = new JLabel("");
		label_4.setForeground(Color.RED);
		label_4.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		label_4.setBounds(193, 230, 95, 30);
		add(label_4);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setToolTipText("用户登录");
		panel_1.setBorder(new LineBorder(new Color(148, 148, 150), 2));
		panel_1.setBounds(31, 270, 257, 195);
		add(panel_1);

		JLabel label_18 = new JLabel("联系我们");
		label_18.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_18.setBounds(98, 17, 75, 20);
		panel_1.add(label_18);

		JLabel label_19 = new JLabel(Config.assistQQqun);
		label_19.setToolTipText("点击进入QQ群");
		label_19.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistQQqunUrl);
			}
		});
		label_19.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_19.setForeground(Color.RED);
		label_19.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_19.setBounds(18, 47, 233, 25);
		panel_1.add(label_19);

		JLabel label_20 = new JLabel("官网：http://yg.yuntengkeji.cn");
		label_20.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://yg.yuntengkeji.cn");
			}
		});
		label_20.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_20.setToolTipText("点击查看官网详情");
		label_20.setForeground(Color.RED);
		label_20.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_20.setBounds(18, 141, 214, 25);
		panel_1.add(label_20);

		JLabel label_21 = new JLabel("当前版本：" + Config.yunGouAssistVersions);
		label_21.setForeground(Color.RED);
		label_21.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_21.setBounds(18, 168, 147, 18);
		panel_1.add(label_21);

		JLabel label_27 = new JLabel(Config.heziQQqun);
		label_27.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_27.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		label_27.setBounds(18, 69, 233, 25);
		label_27.setForeground(Color.RED);
		label_27.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		panel_1.add(label_27);

		JButton button_2 = new JButton();
		button_2.setBounds(220, 49, 21, 21);
		panel_1.add(button_2);
		button_2.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/qqqun.png"));

		JButton button_3 = new JButton();
		button_3.setBounds(220, 70, 21, 21);
		panel_1.add(button_3);
		button_3.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/qqqun.png"));

		if (!Config.hezuoQQqun.equals(" ")) {
			JLabel label_22 = new JLabel(Config.hezuoQQqun);
			label_22.setToolTipText("点击进入QQ群");
			label_22.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_22.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(Config.hezuoQQqunUrl);
				}
			});
			label_22.setForeground(Color.RED);
			label_22.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			label_22.setBounds(18, 92, 233, 25);
			panel_1.add(label_22);
			JButton button_9 = new JButton();
			button_9.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/qqqun.png"));
			button_9.setBounds(220, 94, 21, 21);
			panel_1.add(button_9);
		}

		if (!Config.hezuoQQ.equals(" ")) {
			JButton button_10 = new JButton();
			button_10.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qq_logo.png"));
			button_10.setBounds(220, 118, 21, 21);
			panel_1.add(button_10);
			JLabel label_26 = new JLabel("收货联系QQ：" + Config.hezuoQQ);
			label_26.setForeground(Color.RED);
			label_26.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			label_26.setBounds(18, 116, 223, 25);
			label_26.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_26.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.hezuoQQ));
				}
			});
			panel_1.add(label_26);
		}

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(308, 24, 560, 441);
		panel_2.setBorder(new LineBorder(new Color(148, 148, 150), 2));
		add(panel_2);
		panel_2.setLayout(null);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(200, 39, 39));
		panel_3.setBorder(null);
		panel_3.setBounds(2, 2, 556, 50);
		panel_2.add(panel_3);
		panel_3.setLayout(null);

		JLabel label_6 = new JLabel("智 能 云 购 助 手");
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label_6.setBounds(192, 6, 152, 30);
		panel_3.add(label_6);

		JButton button_4 = new JButton("推广邀请");
		button_4.setForeground(Color.WHITE);
		button_4.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_4.setFocusPainted(false);
		button_4.setBorder(null);
		button_4.setBackground(Color.RED);
		button_4.setBounds(148, 384, 102, 28);
		button_4.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_4.addMouseListener(new MouseAdapter() {
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
		panel_2.add(button_4);

		JButton button_5 = new JButton("我的邀请记录");
		button_5.setForeground(Color.WHITE);
		button_5.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_5.setFocusPainted(false);
		button_5.setBorder(null);
		button_5.setBackground(Color.RED);
		button_5.setBounds(299, 384, 110, 28);
		button_5.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				GeneralizeRecord.startUserLogin();
			}
		});
		panel_2.add(button_5);

		JLabel label_5 = new JLabel("每成功邀请到一位好友您和您的好友都能获得三天的使用时间");
		label_5.setForeground(Color.BLACK);
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_5.setBounds(82, 354, 404, 18);
		panel_2.add(label_5);

		JLabel label_8 = new JLabel("分享助手给您的好友使用");
		label_8.setForeground(Color.BLACK);
		label_8.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_8.setBounds(82, 324, 404, 18);
		panel_2.add(label_8);

		JTextPane txtpnn = new JTextPane() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(214, 217, 223));
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		txtpnn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		txtpnn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		txtpnn.setBorder(null);
		txtpnn.setBackground(new Color(0, 0, 0, 0));
		txtpnn.setFont(new Font("微软雅黑", Font.BOLD, 16));
		txtpnn.setForeground(Color.RED);
		txtpnn.setEditable(false);
		txtpnn.setText(AssistServer.getRecruitMessage().replace("__", "\r\n"));
		JScrollPane scrollPane_1 = new JScrollPane(txtpnn);
		scrollPane_1.setBorder(null);
		scrollPane_1.setBounds(46, 89, 472, 221);
		panel_2.add(scrollPane_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBounds(2, 53, 556, 384);
		// panel_2.add(scrollPane);

		// 定义表格列名数组
		String[] columnNames = { "商品ID", "期数", "商品名", "总价", "购买量", "剩余量", "删除" };
		// 创建指定表格列名和表格数据的表格模型类的对象
		DefaultTableModel tableModel = new DefaultTableModel(null, columnNames);
		table = new JTable(tableModel);
		table.setBorder(null);
		scrollPane.setViewportView(table);

		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameText(String FrameName, String text) {
				if ("loginSucceed".equals(FrameName)) {
					label_3.setText("昵称: " + text);
					if (checkBox.isSelected()) {
						YunGouLoginServer.saveUserLoginInfo(txtqqcom.getText().trim(), passwordField.getText().trim());
					} else {
						YunGouLoginServer.saveUserLoginInfo(txtqqcom.getText().trim(), "");
					}
					// button_1.setVisible(false);
					button_1.setText("刷新余额");
					button.setText("退出登录");
				} else if ("logout".equals(FrameName)) {
					label_3.setText("");
					label_4.setText("");
					txtqqcom.setEnabled(true);
					passwordField.setEnabled(true);
					checkBox.setEnabled(true);
					button_1.setText("注册1元云购");
					// button_1.setVisible(true);
					button.setText("登录1元云购");
				} else if ("loginBalance".equals(FrameName)) {
					label_4.setText("余额: " + text);
				} else if ("loginError".equals(FrameName)) {
					txtqqcom.setEnabled(true);
					passwordField.setEnabled(true);
					checkBox.setEnabled(true);
					label_3.setText("登录失败，请检查用户名和密码。");
					button.setText("登录1元云购");
				}
			}
		};
	}

	private void loginButton() {
		if (button.getText().equals("登录1元云购")) {
			if (Config.isAssistOverdue) {
				ShowMyMenu.ShowAssistOverdue();
				return;
			}
			if (txtqqcom.getText().equals("") && passwordField.getText().equals("")) {
				label_3.setText("请输入用户名或密码");
			} else {
				txtqqcom.setEnabled(false);
				passwordField.setEnabled(false);
				checkBox.setEnabled(false);
				button.setText("正在登录");
				YunGouServer.yunGouLogin(true, txtqqcom.getText().trim(), passwordField.getText().trim(),
						foreknowInterface);
			}
		} else if (button.getText().equals("退出登录")) {
			button.setText("正在退出");
			YunGouServer.yunGouLogin(false, null, null, foreknowInterface);
		}
	}

	/*
	 * 刷新余额
	 */
	public void refreshBalance() {
		button_1.setText("正在获取余额");
		new Thread(new Runnable() {
			public void run() {
				try {
					String balance = YunGouLoginServer.getUserServer().getUserBalance();
					label_4.setText("余额: " + balance);
				} catch (Exception e2) {
				}
				button_1.setText("刷新余额");
			}
		}).start();
	}
}
