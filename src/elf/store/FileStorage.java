/*
 * Elfy library
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

import elf.os.Path;

public class FileStorage implements Storage {
	Path file;
	
	/**
	 * Build a storage on the OS file system.
	 * @param file		File path.
	 */
	public FileStorage(Path file) {
		this.file = file;
	}
	
	/**
	 * Build a storage on the OS file system.
	 * @param file		File path.
	 */
	public FileStorage(String file) {
		this.file = new Path(file);
	}
	
	@Override
	public String getName() {
		return "file:" + file;
	}

	@Override
	public InputStream read() throws IOException {
		return file.read();
	}

	@Override
	public OutputStream write() throws IOException {
		return file.write();
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

}
