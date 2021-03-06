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
import elf.ui.Style;
import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * Text field for Swing implementation.
 * @author casse
 */
public class TextField<T> extends Field implements elf.ui.TextField<T>, Var.ChangeListener<T> {
	private Var<T> var;
	private StringAdapter<T> adapter;
	private JTextField field;
	private boolean break_rec = false, read_only = false;
	private Color back, inv = new Color(0xF1C7C7);
	private java.awt.Font initial_font;
	private java.awt.Color current_color = java.awt.Color.BLACK;

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
		var.remove(this);
		field = null;
		initial_font = null;
	}

	@Override
	public Var<T> get() {
		return var;
	}

	@Override
	public void set(Var<T> var) {
		if(var != null)
			var.remove(this);
		this.var = var;
		var.add(this);
	}

	@Override
	public void onChange(Var<T> data) {
		updateUI();
	}

	/**
	 * Get it as a component.
	 * @return	Swing component.
	 */
	public JComponent getComponent(View view) {
		if(field == null) {
			prepare(view);
			field = new JTextField();
			back = field.getBackground();
			field.getDocument().addDocumentListener(new DocumentListener() {
				@Override public void changedUpdate(DocumentEvent arg0) { updateSync(); }
				@Override public void insertUpdate(DocumentEvent arg0) { updateSync(); }
				@Override public void removeUpdate(DocumentEvent arg0) { updateSync(); }
			});
			field.setEditable(!read_only);
			field.setFocusable(!read_only);
			updateUI();
			if(style != null) {
				initial_font = field.getFont();
				current_color = field.getForeground();
				onUpdate(new int[] { Style.FONT_SIZE, Style.COLOR });
			}
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
			field.setForeground(current_color);
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
		if(field != null) {
			field.setEditable(!read_only);
			field.setFocusable(!read_only);
		}
	}

	@Override
	public boolean isReadOnly() {
		return read_only;
	}

	@Override
	public void setValidity(boolean validity) {
		if(field != null) {
			if(validity)
				field.setBackground(back);
			else
				field.setBackground(inv);
		}
	}

	@Override
	public Var<?> getVar() {
		return var;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void onUpdate(int[] items) {
		super.onUpdate(items);
		for(int key: items)
			switch(key) {
			case Style.FONT_SIZE:
				field.setFont(getFontStyle(field, initial_font));
				break;
			case Style.COLOR:
				current_color = getColor(field.getForeground());
				field.setForeground(current_color);
				break;
			}
	}

	@Override
	public boolean takeFocus() {
		if(field != null && !read_only)
			return field.requestFocusInWindow();
		else
			return false;
	}

}
