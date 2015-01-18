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

/**
 * Variable on an attribute.
 * @author casse
 * @deprecated	Use Var with Accessor.Attribute.
 */
public class AttributeVar<T> extends Var<T> {
	private Object object;
	private String name;
	
	public AttributeVar(Object object, String name) {
		super(new Accessor.Attribute<T, Object>(object, name));
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
		setAccessor(new Accessor.Attribute<T, Object>(object, name));
	}

}
