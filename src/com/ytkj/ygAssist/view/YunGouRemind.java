package com.ytkj.ygAssist.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.tools.ViewTools;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;

/*
 * 云购提醒
 */
public class YunGouRemind extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JComboBox<String> comboBox;

	public static YunGouRemind createYunGouRemind(Component comp) {
		YunGouRemind yunGouRemind = new YunGouRemind();
		return yunGouRemind;
	}

	/**
	 * Create the panel.
	 */
	public YunGouRemind() {
		setLayout(null);

		JLabel label = new JLabel("输入商品号,设定定点,商品到达此位置时触发提醒");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setForeground(Color.RED);
		label.setBounds(22, 10, 331, 30);
		add(label);

		comboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] { "音乐+震动", "音乐", "震动" }));
		comboBox.setBackground(Color.RED);
		comboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		comboBox.setForeground(Color.RED);
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(363, 10, 100, 30);
		add(comboBox);

		textField = new JTextField();
		textField.setCursor(new Cursor(Cursor.HAND_CURSOR));
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField.setDisabledTextColor(Color.BLACK);
		textField.setEnabled(false);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setEditable(false);
		textField.setText("双击来修提醒时播放的音乐");
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
						FileNameExtensionFilter filter = new FileNameExtensionFilter(".wav", "wav");
						JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter(filter);
						int value = chooser.showOpenDialog(YunGouRemind.this);
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
						if (value == JFileChooser.APPROVE_OPTION) {
							File file = chooser.getSelectedFile();
							FilesUtil.saveFileInfo("\\RemindMusicPath.log", file.getAbsolutePath());
							textField.setText(file.getAbsolutePath());
						}
					} catch (Exception e1) {
					}
				}
			}
		});
		textField.setBounds(484, 10, 400, 30);
		add(textField);
		textField.setColumns(10);
		for (int panelIndex = 0; panelIndex < 4; panelIndex++) {
			YunGouRemindSubJPanel panel = new YunGouRemindSubJPanel(panelIndex);
			if (panelIndex == 0) {
				panel.setBounds(35, 59, 410, 210);
			} else if (panelIndex == 1) {
				panel.setBounds(455, 59, 410, 210);
			} else if (panelIndex == 2) {
				panel.setBounds(35, 279, 410, 210);
			} else if (panelIndex == 3) {
				panel.setBounds(455, 279, 410, 210);
			}
			add(panel);
		}
		String RemindMusicPath = FilesUtil.readerFileInfo("\\RemindMusicPath.log");
		if (RemindMusicPath != null) {
			textField.setText(RemindMusicPath);
		}
	}

	public void startRemindEvent() {
		new Thread(new Runnable() {
			public void run() {
				if (comboBox.getSelectedIndex() == 0) {
					ViewTools.windowShake(MainJFrame.getMainJFrame());
					palyAudio();
				} else if (comboBox.getSelectedIndex() == 1) {
					palyAudio();
				} else if (comboBox.getSelectedIndex() == 2) {
					ViewTools.windowShake(MainJFrame.getMainJFrame());
				}
			}
		}).start();
	}

	private void palyAudio() {
		if ("双击来修提醒时播放的音乐".equals(textField.getText())) {
			AudioClip ac = Applet.newAudioClip(YunGouRemind.class.getResource("/audios/remindMusic.wav"));
			ac.play();
		} else {
			try {
				File file = new File(textField.getText());
				AudioClip ac = null;
				if (!file.exists()) {
					ac = Applet.newAudioClip(YunGouRemind.class.getResource("/audios/remindMusic.wav"));
					textField.setText("双击来修提醒时播放的音乐");
				} else {
					ac = Applet.newAudioClip(file.toURL());
				}
				ac = Applet.newAudioClip(YunGouRemind.class.getResource("/audios/remindMusic.wav"));
				ac.play();
			} catch (Exception e) {
			}
		}
	}

}