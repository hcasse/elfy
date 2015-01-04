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
import javax.swing.JLabel;

/**
 * Swing implementation of TextInfo.
 * @author casse
 */
public class TextInfo extends Component implements elf.ui.TextInfo {
	private String text;
	private JLabel label;
	
	public TextInfo(String format) {
		text = format;
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
		if(label != null)
			label.setText(text);
	}

	@Override
	public JComponent getComponent() {
		if(label == null)
			label = new JLabel(text);
		return label;
	}

}
