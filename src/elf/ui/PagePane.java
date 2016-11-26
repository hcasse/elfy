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

import elf.ui.meta.Action;

/**
 * A pane made of succeeding pages (one after one). 
 * @author casse
 */
public interface PagePane extends Component {
	public static final Listener NULL = new Listener() {
		@Override public void onShow() { }
		@Override public void onHide() { }
	};
	
	/**
	 * Add a new page.
	 * @return	Added page.
	 */
	Page addPage();
	
	/**
	 * Set the current page.
	 * @param page	Current page.
	 */
	void set(Page page);
	
	/**
	 * Push a new page, saving the previous page in a stack.
	 * @param page		Pushed page.
	 */
	void push(Page page);
	
	/**
	 * Pop the previously pushed page.
	 */
	void pop();
	
	/**
	 * Go back to the previous page.
	 */
	void back();
	
	/**
	 * Get action causing a back move in the pages.
	 * @return		Back action.
	 */
	Action getBackAction();
	
	/**
	 * A page in the page pane.
	 * @author casse
	 */
	public interface Page extends Container {
	
		/**
		 * Set the page listener.
		 * @param listener	Current page listener.
		 */
		void setListener(Listener listener);
		
	}
	
	/**
	 * Listener of pane page.
	 * @author casse
	 */
	public interface Listener {
		
		/**
		 * Called when the page is shown.
		 */
		void onShow();
		
		/**
		 * Called on page hide.
		 */
		void onHide();
		
	}
	
}
