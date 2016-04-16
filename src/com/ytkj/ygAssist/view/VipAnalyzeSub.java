package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.plaf.basic.BasicButtonUI;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.ViewTools;
import com.ytkj.ygAssist.view.myView.ExpiringHint;
import com.ytkj.ygAssist.view.myView.VIPBuyDialog;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class VipAnalyzeSub extends JPanel {
	public VipAnalyzeSub() {
		setLayout(null);

		JLabel label = new JLabel("续费成为会员账号才能使用智能预测功能（提升中奖率，");
		label.setForeground(Color.RED);
		label.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label.setBounds(220, 55, 400, 18);
		add(label);

		JLabel label_1 = new JLabel("升级会员请使用支付宝扫下面左边二维码进行转账（59元/月）");
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_1.setBounds(220, 88, 466, 18);
		add(label_1);

		JButton button = new JButton();
		button.setBounds(94, 216, 200, 200);
		button.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/zf.png"));
		add(button);

		JLabel lblvip = new JLabel("转账时请务必在“付款说明”中填写需要升级会员的账户");
		lblvip.setForeground(Color.RED);
		lblvip.setFont(new Font("微软雅黑", Font.BOLD, 16));
		lblvip.setBounds(220, 125, 440, 18);
		add(lblvip);

		JLabel lblqq = new JLabel("如转账后请联系客服QQ：" + Config.renewQQ + "，谢谢。");
		lblqq.setForeground(Color.RED);
		lblqq.setFont(new Font("微软雅黑", Font.BOLD, 16));
		lblqq.setBounds(220, 167, 357, 18);
		add(lblqq);

		JLabel label_2 = new JLabel("支付宝账号：18955333680      姓名：云腾");
		label_2.setForeground(Color.RED);
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_2.setBounds(46, 439, 326, 18);
		add(label_2);

		JButton button_1 = new JButton();
		button_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_1.setUI(ViewTools.getBasicButtonUI("/images/userLogin/qqjt.png"));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://wpa.qq.com/msgrd?v=3&uin=" + Config.renewQQ + "&site=qq&menu=yes");
			}
		});
		button_1.setBounds(583, 166, 77, 21);
		add(button_1);

		JLabel label_3 = new JLabel("智能预测功能可根据往期中奖数据智能分析出下期中奖位置");
		label_3.setForeground(Color.RED);
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 24));
		label_3.setBounds(141, 10, 642, 29);
		add(label_3);

		JButton button_2 = new JButton();
		button_2.setBounds(880, 54, 21, 21);
		button_2.setUI(ViewTools.getBasicButtonUI("/images/mainJFrame/qqqun.png"));
		button_2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL("http://jq.qq.com/?_wv=1027&k=2BMX1Qm");
			}
		});
		add(button_2);

		JLabel label_4 = new JLabel("点击进入" + Config.heziQQqun + "）");
		label_4.setForeground(Color.BLACK);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_4.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		label_4.setBounds(618, 55, 277, 18);
		add(label_4);

		JButton button_3 = new JButton();
		button_3.setUI(ViewTools.getBasicButtonUIByUrl(AssistServer.getHeziQQqunEwmUrl()));
		button_3.setBounds(556, 216, 200, 200);
		add(button_3);

		JLabel label_5 = new JLabel("作者亲自操盘，稳稳回血吃肉");
		label_5.setForeground(Color.RED);
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_5.setBounds(530, 428, 292, 18);
		add(label_5);

		JLabel label_6 = new JLabel("就在" + Config.heziQQqun + "（点击加入）");
		label_6.setForeground(Color.RED);
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_6.setBounds(530, 454, 345, 18);
		label_6.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		add(label_6);

	}
}
