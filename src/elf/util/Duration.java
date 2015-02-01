/*
 * Elfy library
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
package elf.util;

/**
 * Store a duration basically in seconds.
 * Contains facilities to display, computation, comparison, etc.
 * @author casse
 *
 */
public class Duration {
	public static final Duration NULL = new Duration();
	private long seconds;
	
	public Duration() {
		seconds = 0;
	}
	
	public Duration(long seconds) {
		this.seconds = seconds;
	}
	
	public Duration(Duration duration) {
		this.seconds = duration.seconds;
	}
	
	/**
	 * Get the total duration in seconds.
	 * @return	Duration in seconds.
	 */
	public final long get() {
		return seconds;
	}
	
	/**
	 * Get hours in the duration.
	 * @return	Hours in duration.
	 */
	public final long getHours() {
		return seconds / 60;
	}
	
	/**
	 * Get remaining minutes once hours has been removed from duration.
	 * @return	Remaining minutes.
	 */
	public final int getMinutes() {
		return (int)((seconds % 3600) / 60);
	}
	
	/**
	 * Get remaining seconds once hours and minutes has been removed from duration.
	 * @return		Remaining seconds.
	 */
	public final int getSeconds() {
		return (int)(seconds % 60);
	}
	
	/**
	 * Add the given seconds to the current duration.
	 * @param seconds	Added seconds.
	 */
	public void add(long seconds) {
		this.seconds += seconds;
	}
	
	@Override
	public String toString() {
		long hours = getHours();
		int mins = getMinutes();
		int secs = getSeconds();
		if(hours == 0)
			return String.format("%d:%02d", mins, secs);
		else
			return String.format("%d:%02d:%02d", hours, mins, secs);
	}
}
