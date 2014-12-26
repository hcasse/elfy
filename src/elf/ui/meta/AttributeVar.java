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
package elf.ui.meta;

import java.lang.reflect.Field;

/**
 * Variable on an attribute.
 * @author casse
 */
public class AttributeVar<T> extends Var<T> {
	private Object object;
	private String name;
	private Field field;
	
	public AttributeVar(Object object, String name) {
		this.object = object;
		this.name = name;
	}
	
	/**
	 * Get the current object.
	 * @return	Current object.
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * Change the object.
	 * @param object	New object.
	 */
	public void setObject(Object object) {
		this.object = object;
		field = null;
		fireChange();
	}
	
	/**
	 * Get the field.
	 * @return	Field.
	 */
	private Field getField() {
		if(field == null) {
			try {
				field = object.getClass().getField(name);
			} catch (NoSuchFieldException e) {
				System.err.println("ERROR: " + e.getLocalizedMessage());
			} catch (SecurityException e) {
				System.err.println("ERROR: " + e.getLocalizedMessage());
			}
		}
		return field;
	}
	
	/**
	 * Get the field value.
	 * @return	Field value.
	 */
	@SuppressWarnings("unchecked")
	public T get() {
		Field field = getField();
		if(field != null)
			try {
				return (T)field.get(object);
			} catch (IllegalArgumentException e) {
				System.err.println("ERROR: " + e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				System.err.println("ERROR: " + e.getLocalizedMessage());
			}
		return null;
	}

	@Override
	public void set(T value) {
		Field field = getField();
		try {
			field.set(object, value);
			fireChange();
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: " + e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			System.err.println("ERROR: " + e.getLocalizedMessage());
		}
	}

}
