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
package elf.swing;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import elf.ui.Icon;
import elf.ui.IconManager;
import elf.ui.meta.Action;
import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * Factory for a Swing UI.
 * @author casse
 */
public class Factory {
	public static final String
		ICON_BACK = "back",
		ICON_MENU = "menu";
	private final IconManager iman;

	public Factory() {
		IconManager i = null;
		try {
			i = new IconManager(new URL(IconManager.class.getResource("."), "../../pix"));
		} catch (MalformedURLException e) {
			System.err.println("INTERNAL ERROR: no standard icons");
		}
		iman = i;
	}
	
	/**
	 * Get a standard icon.
	 * @param id	Identifier of the standard icon (ICON_xxx constants).
	 * @return		Found icon.
	 */
	public Icon getIcon(String id) {
		return iman.get(id);
	}
	
	/**
	 * Initialize button for an entity.
	 * @param component		Button.
	 * @param entity		Current entity.
	 */
	private void prepareEntity(AbstractButton component, Entity entity) {
		if(entity.getHelp() != null)
			component.setToolTipText(entity.getHelp());
		Icon i = entity.getIcon();
		if(i != null)
			component.setIcon(i.get(Icon.NORMAL, Icon.TEXTUAL));		
		if(entity.getMnemonic() != 0)
			component.setMnemonic(entity.getMnemonic());
	}
	
	/**
	 * Prepare the component.
	 * @param component		Current component.
	 * @param action		Current action.
	 */
	private void prepareButton(AbstractButton component, Action action) {
		prepareEntity(component, action);
		component.setEnabled(action.isEnabled());
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
	 * Make a button only with an icon.
	 * @param action	Current action.
	 * @return			Created tool button.
	 */
	public JButton makeToolButton(Action action) {
		Icon icon = action.getIcon();
		if(icon == null)
			icon = iman.getBroken();
		JButton b = new JButton(icon.get(Icon.NORMAL, Icon.TOOLBAR));
		prepareButton(b, action);
		return b;
	}
	
	/**
	 * Build a menu item (no action).
	 * @param action	Current action.
	 * @return			Built menu item.
	 */
	public JMenuItem makeMenu(Action action) {
		JMenuItem menu = new JMenuItem(action.getLabel());
		prepareMenuItem(menu, action);
		return menu;
	}

	/**
	 * Build a text field.
	 * @return	Built text field.
	 */
	public <T> TextField<T> makeTextField() {
		return null;
	}
	
	/**
	 * Build a check box.
	 * @param var
	 * @return
	 */
	public JCheckBox makeCheckBox(Var<Boolean> var) {
		JCheckBox cb = new JCheckBox();
		cb.setSelected(var.get());
		prepareEntity(cb, var);
		return cb;
	}

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
	
}
