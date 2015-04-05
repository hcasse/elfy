/*
 * Elfy library
 * Copyright (c) 2015 - Hugues Cassé <hugues.casse@laposte.net>
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
package elf.ui.meta;

import elf.ui.Icon;

/**
 * Entity providing message and/or icon.
 * @author casse
 */
public class Label extends AbstractEntity {
	private String message;
	private Icon icon;
	
	public Label(String message) {
		this.message = message;
	}
	
	public Label(Icon icon, String message) {
		this.icon = icon;
		this.message = message;
	}
	
	@Override
	public String getLabel() {
		return message;
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

}
