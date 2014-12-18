/*
 * ElfCore library
 * Copyright (c) 2012 - Hugues Cassé <hugues.casse@laposte.net>
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
package elf.ui;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import elf.ui.meta.Action;

/**
 * Factory for a Swing UI.
 * @author casse
 */
public class SwingFactory {

	/**
	 * Action listener for Elf actions.
	 * @author casse
	 */
	private static class Command implements ActionListener, Action.Command {
		Action action;
		AbstractButton button;
		
		public Command(Action action, AbstractButton button) {
			this.action = action;
			this.button = button;
			action.add(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			action.run();
		}

		@Override
		public void enable(boolean enabled) {
			button.setEnabled(enabled);
		}
		
	}
	
	/**
	 * Prepare the component.
	 * @param component		Current component.
	 * @param action		Current action.
	 */
	private void prepareButton(AbstractButton component, Action action) {
		if(action.getMnemonic() != 0)
			component.setMnemonic(action.getMnemonic());
		component.setEnabled(action.isEnabled());
		if(action.getHelp() != null)
			component.setToolTipText(action.getHelp());
		Icon i = action.getIcon();
		if(i != null)
			component.setIcon(i.get(Icon.NORMAL, Icon.TEXTUAL));
		Command command = new Command(action, component);
		component.addActionListener(command);
	}
	
	/**
	 * Prepare a menu item.
	 * @param item		Menu item to prepare.
	 * @param action	Current action.
	 */
	private void prepareMenuItem(JMenuItem item, Action action) {
		prepareButton(item, action);
		if(action.getControl() != 0)
			item.setAccelerator(KeyStroke.getKeyStroke(action.getControl(), Event.CTRL_MASK));
	}
	
	/**
	 * Make a button for the given action.
	 * @param action	Button action.	
	 * @return			Built button.
	 */
	public JButton makeButton(Action action) {
		JButton b = new JButton();
		prepareButton(b, action);
		String l = action.getLabel();
		if(l != null)
			b.setText(l);
		return b;
	}
	
	/**
	 * Build a menu item (no action).
	 * @param action	Current action.
	 * @return			Built menu item.
	 */
	public JMenu makeMenu(Action action) {
		JMenu menu = new JMenu(action.getLabel());
		prepareMenuItem(menu, action);
		return menu;
	}
	
}
