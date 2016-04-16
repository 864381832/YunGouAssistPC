package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.AbstractButton;
import javax.swing.JButton;

import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.main.YunGouLoginServer;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.server.util.MyStringUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.HintDialog;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Cursor;

/*
 * 提前揭晓
 */
public class NowWinning extends JPanel {
	private static final long serialVersionUID = 1L;
	private long beginTime = 0;
	private Thread thread = null;
	private JFrameListeningInterface foreknowInterface;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel hintLabel;
	private JCheckBox checkBox;
	private JLabel lblid_1;
	private JButton button;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel lblqq;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(
				Toolkit.getDefaultToolkit().getImage(NowWinning.class.getResource("/images/mainJFrame/watermark2.png")),
				0, 0, getWidth(), getHeight(), null);
	}

	public NowWinning() {
		setLayout(null);
		JLabel lblid = new JLabel("商品ID");
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		lblid.setBounds(112, 69, 49, 20);
		add(lblid);

		textField = new JTextField("22704");
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}
		});
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(169, 63, 100, 30);
		textField.setColumns(10);
		add(textField);
		JButton comboBox_3 = new JButton();
		comboBox_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textField.isEnabled()) {
					ShowMyMenu.ShowGoodsIDMenu(textField);
				}
			}
		});
		comboBox_3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		comboBox_3.setBorderPainted(false);
		comboBox_3.setBounds(textField.getWidth() - 18, 0, 18, 28);
		comboBox_3.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(IntelligentMonitoring.class.getResource("/images/mainJFrame/xialaanniu.png"))
						.getImage(), 0, 2, c.getWidth() - 2, c.getHeight() - 2, c);
			}
		});
		textField.add(comboBox_3);

		JLabel label = new JLabel("期数");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label.setBounds(312, 69, 33, 15);
		add(label);

		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField_1, arg0.getX(), arg0.getY());
				}
			}
		});
		textField_1.setBounds(359, 63, 100, 30);
		textField_1.setColumns(10);
		add(textField_1);
		JButton comboBox_4 = new JButton();
		comboBox_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textField_1.isEnabled()) {
					ShowMyMenu.ShowGoodsPeriodMenu(textField_1, textField.getText());
				}
			}
		});
		comboBox_4.setCursor(new Cursor(Cursor.HAND_CURSOR));
		comboBox_4.setBorderPainted(false);
		comboBox_4.setBounds(textField.getWidth() - 18, 0, 18, 28);
		comboBox_4.setUI(new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(IntelligentMonitoring.class.getResource("/images/mainJFrame/xialaanniu.png"))
						.getImage(), 0, 2, c.getWidth() - 2, c.getHeight() - 2, c);
			}
		});
		textField_1.add(comboBox_4);

		button = new JButton("查  询");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(Color.RED);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("查  询")) {
					if (Config.isAssistOverdue) {
						ShowMyMenu.ShowAssistOverdue();
						return;
					}
					selectButton();
				} else {
					button.setText("查  询");
					lblid_1.setText("请输入需要查询的商品ID和期数");
					hintLabel.setText("");
					thread.stop();
					thread = null;
				}
			}
		});
		button.setBounds(660, 64, 100, 30);
		add(button);

		hintLabel = new JLabel("");
		hintLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
		hintLabel.setForeground(Color.RED);
		hintLabel.setBounds(647, 98, 224, 30);
		add(hintLabel);

		checkBox = new JCheckBox("验证自己中奖");
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (checkBox.isSelected()) {
					if (!YunGouLoginServer.getUserServer().getIsLogin()) {
						HintDialog.startExpiringHint(HintDialog.yungouLogin, "您还未登录1元云购账户，请先登录后再使用验证中奖功能，谢谢！！！");
						checkBox.setSelected(false);
					}
				}
			}
		});
		checkBox.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		LookAndFeel.installProperty(checkBox, "opaque", Boolean.FALSE);
		checkBox.setBounds(499, 64, 120, 30);
		add(checkBox);

		lblid_1 = new JLabel("请输入需要查询的商品ID和期数");
		lblid_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblid_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textField.getText().equals("")) {
					HttpGetUtil.openBrowseURL("http://www.1yyg.com/products/" + textField.getText() + ".html");
				}
			}
		});
		lblid_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		lblid_1.setForeground(Color.RED);
		lblid_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblid_1.setBounds(90, 129, 701, 30);
		add(lblid_1);

		label_1 = new JLabel("幸运云购码（点击即可复制）");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.BLACK);
		label_1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_1.setBounds(90, 188, 701, 30);
		add(label_1);

		label_2 = new JLabel("请点击查询按钮");
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (label_2.getText().contains("(已复制)")) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // 获得系统剪贴板
					clipboard.setContents(new StringSelection(label_2.getText().substring(0, 8)), null);
				} else {
					try {
						Integer.parseInt(label_2.getText());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // 获得系统剪贴板
						clipboard.setContents(new StringSelection(label_2.getText()), null);
						label_2.setText(label_2.getText() + "(已复制)");
					} catch (Exception e) {
					}
				}
			}
		});
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(Color.RED);
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 40));
		label_2.setBounds(90, 217, 701, 80);
		add(label_2);

		label_3 = new JLabel();
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setForeground(Color.RED);
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_3.setBounds(90, 302, 701, 30);
		add(label_3);

		label_4 = new JLabel("输入需要查询的商品ID和期数即可马上查出该期的幸运云购码");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setForeground(Color.RED);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_4.setBounds(53, 18, 794, 30);
		add(label_4);

		label_5 = new JLabel(Config.heziQQqun + "（点击加入）");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setForeground(Color.BLACK);
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_5.setBounds(90, 396, 701, 30);
		label_5.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HttpGetUtil.openBrowseURL(Config.heziQQqunUrl);
			}
		});
		add(label_5);
		if (!Config.hezuoQQqun.equals(" ")) {
			JLabel label_6 = new JLabel(Config.hezuoQQqun);
			label_6.setHorizontalAlignment(SwingConstants.CENTER);
			label_6.setForeground(Color.BLACK);
			label_6.setFont(new Font("微软雅黑", Font.BOLD, 16));
			label_6.setBounds(169, 451, 260, 30);
			label_6.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label_6.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(Config.hezuoQQqunUrl);
				}
			});
			add(label_6);
		}
		if (!Config.hezuoQQ.equals(" ")) {
			lblqq = new JLabel("收货联系QQ：" + Config.hezuoQQ);
			lblqq.setHorizontalAlignment(SwingConstants.CENTER);
			lblqq.setForeground(Color.BLACK);
			lblqq.setFont(new Font("微软雅黑", Font.BOLD, 16));
			lblqq.setBounds(429, 451, 292, 30);
			lblqq.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lblqq.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HttpGetUtil.openBrowseURL(MyStringUtil.getRelationQQUrl(Config.hezuoQQ));
				}
			});
			add(lblqq);
		}

		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (button.getText().equals("停止查询") && text[0].equals(textField.getText().trim())) {
					if (!text[2].equals("")) {
						label_2.setText(text[2]);
						if (checkBox.isSelected()) {
							if (YunGouLoginServer.getUserServer().getIsLogin()) {
								new Thread(new Runnable() {
									public void run() {
										try {
											if (YunGouLoginServer.getUserServer().getUserBuyDetail(text[7], text[2])) {
												HintDialog.startExpiringHint("恭喜您在\n"
														+ CacheData.getGoodsNameCacheDate(textField.getText().trim())
														+ "\n在第" + textField_1.getText().trim() + "期时购买中奖了\n中奖码为"
														+ text[2]);
												label_3.setText("恭喜您中奖了");
											} else {
												label_3.setText("很遗憾，您未中奖，继续加油，好运总会到来的！！！");
											}
										} catch (Exception e) {
											label_3.setText("验证好像出问题了，请重试。");
										}
									}
								}).start();
							} else {
								checkBox.setSelected(false);
							}
						}
						hintLabel.setText("查询用时：" + (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
						setButtonEnabled(true);
						button.setText("查  询");
					}
				}
			}

			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("setGoodsName")) {
					lblid_1.setText(text + "（第" + textField_1.getText().trim() + "期）");
				} else if (FrameName.equals("webError") || FrameName.equals("selectError")) {
					hintLabel.setText("查询超时，请稍后重试，谢谢。");
					setButtonEnabled(true);
					button.setText("查  询");
				} else if (FrameName.equals("setGoodsNameError")) {
					button.setText("查  询");
					setButtonEnabled(true);
					hintLabel.setText("未查到此商品");
				} else if (FrameName.equals("goodsoldOut")) {
					button.setText("查  询");
					setButtonEnabled(true);
					hintLabel.setText("该商品已经下降了");
				}
			}
		};
	}

	private void selectButton() {
		if (textField.getText().equals("")) {
			hintLabel.setText("请输入商品ID");
			button.setText("查  询");
			return;
		}
		if (textField_1.getText().equals("")) {
			hintLabel.setText("请输入查询期数");
			button.setText("查  询");
			return;
		}
		hintLabel.setText("");
		lblid_1.setText("");
		label_2.setText("请点击查询按钮");
		label_3.setText("");
		button.setText("停止查询");
		hintLabel.setText("正在查询...");
		setButtonEnabled(false);
		thread = new Thread(new Runnable() {
			public void run() {
				beginTime = System.currentTimeMillis();
				YunGouServer.setNowWinning(textField.getText().trim(), textField_1.getText().trim(), null,
						foreknowInterface);
			}
		});
		thread.start();
	}

	private void setButtonEnabled(boolean isEnabled) {
		textField.setEnabled(isEnabled);
		textField_1.setEnabled(isEnabled);
		checkBox.setEnabled(isEnabled);
	}

	public void setTextFieldContent(String goodsID, String period) {
		textField.setText(goodsID);
		textField_1.setText(period);
	}
}
