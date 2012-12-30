package elf.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * Status bar with timed messages and scroll bar.
 * @author casse
 */
public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int WAIT = 5;

	JLabel label = new JLabel(" ");
	int wait;
	Timer timer = new Timer(WAIT * 1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			resetMessage();
		}
	});
	
	public StatusBar() {
		this(WAIT);
	}
	
	public StatusBar(int wait) {
		this.wait = wait;
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		timer.setDelay(wait * 1000);
	}
	
	public void resetMessage() {
		label.setText(" ");
		label.repaint();
		timer.stop();
	}
	
	public void setMessage(String message) {
		timer.stop();
		timer.start();
		label.setText(message);
		label.repaint();
	}
	
	public void setBlockingMessage(String message) {
		timer.stop();
		label.setText(message);
		label.repaint();
	}
	
}
