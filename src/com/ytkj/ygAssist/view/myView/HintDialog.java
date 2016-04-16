package com.ytkj.ygAssist.view.myView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.MainJFrame;
import com.ytkj.ygAssist.view.UserLogin;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class HintDialog extends JDialog {
	public static final int yungouLogin = 1;//云购账户未登录
	public static final int buySucceed = 2;//购买成功
	public static final int loginError = 3;//助手登录异常
	public static final int quitAssist = 4;//退出助手
	private JPanel contentPane;
	private JTextPane textPane;

	public static void startExpiringHint(String hintString) {
		startExpiringHint(0, hintString);
	}

	public static void startExpiringHint(int expiringType, String hintString) {
		HintDialog expiringHint = new HintDialog(expiringType, hintString);
		expiringHint.setVisible(true);
	}

	public HintDialog(int expiringType, String hintString) {
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(HintDialog.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 381) / 2, (screenSize.height - 210) / 3, 381, 238);
		ViewTools.windowMove(this);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(111, 17, 17));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton button_1 = new JButton();
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setUI(ViewTools.getBasicButtonUI("/images/userLogin/tc.png"));
		button_1.setBorderPainted(false);
		button_1.setBounds(341, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HintDialog.this.setVisible(false);
				if (expiringType == loginError) {
					UserLogin.startUserLogin();
				}
			}
		});

		JButton button = new JButton("确    定");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HintDialog.this.setVisible(false);
				if (expiringType == yungouLogin) {
					MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(6);
				} else if (expiringType == loginError) {
					UserLogin.startUserLogin();
				}else if (expiringType == quitAssist) {
					System.exit(0);
				}
			}
		});
		button.setForeground(Color.WHITE);
		button.setFont(new Font("微软雅黑", Font.BOLD, 14));
		button.setBorderPainted(false);
		button.setBackground(new Color(221, 4, 4));
		button.setBounds(130, 193, 119, 35);
		contentPane.add(button);

		textPane = new JTextPane() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(111, 17, 17));
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		textPane.setBorder(null);
		textPane.setBackground(new Color(0, 0, 0, 0));
		textPane.setFont(new Font("微软雅黑", Font.BOLD, 13));
		textPane.setForeground(Color.YELLOW);
		textPane.setEditable(false);
		textPane.setText(hintString);
		contentPane.add(textPane);

		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBorder(null);
		scrollPane.setBounds(31, 36, 316, 161);
		contentPane.add(scrollPane);

		JLabel label = new JLabel("提  示");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label.setBounds(170, 8, 56, 28);
		contentPane.add(label);
		
		JButton button_2 = new JButton();
		button_2.setBorderPainted(false);
		button_2.setBounds(6, 6, 24, 24);
		button_2.setUI(ViewTools.getBasicButtonUI("/images/logo.png"));
		contentPane.add(button_2);
		
		JLabel label_1 = new JLabel("智能云购助手");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 12));
		label_1.setBounds(34, 10, 72, 18);
		contentPane.add(label_1);

		setHintDialog(expiringType);
	}

	private void setHintDialog(int expiringType) {
		if (expiringType == 0) {
		} else if (expiringType == buySucceed) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					HintDialog.this.setVisible(false);
				}
			}, 2000);
		}else if (expiringType == quitAssist) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					HintDialog.this.setVisible(false);
				}
			}, 3000);
		}
	}
}
