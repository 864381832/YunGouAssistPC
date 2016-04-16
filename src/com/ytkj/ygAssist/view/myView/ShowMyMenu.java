package com.ytkj.ygAssist.view.myView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ytkj.ygAssist.server.GetGoodsInfo;
import com.ytkj.ygAssist.server.util.HttpGetUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.view.Foreknow;
import com.ytkj.ygAssist.view.IntelligentMonitoring;
import com.ytkj.ygAssist.view.MainJFrame;
import com.ytkj.ygAssist.view.NowWinning;
import com.ytkj.ygAssist.view.TrendChart;

public class ShowMyMenu {
	/*
	 * 显示商品ID菜单
	 */
	public static void ShowGoodsIDMenu(JTextField jTextField) {
		JPopupMenu jPopupMenu = new JPopupMenu();
		JMenu jMenu = new JMenu("更多");
		jMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		ConcurrentHashMap<String, String> goodsMap = CacheData.getGoodsNameCacheDate();
		int index = 0;
		for (String goodsId : goodsMap.keySet()) {
			String[] gStrings = CacheData.getGoodsInfoCacheDate(goodsId);
			String goodsName = "0".equals(gStrings[3]) ? gStrings[1] : "(限购)" + gStrings[1];
			JMenuItem goodsIDMenuItem = new JMenuItem(goodsId + "    " + goodsName);
			goodsIDMenuItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			goodsIDMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// System.out.println("选择了商品：" + goodsId + "---" +
					// goodsMap.get(goodsId));
					jTextField.setText(goodsId);
				}
			});
			if (index == 30) {
				jPopupMenu.add(jMenu);
			}
			if (index < 30) {
				jPopupMenu.add(goodsIDMenuItem);
				index++;
			} else {
				jMenu.add(goodsIDMenuItem);
			}
		}
		JMenuItem goodsIDMenuItem = new JMenuItem("编辑常用商品ID");
		goodsIDMenuItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		goodsIDMenuItem.setForeground(Color.red);
		goodsIDMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditGoodsInfoFrame.startUserLogin();
			}
		});
		jPopupMenu.add(goodsIDMenuItem);
		jPopupMenu.show(jTextField, 0, jTextField.getHeight());
	}

	/*
	 * 显示商品最近期数菜单
	 */
	public static void ShowGoodsPeriodMenu(JTextField jTextField, String goodsID) {
		JPopupMenu jPopupMenu = new JPopupMenu();
		if (CacheData.getGoodsNameCacheDate(goodsID) == null) {
			GetGoodsInfo.getGoodsInfoByGoodsID(goodsID);
		}
		String codeID = CacheData.getGoodsInfoCacheDate(goodsID)[2];
		try {
			String text[] = GetGoodsInfo.shopCartNew(codeID, HttpGetUtil.createHttpClient());
			int period = Integer.parseInt(text[1]);
			for (int i = 1; i < 9; i++) {
				String periodText = "" + (period - i);
				JMenuItem goodsIDMenuItem = new JMenuItem(periodText);
				goodsIDMenuItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
				goodsIDMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jTextField.setText(periodText);
					}
				});
				jPopupMenu.add(goodsIDMenuItem);
			}
		} catch (Exception e) {
			JMenuItem goodsIDMenuItem = new JMenuItem("请检查商品ID");
			goodsIDMenuItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			jPopupMenu.add(goodsIDMenuItem);
		}
		jPopupMenu.show(jTextField, 10, jTextField.getHeight());
	}

	/*
	 * 显示右键菜单
	 */
	public static void ShowRightClickMenu(JTextField jTextField, int x, int y) {
		JPopupMenu jPopupMenu = new JPopupMenu();// 弹出菜单
		JMenuItem copy = new JMenuItem("复制");
		copy.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		JMenuItem paste = new JMenuItem("粘贴");
		paste.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		JMenuItem cut = new JMenuItem("剪切");
		cut.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextField.copy();
			}
		});
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextField.paste();
			}
		});
		cut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextField.cut();
			}
		});
		copy.setEnabled(jTextField.getSelectedText() != null);
		cut.setEnabled(jTextField.getSelectedText() != null);
		boolean b = false;
		Clipboard clipboard = jTextField.getToolkit().getSystemClipboard();
		Transferable content = clipboard.getContents(jTextField);
		try {
			if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
				b = true;
			}
		} catch (Exception e) {
		}
		paste.setEnabled(b);
		jPopupMenu.add(copy);
		jPopupMenu.add(paste);
		jPopupMenu.add(cut);
		jPopupMenu.show(jTextField, x, y);
	}

	/*
	 * 显示商品推荐右键菜单
	 */
	public static void ShowGoodsRightClickMenu(Component parentComponent, String goodsID, String goodsName,
			String period, int x, int y) {
		JPopupMenu jPopupMenu = new JPopupMenu();// 弹出菜单
		String[] jMenuItemName = new String[] { "复制商品ID   " + goodsID, "复制商品名    " + goodsName, "加入智能监听", "加入提前揭晓",
				"加入走势分析", "加入马上开奖", "查看云购官网：" + goodsName };
		JMenuItem[] jMenuItem = new JMenuItem[jMenuItemName.length];

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // 获得系统剪贴板
		for (int i = 0; i < jMenuItemName.length; i++) {
			jMenuItem[i] = new JMenuItem(jMenuItemName[i]);
			jMenuItem[i].setFont(new Font("微软雅黑", Font.PLAIN, 14));
			int i1 = i;
			jMenuItem[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (i1 == 0) {
						clipboard.setContents(new StringSelection(goodsID), null);
					} else if (i1 == 1) {
						clipboard.setContents(new StringSelection(goodsName), null);
					} else if (i1 == 2) {
						// System.out.println("加入智能监控" + goodsID);
						((IntelligentMonitoring) MainJFrame.getMainJFrame().getTabbedPaneSelected(1))
								.setTextFieldContent(goodsID);
						MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(1);
					} else if (i1 == 3) {
						// System.out.println("加入提前揭晓" + goodsID);
						((Foreknow) MainJFrame.getMainJFrame().getTabbedPaneSelected(2)).setTextFieldContent(goodsID,
								period);
						MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(2);
					} else if (i1 == 4) {
						// System.out.println("加入走势分析" + goodsID);
						((TrendChart) MainJFrame.getMainJFrame().getTabbedPaneSelected(3)).setTextFieldContent(goodsID);
						MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(3);
					} else if (i1 == 5) {
						// System.out.println("加入马上开奖" + goodsID);
						((NowWinning) MainJFrame.getMainJFrame().getTabbedPaneSelected(4)).setTextFieldContent(goodsID,
								period);
						MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(4);
					} else if (i1 == 6) {
						HttpGetUtil.openBrowseURL("http://www.1yyg.com/products/" + goodsID + ".html");
					}
				}
			});
			jPopupMenu.add(jMenuItem[i]);
		}
		jPopupMenu.show(parentComponent, x, y);
	}

	/*
	 * 显示提前揭晓菜单
	 */
	public static void ShowForeknowRightClickMenu(Component parentComponent, String[] goodsInfo, int x, int y) {
		JPopupMenu jPopupMenu = new JPopupMenu();// 弹出菜单
		String[] jMenuItemName = new String[] { "复制商品ID", "复制幸运码", "复制中奖人", "打开云购官网查看" };
		JMenuItem[] jMenuItem = new JMenuItem[jMenuItemName.length];
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // 获得系统剪贴板
		for (int i = 0; i < jMenuItemName.length; i++) {
			int i2 = i;
			jMenuItem[i] = new JMenuItem(jMenuItemName[i] + "   " + goodsInfo[i]);
			jMenuItem[i].setFont(new Font("微软雅黑", Font.PLAIN, 14));
			jMenuItem[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (i2 == 3) {
						String[] goodsInfo2 = CacheData.getSelectCacheDate(goodsInfo[0], goodsInfo[3]);
						if (goodsInfo2 == null) {
							HttpGetUtil.openBrowseURL("http://www.1yyg.com/products/" + goodsInfo[0] + ".html");
						} else {
							HttpGetUtil.openBrowseURL("http://www.1yyg.com/lottery/" + goodsInfo2[7] + ".html");
						}
					} else {
						clipboard.setContents(new StringSelection(goodsInfo[i2]), null);
					}
				}
			});
			jPopupMenu.add(jMenuItem[i]);
		}
		jPopupMenu.show(parentComponent, x, y);
	}

	/*
	 * 显示账户过期对话框
	 */
	public static void ShowAssistOverdue() {
		ExpiringHint.startExpiringHint(1);
		// JOptionPane.showMessageDialog(MainJFrame.getMainJFrame(),
		// "账户已过期，请找管理员续费，谢谢。", "提示",
		// JOptionPane.WARNING_MESSAGE);
		// MainJFrame.getMainJFrame().setTabbedPaneSelectedIndex(7);
	}

}
