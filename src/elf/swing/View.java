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
package elf.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import elf.ui.meta.Action;
import elf.ui.meta.Entity;

/**
 * Swing version of view.
 * @author casse
 */
public class View extends Container implements elf.ui.View {
	private Entity entity;
	private JFrame frame;
	private Action close_action = Action.NULL;
	
	public View(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Get the current JFrame.
	 * @return	Current JFrame.
	 */
	public JFrame getFrame() {
		if(frame == null) {
			frame = new JFrame(entity.getLabel());
			
			// prepare the sub-components
			for(Component component: getComponents())
				frame.getContentPane().add(component.getComponent());
			
			// prepare the frame
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) { close_action.run(); }
			});
			frame.setSize(500, 300);
		}
		return frame;
	}
	
	@Override
	public void dispose() {
		frame = null;
	}

	@Override
	public void show() {
		getFrame().setVisible(true);
	}

	@Override
	public void hide() {
		if(frame != null)
			getFrame().setVisible(false);
	}

	@Override
	public void setCloseAction(Action action) {
		close_action = action;
	}

	@Override
	public JComponent getComponent() {
		return null;
	}

}
