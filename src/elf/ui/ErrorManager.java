package elf.ui;

/**
 * An error manager is in charge of catching errors and messages to the user
 * and to display them in a dedicated area according to the used bar style.
 * @author casse
 */
public interface ErrorManager extends Container {
	public static final int
		ICON 		= 0x01,		/** Display icon in front of message. */
		COLORED 	= 0x02,		/** Change background color according to the message type. */
		FOLDED 		= 0x04,		/** Message bar only displayed when needed. */
		TIMED 		= 0x08,		/** Message only displayed a limited time. */
		DEFAULT		= ICON;
	
	/**
	 * Set the style of the error manager.
	 * @param style		A OR'ed combination of ICON, COLORED, FOLDED and TIMED.
	 */
	public void setStyle(int style);
	
	/**
	 * Set the position of the error bar.
	 * @param pos	One of TOP or BOTTOM.
	 */
	public void setPosition(int pos);
	
	/**
	 * If the bar is timed, set the display time.
	 * @param seconds		Display time in seconds.
	 */
	public void setTimeOut(int seconds);
}
