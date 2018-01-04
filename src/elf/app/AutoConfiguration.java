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
package elf.app;

/**
 * Automatic configuration supporting the automatic-registration to configuration.
 * @author casse
 */
public class AutoConfiguration {
	private Configuration conf;
	
	/**
	 * Build a configuration registering itself to the given application.
	 * the configuration item to save and load are the attributes of the super-class
	 * of this class.
	 * 
	 * @param app		Application to register to.
	 * @param name		Name of the configuration.
	 */
	public AutoConfiguration(Application app, String name) {
		conf = new Configuration(app, name, this);
	}
	
	/**
	 * Record a modification.
	 */
	public void modify() {
		conf.modify();
	}
}
