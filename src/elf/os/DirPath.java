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

/**
 * Directory path.
 * @author casse
 */
public class DirPath extends Path {

	public DirPath(DirPath path) {
		super(path);
	}

	public DirPath(String path) {
		super(path);
	}

	/**
	 * Get the current working directory.
	 * @return		Current working directory.
	 */
	public static DirPath getWorkingDirectory() {
		return new DirPath(System.getProperty("user.dir"));
	}
}
