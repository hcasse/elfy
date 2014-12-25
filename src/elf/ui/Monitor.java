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

/**
 * Monitor provides UI independent way to communicate messages to the user.
 * Depending on the current environment, messages are displayed in an alert dialog,
 * to a textual console or in a log file.
 * @author casse
 */
public interface Monitor {
	public static final Monitor NULL = new TextMonitor();
	
	/**
	 * Simple information to the user (display is not mandatory).
	 * @param message	Message to display.
	 */
	void info(String message);
	
	/**
	 * Warning message to the user (display may not be immediate but accessible).
	 * @param message	Message to display.
	 */
	void warn(String message);
	
	/**
	 * Error message for the user (display immediate).
	 * @param message	Message to display.
	 */
	void error(String message);
	
	/**
	 * Panic message for the user: according the available resources,
	 * must be shown to the user as fast as possible.
	 * @param message	Message to display.
	 */
	void panic(String message);
	
	/**
	 * Begin a message session: a job is done and may be produce
	 * several messages. They will be displayed together at the end
	 * of the job.
	 * @param title		Title of the job.
	 */
	void begin(String title);
	
	/**
	 * End a job session started from begin().
	 * @return	True if there is no error in the job, false else (info and warning are not counted as errors).
	 */
	boolean end();
	
}
