package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.ytkj.ygAssist.main.YunGouServer;
import com.ytkj.ygAssist.tools.Config;
import com.ytkj.ygAssist.tools.JFrameListeningInterface;
import com.ytkj.ygAssist.view.myView.ShowMyMenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.Font;
import java.awt.Graphics;

public class YunGouRemindSubJPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String goodsName;// 商品名称
	private JFrameListeningInterface foreknowInterface;
	private JTextField textField;
	private JTextField textField_1;
	private JProgressBar progressBar;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JButton button;
	private JLabel label_8;

	/**
	 * Create the
	 */
	public YunGouRemindSubJPanel(int panelIndex) {
		setBorder(new LineBorder(new Color(148, 148, 150), 2));
		setLayout(null);

		JLabel label = new JLabel("商品 " + (panelIndex + 1) + " 监控");
		label.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label.setForeground(Color.BLACK);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(155, 8, 124, 30);
		add(label);

		JLabel lblNewLabel = new JLabel("商品ID");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(10, 48, 54, 30);
		add(lblNewLabel);

		textField = new JTextField();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {// 右键点击
					ShowMyMenu.ShowRightClickMenu(textField, arg0.getX(), arg0.getY());
				}
			}
		});
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField.setBounds(74, 48, 77, 30);
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
						.getImage(), 0, 2, c.getWidth() - 2, c.getHeight() - 3, c);
			}
		});
		textField.add(comboBox_3);

		JLabel label_1 = new JLabel("定点");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_1.setForeground(Color.BLACK);
		label_1.setBounds(163, 48, 33, 30);
		add(label_1);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField_1.setToolTipText("");
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setBounds(206, 48, 75, 30);
		add(textField_1);
		textField_1.setColumns(10);

		button = new JButton("开始监控");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(Color.RED);
		button.setBorder(null);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (button.getText().equals("开始监控")) {
					if (Config.isAssistOverdue) {
						ShowMyMenu.ShowAssistOverdue();
						return;
					}
					if (textField.getText().equals("") && textField_1.getText().equals("")) {
						label_8.setText("请输入监控商品ID（定点可不填）");
					} else {
						try {
							textField.setEnabled(false);
							textField_1.setEnabled(false);
							button.setText("停止监控");
							YunGouServer.setYunGouRemind(true, panelIndex, textField.getText().trim(),
									textField_1.getText().trim(), foreknowInterface);
						} catch (Exception e) {
							textField.setEnabled(true);
							textField_1.setEnabled(true);
							button.setText("开始监控");
							label_8.setText("无法找到该商品或网络异常");
						}
					}
				} else {
					button.setText("正在停止监控");
					button.setEnabled(false);
					YunGouServer.setYunGouRemind(false, panelIndex, null, null, foreknowInterface);
				}
			}
		});
		button.setBounds(293, 48, 100, 30);
		add(button);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setFont(new Font("微软雅黑", Font.BOLD, 14));
		progressBar.setBackground(Color.BLACK);
		progressBar.setForeground(Color.RED);
		progressBar.setMaximum(5188);
		progressBar.setBounds(32, 113, 347, 30);
		add(progressBar);

		label_2 = new JLabel("0");
		label_2.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_2.setForeground(new Color(255, 0, 0));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(32, 155, 76, 20);
		add(label_2);

		label_3 = new JLabel("0");
		label_3.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_3.setForeground(new Color(255, 0, 0));
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(171, 155, 76, 20);
		add(label_3);

		label_4 = new JLabel("0");
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label_4.setForeground(new Color(255, 0, 0));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(303, 155, 76, 20);
		add(label_4);

		JLabel label_5 = new JLabel("已经参与人次");
		label_5.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_5.setForeground(Color.BLACK);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(21, 180, 100, 20);
		add(label_5);

		JLabel label_6 = new JLabel("总需要人次");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_6.setForeground(Color.BLACK);
		label_6.setBounds(163, 180, 92, 20);
		add(label_6);

		JLabel label_7 = new JLabel("剩余人次");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_7.setForeground(Color.BLACK);
		label_7.setBounds(293, 178, 85, 20);
		add(label_7);

		label_8 = new JLabel();
		label_8.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label_8.setFont(new Font("微软雅黑", Font.BOLD, 14));
		label_8.setForeground(new Color(255, 0, 0));
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setBounds(10, 78, 394, 30);
		add(label_8);
		setListening();
	}

	private void setListening() {
		foreknowInterface = new JFrameListeningInterface() {
			@Override
			public void setFrameListeningText(String FrameName, String[] text) {
				if (FrameName.equals("1")) {
					progressBar.setMaximum(Integer.parseInt(text[3]));
					progressBar.setValue(Integer.parseInt(text[2]));
					label_2.setText(text[2]);
					label_3.setText(text[3]);
					label_4.setText(text[4]);
					label_8.setText("(第" + text[1] + "云)" + goodsName);
				} else {
					progressBar.setValue(Integer.parseInt(text[2]));
					label_2.setText(text[2]);
					label_4.setText(text[4]);
					label_8.setText("(第" + text[1] + "云)" + goodsName);
				}
			}

			@Override
			public void setFrameText(String FrameName, String text) {
				if (FrameName.equals("setGoodsName")) {
					goodsName = text;
				} else if (FrameName.equals("playRemind")) {
					((YunGouRemind) (YunGouRemindSubJPanel.this.getParent())).startRemindEvent();
				} else if (FrameName.equals("setGoodsNameError")) {
					textField.setEnabled(true);
					textField_1.setEnabled(true);
					button.setText("开始监控");
					label_8.setText("无法找到该商品或网络异常");
				} else if (FrameName.equals("goodsoldOut")) {
					textField.setEnabled(true);
					textField_1.setEnabled(true);
					button.setText("开始监控");
					label_8.setText("（已下降）" + goodsName);
				} else if (FrameName.equals("stopRemind")) {
					progressBar.setValue(0);
					label_2.setText("0");
					label_3.setText("0");
					label_4.setText("0");
					label_8.setText("");
					button.setText("开始监控");
					textField.setEnabled(true);
					textField_1.setEnabled(true);
					button.setEnabled(true);
				}
			}
		};
	}
}
