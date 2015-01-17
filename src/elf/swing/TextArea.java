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
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * TextArea implementation in Swing.
 * @author casse
 */
public class TextArea extends Component implements elf.ui.TextArea {
	private JTextPane pane;
	private JScrollPane spane;
	private StringBuffer text = new StringBuffer();

	@Override
	public void clear() {
		text = new StringBuffer();
	}

	@Override
	public void display(String text) {
		this.text.append(text + "\n");
		if(pane != null)
			pane.setText(this.text.toString());
	}

	@Override
	public JComponent getComponent() {
		if(spane == null) {
			pane = new JTextPane();
			pane.setContentType("text/html");
			pane.setText(text.toString());
			spane = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return spane;
	}

	@Override
	public void dispose() {
		super.dispose();
		pane = null;
		spane = null;
		text = new StringBuffer();
	}

}
