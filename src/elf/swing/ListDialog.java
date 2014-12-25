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

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
 
/**
 * Let the user choice inside a list of choices.
 */
public class ListDialog<T> extends JDialog {
	private static final long serialVersionUID = 1L;
	private JList<T> list;
	private boolean success = false;

	/**
	 * Set the current value.
	 * @param index	Index of current value.
	 */
	private void setValue(int index) {
		list.setSelectedIndex(index);
	}
	
	/**
	 * Display the dialog and process it.
	 * @return	Selected item.
	 */
	public T run() {
		success = false;
		setVisible(true);
		if(!success)
			return null;
		else
			return list.getSelectedValue();		
	}

	private void init(
		Frame parent,
		String comment,
		String title,
		Vector<T> values,
		int init,
		String action
	) {
		// cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}			
		});

		// action button
		final JButton setButton = new JButton(action);
		setButton.setActionCommand("Set");
		setButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				success = true;
				setVisible(false);
			}
		});
		setButton.setEnabled(false);
		getRootPane().setDefaultButton(setButton);

		// setup the list
		list = new JList<T>(values) {
			private static final long serialVersionUID = 1L;

			public int getScrollableUnitIncrement(Rectangle visibleRect,
					int orientation,
					int direction) {
				int row;
				if (orientation == SwingConstants.VERTICAL &&
						direction < 0 && (row = getFirstVisibleIndex()) != -1) {
					Rectangle r = getCellBounds(row, row);
					if ((r.y == visibleRect.y) && (row != 0))  {
						Point loc = r.getLocation();
						loc.y--;
						int prevIndex = locationToIndex(loc);
						Rectangle prevR = getCellBounds(prevIndex, prevIndex);

						if (prevR == null || prevR.y >= r.y) {
							return 0;
						}
						return prevR.height;
					}
				}
				return super.getScrollableUnitIncrement(
						visibleRect, orientation, direction);
			}

		};
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				java.awt.Component res = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				this.setText(asString((T)value));
				return res;
			}
			
		});

		// initialize the list
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setButton.doClick();
				}
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				setButton.setEnabled(list.getSelectedIndex() != -1);
			}
		});

		// set the scroll pane
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);

		// prepare the main panel
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(comment);
		label.setLabelFor(list);
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// set the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);

		// set the frame
		Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

		//Initialize values.
		setValue(init);
		pack();
		setLocationRelativeTo(parent);		
	}
	
	/**
	 * Build the dialog.
	 * @param parent	Parent component.
	 * @param comment	Comment of the dialog.
	 * @param title		Title of the dialog.
	 * @param values	List of values.
	 * @param init		Index of initial value (or -1).
	 */
	public ListDialog(
		JComponent parent,
		String comment,
		String title,
		Vector<T> values,
		int init,
		String action
	) {
		super(JOptionPane.getFrameForComponent(parent), title, true);
		init(JOptionPane.getFrameForComponent(parent), comment, title, values, init, action);
	}

	/**
	 * Build the dialog.
	 * @param parent	Parent component.
	 * @param comment	Comment of the dialog.
	 * @param title		Title of the dialog.
	 * @param values	List of values.
	 * @param init		Index of initial value (or -1).
	 */
	public ListDialog(
		Frame parent,
		String comment,
		String title,
		Vector<T> values,
		int init,
		String action
	) {
		super(parent, title, true);
		init(parent, comment, title, values, init, action);
	}

	/**
	 * May be customized to change the display.
	 * @param object	Object to display.
	 * @return			Matching string.
	 */
	protected String asString(T object) {
		return object.toString();
	}
}
