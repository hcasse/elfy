/*
 * ElfCore tool
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

import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * An icon in the Orchid meaning, that is an image, with different display
 * flavours according the current component state. 
 * @author H. Cassé
 */
public interface Icon {
	public static final int
		NORMAL = 0x00,
		SELECTED = 0x01,
		DISABLED = 0x02;
	public static final int
		TEXTUAL = 0,
		TOOLBAR = 1,
		DESKTOP = 2,
		ORIGINAL = 3;
	public static final int
		MAX_TEXTUAL = 16,
		MAX_TOOLBAR = 24,
		MAX_DESKTOP = 32;
	public static final Icon NULL = new Null();
	public static final Icon BROKEN = Image.getBroken();

	/**
	 * Get an icon according the given state.
	 * @param state		OR'ed composition of SELECTED, DISABLED, etc.
	 * @return			Matching icon (null if there is no icon).
	 */
	public javax.swing.Icon get(int state);
	
	/**
	 * Get an icon according the given state.
	 * @param state		OR'ed composition of SELECTED, DISABLED, etc.
	 * @param size		One of TEXTUAL, TOOLBAR, DESKTOP
	 * @return			Matching icon (null if there is no icon).
	 */
	public javax.swing.Icon get(int state, int size);
	
	/**
	 * Null icon. 
	 */
	public class Null implements Icon {

		@Override
		public javax.swing.Icon get(int state) {
			return null;
		}

		@Override
		public javax.swing.Icon get(int state, int size) {
			return null;
		}
		
	}
	
	/**
	 * Implementation of an icon with a single image.
	 * @author H. Cassé
	 */
	public class Simple implements Icon {
		javax.swing.Icon icon;		
		
		public Simple(javax.swing.Icon icon) {
			this.icon = icon;
		}
		
		@Override
		public javax.swing.Icon get(int state) {
			return get(state, TEXTUAL);
		}

		@Override
		public javax.swing.Icon get(int state, int size) {
			if(state == 0)
				return icon;
			else
				return null;
		}
		
	}
	
	/**
	 * Icon based on an AWT image.
	 * @author casse
	 */
	public static class Image implements Icon {
		java.awt.Image image;
		int w, h;
		javax.swing.Icon textual, toolbar, desktop;		

		public Image(java.awt.Image image) {
			this.image = image;
			w = image.getWidth(null);
			h = image.getHeight(null);
		}
		
		@Override
		public javax.swing.Icon get(int state) {
			return get(state, TEXTUAL);
		}

		@Override
		public javax.swing.Icon get(int state, int size) {
			if(state != 0)
				return null;
			switch(size) {
			case ORIGINAL:
				return new javax.swing.ImageIcon(image);
			case TOOLBAR:
				return getToolbar();
			case DESKTOP:
				return getDesktop();
			case TEXTUAL:
			default:
				return getTextual();
			}
		}

		/**
		 * Get the textual form of the icon.
		 * @return		Textual form.
		 */
		private javax.swing.Icon getTextual() {
			if(textual == null) {
				if(h <= MAX_TEXTUAL)
					textual = new javax.swing.ImageIcon(image);
				else
					textual = new javax.swing.ImageIcon(image.getScaledInstance(
								w * MAX_TEXTUAL / h,
								MAX_TEXTUAL,
								java.awt.Image.SCALE_SMOOTH));
			}
			return textual;
		}

		/**
		 * Get the toolbar form of the icon.
		 * @return		Textual form.
		 */
		private javax.swing.Icon getToolbar() {
			if(toolbar == null) {
				if(MAX_TEXTUAL < h && h <= MAX_TOOLBAR)
					toolbar = new javax.swing.ImageIcon(image);
				else
					toolbar = new javax.swing.ImageIcon(image.getScaledInstance(
								w * MAX_TOOLBAR / h,
								MAX_TOOLBAR,
								java.awt.Image.SCALE_SMOOTH));
			}
			return toolbar;
		}

		/**
		 * Get the desktop form of the icon.
		 * @return		Desktop form.
		 */
		private javax.swing.Icon getDesktop() {
			if(toolbar == null) {
				if(MAX_TOOLBAR < h && h <= MAX_DESKTOP)
					toolbar = new javax.swing.ImageIcon(image);
				else
					toolbar = new javax.swing.ImageIcon(image.getScaledInstance(
								w * MAX_DESKTOP / h,
								MAX_DESKTOP,
								java.awt.Image.SCALE_SMOOTH));
			}
			return toolbar;
		}

		public static Image getBroken() {
			try {
				return new Image(ImageIO.read(Image.class.getResourceAsStream("broken.png")));
			} catch (IOException e) {
				return null;
			}
		}
	}
	
}
