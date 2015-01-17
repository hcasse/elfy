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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import elf.app.AutoConfiguration;
import elf.ui.Monitor;

/**
 * Common interface to access a data item.
 * @author casse
 */
public interface Accessor<T> {

	/**
	 * Get the data.
	 * @return	Data.
	 */
	T get();
	
	/**
	 * Set the value of a data item.
	 * @param value		Value to set.
	 */
	void set(T value);
	
	/**
	 * Accessor storing itself the data.
	 * @author casse
	 * 
	 * @param <T>	Type of stored value.
	 */
	static class Store<T> implements Accessor<T> {
		private T value;
		
		/**
		 * Empty store.
		 */
		public Store() {
		}
		
		/**
		 * Build a store initialized with the given value.
		 * @param value		Initialization value.
		 */
		public Store(T value) {
			this.value = value;
		}
		
		@Override
		public T get() {
			return value;
		}

		@Override
		public void set(T value) {
			this.value = value;
		}
		
	}
	
	/**
	 * Attribute accessor.
	 * @author casse
	 *
	 * @param <T>	Type of accessor.
	 */
	class Attribute<T> implements Accessor<T> {
		private Object object;
		private String name;
		private Field field;
		private boolean tested;
		
		/**
		 * Build an attribute accessor.
		 * @param object	Concerned object.
		 * @param name		Attribute name.
		 */
		public Attribute(Object object, String name) {
			this.object = object;
			this.name = name;
		}
		
		/**
		 * Build an attribute accessor.
		 * @param object	Concerned object.
		 * @param field		Attribute itself.
		 */
		public Attribute(Object object, Field field) {
			this.object = object;
			this.name = field.getName();
			this.field = field;
			tested = true;
		}
		
		/**
		 * Get the current object.
		 * @return	Current object.
		 */
		public Object getObject() {
			return object;
		}
		
		/**
		 * Obtain the field (if any).
		 * @return	Found field.
		 */
		public Field getField() {
			if(!tested) {
				tested = true;
				try {
					field = object.getClass().getDeclaredField(name);
				} catch (NoSuchFieldException e) {
					System.err.println("ERROR: cannot get field " + name + ": " + e.getLocalizedMessage());
				} catch (SecurityException e) {
					System.err.println("ERROR: cannot get field " + name + ": " + e.getLocalizedMessage());
				}
			}
			return field;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public T get() {
			getField();
			if(field == null)
				return null;
			else
				try {
					return (T)field.get(object);
				} catch (IllegalArgumentException e) {
					System.err.println("ERROR: " + e.getLocalizedMessage());
					return null;
				} catch (IllegalAccessException e) {
					System.err.println("ERROR: " + e.getLocalizedMessage());
					return null;
				}
		}

		@Override
		public void set(T value) {
			getField();
			if(field != null)
				try {
					field.set(object, value);
				} catch (IllegalArgumentException e) {
					System.err.println("ERROR: " + e.getLocalizedMessage());
				} catch (IllegalAccessException e) {
					System.err.println("ERROR: " + e.getLocalizedMessage());
				}
		}
		
	}
	
	/**
	 * Accessor based on get/set methods.
	 * @author casse
	 *
	 * @param <T>	Type of accessed data.
	 */
	class GetSet<T> implements Accessor<T> {
		private String name;
		private Object object;
		private Method setter, getter;
		
		/**
		 * Build a setter variable.
		 * @param object	Object it is applied to.
		 * @param name		Name of the field (suffixing the get and set methods).
		 */
		public GetSet(Object object, String name) {
			this.object = object;
			this.name = name;
		}
		
		/**
		 * Build a setter variable.
		 * @param object	Object providing get and set.
		 * @param get		Getter method.
		 * @param set		Setter method.
		 */
		public GetSet(Object object, Method get, Method set) {
			this.object = object;
			this.getter = get;
			this.setter = set;
		}
		
		/**
		 * Get the current object.
		 * @return	Current object.
		 */
		public Object getObject() {
			return object;
		}

		/**
		 * Obtain the getter method.
		 * @return	Getter method.
		 */
		public Method getGetter() {
			if(getter == null)
				try {
					getter = object.getClass().getDeclaredMethod("get" + name);
				} catch (NoSuchMethodException e) {
					Monitor.STD.error("cannot get method get" + name + ": " + e.getLocalizedMessage());
				} catch (SecurityException e) {
					Monitor.STD.error("cannot get method get" + name + ": " + e.getLocalizedMessage());
				}
			return getter;
		}
		
		/**
		 * Get the setter method.
		 * @return	Setter method.
		 */
		public Method getSetter() {
			if(setter == null) {
				getGetter();
				if(getter == null)
					return null;
				try {
					setter = object.getClass().getDeclaredMethod("set" + name, getter.getReturnType());
				} catch (NoSuchMethodException e) {
					Monitor.STD.error("cannot get method set" + name + ": " + e.getLocalizedMessage());
				} catch (SecurityException e) {
					Monitor.STD.error("cannot get method set" + name + ": " + e.getLocalizedMessage());
				}			
			}
			return setter;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public T get() {
			getGetter();
			if(getter != null)
				try {
					return (T) getter.invoke(object);
				} catch (IllegalAccessException e) {
					Monitor.STD.error("cannot call " + getter.getName() + ": " + e.getLocalizedMessage());
				} catch (IllegalArgumentException e) {
					Monitor.STD.error("cannot call " + getter.getName() + ": " + e.getLocalizedMessage());
				} catch (InvocationTargetException e) {
					Monitor.STD.error("cannot call " + getter.getName() + ": " + e.getLocalizedMessage());
				}
			return null;
		}

		@Override
		public void set(T value) {
			getSetter();
			if(setter != null)
				try {
					setter.invoke(object, value);
				} catch (IllegalAccessException e) {
					Monitor.STD.error("cannot call " + setter.getName() + ": " + e.getLocalizedMessage());
				} catch (IllegalArgumentException e) {
					Monitor.STD.error("cannot call " + setter.getName() + ": " + e.getLocalizedMessage());
				} catch (InvocationTargetException e) {
					Monitor.STD.error("cannot call " + setter.getName() + ": " + e.getLocalizedMessage());
				}
		}

	}

	/**
	 * An attribute from a configuration.
	 * @author casse
	 *
	 * @param <T>	Type of attribute.
	 */
	class Config<T> extends Attribute<T> {
		private AutoConfiguration config;
		
		public Config(AutoConfiguration config, String name) {
			super(config, name);
			this.config = config;
		}

		@Override
		public void set(T value) {
			super.set(value);
			config.modify();
		}
		
	}
}
