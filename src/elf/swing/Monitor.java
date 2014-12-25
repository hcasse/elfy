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

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Monitor implementation for Swing UI.
 * @author casse
 */
public class Monitor implements elf.ui.Monitor {
	private JFrame owner;
	private String title;
	private boolean success = false, in_job = false;
	private Vector<String> msgs = new Vector<String>();
	
	/**
	 * Build a Swing monitor.
	 * @param owner		Owner window.
	 */
	public Monitor(JFrame owner) {
		this.owner = owner;
	}
	
	@Override
	public void info(String message) {
		if(in_job)
			msgs.add("INFO: " + message);
		else
			JOptionPane.showMessageDialog(owner, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void warn(String message) {
		if(in_job)
			msgs.add("WARNING: " + message);
		else
			JOptionPane.showMessageDialog(owner, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void error(String message) {
		success = false;
		if(in_job)
			msgs.add("INFO: " + message);
		else
			JOptionPane.showMessageDialog(owner, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void panic(String message) {
		JOptionPane.showMessageDialog(owner, message, "Panic", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	@Override
	public void begin(String title) {
		this.title = title;
		in_job = true;
		success = true;
		msgs.clear();
	}

	@Override
	public boolean end() {
		if(!msgs.isEmpty()) {
			// should be fixed: read-only list, only one button.
			ListDialog<String> list = new ListDialog<String>(owner, "", title, msgs, -1, "Proceed");
			list.run();
		}
		return success;
	}

}
