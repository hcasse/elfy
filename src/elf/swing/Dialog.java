/*
 * Elfy library
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

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JDialog;

import elf.ui.Monitor;
import elf.ui.SelectionDialog;
import elf.ui.meta.Action;
import elf.ui.meta.Entity;

/**
 * Dialog view.
 * @author casse
 */
public class Dialog extends Container implements elf.ui.View {
	private Entity entity;
	private View parent;
	private JDialog dialog;
	
	public Dialog(View parent, Entity entity) {
		this.parent = parent;
		this.entity = entity;
	}
	
	@Override
	public JComponent getComponent(UI ui) {
		return null;
	}

	@Override
	public void show() {
		if(dialog == null) {
			dialog = new JDialog(parent.getFrame(), entity.getLabel(), true);
			for(Component component: getComponents())
				dialog.add(component.getComponent(parent.getUI()));
		}
		dialog.setVisible(true);
	}

	@Override
	public void hide() {
		if(dialog != null)
			dialog.setVisible(false);
	}

	@Override
	public void setCloseAction(Action action) {
	}

	@Override
	public Monitor getMonitor() {
		return parent.getMonitor();
	}

	@Override
	public boolean showConfirmDialog(String message, String title) {
		return parent.showConfirmDialog(message, title);
	}

	@Override
	public <T> SelectionDialog<T> makeSelectionDialog(String message, String title, Collection<T> collection) {
		return parent.makeSelectionDialog(message, title, collection);
	}

	@Override
	public elf.ui.View makeDialog(Entity entity) {
		return parent.makeDialog(entity);
	}

	@Override
	public void dispose() {
		super.dispose();
		dialog = null;
	}

}
