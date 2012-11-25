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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provide a stream access to an OS ressource.
 * @author casse
 */
public interface Storage {

	/**
	 * Get an identification for the storage.
	 * @return		Storage identification.
	 */
	String getName();
	
	/**
	 * Test if the store actually exists.
	 * @return		True if it exists, false else.
	 */
	boolean exists();
	
	/**
	 * Obtain a stream for reading the storage.
	 * @return					Built input stream.
	 * @throws IOException		IO error.
	 */
	InputStream read() throws IOException;
	
	/**
	 * Obtain a stream for writing to the storage.
	 * @return					Built output stream.
	 * @throws IOException		IO error.
	 */
	OutputStream write() throws IOException;
}
