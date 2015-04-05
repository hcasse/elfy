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

import java.lang.reflect.Field;

import elf.app.Configuration;

/**
 * A data that may be observed.
 * @author H. Cassé <casse@irit.fr>
 *
 * @param <T>	Stored data.
 * @deprecated
 */
public class SingleVar<T> extends Var<T> {
	
	/**
	 * Build the data with default value.
	 */
	public SingleVar() { super(new Accessor.Store<T>()); }
	
	/**
	 * Build the data with the given value.
	 * @param value	Set value.
	 */
	public SingleVar(T value) { super(new Accessor.Store<T>(value)); }
	
	/**
	 * Variable from a configuration.
	 * @author casse
	 */
	public static class FromConfig<T> implements ChangeListener<T> {
		private Configuration config;
		private String name;
		private Field field;
		
		public FromConfig(Configuration config, String name) {
			this.config = config;
			this.name = name;
		}
		
		/**
		 * Get the field.
		 * @return	Field.
		 */
		public Field getField() {
			if(field == null) {
				try {
					field = config.getClass().getField(name);
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
		public Object get() {
			Field field = getField();
			if(field != null)
				try {
					return field.get(config);
				} catch (IllegalArgumentException e) {
					System.err.println("ERROR: " + e.getLocalizedMessage());
				} catch (IllegalAccessException e) {
					System.err.println("ERROR: " + e.getLocalizedMessage());
				}
			return null;
		}

		@Override
		public void change(Var<T> data) {
			Field field = getField();
			try {
				field.set(config, data.get());
			} catch (IllegalArgumentException e) {
				System.err.println("ERROR: " + e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				System.err.println("ERROR: " + e.getLocalizedMessage());
			}
		}
		
	}
}
