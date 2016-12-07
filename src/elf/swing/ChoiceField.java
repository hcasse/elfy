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

import elf.ui.Displayer;
import elf.ui.Icon;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * Swing implementation of a choice field.
 * @author casse
 */
public class ChoiceField<T> extends Field implements elf.ui.ChoiceField<T>, Var.ChangeListener<T>, ActionListener {
	private Var<T> chosen;
	private JComboBox<?> combo;
	private CollectionVar<T> choices;
	private boolean updating = false;
	private Displayer<T> displayer = new Displayer<T>() {
		@Override public String asString(T value) { return value.toString(); }
		@Override public Icon getIcon(T value) { return null; }
	};

	/**
	 * Build an enumeration field.
	 * @param var	Enumerated variable.
	 */
	public ChoiceField(Var<T> chosen, CollectionVar<T> choices) {
		this.chosen = chosen;
		this.choices = choices;
	}

	@Override
	public Entity getEntity() {
		return chosen;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public JComponent getComponent(View view) {
		if(combo == null) {

			// create the combo
			Vector<T> v = new Vector<T>();
			v.addAll(choices.getCollection());
			combo = new JComboBox<T>(v);
			String help = chosen.getHelp();
			if(help != null)
				combo.setToolTipText(help);
			combo.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("unchecked")
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					label.setText(displayer.asString((T)value));
					label.setBorder(new CustomBorder(label.getBorder()));
					return label;
				}
			});
			combo.addActionListener(this);
			updateUI();
		}
		return combo;
	}

	/**
	 * Update user interface.
	 */
	private void updateUI() {
		updating = true;
		combo.setSelectedItem(chosen.get());
		updating = false;
	}

	/**
	 * Update the variable.
	 */
	@SuppressWarnings("unchecked")
	private void updateVar() {
		chosen.set((T)combo.getSelectedItem());
	}

	@Override
	public void change(Var<T> data) {
		updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!updating)
			updateVar();
	}

	@Override
	public void setValidity(boolean validity) {
	}

	@Override
	public Var<?> getVar() {
		return chosen;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void setDisplayer(Displayer<T> displayer) {
		this.displayer = displayer;
	}

	@Override
	public boolean takeFocus() {
		return combo.requestFocusInWindow();
	}

}
