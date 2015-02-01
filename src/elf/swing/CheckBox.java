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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * Swing implementation of a checkbox.
 * @author casse
 *
 */
public class CheckBox extends Field implements elf.ui.CheckBox {
	private Var<Boolean> var;
	private JCheckBox cbox;
	private boolean do_break = false;
	
	public CheckBox(Var<Boolean> var) {
		this.var = var;
	}
	
	@Override
	public Entity getEntity() {
		return var;
	}

	@Override
	public JComponent getComponent(UI ui) {
		if(cbox == null) {
			cbox = new JCheckBox();
			cbox.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent arg0) { updateVar(); }
			});
			var.addListener(new Var.Listener<Boolean>() {
				@Override public void change(Var<Boolean> data) { updateUI(); }
			});
			updateUI();
		}
		return cbox;
	}

	/**
	 * Update the displayed UI.
	 */
	private void updateUI() {
		do_break = true;
		cbox.setSelected(var.get());
		do_break = false;
	}
	
	/**
	 * Update the variable.
	 */
	private void updateVar() {
		if(do_break)
			return;
		var.set(cbox.isSelected());		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		cbox = null;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

}
