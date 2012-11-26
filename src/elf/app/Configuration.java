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
package elf.app;

import java.io.IOException;

import elf.os.OS;
import elf.store.AutoStructuredSerializer;
import elf.store.StructuredStore;

/**
 * A configuration manages a configuration object and is responsible
 * for loading and saving it.
 * @author casse
 */
public class Configuration {
	Application app;
	String name;
	Object object;
	boolean modified;
	
	/**
	 * Build a configuration for the given object.
	 * @param app			Owner application.
	 * @param name			Configuration name.
	 * @param object		Object containing the configuration.
	 */
	public Configuration(Application app, String name, Object object) {
		this.app = app;
		app.configs.add(this);
		this.name = name;
		this.object = object;
		modified = false;
	}
	
	/**
	 * Inform the configuration it is modified.
	 */
	public void modify() {
		modified = true;
	}
	
	/**
	 * Ensure the configuration is loaded
	 * (if the configuration does not exist, do nothing).
	 */
	public void load() throws IOException {
		assert app != null;
		StructuredStore store = OS.os.getConfig(app.getName(), name);
		if(store.exists()) {
			AutoStructuredSerializer serial = new AutoStructuredSerializer(store, object);
			serial.load();
		}
	}
	
	/**
	 * Save the current configuration if required.
	 */
	public void save() throws IOException {
		if(!modified)
			return;
		StructuredStore store = OS.os.getConfig(app.getName(), name);
		AutoStructuredSerializer serial = new AutoStructuredSerializer(store, object);
		serial.save();
	}
}

