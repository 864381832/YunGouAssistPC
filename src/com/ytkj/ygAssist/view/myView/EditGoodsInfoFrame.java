package com.ytkj.ygAssist.view.myView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.ytkj.ygAssist.main.AssistServer;
import com.ytkj.ygAssist.server.util.FilesUtil;
import com.ytkj.ygAssist.tools.CacheData;
import com.ytkj.ygAssist.tools.ViewTools;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.JComponent;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class EditGoodsInfoFrame extends JDialog {
	private static EditGoodsInfoFrame vIPBuyDialog = null;
	private JPanel contentPane;
	private JTable table;

	public static void startUserLogin() {
		if (vIPBuyDialog == null) {
			vIPBuyDialog = new EditGoodsInfoFrame();
			vIPBuyDialog.setVisible(true);
		} else {
			vIPBuyDialog.setTableData();
			vIPBuyDialog.setVisible(true);
		}
	}

	/**
	 * Create the frame.
	 */
	public EditGoodsInfoFrame() {
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditGoodsInfoFrame.class.getResource("/images/logo.png")));
		setTitle("智能云购助手");
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 750, 489);
		// 获取屏幕大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 3, this.getWidth(),
				this.getHeight());
		ViewTools.windowMove(this);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g2d) {
				g2d.drawImage(
						Toolkit.getDefaultToolkit()
								.getImage(EditGoodsInfoFrame.class.getResource("/images/userLogin/tp1.png")),
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
				g.drawImage(new ImageIcon(EditGoodsInfoFrame.class.getResource("/images/userLogin/tc.png")).getImage(),
						0, 0, c.getWidth(), c.getHeight(), c);
			}
		});
		button_1.setBorderPainted(false);
		button_1.setBounds(712, 10, 28, 28);
		contentPane.add(button_1);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FilesUtil.editGoodsInfoFile();
				vIPBuyDialog.setVisible(false);
			}
		});

		JLabel label_4 = new JLabel("常用商品ID列表（双击可删除）");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("微软雅黑", Font.BOLD, 18));
		label_4.setBounds(238, 10, 264, 28);
		contentPane.add(label_4);

		// 定义表格列名数组
		String[] columnNames = { "商品ID", "商品名" };
		// 创建指定表格列名和表格数据的表格模型类的对象
		DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					// System.out.println("点击了"+table.getSelectedRow());
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					String goodsID = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
					CacheData.getGoodsNameCacheDate().remove(goodsID);
					tableModel.removeRow(table.getSelectedRow());
				}
			}
		});
		table.setSelectionForeground(Color.BLACK);
		table.setSelectionBackground(Color.CYAN);
		table.setSize(838, 430);
		table.setRowHeight(25);
		table.setForeground(Color.RED);
		table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 14));
		table.setBorder(null);

		// table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(0).setMaxWidth(600);
		// table.getColumnModel().getColumn(1).setPreferredWidth(50);

		// 设置table表头居中
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		// 设置table内容居中
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		scrollPane.setBorder(null);
		scrollPane.setBounds(25, 50, 699, 418);
		contentPane.add(scrollPane);
		setTableData();
	}

	private void setTableData() {
		ConcurrentHashMap<String, String> goodsMap = CacheData.getGoodsNameCacheDate();
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		for (String goodsId : goodsMap.keySet()) {
			String[] strs = new String[] { goodsId, goodsMap.get(goodsId) };
			tableModel.addRow(strs);
		}
	}
}
