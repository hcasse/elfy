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
package elf.data;

/**
 * Version representation.
 * @author casse
 */
public class Version {
	int major, minor, release;
	
	/**
	 * Build a version.
	 * @param major			Major number.
	 * @param minor			Minor number.
	 * @param release		Release number.
	 */
	public Version(int major, int minor, int release) {
		this.major = major;
		this.minor = minor;
		this.release = release;
	}

	/**
	 * Get major number.
	 * @return		Major number.
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * Get minor number.
	 * @return		Minor number.
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * Get release number.
	 * @return		Release number.
	 */
	public int getRelease() {
		return release;
	}

	@Override
	public String toString() {
		return major + "." + minor + "." + release;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Version))
			return false;
		else {
			Version v = (Version)obj;
			return major == v.major && minor == v.minor && release == v.release;
		}
	}

	/**
	 * Compare both version. The comparison does not involve the release version.
	 * @param v		Version to compare with.
	 * @return		0 for equality, >0 if the current version is greater than the current one, <0 else.
	 */
	public int compare(Version v) {
		int r = major - v.major;
		if(r == 0)
			r = minor - v.minor;
		return r;
	}
}
