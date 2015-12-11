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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.Transient;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import elf.ui.meta.Action;

/**
 * A fixed position tool bar made of icons.
 * @author casse
 */
public class ActionBar extends Component implements elf.ui.ActionBar {
	private LinkedList<Action> actions = new LinkedList<Action>();
	private int
		align = LEFT,
		axis = HORIZONTAL,
		pad = 4,
		margin = 1,
		style = Button.STYLE_ICON_TEXT;
	private JPanel panel;
	private LinkedList<Button> buttons = new LinkedList<Button>();

	@Override
	public JComponent getComponent(View view) {
		if(panel == null) {

			// build the panel
			panel = new JPanel() {
				private static final long serialVersionUID = 1L;

				@Override
				@Transient
				public Dimension getMaximumSize() {
					Dimension d = getPreferredSize();
					if(axis == HORIZONTAL)
						return new Dimension(Short.MAX_VALUE, d.height);
					else
						return new Dimension(d.width, Short.MAX_VALUE);
				}

			};
			panel.setLayout(new ButtonLayout());

			// create the actions
			for(Action action: actions) {
				Button button = new Button(action, style);
				buttons.add(button);
				JComponent component = button.getComponent(view);
				panel.add(component);
			}
		}
		return panel;
	}

	/**
	 * Get the alignment.
	 * @return	Alignment (one of LEFT, CENTER or RIGHT).
	 */
	public int getAlign() {
		return align;
	}

	@Override
	public void setAlignment(int alignment) {
		this.align = alignment;
	}


	/**
	 * Set the axis.
	 * @param axis	Axis (one of HORIZONTAL or VERTICAL).
	 */
	public void setAxis(int axis) {
		this.axis = axis;
	}

	@Override
	public void setPadding(int width) {
		this.pad = width;
	}

	/**
	 * Set the margin (around the bar).
	 * @param margin	Margin size.
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	@Override
	public void setStyle(int style) {
		this.style = style;
	}

	@Override
	public void add(Action action) {
		actions.add(action);
	}

	@Override
	public void dispose() {
		super.dispose();
		for(Button button: buttons)
			button.dispose();
		panel = null;
	}

	/**
	* @author Inspired by work of Santhosh Kumar - santhosh@in.fiorano.com
	*/
	public class ButtonLayout implements LayoutManager {
		private int visibleCount = -1;
		private Dimension max = new Dimension(), used = new Dimension();

		private void dimensions(java.awt.Component children[]) {

			// already done?
			if(visibleCount >= 0)
				return;

			// compute max width and height
			int maxWidth = 0;
			int maxHeight = 0;
			visibleCount = 0;
			for(int i = 0, c = children.length; i < c; i++)
				if (children[i].isVisible()) {
					Dimension componentPreferredSize = children[i].getPreferredSize();
					maxWidth = Math.max(maxWidth, componentPreferredSize.width);
					maxHeight = Math.max(maxHeight, componentPreferredSize.height);
					visibleCount++;
				}

			// compute preferred width and height
			int usedWidth = 0;
			int usedHeight = 0;
			if(axis == HORIZONTAL) {
				usedWidth = maxWidth * visibleCount + pad * (visibleCount - 1);
				usedHeight = maxHeight;
			}
			else {
				usedWidth = maxWidth;
				usedHeight = maxHeight * visibleCount + pad * (visibleCount - 1);
			}

			// return result
			max.width = maxWidth;
			max.height = maxHeight;
			used.width = usedWidth + margin * 2;
			used.height = usedHeight + margin * 2;
		}

		@Override
		public void layoutContainer(java.awt.Container container) {

			// compute available width and height
			Insets insets = container.getInsets();
			int width = container.getWidth() - (insets.left + insets.right)- margin * 2;
			int height = container.getHeight() - (insets.top + insets.bottom) - margin * 2;

			// compute dimension information from components
			java.awt.Component[] children = container.getComponents();
			dimensions(children);
			int maxWidth = max.width;
			int maxHeight = max.height;
			int usedWidth = used.width;
			int usedHeight = used.height;
			int cgap = pad;

			// support overflow
			if(width < usedWidth) {
				usedWidth = width;
				if(axis == HORIZONTAL) {
					cgap = 0;
					maxWidth = width / visibleCount;
				}
				else
					maxWidth = width;
			}
			if(height < usedHeight) {
				usedHeight = height;
				if(axis == VERTICAL) {
					cgap = 0;
					maxHeight = height / visibleCount;
				}
				else
					maxHeight = height;
			}

			// prepare computation
			int x = insets.left + margin;
			int y = insets.top + margin;
			if(axis == HORIZONTAL) {
				switch(align) {
				case LEFT:		break;
				case RIGHT:		x += width - usedWidth; break;
				case SPREAD:	cgap = (width - usedWidth) / (visibleCount + 1); x += cgap; break;
				}
				y += (height - maxHeight) / 2;
			}
			else {
				switch(align) {
				case TOP:		break;
				case BOTTOM:	y += height - usedHeight; break;
				case SPREAD:	cgap = (height - usedHeight) / (visibleCount + 1); y += cgap; break;
				}
				x += (width - maxWidth) / 2;
			}

			// layout the component
			if(axis == HORIZONTAL)
				for (int i = 0, c = children.length; i < c; i++) {
					if (children[i].isVisible())
							children[i].setBounds(x + maxWidth * i + cgap * i, y, maxWidth, maxHeight);
				}
			else
				for (int i = 0, c = children.length; i < c; i++) {
					if (children[i].isVisible())
							children[i].setBounds(x, y + maxHeight * i + cgap * i, maxWidth, maxHeight);
				}
		}

		@Override
		public Dimension minimumLayoutSize(java.awt.Container c) {
			return preferredLayoutSize(c);
		}

		@Override
		public Dimension preferredLayoutSize(java.awt.Container container) {
			Insets insets = container.getInsets();
			java.awt.Component[] children = container.getComponents();
			dimensions(children);
			int usedWidth = used.width;
			int usedHeight = used.height;
			return new Dimension(insets.left + usedWidth + insets.right, insets.top + usedHeight + insets.bottom);
		}

		@Override
		public void addLayoutComponent(String string, java.awt.Component comp) {
			visibleCount = -1;
		}

		@Override
		public void removeLayoutComponent(java.awt.Component c) {
			visibleCount = - 1;
		}

	}
}
