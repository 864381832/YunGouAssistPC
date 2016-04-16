package com.ytkj.ygAssist.view.myView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class AddUserInfo extends JDialog {
	private static AddUserInfo addUserInfo = null;
	private JFrameListeningInterface foreknowInterface;
	private JPanel contentPane;
	private JTextField textField;
	private JButton button;
	private JLabel lblqq_1;
	private JLabel label_2;

	public static void startUserLogin(String time) {
		String time1 = time;
		new Thread(new Runnable() {
			public void run() {
				long time = Long.parseLong(time1);
				long t = new Date().getTime();
				if (time <= (t + 24 * 60 * 60 * 1000) && time > (t + 24 * 60 * 60 * 1000 - 3 * 60 * 1000)) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					AddUserInfo.startUserLogin();
				}
			}
		}).start();
	}

	public static void startUserLogin() {
		if (addUserInfo == null) {
			addUserInfo = new AddUserInfo();
			addUserInfo.setVisible(true);
		} else {
			addUserInfo.setVisible(true);
		}
	}

	/**
	 * Create the frame.
	 */
	public AddUserInfo() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddUserInfo.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 430, 340);
		setModal(true);
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 3, this.getWidth(),
				this.getHeight());
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.drawImage(
						Toolkit.getDefaultToolkit()
								.getImage(AddUserInfo.class.getResource("/images/userLogin/tp1.png")),
						0, 0, getWidth(), getHeight() / 2, this);
				g2d.setColor(new Color(27, 27, 27));
				g2d.fillRoundRect(0, getHeight() / 2, getWidth(), getHeight(), 0, 0);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		button = new JButton("确    定");
		button.setFont(new Font("微软雅黑", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(221, 4, 4));
		LookAndFeel.installProperty(button, "opaque", Boolean.FALSE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label_2.setVisible(false);
				try {
					int generalizeId = Integer.parseInt(textField.getText().trim());
					String isAdd = AssistServer.addUserGeneralize("" + generalizeId);
					if ("true".equals(isAdd)) {
						addUserInfo.setVisible(false);
					} else {
						label_2.setVisible(true);
					}
				} catch (Exception e2) {
					label_2.setVisible(true);
				}
			}
		});
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBorderPainted(false);
		button.setBounds(83, 284, 119, 35);
		contentPane.add(button);

		JLabel label = new JLabel("邀请码：");
		label.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label.setForeground(Color.WHITE);
		label.setBounds(83, 189, 65, 32);
		contentPane.add(label);

		textField = new JTextField("请填写正确的邀请码");
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (textField.getText().equals("请填写正确的邀请码")) {
					textField.setText("");
				}
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (textField.getText().equals("")) {
					textField.setText("请填写正确的邀请码");
				}
			}
		});
		textField.setFont(new Font("微软雅黑", Font.BOLD, 14));
		textField.setBounds(150, 188, 197, 32);
		contentPane.add(textField);

		JButton button_1 = new JButton();
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(AddUserInfo.class.getResource("/images/userLogin/tc.png")).getImage(), 0, 0,
						c.getWidth(), c.getHeight(), c);
			}
		});
		button_1.setBorderPainted(false);
		button_1.setBounds(392, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addUserInfo.setVisible(false);
			}
		});

		lblqq_1 = new JLabel("填写正确的邀请码可获得三天的软件使用时间");
		lblqq_1.setForeground(Color.YELLOW);
		lblqq_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq_1.setBounds(18, 67, 404, 18);
		contentPane.add(lblqq_1);

		JLabel label_1 = new JLabel("我的邀请码：" + AssistServer.getUserGeneralize());
		label_1.setForeground(Color.YELLOW);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_1.setBounds(120, 234, 152, 18);
		contentPane.add(label_1);

		JLabel label_3 = new JLabel("每成功邀请到一位好友您和您的好友都能获得三天的使用时间");
		label_3.setForeground(Color.YELLOW);
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_3.setBounds(16, 97, 404, 18);
		contentPane.add(label_3);

		JButton button_2 = new JButton("分  享");
		button_2.setForeground(Color.WHITE);
		button_2.setFont(new Font("微软雅黑", Font.BOLD, 12));
		button_2.setBorderPainted(false);
		button_2.setBackground(new Color(221, 4, 4));
		button_2.setBounds(268, 232, 75, 25);
		contentPane.add(button_2);
		button_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_2.addMouseListener(new MouseAdapter() {
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

		JLabel label_4 = new JLabel("提  示");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label_4.setBounds(189, 0, 65, 28);
		contentPane.add(label_4);

		label_2 = new JLabel("邀请码错误");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(Color.RED);
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 12));
		label_2.setBounds(348, 199, 82, 15);
		label_2.setVisible(false);
		contentPane.add(label_2);

		JButton button_3 = new JButton("跳    过");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addUserInfo.setVisible(false);
			}
		});
		button_3.setForeground(Color.WHITE);
		button_3.setFont(new Font("微软雅黑", Font.BOLD, 14));
		button_3.setBorderPainted(false);
		button_3.setBackground(new Color(221, 4, 4));
		button_3.setBounds(238, 284, 119, 35);
		contentPane.add(button_3);
		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("loginError")) {
					button.setText("登    录");
				}
			}
		};
	}
}
