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

import elf.data.Version;
import elf.store.FileStorage;
import elf.store.Storage;
import elf.store.StructuredStore;
import elf.store.XMLStructuredStore;

/**
 * Interface providing access to OS features.
 * @author casse
 */
public class OS {
	public static final Version VERSION = new Version(0, 9, 0);
	public static final OS os = findOS();
	private Path config_path;
	
	/**
	 * Get the current OS.
	 * @return		Current OS.
	 */
	private static OS findOS() {
		 String name = System.getProperty("os.name").toLowerCase();
		 if(name.indexOf("win") >= 0)
			 return new OS();	// Windows
		 else if(name.indexOf("mac") >= 0)
			 return new Linux();	// should be fixed
		 else if(name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0 )
			 return new Linux();	// maybe we can make difference between Unices ?
		 else if(name.indexOf("sunos") >= 0)
		 	return new Linux();
		 else
			 return new OS();
	}
	
	/**
	 * Get the version of the ELF system.
	 * @return		ELF system version.
	 */
	public static Version getElfVersion() {
		return VERSION;
	}
	
	/**
	 * Get access to configuration storage.
	 * @param app			Name of the application.
	 * @param ressource		Name of the ressource.
	 * @return				Storage for the configuration.
	 */
	public Storage getConfigStore(String app, String ressource) {
		if(config_path == null)
			config_path = new Path(System.getProperty("user.dir"));
		return new FileStorage(config_path + File.separator + app + File.separator + ressource);
	}
	
	/**
	 * Get user home.
	 * @return	User home.
	 */
	public Path getHome() {
		return Path.getHome();
	}
	
	/**
	 * Get storage for local data of an application.
	 * The difference between local data and configuration is that local does not represent
	 * options of the application or customization of the application. The difference between
	 * local data and user / file data is that the user is not supposed to handle directly
	 * these data (they may be hidden somewhere in the user home directory).
	 * @param app			Application.
	 * @param ressource		Ressource name.
	 * @return				Storage if any.
	 */
	public Storage getLocalStore(String app, String ressource) {
		return new FileStorage(getHome().append(app).append(ressource));
	}
	
	/**
	 * Get a structured for configuration store.
	 * @param app			Name of the application.
	 * @param ressource		Name of the ressource.
	 * @return		Structured store.
	 */
	public StructuredStore getConfig(String app, String ressource) {
		return new XMLStructuredStore(getConfigStore(app, ressource));
	}
	
}
