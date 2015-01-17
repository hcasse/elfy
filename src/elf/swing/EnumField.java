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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import elf.ui.meta.Entity;
import elf.ui.meta.EnumVar;
import elf.ui.meta.Var;

/**
 * Swing implementation of an enumeration field.
 * @author casse
 */
public class EnumField<T> extends Field implements elf.ui.EnumField<T>, Var.Listener<T>, ActionListener {
	private EnumVar<T> var;
	private JComboBox<?> combo;
	private Vector<T> values;
	private boolean updating = false;
	
	/**
	 * Build an enumeration field.
	 * @param var	Enumerated variable.
	 */
	public EnumField(EnumVar<T> var) {
		this.var = var;
	}
	
	@Override
	public Entity getEntity() {
		return var;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public JComponent getComponent() {
		if(combo == null) {
			
			// fill the values
			values = new Vector<T>();
			for(T val: var.getValues())
				values.add(val);
			
			// create the combo
			combo = new JComboBox<T>(values);
			String help = var.getHelp();
			if(help != null)
				combo.setToolTipText(help);
			combo.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("unchecked")
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					label.setText(var.getLabel((T)value));
					return label;
				}
			});
			updateUI();
		}
		return combo;
	}
	
	/**
	 * Update user interface.
	 */
	private void updateUI() {
		updating = true;
		combo.setSelectedItem(var.get());
		updating = false;
	}
	
	/**
	 * Update the variable.
	 */
	private void updateVar() {
		var.set(values.elementAt(combo.getSelectedIndex()));
	}

	@Override
	public void change(Var<T> data) {
		updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(updating)
			updateVar();
	}

}
