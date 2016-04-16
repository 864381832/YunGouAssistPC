package com.ytkj.ygAssist.view.myView;

import javax.swing.*;
import com.ytkj.ygAssist.view.UserLogin;

import java.awt.*;
import java.net.*;
import java.io.*;

public class CheckUpdate extends JFrame {
	private String newFileName = "智能云购助手_cache.exe";
	private String updateUrl = null;

	public static void startUpdate(String updateUrl) {
		CheckUpdate checkupdate = new CheckUpdate(updateUrl);
		checkupdate.setVisible(true);
	}

	public CheckUpdate(String updateUrl) {
		this.updateUrl = updateUrl;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 3);
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserLogin.class.getResource("/images/logo.png")));
		setAutoRequestFocus(false);
		setResizable(false);
		this.setTitle("更新云购助手");
		this.setSize(219, 150);
		getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextArea msg = new JTextArea();
		msg.setFont(new Font("楷体", Font.PLAIN, 14));
		getContentPane().add(msg, BorderLayout.CENTER);
		JLabel process = new JLabel();
		getContentPane().add(process, BorderLayout.SOUTH);
		msg.append("发现新版本\n");
		// 启动更新线程
		new Check(msg, process).start();
	}

	private class Check extends Thread {
		// 标识,是否存在新的更新文件
		private JTextArea msg;
		private JLabel process;

		public Check(JTextArea msg, JLabel process) {
			this.msg = msg;
			this.process = process;
		}

		public void run() {
			URL url = null;
			InputStream is = null;
			File newFile = new File(newFileName);
			HttpURLConnection httpUrl = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			try {
				url = new URL(updateUrl);
				httpUrl = (HttpURLConnection) url.openConnection();
				httpUrl.connect();
				byte[] buffer = new byte[1024];
				int size = 0;
				is = httpUrl.getInputStream();
				bis = new BufferedInputStream(is);
				fos = new FileOutputStream(newFile);
				msg.append("正在从网络上下载新的更新文件\n");
				// 保存文件
				try {
					int flag = 0;
					int flag2 = 0;
					while ((size = bis.read(buffer)) != -1) {
						// 读取并刷新临时保存文件
						fos.write(buffer, 0, size);
						fos.flush();
						// 模拟一个简单的进度条
						if (flag2 == 99) {
							flag2 = 0;
							process.setText(process.getText() + ".");
						}
						flag2++;
						flag++;
						if (flag > 99 * 50) {
							flag = 0;
							process.setText("");
						}
					}
				} catch (Exception ex4) {
					System.out.println(ex4.getMessage());
				}
				msg.append("\n文件下载完成\n");
				// 把下载的临时文件替换原有文件
			} catch (Exception ex) {
				msg.append("文件读取错误\n");
			} finally {
				try {
					fos.close();
					bis.close();
					is.close();
					httpUrl.disconnect();
				} catch (Exception ex3) {
				}
			}
			// 启动应用程序
			try {
				msg.append("启动应用程序");
				Thread.sleep(500);
				Runtime.getRuntime().exec(newFileName);
			} catch (Exception ex5) {
			}
			// 退出更新程序
			System.exit(0);
		}
	}
}
