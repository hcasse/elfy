/*
 * Orchid Framework
 * Copyright (c) 2009 - Hugues Cassé <hugues.casse@laposte.net>
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

/**
 * This class facilitate the support of I18N.
 * It gives transparent access to I18N number display and make easier
 * the translation work by accessing a family of files from a basic URL.
 * The files must be named URL/i18n_LANG_COUNTRY.txt
 * @author casse
 */
public class I18N {
	public static final String PREFIX = "../../i18n/", SUFFIX = ".txt";
	Locale locale = Locale.getDefault();
	Properties props = new Properties();
	
	/**
	 * Build an I18N support.
	 * @param name		Module name.
	 * @param locales	Supported locales.
	 */
	public I18N(String name, Locale[] locales) {
		
		// look for the good I18N file
		for(Locale loc: locales) {
			
			// build the full name
			int p = -1;
			StringBuffer buf = new StringBuffer();
			buf.append(PREFIX);
			buf.append(loc.getLanguage());
			if(!loc.getCountry().isEmpty()) {
				p = buf.length();
				buf.append('_');
				buf.append(loc.getCountry());
			}
			buf.append('/');
			buf.append(name);
			buf.append(SUFFIX);
			if(load(buf.toString()))
				break;
			
			// if a country is given, try without the country
			if(p >= 0) {
				buf.setLength(p);
				buf.append('/');
				buf.append(name);
				buf.append(SUFFIX);
				if(load(buf.toString()))
					break;
			}
		}
	}
	
	/**
	 * Try to load the properties from the given path file.
	 * @param path		Path to load from.
	 * @return			True if load is successful, false else.
	 */
	private boolean load(String path) {
		URL url = I18N.class.getResource(path);
		if(url == null)
			return false;
		try {
			InputStream stream = url.openStream();
			props.load(new InputStreamReader(stream, "UTF-8"));
			stream.close();
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * Get the translation for the given.
	 * @param key		Key to translate.
	 * @return			Translated key.
	 */
	public String t(String key) {
		return props.getProperty(key, key);
	}
	
}
