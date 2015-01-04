/*
 * ElfCore library
 * Copyright (c) 2014 - Hugues Cassé <hugues.casse@laposte.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package elf.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;

import elf.ui.I18N;

/**
 * Swing implementation of status bar.
 * @author casse
 */
public class StatusBar extends Component implements elf.ui.StatusBar {
	private int delay = WAIT;
	private Box box;
	private JLabel label;
	private Timer timer = new Timer(WAIT * 1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			clear();
		}
	});
	private LinkedList<elf.swing.TextInfo>
		left = new LinkedList<elf.swing.TextInfo>(),
		right = new LinkedList<elf.swing.TextInfo>();
	private String message;
	
	@Override
	public void clear() {
		this.message = "";
		if(box != null) {
			timer.stop();
			label.setText(" ");
			label.repaint();			
		}
	}
	
	@Override
	public void set(String message) {
		this.message = message;
		if(box != null) {
			timer.stop();
			if(delay != FOREVER) {
				timer.setDelay(delay * 1000); 
				timer.start();			
			}
			label.setText(message);
			label.repaint();			
		}
	}
	
	@Override
	public void info(String message) {
		set(message);
	}

	@Override
	public void warn(String message) {
		set(I18N.STD.t("WARNING:") + message);
	}

	@Override
	public void error(String message) {
		set(I18N.STD.t("ERROR:") + message);
	}

	@Override
	public void panic(String message) {
		set(I18N.STD.t("FATAL:") + message);
	}

	@Override
	public void begin(String title) {
	}

	@Override
	public boolean end() {
		return false;
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public TextInfo addTextInfo(String format, int align) {
		elf.swing.TextInfo info = new elf.swing.TextInfo(format);
		if(align == LEFT)
			left.add(info);
		else
			right.add(info);
		return info;
	}

	@Override
	public JComponent getComponent() {
		if(box == null) {
			box = Box.createHorizontalBox();
			for(elf.swing.TextInfo info: left)
				box.add(info.getComponent());
			label = new JLabel();
			label.setAlignmentX(0);
			label.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			box.add(label);
			if(message != null)
				set(message);
			label.setPreferredSize(new Dimension(Short.MAX_VALUE, 0));
			label.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
			for(elf.swing.TextInfo info: right) {
				box.add(Box.createHorizontalStrut(4));
				JComponent component = info.getComponent(); 
				box.add(component);
				component.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			}
		}
		return box;
	}

	@Override
	public void dispose() {
		super.dispose();
		box = null;
		label = null;
		message = null;
	}
	
}
