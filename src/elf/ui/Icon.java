/*
 * Elfy tool
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
package elf.ui;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents an image used in the UI (for button menus, etc).
 * @author casse
 */
public class Icon {
	public static final Manager STD = makeSTD();
	public static final Icon NULL = new Icon();
	public static final Icon
		BACK 		= STD.get("back"),
		MENU 		= STD.get("menu"),
		QUIT 		= STD.get("quit"),
		INFO 		= STD.get("info"),
		WARNING		= STD.get("warning"),
		ERROR		= STD.get("error"),
		HELP		= STD.get("help"),
		BROKEN		= STD.get("broken");

	private URL url;

	/**
	 * Build STD manager.
	 * @return	STD manager.
	 */
	private static Manager makeSTD() {
		if(System.getenv("ELFY_DEV") == null) 
			return new Manager(Icon.class.getResource("/pix/"));
		else
			try {
				return new Manager(new URL(Icon.class.getResource("."), "../../pix/"));
			} catch (MalformedURLException e) {
				System.err.println("INTERNAL ERROR: no standard icons");
				return null;
			}
	}
	
	/**
	 * Null icon constructor.
	 */
	private Icon() {
	}
	
	/**
	 * Build a new icon.
	 */
	public Icon(URL url) {
		this.url = url;
	}
	
	/**
	 * Get URL pf the icon
	 * @return	Icon URL.
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * An icon manager provide automatic access to icon by name
	 * and automatic storage (avoiding duplication). The icon manager
	 * allows also to automatically load the icon from any file system.
	 * @author casse
	 */
	public static class Manager {
		private URL base;
		
		/**
		 * Build the icon manager.
		 * @param base	Base to find back icons.
		 */
		public Manager(URL base) {
			this.base = base;
		}
		
		/**
		 * Build an icon by its name.
		 * @param name	Icon name.
		 * @return		Loaded icon or null.
		 */
		public Icon get(String name) {
			try {
				return new Icon(new URL(base, name + ".png"));
			} catch (MalformedURLException e) {
				System.err.println("INTERNAL ERROR: icon: " + e.getLocalizedMessage());
				return getBroken();
			}
		}
		
		/**
		 * Get the broken icon.
		 * @return	Broken icon.
		 */
		public Icon getBroken() {
			return BROKEN;
		}
		
	}

}
