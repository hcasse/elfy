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
package elf.ui;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

// VERSION=1.0

/**
 * Handle a set of icons.
 * @author casse
 */
public class IconManager {
	private URL path;
	private Hashtable<String, Icon> icons = new Hashtable<String, Icon>();
	private Icon broken;

	public static final String
		ICON_BACK = "back",
		ICON_MENU = "menu",
		ICON_QUIT = "quit",
		ICON_INFO = "info",
		ICON_WARNING = "warning",
		ICON_ERROR = "error",
		ICON_HELP = "help";
	public static final IconManager STD;
	static {
		IconManager i = null;
		try {
			i = new IconManager(new URL(IconManager.class.getResource("."), "../../pix"));
		} catch (MalformedURLException e) {
			System.err.println("INTERNAL ERROR: no standard icons");
		}
		STD = i;
	}

	/**
	 * Build the icon manager.
	 * @param path		Path to get the icon from.		
	 */
	public IconManager(URL path) {
		this.path = path;
	}
	
	/**
	 * Get the broken icon.
	 * @return	Icon for broken image.
	 */
	public Icon getBroken() {
		if(broken == null) {
			URL url;
			try {
				url = IconManager.class.getResource("/pix/broken.png");
				Image image = ImageIO.read(url);
				broken = new Icon.Image(image);			
			} catch (IOException e) {
				System.err.println("ERROR: no broken icon");
			}
		}
		return broken;
	}
	
	/**
	 * Get the icon for the given name. Look for a file named path / name ".png".
	 * @param name		Name of the icon.
	 * @return			Found icon or a null icon.
	 */
	public Icon get(String name) {
		Icon icon = icons.get(name);
		if(icon == null) {
			try {
				URL url = new URL(path.toString() + "/" + name + ".png");
				Image image = ImageIO.read(url);
				icon = new Icon.Image(image);
			} catch (MalformedURLException e) {
				icon = getBroken();
			} catch (IOException e) {
				icon = getBroken();
			}
			icons.put(name, icon);
		}
		return icon;
	}
}
