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
	 * Linked variable to accessor.
	 * @param var	Linked variable.
	 */
	void link(Var<T> var);
	
	/**
	 * Unlink current variable from accessor.
	 */
	void unlink();
	
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

		@Override
		public void link(Var<T> var) {
		}

		@Override
		public void unlink() {
		}
		
	}
	
	/**
	 * Common facilities for accessor to indirect data.
	 * @author casse
	 *
	 * @param <T>	Type of indirect data.
	 * @param <U>	Type of object.
	 */
	abstract class Indirect<T, U> implements Accessor<T>, Var.ChangeListener<U> {
		private Var<U> object;
		private Var<T> var;
	
		public Indirect(Var<U> object) {
			this.object = object;
		}
		
		protected Object getObject() {
			return object.get();
		}

		@Override
		public void change(Var<U> data) {
			if(var != null)
				var.fireChange();
		}

		@Override
		public void link(Var<T> var) {
			if(var == null)
				object.addChangeListener(this);
			this.var = var;
		}

		@Override
		public void unlink() {
			if(var != null) {
				object.removeChangeListener(this);
				var = null;
			}
		}

		
	}
	
	/**
	 * Attribute accessor.
	 * @author casse
	 *
	 * @param <T>	Type of accessor.
	 * @param <U>	Type of object.
	 */
	class Attribute<T, U> extends Indirect<T, U> {
		private String name;
		private Field field;
		private boolean tested;
		
		/**
		 * Build an attribute accessor.
		 * @param object	Concerned object.
		 * @param name		Attribute name.
		 */
		public Attribute(U object, String name) {
			super(Var.make(object));
			this.name = name;
		}
		
		/**
		 * Build an attribute accessor.
		 * @param object	Concerned object.
		 * @param field		Attribute itself.
		 */
		public Attribute(U object, Field field) {
			super(Var.make(object));
			this.field = field;
			tested = true;
		}
		
		/**
		 * Build an attribute accessor.
		 * @param object	Concerned object.
		 * @param name		Attribute name.
		 */
		public Attribute(Var<U> object, String name) {
			super(object);
			this.name = name;
		}
		
		/**
		 * Build an attribute accessor.
		 * @param object	Concerned object.
		 * @param field		Attribute itself.
		 */
		public Attribute(Var<U> object, Field field) {
			super(object);
			this.name = field.getName();
			this.field = field;
			tested = true;
		}
		
		/**
		 * Obtain the field (if any).
		 * @return	Found field.
		 */
		public Field getField() {
			if(!tested) {
				tested = true;
				try {
					field = getObject().getClass().getDeclaredField(name);
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
					return (T)field.get(getObject());
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
					field.set(getObject(), value);
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
	 * @param <U>	Type of objects.
	 */
	class GetSet<T, U> extends Indirect<T, U> {
		private String name;
		private Method setter, getter;
		
		/**
		 * Build a setter variable.
		 * @param object	Object it is applied to.
		 * @param name		Name of the field (suffixing the get and set methods).
		 */
		public GetSet(U object, String name) {
			super(Var.make(object));
			this.name = name;
		}
		
		/**
		 * Build a setter variable.
		 * @param object	Object providing get and set.
		 * @param get		Getter method.
		 * @param set		Setter method.
		 */
		public GetSet(U object, Method get, Method set) {
			super(Var.make(object));
			this.getter = get;
			this.setter = set;
		}
		
		/**
		 * Build a setter variable.
		 * @param object	Object it is applied to.
		 * @param name		Name of the field (suffixing the get and set methods).
		 */
		public GetSet(Var<U> object, String name) {
			super(object);
			this.name = name;
		}
		
		/**
		 * Build a setter variable.
		 * @param object	Object providing get and set.
		 * @param get		Getter method.
		 * @param set		Setter method.
		 */
		public GetSet(Var<U> object, Method get, Method set) {
			super(object);
			this.getter = get;
			this.setter = set;
		}
		
		/**
		 * Obtain the getter method.
		 * @return	Getter method.
		 */
		public Method getGetter() {
			if(getter == null)
				try {
					getter = getObject().getClass().getDeclaredMethod("get" + name);
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
					setter = getObject().getClass().getDeclaredMethod("set" + name, getter.getReturnType());
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
					return (T) getter.invoke(getObject());
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
					setter.invoke(getObject(), value);
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
	class Config<T> extends Attribute<T, AutoConfiguration> {
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
