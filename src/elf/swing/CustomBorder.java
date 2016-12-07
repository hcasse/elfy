/*
 * ElfCore library
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
package elf.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * Customizable border.
 * @author casse
 */
public class CustomBorder implements Border {
	private Border border;
	private int
		top_pad = 2,
		bottom_pad = 2,
		left_pad = 4,
		right_pad = 4;
	
	public CustomBorder(Border border) {
		this.border = border;
	}
	
	@Override
	public Insets getBorderInsets(Component c) {
		Insets i = border.getBorderInsets(c);
		i.top += top_pad;
		i.bottom += bottom_pad;
		i.left += left_pad;
		i.right += right_pad;
		return i;
	}

	@Override
	public boolean isBorderOpaque() {
		return border.isBorderOpaque();
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		border.paintBorder(c,  g, x, y, w, h);
	}

	public int getTopPad() {
		return top_pad;
	}

	public void setTopPad(int top_pad) {
		this.top_pad = top_pad;
	}

	public int getBottomPad() {
		return bottom_pad;
	}

	public void setBottomPad(int bottom_pad) {
		this.bottom_pad = bottom_pad;
	}

	public int getLeftPad() {
		return left_pad;
	}

	public void setLeftPad(int left_pad) {
		this.left_pad = left_pad;
	}

	public int getRightPad() {
		return right_pad;
	}

	public void setRightPad(int right_pad) {
		this.right_pad = right_pad;
	}

}
