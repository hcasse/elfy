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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import elf.ui.Icon;
import elf.ui.IconManager;
import elf.ui.meta.Action;

/**
 * Title bar.
 * @author casse
 */
public class TitleBar extends Component implements elf.ui.TitleBar  {
	private LinkedList<Action>
		menus = new LinkedList<Action>(),
		lefts = new LinkedList<Action>(),
		rights = new LinkedList<Action>();
	private LinkedList<Component> buttons = new LinkedList<Component>();
	private JLabel label;
	private JComponent bar;
	private JPopupMenu popup;
	private JButton button;
	private String title = "";
	
	/**
	 * Set the current title message.
	 * @param title		Title message to display.
	 */
	public void setTitle(String title) {
		if(label != null)
			label.setText(title);
		this.title = title;
	}
	
	/**
	 * Add a menu action.
	 * @param action	Added menu.
	 */
	public void addMenu(Action action) {
		menus.add(action);
	}
	
	/**
	 * Add an action to the left.
	 * @param action	Added actions.
	 */
	public void addLeft(Action action) {
		lefts.add(action);
	}

	/**
	 * Add an action to the right.
	 * @param action	Added actions.
	 */
	public void addRight(Action action) {
		rights.add(action);
	}

	@Override
	public JComponent getComponent() {
		if(bar == null) {
			bar = Box.createHorizontalBox();
			for(Action action: lefts) {
				Button button = new Button(action, Button.STYLE_ICON);
				buttons.add(button);
				bar.add(button.getComponent());
			}
			
			// prepare title
			label = new JLabel(title);
			label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			bar.add(label);
			label.setPreferredSize(new Dimension(Short.MAX_VALUE, 48));
			label.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));
			
			// build other buttons
			for(Action action: rights) {
				Button button = new Button(action, Button.STYLE_ICON);
				buttons.add(button);
				bar.add(button.getComponent());
			}
			
			// add menu if needed
			if(!menus.isEmpty()) {				
				popup = new JPopupMenu();
				for(Action action: menus) {
					MenuItem item = new MenuItem(action);
					popup.add(item.getComponent());
					buttons.add(item);
				}
				button = new JButton(IconManager.STD.get(Factory.ICON_MENU).get(Icon.NORMAL, Icon.TOOLBAR));
				button.addActionListener(new ActionListener() {
					@Override public void actionPerformed(ActionEvent event) {
				        popup.show(button, 0, button.getBounds().height);
					}
				});
				bar.add(button);
			}
		}
		return bar;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void dispose() {
		for(Component button: buttons)
			button.dispose();
		label = null;
		bar = null;
		popup = null;
		button = null;
		super.dispose();
	}

}
