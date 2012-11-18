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
package elf.ui.canvas;

import java.awt.event.MouseEvent;

/**
 * Interface for canvas drag handler.
 * @author casse
 */
public interface DragHandler {
	public static final DragHandler NULL =
		new DragHandler() {
			@Override public void install(Canvas canvas) { }
			@Override public void uninstall() { }
			@Override public void onBegin(MouseEvent event) { }
			@Override public void onDrag(MouseEvent event) { }
			@Override public void onEnd(MouseEvent event) { }
		};
	
	/**
	 * Called when the handler is installed.
	 * @param canvas	Current canvas.
	 */
	void install(Canvas canvas);
	
	/**
	 * Called when the handler is uninstalled.
	 */
	void uninstall();
	
	/**
	 * Called when a drag begin.
	 * @param event		Mouse event.
	 */
	void onBegin(MouseEvent event);
	
	/**
	 * Called during the drag.
	 * @param event		Mouse event.
	 */
	void onDrag(MouseEvent event);
	
	/**
	 * Called when the drag ends.
	 * @param event		Mouse event.
	 */
	void onEnd(MouseEvent event);
}
