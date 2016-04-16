package com.ytkj.ygAssist.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.ExpiringHint;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserLogin extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrameListeningInterface foreknowInterface;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel label_2;
	private JButton button;
	private JCheckBox checkBox;
	private JCheckBox checkBox_1;
	private JLabel lblqq;
	private JLabel lblNewLabel;
	private JLabel lblqq_1;
	private JLabel label_5;
	private JLabel label_6;
	private JButton button_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					init();
					startUserLogin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void startUserLogin() {
		UserLogin frame = new UserLogin();
		frame.setVisible(true);
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 3);
	}

	private static void init() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		FilesUtil.readerConfigFile();
		try {
			new File("智能云购助手.exe").delete();
			new File("智能云购助手_cache.exe").renameTo(new File("智能云购助手.exe"));
		} catch (Exception e) {
		}
		try {
			Config.SystemAddressCode = MyStringUtil.getMACAddress() + new Date().getTime();
			AssistServer.getHeziQQqunInfo();
		} catch (Exception e) {
		}
	}

	/**
	 * Create the frame.
	 */
	public UserLogin() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserLogin.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 430, 340);
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.drawImage(
						Toolkit.getDefaultToolkit().getImage(UserLogin.class.getResource("/images/userLogin/tp1.png")),
						0, 0, getWidth(), getHeight() / 2, this);
				g2d.setColor(new Color(27, 27, 27));
				g2d.fillRoundRect(0, getHeight() / 2, getWidth(), getHeight(), 0, 0);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		button = new JButton("登    录");
		button.setFont(new Font("微软雅黑", Font.BOLD, 12));
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(221, 4, 4));
		LookAndFeel.installProperty(button, "opaque", Boolean.FALSE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginButton();
			}
		});
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBorderPainted(false);
		button.setBounds(109, 299, 199, 35);
		contentPane.add(button);

		JLabel label = new JLabel("用户名:");
		label.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label.setForeground(Color.WHITE);
		label.setBounds(42, 189, 54, 32);
		contentPane.add(label);

		JLabel label_1 = new JLabel("密    码:");
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(42, 229, 54, 32);
		contentPane.add(label_1);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					passwordField.requestFocus();
				}
				super.keyReleased(e);
			}
		});
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (textField.getText().equals("请使用一元云购账户登录")) {
					textField.setText("");
				}
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// if (textField.getText().equals("请使用一元云购账户登录")) {
				// textField.setText("");
				// }
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (textField.getText().equals("")) {
					textField.setText("请使用一元云购账户登录");
				}
			}
		});
		textField.setToolTipText("该一元云购账户仅作为本软件的登录账号，里面使用的账户可自由切换");
		textField.setFont(new Font("微软雅黑", Font.BOLD, 14));
		textField.setBounds(109, 188, 197, 32);
		contentPane.add(textField);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("微软雅黑", Font.BOLD, 14));
		passwordField.setBounds(109, 230, 197, 32);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					loginButton();
				}
				super.keyReleased(e);
			}
		});
		contentPane.add(passwordField);

		checkBox = new JCheckBox("记住密码");
		checkBox.setFont(new Font("微软雅黑", Font.BOLD, 13));
		LookAndFeel.installProperty(checkBox, "opaque", Boolean.FALSE);
		checkBox.setBackground(new Color(0, 0, 0, 0));
		checkBox.setForeground(Color.WHITE);
		checkBox.setSelected(true);
		checkBox.setBounds(109, 268, 81, 23);
		contentPane.add(checkBox);

		label_2 = new JLabel();
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 12));
		label_2.setForeground(Color.RED);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(308, 310, 114, 15);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("注册");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.yunGouRegisterUrl);
			}
		});
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_3.setForeground(Color.RED);
		label_3.setBounds(308, 189, 114, 32);
		label_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(label_3);

		JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setBounds(317, 235, 90, 22);
		btnNewButton_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(btnNewButton_1);
		btnNewButton_1.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/addQQ.png"));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://jq.qq.com/?_wv=1027&k=25RDhFq");
			}
		});

		checkBox_1 = new JCheckBox("自动登录");
		checkBox_1.setFont(new Font("微软雅黑", Font.BOLD, 13));
		LookAndFeel.installProperty(checkBox_1, "opaque", Boolean.FALSE);
		checkBox_1.setForeground(Color.WHITE);
		checkBox_1.setBackground(new Color(0, 0, 0, 0));
		checkBox_1.setBounds(225, 268, 81, 23);
		contentPane.add(checkBox_1);

		JButton btnNewButton = new JButton();
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNewButton.setUI(ViewTools.getBasicButtonUI("/images/userLogin/zxh.png"));
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBounds(354, 10, 28, 28);
		contentPane.add(btnNewButton);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
		});

		JButton button_1 = new JButton();
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setUI(ViewTools.getBasicButtonUI("/images/userLogin/tc.png"));
		button_1.setBorderPainted(false);
		button_1.setBounds(392, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});

		String userInfo[] = AssistServer.readerLoginAssistInfo();
		if (userInfo[0] == null || "".equals(userInfo[0])) {
			textField.setText("请使用一元云购账户登录");
		} else {
			textField.setText(userInfo[0]);
		}
		passwordField.setText(userInfo[1]);

		lblqq = new JLabel("续费联系QQ：" + Config.renewQQ);
		lblqq.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblqq.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.renewQQ));
			}
		});
		lblqq.setForeground(Color.YELLOW);
		lblqq.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblqq.setBounds(18, 114, 200, 18);
		contentPane.add(lblqq);

		JButton btnNewButton_2 = new JButton();
		btnNewButton_2.setBounds(225, 114, 77, 21);
		contentPane.add(btnNewButton_2);
		btnNewButton_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNewButton_2.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qqjt.png"));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.renewQQ));
			}
		});

		lblNewLabel = new JLabel(Config.yunGouAssistVersions);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
		lblNewLabel.setBounds(100, 6, 54, 18);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel(Config.shouyeText1);
		lblNewLabel_1.setForeground(Color.YELLOW);
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblNewLabel_1.setBounds(18, 42, 389, 18);
		contentPane.add(lblNewLabel_1);

		JLabel lblhttpygyuntengkejicn = new JLabel("官网:http://yg.yuntengkeji.cn");
		lblhttpygyuntengkejicn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://yg.yuntengkeji.cn");
			}
		});
		lblhttpygyuntengkejicn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblhttpygyuntengkejicn.setForeground(Color.YELLOW);
		lblhttpygyuntengkejicn.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblhttpygyuntengkejicn.setBounds(18, 90, 213, 18);
		contentPane.add(lblhttpygyuntengkejicn);

		lblqq_1 = new JLabel("请联系商务QQ续费");
		lblqq_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblqq_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.renewQQ));
			}
		});
		lblqq_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblqq_1.setForeground(Color.YELLOW);
		lblqq_1.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		lblqq_1.setBounds(308, 266, 114, 15);
		contentPane.add(lblqq_1);
		lblqq_1.setVisible(false);

		label_5 = new JLabel("或进群交流延长试用");
		label_5.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistQQqunUrl);
			}
		});
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setForeground(Color.YELLOW);
		label_5.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		label_5.setBounds(305, 286, 120, 15);
		contentPane.add(label_5);

		label_6 = new JLabel(Config.shouyeText2);
		label_6.setForeground(Color.YELLOW);
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_6.setBounds(18, 66, 389, 18);
		contentPane.add(label_6);

		label_5.setVisible(false);

		String loginConfiguration[] = FilesUtil.readerUserInfo("\\loginConfiguration.log");
		if (loginConfiguration[0] != null) {
			checkBox.setSelected(loginConfiguration[0].equals("true"));
			checkBox_1.setSelected(loginConfiguration[1].equals("true"));
		}

		setListening();
		if (checkBox_1.isSelected()) {
			setButtonEnabled(false);
			button.setText("取消登录");
			label_2.setText("正在自动登录...");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					YunGouServer.loginAssist(textField.getText().trim(), passwordField.getText().trim(),
							foreknowInterface);
				}
			}, 2000);
		}
		AssistServer.getRenewQQ();
		lblqq.setText("续费联系QQ：" + Config.renewQQ);

		button_3 = new JButton();
		button_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_3.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/addQQ3.png"));
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistQQqunUrl);
			}
		});
		button_3.setBounds(218, 140, 90, 22);
		contentPane.add(button_3);

		JLabel label_7 = new JLabel(Config.assistQQqun);
		label_7.setForeground(Color.YELLOW);
		label_7.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_7.setBounds(18, 142, 200, 18);
		contentPane.add(label_7);
		AssistServer.getSoftVersions(UserLogin.this);// 检查是否有新版本

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
//				HintDialog.startExpiringHint(AssistServer.getCommonality().replace("__", "\r\n"));
			}
		}, 2000);
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("loginError")) {
					label_2.setText(text);
					button.setText("登    录");
					setButtonEnabled(true);
				} else if (FrameName.equals("loginSeriousnessError")) {
					button.setText("登    录");
					label_2.setText("登录异常");
					setButtonEnabled(true);
					JOptionPane.showMessageDialog(UserLogin.this, "请检查用户名或密码，或者检查网络", "提示",
							JOptionPane.WARNING_MESSAGE);
				} else if (FrameName.equals("loginSucceed")) {
					if (button.getText().equals("取消登录")) {
						if (checkBox.isSelected()) {
							AssistServer.saveLoginAssistInfo(textField.getText().trim(),
									passwordField.getText().trim());
						} else {
							AssistServer.saveLoginAssistInfo(textField.getText().trim(), "");
						}
						FilesUtil.saveUserInfo("\\loginConfiguration.log", "" + checkBox.isSelected(),
								"" + checkBox_1.isSelected());
						Config.userName = textField.getText().trim();
						Config.userPassword = passwordField.getText().trim();
						FilesUtil.readerGoodsInfoFile();
						MainJFrame.startMainJFrame();
						UserLogin.this.removeAll();
						UserLogin.this.setVisible(false);
					}
				} else if (FrameName.equals("loginErrorOverdue")) {
					label_2.setText(text);
					label_5.setVisible(true);
					lblqq_1.setVisible(true);
					button.setText("登    录");
					setButtonEnabled(true);
					ExpiringHint.startExpiringHint(2);
				}
			}
		};
	}

	private void loginButton() {
		if (button.getText().equals("登    录")) {
			if (textField.getText().equals("") || textField.getText().equals("请使用一元云购账户登录")
					|| passwordField.getText().equals("")) {
				label_2.setText("请输入用户名或密码");
			} else {
				setButtonEnabled(false);
				button.setText("取消登录");
				label_2.setText("正在登录...");
				YunGouServer.loginAssist(textField.getText().trim(), passwordField.getText().trim(), foreknowInterface);
			}
		} else {
			button.setText("登    录");
			label_2.setText("");
			setButtonEnabled(true);
		}
	}

	private void setButtonEnabled(boolean isTrue) {
		textField.setEnabled(isTrue);
		passwordField.setEnabled(isTrue);
		checkBox.setEnabled(isTrue);
		checkBox_1.setEnabled(isTrue);
	}
}
