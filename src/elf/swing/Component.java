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

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import elf.ui.Monitor;
import elf.ui.meta.Action;
import elf.ui.meta.Entity;

/**
 * Interface for objects producing a Swing component.
 * @author casse
 */
public abstract class Component implements elf.ui.Component {
	static final Dimension
		ZERO = new Dimension(0, 0),
		HFILL = new Dimension(Short.MAX_VALUE, 0),
		VFILL = new Dimension(0, Short.MAX_VALUE),
		FILL = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	public static final Color
		BLUE = Color.BLUE,
		GREEN = Color.GREEN,
		MAGENTA = Color.MAGENTA,
		ORANGE = Color.ORANGE,
		RED = Color.RED,
		YELLOW = Color.YELLOW;
	Parent parent;

	/**
	 * Get the parent of the component.
	 * @return	Component container.
	 */
	public Parent getParent() {
		return parent;
	}
	
	/**
	 * Get the matching Swing component.
	 * @param view TODO
	 * @return	Swing Component.
	 */
	public abstract JComponent getComponent(View view);

	/**
	 * Initialize button for an entity.
	 * @param component		Button.
	 * @param entity		Current entity.
	 */
	protected void prepareEntity(AbstractButton component, Entity entity) {
		if(entity.getHelp() != null)
			component.setToolTipText(entity.getHelp());
		if(entity.getMnemonic() != 0)
			component.setMnemonic(entity.getMnemonic());
	}
	
	/**
	 * Prepare the component.
	 * @param component		Current component.
	 * @param action		Current action.
	 */
	protected void prepareButton(AbstractButton component, Action action) {
		prepareEntity(component, action);
		component.setEnabled(action.isEnabled());
	}

	@Override
	public void dispose() {
	}
	
	/**
	 * Add a border around the component for debugging purpose.
	 * @param component		Component to put border around.
	 * @param color			Color of the border.
	 */
	protected void debugBorder(JComponent component, Color color) {
		component.setBorder(BorderFactory.createLineBorder(color, 4));
	}

	@Override
	public Monitor getMonitor() {
		return getParent().getMonitor();
	}

	/**
	 * Get the owner view.
	 * @return	Parent view.
	 */
	public View getView() {
		return getParent().getView();
	}
	
}
