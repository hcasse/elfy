/*
 * ElfUI library
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
package elf.swing.canvas;

import java.awt.event.MouseEvent;

/**
 * Handler for the mouse-over events.
 * @author casse
 */
public interface OverHandler {

	/**
	 * Called when the over-handler is installed.
	 * @param canvas	Owner canvas.
	 */
	void install(Canvas canvas);
	
	/**
	 * Called when the over-handler is uninstalled.
	 */
	void uninstall();
	
	/**
	 * Called each time the mouse is moved.
	 * @param event		Mouse motion event.
	 */
	void onMove(MouseEvent event);
	
	/**
	 * Called when the mouse leaves the window.
	 */
	void onLeave();
	
}
