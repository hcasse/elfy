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

import elf.data.Version;
import elf.swing.Icon;
import elf.ui.Box;
import elf.ui.Component;
import elf.ui.I18N;
import elf.ui.TextArea;
import elf.ui.View;
import elf.ui.meta.AbstractEntity;
import elf.ui.meta.Action;

/**
 * Provide common facilities required by an application.
 * @author casse
 */
public abstract class Application extends AbstractEntity {
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

	/**
	 * Get license.
	 * @return	License.
	 */
	public String getLicense() {
		return null;
	}

	/**
	 * Get the site of the application.
	 * @return	Application site.
	 */
	public String getSite() {
		return null;
	}

	/**
	 * Get authors.
	 * @return	Authors array.
	 */
	public String[] getAuthors() {
		return null;
	}

	/**
	 * Get the icon of the application.
	 * @return	Application icon.
	 */
	public Icon getLogo() {
		return null;
	}

	@Override
	public String getLabel() {
		return getName() + " " + getVersion();
	}

	/**
	 * Get the action providing an "about' dialog on the application.
	 * @return
	 */
	public Action getAboutAction(View view) {
		return new AboutAction(view);
	}
	
	/**
	 * Action displaying a model "about" dialog.
	 * @author casse
	 */
	private class AboutAction extends Action {
		private View view;
		
		public AboutAction(View view) {
			this.view = view;
		}
		
		@Override
		public void run() {
			View dialog = view.makeDialog(this);
			Box hbox = dialog.addBox(Component.HORIZONTAL);
			
			// add the logo
			Icon logo = getLogo();
			if(logo != null)
				hbox.add(logo);
			
			// add the text
			StringBuffer buf = new StringBuffer();
			TextArea text = hbox.addTextArea();
			buf.append("<h1>");
			buf.append(getLabel());
			buf.append("</h1>");
			String[] authors = getAuthors();
			/*if(authors != null) {
				buf.append("<h2>" + I18N.STD.t("Authors") + "</h2><ul>");
				for(String author: authors);
					buf.append("<li>" + author + "</li>");
			}*/
			
		}

		@Override
		public String getLabel() {
			return I18N.STD.t("About Application");
		}

		@Override
		public elf.ui.Icon getIcon() {
			return elf.ui.Icon.INFO;
		}
		
	}
}
