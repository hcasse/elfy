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

import java.io.IOException;
import java.util.LinkedList;

import elf.data.Attribute;
import elf.data.Attributes;
import elf.data.Version;

/**
 * Provide common facilities required by an application.
 * @author casse
 */
public abstract class Application extends Attributes {
	public static final Attribute<String>
		AUTHOR = new Attribute<String>("elf.app.Application.AUTHOR"),
		DESCRIPTION = new Attribute<String>("elf.app.Application.DESCRIPTION"),
		LICENSE = new Attribute<String>("elf.app.Application.LICENSE"),
		SITE = new Attribute<String>("elf.app.Application.SITE");

	private String name;
	private Version version;
	LinkedList<Configuration> configs = new LinkedList<Configuration>();
	
	/**
	 * Build an application.
	 * @param name		Application name.
	 * @param version	Application version.
	 */
	public Application(String name, Version version) {
		this.name = name;
		this.version = version;
	}
	
	/**
	 * Get the application name.
	 * @return			Application name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the application version.
	 * @return			Application version.
	 */
	public Version getVersion() {
		return version;
	}
	
	/**
	 * Called to start the application.
	 */
	protected abstract void proceed();
	
	/**
	 * Start the application.
	 * @param argv		Command arguments.
	 */
	public void run(String[] argv) {
		
		// perform initialize
		for(Configuration config: configs)
			try {
				config.load();
			} catch (IOException e) {
				System.out.println("ERROR: " + e.getLocalizedMessage());
				e.printStackTrace();
				return;
			}
		
		// prepare exit hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override public void run() { cleanup(); }
		});
		
		// run the application
		proceed();
	}
	
	/**
	 * Perform the cleanup action (done before exiting).
	 * May be overload but the super method must be called !
	 */
	protected void cleanup() {
		for(Configuration config: configs)
			try {
				config.save();
			} catch (IOException e) {
				System.out.println("ERROR: " + e.getLocalizedMessage());
				return;
			}
	}
	
}
