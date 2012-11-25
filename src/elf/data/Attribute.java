/*
 * ElfCore library
 * Copyright (c) 2012 - Hugues Cassé <hugues.casse@laposte.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in theString hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package elf.data;

/**
 * Attribute with a generic value.
 * @author casse
 *
 * @param <T>		Type of the stored value.
 */
public class Attribute<T> extends AbstractAttribute {

	public Attribute(String name) {
		super(name);
	}

	/**
	 * Get the value of an attribute.
	 * @param attrs		Attributes to get value from.
	 * @param def		Default value.
	 * @return			Found value if its exists in attrs, default value else.
	 */
	@SuppressWarnings("unchecked")
	public T get(Attributes attrs, T def) {
		Object v = attrs.get(this);
		if(v == null)
			return def;
		else
			return (T)v;
	}
	
	/**
	 * Set the value of an attribute.
	 * @param attrs		Attribute to set value to.
	 * @param value		Value to set.
	 */
	public void set(Attributes attrs, T value) {
		attrs.set(this, value);
	}
}
