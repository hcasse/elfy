package elf.ui;

import elf.ui.meta.Action;

/**
 * Bar of actions. It may be styled with button style, aligned or horizontal/vertical.
 * Whatever the configuration, it will occupy the whole available space.
 * @author casse
 */
public interface ActionBar extends Component {

	/**
	 * Add an action.
	 * @param action	Added action.
	 */
	void add(Action action);
	
	/**
	 * Set the style of button (default Button.STYLE_ICON_TEXT).
	 * @param style		Button style (one of Button.STYLE_TEXT, Button.STYLE_ICON_TEXT, Button.STYLE_ICON, Button.STYLE_TOOL or Button.STYLE_TOOL_TEXT).
	 */
	void setStyle(int style);
	
	/**
	 * Set the alignment (default Component.LEFT).
	 * @param alignment	Alignment (one of Component.LEFT, Component.RIGHT, Component.CENTER or Component.SPREAD).
	 */
	void setAlignment(int alignment);

	/**
	 * Set the axis of the action bar (default Component.HORIZONTAL).
	 * @param axis	Axis (one of Component.HORIZONTAL or Component.VERTICAL).
	 */
	void setAxis(int axis);
	
	/**
	 * Set the margin width.
	 * @param width		Margin width.
	 */
	void setMargin(int width);
	
	/**
	 * Set the padding width.
	 * @param width		Padding width.
	 */
	void setPadding(int width);
}
