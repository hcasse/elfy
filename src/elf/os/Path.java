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
package elf.os;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Path management (easier than the original Java file class).
 * @author casse
 */
public class Path {
	static Path home;
	File file;
	
	/**
	 * Get the home path.
	 * @return		Home path.
	 */
	public static Path getHome() {
		if(home == null)
			home = new Path(System.getProperty("user.home"));
		return home;
	}
	
	/**
	 * Build a path from a string.
	 * @param path		Path as string.
	 */
	public Path(String path) {
		file = new File(path);
	}
	
	/**
	 * Build a path by cloning.
	 * @param path	Cloned path.
	 */
	public Path(Path path) {
		file = path.file;
	}
	
	/**
	 * Append a component to the current path.
	 * @param name		Appended component.
	 * @return			Path with the component appended.
	 */
	public Path append(String name) {
		return new Path(this + File.separator + name);
	}
	
	/**
	 * Get the parent path.
	 * @return		Parent path.
	 */
	public Path getParent() {
		return new Path(file.getParent());
	}
	
	/**
	 * Test if the file exists.
	 * @return		True if it exists, false else.
	 */
	public boolean exists() {
		return file.exists();
	}
	
	/**
	 * Build the current path as a directory, possibly building
	 * missing sub-directories.
	 * @throws IOException
	 */
	public void makeAsDir() throws IOException {
		if(!file.mkdirs())
			throw new IOException("cannot create dircetory " + file);
	}
	
	/**
	 * Get input from the file matching the path for reading.
	 * @return					Open input stream.
	 * @throws IOException		Thrown in case of error.
	 */
	public InputStream read() throws IOException {
		return new FileInputStream(file);
	}
		
	/**
	 * Get output from the file matching the path for reading.
	 * @return					Open output stream.
	 * @throws IOException		Thrown in case of error.
	 */
	public OutputStream write() throws IOException {
		return new FileOutputStream(file);		
	}

	@Override
	public String toString() {
		return file.toString();
	}
	
}
