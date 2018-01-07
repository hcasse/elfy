/*
 * ElfCore library
 * Copyright (c) 2017 - Hugues Cassé <hugues.casse@laposte.net>
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
package elf.text;

import java.util.HashMap;

/**
 * The formatter class is dedicated to output string containing escape sequences between "@@" and "@@".
 * The sequence may be composed by an identifier that will be looked in a map, converted to string and will replace
 * the escape sequence. If it contains an additional "@" (not the last one), the following characters are considered
 * as format for Java formatting facilities. The corresponding object retrieved from the map is displayed
 * accordingly. If an identifier cannot be found in the map, 
 * 
 * The formatter is well-adapted for internationalization strings where the escape sequences order does not always
 * match the argument order.
 * 
 * @author casse
 */
public class Formatter {
	private HashMap<String, Object> map = new HashMap<String, Object>(); 
	
	/**
	 * Build a formatter. The values are a sequence of (key, value) pair representing the identifiers and their
	 * corresponding value.
	 * @param values	(identifier, value) sequence.
	 */
	public Formatter(Object... values) {
		String key = null;
		for(int i = 0; i < values.length; i++) {
			if(key == null)
				key = (String)values[i];
			else {
				map.put(key, values[i]);
				key = null;
			}
		}
	}
	
	/**
	 * Build a formatter by cloning the given one.
	 * @param f		Formatted to clone.
	 */
	public Formatter(Formatter f) {
		map.putAll(f.map);
	}
	
	/**
	 * Add the given identifier for the given object to the map.
	 * @param id	Identifier.
	 * @param val	Value.
	 */
	public void put(String id, Object val) {
		map.put(id, val);
	}
	
	/**
	 * Remove the given identifier.
	 * @param id	Removed identifier.
	 */
	public void remvove(String id) {
		map.remove(id);
	}
	
	/**
	 * Perform the replacement of escape sequences in the given string.
	 * @param s					String to replace in.
	 * @return					String where are sequences has been replaced.
	 * @throws NotFound			If an identifier is missing in the map.
	 * @throws UnclosedEscape	If an escape sequence is not closed.
	 */
	public String format(String s) {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		while(i < s.length()) {
			
			// get first index
			int f = s.indexOf("@@", i);
			if(f < 0) {
				buf.append(s.substring(i, s.length()));
				break;
			}
			
			// get last index
			int l = s.indexOf("@@", f + 2);
			if(l < 0)
				throw new UnclosedEscape(f);
			
			// look for an internal format
			int p = s.indexOf('@', f + 2);
			String id, fmt;
			if(p == l) {
				id = s.substring(f + 2, l);
				fmt = null;
			}
			else {
				id = s.substring(f + 2, p);
				fmt = s.substring(p + 1, l);
			}
			
			// look for the identifier
			Object val = map.get(id);
			if(val == null)
				throw new NotFound(id);
			
			// display text before
			if(i != f)
				buf.append(s.substring(i, f));
			
			// display the text itself
			if(fmt == null)
				buf.append(val.toString());
			else
				buf.append(String.format("%" + fmt, val));
			
			// move to next
			i = l + 2;
		}
		return buf.toString();
	}
	
	/**
	 * Error raised when an identifier cannot be found in the map.
	 * @author casse
	 */
	public static class NotFound extends Error {
		private static final long serialVersionUID = 1L;
		private String name;
		
		public NotFound(String name) {
			this.name = name;
		}

		@Override
		public String getMessage() {
			return "identifier " + name + " is undefined";
		}
		
	}

	/**
	 * Error raised when an escape sequence is not closed.
	 * @author casse
	 */
	public static class UnclosedEscape extends Error {
		private static final long serialVersionUID = 1L;
		private int p;
		
		public UnclosedEscape(int p) {
			this.p = p;
		}

		@Override
		public String getMessage() {
			return "escape sequence starting at " + p + " is not closed";
		}
		
	}
}
