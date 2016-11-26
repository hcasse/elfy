/*
 * ElfCore library
 * Copyright (c) 2014 - Hugues Cassé <hugues.casse@laposte.net>
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
import java.net.URL;

import elf.ui.meta.Entity;

/**
 * Generic way to access graphical User Interface.
 * @author casse
 */
public interface UI {

	/**
	 * Build a new view (using entity information for identification).
	 * @param entity	Entity representing the view.
	 * @return			Created view.
	 */
	View makeView(Entity entity);
	
	/**
	 * Start a task.
	 * @param task	Started task.
	 */
	void start(Task task);
	
	/**
	 * Stop a task.
	 * @param task	Stopped task.
	 */
	void stop(Task task);
	
	/**
	 * Interface of programmable task.
	 * @author casse
	 */
	public static abstract class Task {
		private boolean periodic;
		private long period;
		
		public Task(long period) {
			this.period = period;
		}
		
		public Task(long period, boolean periodic) {
			this.period = period;
			this.periodic = periodic;
		}
		
		/**
		 * Get current period.
		 * @return	Period,.
		 */
		public long getPeriod() {
			return period;
		}
		
		/**
		 * Task if the task is periodic.
		 * @return	True for periodic, false else.
		 */
		public boolean isPeriodic() {
			return periodic;
		}
		
		/**
		 * Called when the task start.
		 */
		public abstract void run();
		
	}

	/**
	 * Load a sound from the given URL.
	 * @param url			URL of the sound.
	 * @return				Loaded sound.
	 * @throws IOException	Thrown if there is an error during load.
	 */
	public Sound getSound(URL url) throws IOException;
	
	/**
	 * Get the color from RGB(A) form.
	 * @param rgba	RGBA form (with or without alpha).
	 * @return		Matching color.
	 */
	public Color getColor(String rgba);

	/**
	 * Get the color from red, green, blue components.
	 * @param r		Red component in [0, 255].
	 * @param g		Green component in [0, 255].
	 * @param b		Blue component in [0, 255].
	 * @return		Matching color.
	 */
	public Color getColor(int r, int g, int b);
	
	/**
	 * Get the color from red, green, blue, alpha components.
	 * @param r		Red component in [0, 255].
	 * @param g		Green component in [0, 255].
	 * @param b		Blue component in [0, 255].
	 * @param a		Alpha component in [0, 255].
	 * @return		Matching color.
	 */
	public Color getColor(int r, int g, int b, int a);

	/**
	 * Represents a color in the current UI.
	 * @author casse
	 */
	public interface Color {
		
		/**
		 * Get the textual representation of the color:
		 * #RRGGBBAA
		 * @return	RGBA representation.
		 */
		public String getRGBA();
		
		/**
		 * Get the textual representation of the color:
		 * #RRGGBB
		 * @return	RGB representation.
		 */
		public String getRGB();
		
		/**
		 * Get red component.
		 * @return	Red in [0, 255].
		 */
		public int getRed();
		
		/**
		 * Get green component.
		 * @return	Green in [0, 255].
		 */
		public int getGreen();
		
		/**
		 * Get blue component.
		 * @return	Blue in [0, 255].
		 */
		public int getBlue();
		
		/**
		 * Get alpha component.
		 * @return	Alpha in [0, 255].
		 */
		public int getAlpha();
			
	}

}
