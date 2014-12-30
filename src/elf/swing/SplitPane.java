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

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import elf.ui.Container;

/**
 * Swing implementation of split pane.
 * @author casse
 */
public class SplitPane extends Component implements elf.ui.SplitPane {
	private int axis;
	private elf.swing.Container fst, snd;
	private JSplitPane spane;

	public SplitPane(int axis) {
		this.axis = axis;
		fst = new elf.swing.Container.Box(axis);
		snd = new elf.swing.Container.Box(axis);		
	}
	
	@Override
	public Container getFirst() {
		return fst;
	}

	@Override
	public Container getSecond() {
		return snd;
	}

	@Override
	public JComponent getComponent() {
		if(spane == null) {
			int jaxis;
			if(axis == HORIZONTAL)
				jaxis = JSplitPane.VERTICAL_SPLIT;
			else
				jaxis = JSplitPane.HORIZONTAL_SPLIT;
			spane = new JSplitPane(jaxis, fst.getComponent(), snd.getComponent());
		}
		return spane;
	}

	@Override
	public void dispose() {
		spane = null;
		fst.dispose();
		snd.dispose();
	}

}
