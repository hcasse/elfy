/*
 * Elfy library
 * Copyright (c) 2015 - Hugues Cassé <hugues.casse@laposte.net>
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

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import elf.store.FileStorage;
import elf.store.Storage;
import elf.swing.UI;

/**
 * Windows adaptation of ElfCore.
 * @author casse
 */
public class Windows extends OS {
	private UI ui;
	private Path config_path;
	
	@Override
	public Storage getConfigStore(String app, String ressource) {

		// get config path
		if(config_path == null)
			config_path = new Path(System.getProperty("user.home"));
		
		// build the path
		Path app_path = config_path.append(app);
		System.out.println("DEBUG: config_path = " + app_path);
		if(!app_path.exists())
			try {
				app_path.makeAsDir();
			} catch (IOException e) {
				System.err.println("ERROR: cannot access the configuration directory: " + e.getLocalizedMessage());
			}
		return new FileStorage(app_path.append(ressource));
	}

	@Override
	public UI getUI() {
		if(ui == null) {
			ui = new elf.swing.UI();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {
				// TODO
			} catch (InstantiationException e) {
				// TODO
			} catch (IllegalAccessException e) {
				// TODO
			} catch (UnsupportedLookAndFeelException e) {
				// TODO
			}
		}
		return ui;
	}

}

