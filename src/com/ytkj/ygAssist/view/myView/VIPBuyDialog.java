package com.ytkj.ygAssist.view.myView;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VIPBuyDialog extends JFrame {
	private static VIPBuyDialog vIPBuyDialog = null;
	private JFrameListeningInterface foreknowInterface;
	private JPanel contentPane;
	private JLabel lblqq_1;

	public static void startUserLogin() {
		if (vIPBuyDialog == null) {
			vIPBuyDialog = new VIPBuyDialog();
			vIPBuyDialog.setVisible(true);
		} else {
			vIPBuyDialog.setVisible(true);
		}
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		vIPBuyDialog.setLocation((screenSize.width - vIPBuyDialog.getWidth()) / 2,
				(screenSize.height - vIPBuyDialog.getHeight()) / 3);
	}

	/**
	 * Create the frame.
	 */
	public VIPBuyDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VIPBuyDialog.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 430, 340);
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.drawImage(
						Toolkit.getDefaultToolkit()
								.getImage(VIPBuyDialog.class.getResource("/images/userLogin/tp1.png")),
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
				g.drawImage(new ImageIcon(VIPBuyDialog.class.getResource("/images/userLogin/tc.png")).getImage(), 0, 0,
						c.getWidth(), c.getHeight(), c);
			}
		});
		button_1.setBorderPainted(false);
		button_1.setBounds(392, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				vIPBuyDialog.setVisible(false);
			}
		});

		lblqq_1 = new JLabel("您的账号非会员账号，请升级为会员账号才能使用该功能");
		lblqq_1.setForeground(Color.YELLOW);
		lblqq_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq_1.setBounds(18, 48, 404, 18);
		contentPane.add(lblqq_1);

		JLabel lblqq = new JLabel("升级会员请使用支付宝扫下面二维码进行转账（需要**钱/月）");
		lblqq.setForeground(Color.YELLOW);
		lblqq.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq.setBounds(18, 76, 404, 18);
		contentPane.add(lblqq);

		JLabel lblvip = new JLabel("转账时请务必在“付款说明”中填写需要升级vip的账户");
		lblvip.setForeground(Color.YELLOW);
		lblvip.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblvip.setBounds(18, 104, 404, 18);
		contentPane.add(lblvip);

		JLabel lblqq_2 = new JLabel("如转账后长时间为到账请联系商务QQ，谢谢。");
		lblqq_2.setForeground(Color.YELLOW);
		lblqq_2.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq_2.setBounds(18, 132, 404, 18);
		contentPane.add(lblqq_2);

		JLabel label_4 = new JLabel("提  示");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label_4.setBounds(189, 0, 65, 28);
		contentPane.add(label_4);

		JButton btnNewButton = new JButton();
		btnNewButton.setBounds(137, 160, 170, 170);
		btnNewButton.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(VIPBuyDialog.class.getResource("/images/mainJFrame/zf.png")).getImage(), 0, 0,
						c.getWidth(), c.getHeight(), c);
			}
		});
		contentPane.add(btnNewButton);
		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("loginError")) {
				}
			}
		};
	}
}
