package com.ytkj.ygAssist.view;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.main.YunGouLoginServer;
import com.ytkj.ygAssist.server.BackgroundMonitorServer;
import com.ytkj.ygAssist.server.SelectAssistPublishs;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.BackgroundMonitor;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;
import com.ytkj.ygAssist.viewControl.IntelligentMonitoringControl;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MainJFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrameListeningInterface foreknowInterface;
	private JPanel contentPane;
	private JLabel label;
	private static MainJFrame frame;
	private JTabbedPane tabbedPane;
	private Timer timer = null;

	public static void startMainJFrame() {
		SelectAssistPublishs.getGoodsInfos();
		frame = new MainJFrame();
		frame.setVisible(true);
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 3);
		// if (Config.isBackgroundMonitorServer) {
		// BackgroundMonitorServer.getBackgroundMonitor().startServer();
		// }
	}

	public static MainJFrame getMainJFrame() {
		return frame;
	}

	/**
	 * Create the frame.
	 */
	public MainJFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserLogin.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.setColor(new Color(214, 217, 223));
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
				g2d.drawImage(new ImageIcon(UserLogin.class.getResource("/images/mainJFrame/daohang.png")).getImage(),
						0, 0, 900, 81, contentPane);
				g2d.drawImage(new ImageIcon(UserLogin.class.getResource("/images/mainJFrame/logo.png")).getImage(), 10,
						10, 120, 39, contentPane);
			}
		};
		// contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setForeground(Color.WHITE);
		// tabbedPane.setBackground(Color.RED);
		tabbedPane.setBounds(0, 52, 900, 560);
		tabbedPane.setBorder(null);
		contentPane.add(tabbedPane);

		RecommendGoods panel = new RecommendGoods();
		tabbedPane.addTab("热门推荐", null, panel, null);
		IntelligentMonitoring panel_7 = new IntelligentMonitoringControl();
		tabbedPane.addTab("智能监听", null, panel_7, null);
		panel_7.setBorder(null);

		JPanel panel_2 = new Foreknow();
		tabbedPane.addTab("提前揭晓", null, panel_2, null);

//		TrendChart panel_1 = new TrendChart();
//		tabbedPane.addTab("走势分析", null, panel_1, null);

		NowWinning panel_3 = new NowWinning();
		tabbedPane.addTab("马上开奖", null, panel_3, null);

		YunGouRemind panel_4 = new YunGouRemind();
		tabbedPane.addTab("云购提醒", null, panel_4, null);

		YunGouLogin panel_5 = new YunGouLogin();
		tabbedPane.addTab("我的云购", null, panel_5, null);

		AssistHelp panel_6 = new AssistHelp();
		tabbedPane.addTab("使用说明", null, panel_6, null);

		JPanel panel_9 = null;
		if (Config.isVip) {
			panel_9 = new VipAnalyze();
		} else {
			panel_9 = new VipAnalyzeSub();
		}
		tabbedPane.addTab(null, new ImageIcon(UserLogin.class.getResource("/images/mainJFrame/znyc.png")), panel_9,
				null);

		BackgroundMonitor panel_8 = new BackgroundMonitor();
		tabbedPane.addTab("服务器监听", null, panel_8, null);

		tabbedPane.setSelectedIndex(5);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				int selectedIndex = tabbedPane.getSelectedIndex();
				switch (selectedIndex) {
				case 1:
					((IntelligentMonitoringControl) tabbedPane.getComponentAt(1)).refreshBalance();
					break;
				case 5:
					if (YunGouLoginServer.getUserServer().getIsLogin()) {
						((YunGouLogin) tabbedPane.getComponentAt(5)).refreshBalance();
					}
					break;
				}
			}
		});

		JButton btnNewButton = new JButton();
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNewButton.setUI(ViewTools.getBasicButtonUI("/images/userLogin/zxh.png"));
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBounds(820, 12, 28, 28);
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
		button_1.setBounds(860, 12, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HintDialog.startExpiringHint(HintDialog.quitAssist, "确定要退出智能云购助手吗？");
			}
		});

		JLabel lblV = new JLabel(Config.yunGouAssistVersions);
		lblV.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblV.setForeground(Color.WHITE);
		lblV.setBounds(130, 20, 49, 20);
		contentPane.add(lblV);

		JLabel lblhttpygyuntengkejicn = new JLabel(Config.assistTitle);
		lblhttpygyuntengkejicn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblhttpygyuntengkejicn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistTitleUrl);
			}
		});
		lblhttpygyuntengkejicn.setForeground(Color.WHITE);
		lblhttpygyuntengkejicn.setFont(new Font("微软雅黑", Font.BOLD, 14));
		lblhttpygyuntengkejicn.setBounds(350, 20, 197, 20);
		contentPane.add(lblhttpygyuntengkejicn);

		JLabel lblqq = new JLabel(Config.assistQQqun);
		lblqq.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblqq.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistQQqunUrl);
			}
		});
		lblqq.setForeground(Color.WHITE);
		lblqq.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblqq.setBounds(190, 20, 150, 20);
		contentPane.add(lblqq);

		JLabel lblqqcom = new JLabel("欢迎 " + Config.userName);
		lblqqcom.setHorizontalAlignment(SwingConstants.RIGHT);
		lblqqcom.setFont(new Font("微软雅黑", Font.BOLD, 12));
		lblqqcom.setBounds(559, 23, 168, 18);
		contentPane.add(lblqqcom);

		label = new JLabel();
		label.setFont(new Font("微软雅黑", Font.BOLD, 12));
		label.setBounds(730, 23, 96, 18);
		contentPane.add(label);

		setListening();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				AssistServer.getUserExpirationTime(foreknowInterface);
			}
		}, 0, 2 * 60 * 1000);

	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("ExpirationTime")) {
					Config.isAssistOverdue = false;
					label.setText(text);
				} else if (FrameName.equals("loginError")) {
					if (timer != null) {
						timer.cancel();
					}
					HintDialog.startExpiringHint(HintDialog.loginError, text);
					MainJFrame.this.removeAll();
					MainJFrame.this.setVisible(false);
				} else if (FrameName.equals("loginErrorOverdue")) {
					Config.isAssistOverdue = true;
					if (timer != null) {
						timer.cancel();
					}
					ShowMyMenu.ShowAssistOverdue();
				}
			}
		};
	}

	public void setTabbedPaneSelectedIndex(int index) {
		tabbedPane.setSelectedIndex(index);
	}

	public Component getTabbedPaneSelected(int index) {
		return tabbedPane.getComponentAt(index);
	}
}
