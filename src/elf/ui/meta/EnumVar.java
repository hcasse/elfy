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
package elf.ui.meta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import elf.ui.I18N;

/**
 * Variable for enumeration with the ability to produce label for enumerated values.
 * @author casse
 */
public class EnumVar<T> extends Var<T> {
	private I18N i18n;
	private Method method;
	private boolean tested;
	private Vector<T> values;
	
	/**
	 * Constructor from simple value.
	 * @param value		Current value.
	 */
	public EnumVar(T value) {
		super(value);
		i18n = I18N.STD;
	}
	
	/**
	 * Build a an enumerated variable from an accessor.
	 * @param accessor	Accessor to use.
	 */
	public EnumVar(Accessor<T> accessor) {
		super(accessor);
		i18n = I18N.STD;
	}
	
	/**
	 * Constructor from value and translation.
	 * @param value		Current value.
	 * @param i18n		Translation handle to translate values.
	 */
	public EnumVar(T value, I18N i18n) {
		super(value);
		this.i18n = i18n;
	}
	
	/**
	 * Constructor from value and translation.
	 * @param accessor	Accessor to use.
	 * @param i18n		Translation handle to translate values.
	 */
	public EnumVar(Accessor<T> accessor, I18N i18n) {
		super(accessor);
		this.i18n = i18n;
	}

	/**
	 * Get the label for an enumerated value and using the translation handle.
	 * @param value		Value to get label for.
	 * @return			Value label.
	 */
	public String getLabel(T value) {
		
		// initialize stringification context
		if(!tested) {
			tested = true;
			try {
				method = get().getClass().getMethod("getLabel");
			} catch (NoSuchMethodException e) {
				method = null;
			} catch (SecurityException e) {
				method = null;
			}
		}
		
		// build the label
		String raw = "";
		if(method == null)
			raw = value.toString();
		else
			try {
				raw = (String)method.invoke(value);
			} catch (IllegalAccessException e) {
				// TODO		Log it somewhere
			} catch (IllegalArgumentException e) {
				// TODO		Log it somewhere
			} catch (InvocationTargetException e) {
				// TODO		Log it somewhere
			}
		return i18n.t(raw);	
	}
	
	/**
	 * Get values in the enumeration.
	 * @return		Enumerated values.
	 */
	@SuppressWarnings("unchecked")
	public Iterable<T> getValues() {
		if(values == null) {
			values = new Vector<T>();
			Class<?> cls = get().getClass();
			for(Object val: cls.getEnumConstants())
				values.add((T)val);
		}
		return values;
	}
	
	/**
	 * Get the number of values.
	 * @return		Value count.
	 */
	public int getValueCount() {
		Class<?> cls = get().getClass();
		return cls.getEnumConstants().length;
	}
}
