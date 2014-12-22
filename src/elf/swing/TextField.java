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

import java.awt.Color;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import elf.ui.meta.Var;

/**
 * Text field for Swing implementation.
 * @author casse
 */
public class TextField<T> extends elf.ui.TextField<T> {
	private JTextField field;
	private boolean break_rec = false;
	
	/**
	 * Initialization of Swing text field.
	 */
	private void init() {
		field = new JTextField();
		field.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent arg0) { updateSync(); }
			@Override public void insertUpdate(DocumentEvent arg0) { updateSync(); }
			@Override public void removeUpdate(DocumentEvent arg0) { updateSync(); }
		});
	}
	
	public TextField(T init) {
		super(init);
		init();
	}
	
	public TextField(Var<T> var) {
		super(var);
		init();
	}
	
	/**
	 * Get it as a component.
	 * @return	Swing component.
	 */
	public JComponent getComponent() {
		return field;
	}

	@Override
	protected void updateUI() {
		if(!break_rec)
			try {
				field.setText(getSerializer().serialize(getVar().get()));
			} catch (IOException e) {
				// shouldn't arise
			}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateVar() {
		try {
			Object value = getSerializer().unserialize(field.getText());
			field.setForeground(Color.BLACK);
			getVar().set((T)value);
		} catch (IOException e) {
			field.setForeground(Color.RED);
		}
	}

	/**
	 * Wrapper method around updateVar() preventing recursive calls
	 * if update comes from UI.
	 */
	private void updateSync() {
		break_rec = true;
		updateVar();
		break_rec = false;
	}
	
}
