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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import elf.ui.meta.Action;

/**
 * Swing implementation of menu item.
 * @author casse
 */
public class MenuItem extends Component implements ActionListener, Action.Command {
	Action action;
	JMenuItem menu;
	
	public MenuItem(Action action) {
		this.action = action;
	}
	
	@Override
	public JComponent getComponent(UI ui) {
		if(menu == null) {
			menu = new JMenuItem();
			prepareButton(menu, action);
			menu.setText(action.getLabel());
			Icon icon = ui.getIcon(action.getIcon());
			if(icon != null)
				menu.setIcon(icon.get(Icon.NORMAL, Icon.TEXTUAL));
			menu.addActionListener(this);
			action.add(this);
		}
		return menu;
	}

	@Override
	public void enable(boolean enabled) {
		menu.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		action.run();
	}

	@Override
	public void dispose() {
		action.remove(this);
	}

}
