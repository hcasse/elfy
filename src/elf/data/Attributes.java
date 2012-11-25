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

import java.util.Comparator;
import java.util.TreeMap;

/**
 * List of attribute values.
 * @author casse
 */
public class Attributes implements Comparator<AbstractAttribute> {
	TreeMap<AbstractAttribute, Object> attrs = new TreeMap<AbstractAttribute, Object>();
	
	@Override
	public int compare(AbstractAttribute o1, AbstractAttribute o2) {
		return o1.getName().compareTo(o2.getName());
	}

	/**
	 * Get the value of an attribute.
	 * @param attr		Attribute to get.
	 * @return			Found attribute value or null.
	 */
	Object get(AbstractAttribute attr) {
		return attrs.get(attr);
	}
	
	/**
	 * Get an attribute value with default value.
	 * @param attr		Attribute to get.
	 * @param def		Default value.
	 * @return			Found value if the attribute is defined, else default.
	 */
	Object get(AbstractAttribute attr, Object def) {
		Object r = attrs.get(attr);
		if(r == null)
			return def;
		else
			return r;
	}
	
	/**
	 * Set the value of an attribute.
	 * @param attr		Attribute to set.
	 * @param value		Value to set.
	 */
	void set(AbstractAttribute attr, Object value) {
		attrs.put(attr, value);
	}
}
