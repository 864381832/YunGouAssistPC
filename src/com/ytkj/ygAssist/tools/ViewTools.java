package com.ytkj.ygAssist.tools;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.basic.BasicButtonUI;

import com.ytkj.ygAssist.view.myView.ExpiringHint;

public class ViewTools {
	private static int mx, my, jfx, jfy;

	public static void windowShake(Component comp) {
		new Thread(new Runnable() {
			public void run() {
				int x = comp.getX();
				int y = comp.getY();
				for (int i = 0; i < 16; i++) {
					if ((i & 1) == 0) {
						x += 3;
						y += 3;
					} else {
						x -= 3;
						y -= 3;
					}
					comp.setLocation(x, y);
					try {
						Thread.sleep(30);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void windowMove(Component comp) {
		comp.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				comp.setLocation(jfx + (e.getXOnScreen() - mx), jfy + (e.getYOnScreen() - my));
			}
		});
		comp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mx = e.getXOnScreen();
				my = e.getYOnScreen();
				jfx = comp.getX();
				jfy = comp.getY();
			}
		});
	}

	public static BasicButtonUI getBasicButtonUI(String picPath) {
		return new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				g.drawImage(new ImageIcon(ExpiringHint.class.getResource(picPath)).getImage(), 0, 0, c.getWidth(),
						c.getHeight(), c);
			}
		};
	}

	public static BasicButtonUI getBasicButtonUIByUrl(String picPath) {
		return new BasicButtonUI() {
			@Override
			protected void installDefaults(AbstractButton b) {
				LookAndFeel.installProperty(b, "opaque", Boolean.FALSE);
			}

			@Override
			public void paint(Graphics g, JComponent c) {
				try {
					g.drawImage(new ImageIcon(new URL(picPath))
							.getImage(), 0, 0, c.getWidth(), c.getHeight(), c);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		};
	}
}
