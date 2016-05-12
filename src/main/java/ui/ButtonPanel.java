/**@author:idevcod@163.com
 * @date:2015年9月13日下午12:42:57
 * @description:<TODO>
 */
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private JTabbedPane tabbedPane;

	public ButtonPanel(final JTabbedPane tabbedPane) {
		if (tabbedPane == null) {
			String errMsg = "tabbedPanel is null";
			logger.error(errMsg);
			throw new NullPointerException(errMsg);
		}

		this.tabbedPane = tabbedPane;

		setLayout(new FlowLayout());

		JLabel label = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8029090022485951513L;

			@Override
			public String getText() {
				int index = tabbedPane.indexOfTabComponent(ButtonPanel.this);
				if (index != -1) {
					return tabbedPane.getTitleAt(index);
				}

				return null;
			}
		};

		add(label);

		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		JButton button = new TabButton();
		add(button);
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	class TabButton extends JButton implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TabButton() {
			int size = 16;
			setPreferredSize(new Dimension(size, size));
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			addMouseListener(buttonMouseListener);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			int i = tabbedPane.indexOfTabComponent(ButtonPanel.this);
			if (i != -1) {
				tabbedPane.remove(i);
			}
		}

		// paint the cross
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.MAGENTA);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
