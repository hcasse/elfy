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

import elf.ui.StringAdapter;
import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * Text field for Swing implementation.
 * @author casse
 */
public class TextField<T> extends Field implements elf.ui.TextField<T>, Var.Listener<T> {
	private Var<T> var;
	private StringAdapter<T> adapter;
	private JTextField field;
	private boolean break_rec = false, read_only = false;
	
	private Class<?> getGenericTypeArgument() {
		return var.get().getClass();		
	}

	public TextField(T init) {
		set(new Var<T>(init));
		adapter = new StringAdapter.SerializerAdapter<T>(getGenericTypeArgument());
	}
	
	public TextField(Var<T> var) {
		set(var);
		adapter = new StringAdapter.SerializerAdapter<T>(getGenericTypeArgument());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		var.removeListener(this);
		field = null;
	}
	
	@Override
	public Var<T> get() {
		return var;
	}
	
	@Override
	public void set(Var<T> var) {
		if(var != null)
			var.removeListener(this);
		this.var = var;
		var.addListener(this);
	}
	
	@Override
	public void change(Var<T> data) {
		updateUI();
	}
	
	/**
	 * Get it as a component.
	 * @return	Swing component.
	 */
	public JComponent getComponent() {
		if(field == null) {
			field = new JTextField();
			field.getDocument().addDocumentListener(new DocumentListener() {
				@Override public void changedUpdate(DocumentEvent arg0) { updateSync(); }
				@Override public void insertUpdate(DocumentEvent arg0) { updateSync(); }
				@Override public void removeUpdate(DocumentEvent arg0) { updateSync(); }
			});
			field.setEditable(!read_only);
			updateUI();
		}
		return field;
	}

	protected void updateUI() {
		if(field == null)
			return;
		if(!break_rec)
			field.setText(adapter.toString(get().get()));
	}

	@SuppressWarnings("unchecked")
	protected void updateVar() {
		if(field == null)
			return;
		try {
			Object value = adapter.fromString(field.getText());
			field.setForeground(Color.BLACK);
			get().set((T)value);
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

	@Override
	public void setAdapter(StringAdapter<T> adapter) {
		this.adapter = adapter;
		updateVar();
	}

	@Override
	public Entity getEntity() {
		return var;
	}

	@Override
	public void setReadOnly(boolean ro) {
		read_only = ro;
		if(field != null)
			field.setEditable(!read_only);
	}

	@Override
	public boolean isReadOnly() {
		return read_only;
	}
	
}
