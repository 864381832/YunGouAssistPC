package com.ytkj.ygAssist.view.myView;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.MainJFrame;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class ExpiringHint extends JDialog {
	private JPanel contentPane;
	private JLabel lblqq_1;

	public static void startExpiringHint(int expiringType) {
		ExpiringHint expiringHint = new ExpiringHint(expiringType);
		expiringHint.setVisible(true);
	}

	public ExpiringHint(int expiringType) {
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ExpiringHint.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 430, 340);
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 3, 430, 340);
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.drawImage(
						Toolkit.getDefaultToolkit()
								.getImage(ExpiringHint.class.getResource("/images/userLogin/tp1.png")),
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
		button_1.setUI(ViewTools.getBasicButtonUI("/images/userLogin/tc.png"));
		button_1.setBorderPainted(false);
		button_1.setBounds(392, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ExpiringHint.this.setVisible(false);
				if (expiringType == 1) {
					MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(6);
				}
			}
		});

		lblqq_1 = new JLabel("您的账号即将到期，请及时续费");
		if (expiringType == 0) {
			lblqq_1.setText("您的账号即将到期，请及时续费");
		} else if (expiringType == 1) {
			lblqq_1.setText("您的账号已到期，请及时续费");
		} else if (expiringType == 2) {
			lblqq_1.setText("您的账号已到期，请及时续费");
		}
		lblqq_1.setForeground(Color.YELLOW);
		lblqq_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq_1.setBounds(18, 48, 212, 18);
		contentPane.add(lblqq_1);

		JLabel lblqq = new JLabel("续费请使用支付宝扫下面二维码进行转账（59元/月）");
		lblqq.setForeground(Color.YELLOW);
		lblqq.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq.setBounds(18, 76, 404, 18);
		contentPane.add(lblqq);

		JLabel label_1 = new JLabel("转账时请务必在“付款说明”中填写需要续费的账户");
		label_1.setForeground(Color.YELLOW);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_1.setBounds(18, 104, 404, 18);
		contentPane.add(label_1);

		JLabel lblqq_2 = new JLabel("如转账后请联系客服QQ：" + Config.renewQQ + "，谢谢。");
		lblqq_2.setForeground(Color.YELLOW);
		lblqq_2.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq_2.setBounds(18, 132, 315, 18);
		contentPane.add(lblqq_2);

		JLabel label_4 = new JLabel("提  示");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label_4.setBounds(189, 0, 65, 28);
		contentPane.add(label_4);

		JButton btnNewButton = new JButton();
		btnNewButton.setBounds(137, 160, 150, 150);
		btnNewButton.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/zf.png"));
		contentPane.add(btnNewButton);

		JLabel label = new JLabel("支付宝账号：18955333680      姓名：云腾");
		label.setForeground(Color.YELLOW);
		label.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label.setBounds(74, 312, 321, 18);
		contentPane.add(label);

		JButton button = new JButton();
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qqjt.png"));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://wpa.qq.com/msgrd?v=3&uin=" + Config.renewQQ + "&site=qq&menu=yes");
			}
		});
		button.setBounds(330, 132, 77, 21);
		contentPane.add(button);

		JButton button_2 = new JButton();
		button_2.setBounds(234, 47, 90, 22);
		button_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_2.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/addQQ.png"));
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistQQqunUrl);
			}
		});
		contentPane.add(button_2);
	}

}
