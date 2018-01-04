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

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import elf.ui.Displayer;
import elf.ui.I18N;
import elf.ui.Icon;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * Swing implementation of a choice field.
 * @author casse
 */
public class ChoiceField<T> extends Field implements elf.ui.ChoiceField<T>, Var.ChangeListener<T>, ActionListener, CollectionVar.Listener<T> {
	private Var<T> chosen;
	private JComboBox<T> combo;
	private CollectionVar<T> choices;
	private boolean updating = false;
	private DefaultComboBoxModel<T> model;
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
		chosen.add(this);
		this.choices = choices;
		choices.addListener(this);
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
			model = new DefaultComboBoxModel<T>();
			combo = new JComboBox<T>(model);
			for(T item: choices)
				model.addElement(item);
			String help = chosen.getHelp();
			if(help != null)
				combo.setToolTipText(help);
			combo.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("unchecked")
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if(value == null)
						label.setText(I18N.STD.t("no choice available"));
					else
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
		model.setSelectedItem(chosen.get());
		updating = false;
	}

	/**
	 * Update the variable.
	 */
	@SuppressWarnings("unchecked")
	private void updateVar() {
		chosen.set((T)model.getSelectedItem());
	}

	@Override
	public void onChange(Var<T> data) {
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

	@Override
	public void onAdd(T item) {
		model.addElement(item);
	}

	@Override
	public void onRemove(T item) {
		model.removeElement(item);
	}

	@Override
	public void onClear() {
		model.removeAllElements();
	}

	@Override
	public void onChange() {
		model.removeAllElements();
		for(T item: choices)
			model.addElement(item);
	}

}
