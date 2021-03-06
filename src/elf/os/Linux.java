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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import elf.store.FileStorage;
import elf.store.Storage;
import elf.swing.UI;

/**
 * Linux implementation of OS interface.
 * Try to be freedesktop compatible (http://www.freedesktop.org/wiki/).
 * @author casse
 */
public class Linux extends OS {
	private elf.swing.UI ui;
	Path config_path;
	
	@Override
	public Storage getConfigStore(String app, String ressource) {

		// get config path
		if(config_path == null) {
			String config = System.getenv("XDG_CONFIG_HOME");
			if(config != null)
				config_path = new Path(config);
			else {
				config = System.getenv("HOME");
				if(config != null)
					config_path = new Path(config);
				else
					config_path = Path.getHome();
				config_path = config_path.append(".config");
			}
		}
		
		// build the path
		Path app_path = config_path.append(app);
		if(!app_path.exists())
			try {
				app_path.makeAsDir();
			} catch (IOException e) {
				System.err.println("ERROR: cannot access the configuration directory: " + e.getLocalizedMessage());
			}
		return new FileStorage(app_path.append(ressource));
	}

	@Override
	public Storage getLocalStore(String app, String ressource) throws IOException {
		Path apath = getHome().append("." + app);
		if(!apath.exists())
			apath.makeAsDir();
		return new FileStorage(apath.append(ressource));
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

	@Override
	public void browse(URL url) throws IOException {
		try {
			java.awt.Desktop.getDesktop().browse(url.toURI());
		} catch (IOException e) {
			throw new IOException("cannot open browser", e);
		} catch (URISyntaxException e) {
			throw new IOException("cannot open browser", e);
		}
	}

}
