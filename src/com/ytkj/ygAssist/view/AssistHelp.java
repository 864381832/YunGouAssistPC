package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.GeneralizeRecord;
import com.ytkj.ygAssist.view.myView.HintDialog;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
 * 使用说明
 */
public class AssistHelp extends JPanel {
	private static final long serialVersionUID = 1L;

	public AssistHelp() {
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(148, 148, 150), 2));
		panel.setBounds(307, 25, 560, 465);
		add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(null);
		panel_1.setBackground(new Color(200, 39, 39));
		panel_1.setBounds(2, 2, 556, 50);
		panel.add(panel_1);

		JLabel label = new JLabel("软 件 介 绍");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("微软雅黑", Font.BOLD, 24));
		label.setBounds(10, 8, 536, 30);
		panel_1.add(label);

		JLabel label_1 = new JLabel("智能监听");
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_1.setBounds(46, 129, 70, 30);
		panel.add(label_1);

		JLabel label_2 = new JLabel("能够对云购商品进行监控，绘制曲线图，并且快捷购买");
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_2.setBounds(159, 129, 344, 30);
		panel.add(label_2);

		JLabel label_3 = new JLabel("包括智能购买，手动购买，等等.. ");
		label_3.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_3.setBounds(159, 159, 312, 30);
		panel.add(label_3);

		JLabel label_4 = new JLabel("提前揭晓");
		label_4.setForeground(Color.RED);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_4.setBounds(46, 201, 70, 30);
		panel.add(label_4);

		JLabel label_5 = new JLabel("商品开奖需要三分钟的等待，此功能可以在三分钟之前");
		label_5.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_5.setBounds(159, 201, 344, 30);
		panel.add(label_5);

		JLabel label_6 = new JLabel("提前揭晓答案和中奖信息，无需等待。");
		label_6.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_6.setBounds(159, 231, 344, 30);
		panel.add(label_6);

		JLabel label_7 = new JLabel("云购提醒");
		label_7.setForeground(Color.RED);
		label_7.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_7.setBounds(46, 274, 70, 30);
		panel.add(label_7);

		JLabel label_8 = new JLabel("可以同时监控四个商品，当商品购买(已参与)人数");
		label_8.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_8.setBounds(159, 274, 330, 30);
		panel.add(label_8);

		JLabel label_9 = new JLabel("达到设置值，将会有窗口震动或声音提醒。");
		label_9.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_9.setBounds(159, 304, 312, 30);
		panel.add(label_9);

		JLabel label_10 = new JLabel("我的云购");
		label_10.setForeground(Color.RED);
		label_10.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_10.setBounds(46, 346, 70, 30);
		panel.add(label_10);

		JLabel label_11 = new JLabel("登录1元云购账户，查询账户余额、购物车、及中奖详情");
		label_11.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_11.setBounds(159, 346, 344, 30);
		panel.add(label_11);

		JLabel label_12 = new JLabel("但是能够分析历史数据,找出规律,提高成功几率。");
		label_12.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_12.setBounds(159, 418, 312, 30);
		panel.add(label_12);

		JLabel label_13 = new JLabel("本软件只是辅助作用,不能保证100%云购成功，");
		label_13.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_13.setBounds(159, 388, 330, 30);
		panel.add(label_13);

		JLabel label_14 = new JLabel("软件声明");
		label_14.setForeground(Color.RED);
		label_14.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_14.setBounds(46, 388, 70, 30);
		panel.add(label_14);

		JLabel label_15 = new JLabel("请点击查看智能云购助手软件说明");
		label_15.setForeground(Color.RED);
		label_15.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_15.setBounds(159, 59, 285, 30);
		panel.add(label_15);
		label_15.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_15.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistExplainTextUrl);
			}
		});

		JLabel label_16 = new JLabel("软件说明");
		label_16.setForeground(Color.RED);
		label_16.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_16.setBounds(46, 59, 84, 30);
		panel.add(label_16);

		JLabel label_17 = new JLabel("请点击查看智能云购助手教程视频");
		label_17.setForeground(Color.RED);
		label_17.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_17.setBounds(159, 89, 285, 30);
		panel.add(label_17);
		label_17.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_17.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistHelpVideoUrl);
			}
		});

		JLabel label_29 = new JLabel("教程视频");
		label_29.setForeground(Color.RED);
		label_29.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_29.setBounds(46, 87, 84, 30);
		panel.add(label_29);

		JButton button_4 = new JButton();
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_4.setUI(ViewTools.getBasicButtonUI("/images/ie.png"));
		button_4.setBounds(419, 62, 25, 25);
		panel.add(button_4);

		JButton button_5 = new JButton();
		button_5.setUI(ViewTools.getBasicButtonUI("/images/ie.png"));
		button_5.setBounds(419, 90, 25, 25);
		panel.add(button_5);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new LineBorder(new Color(148, 148, 150), 2));
		panel_2.setBounds(25, 279, 257, 211);
		add(panel_2);

		JLabel label_18 = new JLabel("联系我们");
		label_18.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_18.setBounds(98, 17, 75, 20);
		panel_2.add(label_18);

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
		panel_2.add(label_19);

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
		label_20.setBounds(18, 152, 223, 25);
		panel_2.add(label_20);

		JLabel label_21 = new JLabel("当前版本：" + Config.yunGouAssistVersions);
		label_21.setForeground(Color.RED);
		label_21.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_21.setBounds(18, 180, 147, 18);
		panel_2.add(label_21);

		JLabel label_27 = new JLabel(Config.heziQQqun);
		label_27.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_27.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		label_27.setBounds(18, 76, 233, 25);
		label_27.setForeground(Color.RED);
		label_27.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		panel_2.add(label_27);

		JButton button_2 = new JButton();
		button_2.setBounds(220, 49, 21, 21);
		panel_2.add(button_2);
		button_2.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/qqqun.png"));

		JButton button_3 = new JButton();
		button_3.setBounds(220, 80, 21, 21);
		panel_2.add(button_3);
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
			label_22.setBounds(18, 104, 223, 25);
			panel_2.add(label_22);
			JButton button_9 = new JButton();
			button_9.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/qqqun.png"));
			button_9.setBounds(220, 106, 21, 21);
			panel_2.add(button_9);
		}

		if (!Config.hezuoQQ.equals(" ")) {
			JButton button_10 = new JButton();
			button_10.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qq_logo.png"));
			button_10.setBounds(220, 134, 21, 21);
			panel_2.add(button_10);

			JLabel label_26 = new JLabel("收货联系QQ：" + Config.hezuoQQ);
			label_26.setForeground(Color.RED);
			label_26.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			label_26.setBounds(18, 130, 223, 25);
			label_26.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_26.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.hezuoQQ));
				}
			});
			panel_2.add(label_26);
		}

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new LineBorder(new Color(148, 148, 150), 2));
		panel_3.setBounds(25, 25, 257, 232);
		add(panel_3);

		JLabel label_23 = new JLabel("软件续费（59/月）");
		label_23.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_23.setBounds(73, 16, 140, 20);
		panel_3.add(label_23);

		JLabel label_24 = new JLabel("智云助手 续费③：910712801");
		label_24.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_24.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://wpa.qq.com/msgrd?v=3&uin=910712801&site=qq&menu=yes");
			}
		});

		JLabel label_28 = new JLabel("智云助手 续费①：" + Config.renewQQ);
		label_28.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_28.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.renewQQ));
			}
		});
		label_28.setToolTipText("点击进入QQ联系");
		label_28.setForeground(Color.RED);
		label_28.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_28.setBounds(20, 47, 203, 25);
		panel_3.add(label_28);

		JLabel label_25 = new JLabel("智云助手 续费②：1277032935");
		label_25.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_25.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://wpa.qq.com/msgrd?v=3&uin=1277032935&site=qq&menu=yes");
			}
		});
		label_25.setToolTipText("点击进入QQ联系");
		label_25.setForeground(Color.RED);
		label_25.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_25.setBounds(20, 87, 203, 25);
		panel_3.add(label_25);
		label_24.setToolTipText("点击进入QQ联系");
		label_24.setForeground(Color.RED);
		label_24.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_24.setBounds(20, 129, 203, 25);
		panel_3.add(label_24);

		JButton btnNewButton = new JButton();
		btnNewButton.setBounds(222, 49, 21, 21);
		panel_3.add(btnNewButton);
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNewButton.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qq_logo.png"));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.renewQQ));
			}
		});

		JButton button = new JButton();
		button.setBounds(222, 89, 21, 21);
		panel_3.add(button);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qq_logo.png"));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://wpa.qq.com/msgrd?v=3&uin=1277032935&site=qq&menu=yes");
			}
		});

		JButton button_1 = new JButton();
		button_1.setBounds(222, 132, 21, 21);
		panel_3.add(button_1);
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qq_logo.png"));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://wpa.qq.com/msgrd?v=3&uin=910712801&site=qq&menu=yes");
			}
		});

		JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setBounds(19, 166, 90, 22);
		panel_3.add(btnNewButton_1);
		btnNewButton_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNewButton_1.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/addQQ.png"));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.assistQQqunUrl);
			}
		});

		JButton button_6 = new JButton();
		button_6.setBounds(133, 166, 90, 22);
		button_6.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_6.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/addQQ2.png"));
		button_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		panel_3.add(button_6);

		JButton button_7 = new JButton("推广邀请");
		button_7.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_7.addMouseListener(new MouseAdapter() {
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
		button_7.setForeground(Color.WHITE);
		button_7.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_7.setFocusPainted(false);
		button_7.setBorder(null);
		button_7.setBackground(Color.RED);
		button_7.setBounds(17, 196, 102, 28);
		panel_3.add(button_7);

		JButton button_8 = new JButton("我的邀请记录");
		button_8.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				GeneralizeRecord.startUserLogin();
			}
		});
		button_8.setForeground(Color.WHITE);
		button_8.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button_8.setFocusPainted(false);
		button_8.setBorder(null);
		button_8.setBackground(Color.RED);
		button_8.setBounds(130, 196, 110, 28);
		panel_3.add(button_8);
	}
}
