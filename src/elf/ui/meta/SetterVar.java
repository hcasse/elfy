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

import elf.ui.Monitor;

/**
 * Variable with a setter method. Setter method name must be built as 
 * set<UpperCase attribute name> and take the good value as parameter.
 * It must also have an accessor method named get<UpperCase attribute name>.
 * @author casse
 */
public class SetterVar<T> extends Var<T> {
	private String name;
	private Object object;
	private Method setter, getter;
	
	/**
	 * Build a setter variable.
	 * @param object	Object it is applied to.
	 * @param name		Name of the field (suffixing the get and set methods).
	 */
	public SetterVar(Object object, String name) {
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
	 * Set the current object.
	 * @param object	New object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
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
