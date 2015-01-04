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
}
