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
package elf.store;

import java.io.IOException;

/**
 * Structured allows to save/load data in a structured way.
 * @author casse
 */
public interface StructuredStore {

	/**
	 * Test if the object is supported by the serialization.
	 * @param type		Type to test.
	 * @return			True if is supported, false else.
	 */
	public boolean supports(Class<?> type);
	
	/**
	 * Test if the store actually exists.
	 * @return		True if it exists, false else.
	 */
	public boolean exists();
	
	/**
	 * Get a Save structure to perform save.
	 * @return		Save structure.
	 */
	public Save save() throws IOException;
	
	/**
	 * Get a Load structure to load.
	 * @return		Load structure.
	 */
	public Load load() throws IOException;
	
	/**
	 * Provide facility to perform save.
	 * @author casse
	 */
	public interface Save {
		
		/**
		 * Put a value.
		 * @param value
		 * @throws IOException		IO error.
		 */
		void put(Object value) throws IOException;
		
		/**
		 * Put a list of values.
		 * @throws IOException		IO error.
		 */
		void putList() throws IOException;
		
		/**
		 * Put a structure of values (fields).
		 * @throws IOException		IO Error.
		 */
		void putStruct() throws IOException;
		
		/**
		 * Put a field as part of a structure.
		 * @param name				Field name.
		 * @throws IOException		IO error.
		 */
		void putField(String name) throws IOException;
		
		/**
		 * End the current structure or list.
		 * @throws IOException		IO error.
		 */
		void end() throws IOException;
		
	}
	
	/**
	 * Structure to perform a load.
	 * @author casse
	 */
	public interface Load {
	
		/**
		 * Get a value.
		 * @param type		Type of value.
		 * @return			Got value.
		 * @throws IOException		IO error.
		 */
		Object get(Class<?> type) throws IOException;
		
		/**
		 * Get a list of values.
		 * @return		Number of items in the list.
		 * @throws IOException		IO error.
		 */
		int getList() throws IOException;
		
		/**
		 * Get a structure of values (fields).
		 * @throws IOException		IO Error.
		 */
		void getStruct() throws IOException;
		
		/**
		 * Get a field as part of a structure.
		 * @param name				Field name.
		 * @return					True if the field has been found, false else.
		 * @throws IOException		IO error.
		 */
		boolean getField(String name) throws IOException;
		
		/**
		 * End the current structure or list.
		 * @throws IOException		IO error.
		 */
		void end() throws IOException;
		
	}
	
	/**
	 * Thrown if the save/load methods are not called unconsistantly.
	 * @author casse
	 */
	public class StructuralError extends Error {
		private static final long serialVersionUID = -1979448985909750093L;
		
		public StructuralError(String message) {
			super(message);
		}
	}
}
