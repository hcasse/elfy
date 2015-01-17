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

import java.lang.reflect.Method;

/**
 * Variable with a setter method. Setter method name must be built as 
 * set<UpperCase attribute name> and take the good value as parameter.
 * It must also have an accessor method named get<UpperCase attribute name>.
 * @author casse
 * @deprecated		Use Var with Accessor.GetSet.	
 */
public class SetterVar<T> extends Var<T> {
	private String name;
	private Object object;
	
	/**
	 * Build a setter variable.
	 * @param object	Object it is applied to.
	 * @param name		Name of the field (suffixing the get and set methods).
	 */
	public SetterVar(Object object, String name) {
		super(new Accessor.GetSet<T>(object, name));
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
		setAccessor(new Accessor.GetSet<T>(object, name));
	}
	
	public Method getGetter() {
		return null;
	}
	
	public Method getSetter() {
		return null;
	}
	
}
