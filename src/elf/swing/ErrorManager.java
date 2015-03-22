package elf.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import elf.ui.Monitor;

/**
 * Swing implementation of error manager.
 * @author casse
 */
public class ErrorManager extends Container implements elf.ui.ErrorManager, elf.ui.Monitor {
	
	// useful constants
	private static final int
		CLEAR = 0,
		INFO = 1,
		WARN = 2,
		ERROR = 3;
	private static final Color[] BACKS = {
		null,
		Color.BLUE.brighter(),
		Color.ORANGE.brighter(),
		Color.RED.brighter()
	};
	private static final elf.ui.Icon[] ICONS = {
		null,
		elf.ui.Icon.INFO,
		elf.ui.Icon.WARNING,
		elf.ui.Icon.ERROR
	};
	
	// state
	private int style = DEFAULT, pos = BOTTOM, timeout = 5;
	private Color back;
	private javax.swing.Box main;
	private JPanel epanel;
	private JLabel label;
	private View view;
	private Timer timer;

	/**
	 * Display the given message.
	 * @param level		Level of error.
	 * @param message	Message to display (null for clear).
	 */
	void display(int level, String message) {
		
		// prepare error panel if needed
		if(label == null) {
			label = new JLabel("coucou");
			//epanel = new JPanel();
			//epanel.add(label);
			//main.add(epanel);
			main.add(label);
			debugBorder(label, MAGENTA);
			back = main.getBackground();
		}
		
		// hide panel if needed
		if((style & FOLDED) != 0)
			epanel.setVisible(message != null);
		
		// set the background color
		if((style & COLORED) != 0) {
			Color col = BACKS[level];
			if(col == null)
				col = back;
			epanel.setBackground(col);			
		}
		
		// set the icon
		if((style & ICON) != 0) {
			elf.ui.Icon icon = ICONS[level];
			if(icon == null)
				label.setIcon(null);
			else
				label.setIcon(view.getIcon(icon).get(Icon.NORMAL, Icon.TEXTUAL));
		}
		
		// if needed set timed
		if((style & TIMED) != 0) {
			if(message == null) {
				if(timer != null)
					timer.stop();
			}
			else {
				if(timer == null)
					timer = new Timer(timeout, new ActionListener() {
						@Override public void actionPerformed(ActionEvent arg0) { clear(); }
					});
				timer.start();
			}
		}
		
		// display the message
		if(message == null)
			label.setText("");
		else
			label.setText(message);
	}
	
	@Override
	public JComponent getComponent(View view) {
		if(main == null) {
			this.view = view;
			main = new javax.swing.Box(BoxLayout.Y_AXIS);
			//main.setLayout(new BorderLayout());
			for(Component component: getComponents())
				main.add(component.getComponent(view), BorderLayout.CENTER);
			clear();
			debugBorder(main, GREEN);
		}
		return main;
	}

	@Override
	public void setStyle(int style) {
		this.style = style;
	}

	@Override
	public void setPosition(int pos) {
		this.pos = pos;
	}

	@Override
	public void setTimeOut(int seconds) {
		this.timeout = seconds;
	}

	@Override
	public void dispose() {
		super.dispose();
		main = null;
		epanel = null;
		label = null;
	}

	@Override
	public void clear() {
		display(CLEAR, "");
	}

	@Override
	public void info(String message) {
		display(INFO, message);
	}

	@Override
	public void warn(String message) {
		display(WARN, message);
	}

	@Override
	public void error(String message) {
		display(ERROR, message);
	}

	@Override
	public void panic(String message) {
		getParent().getMonitor().panic(message);
	}

	@Override
	public void begin(String title) {
		getParent().getMonitor().begin(title);		
	}

	@Override
	public boolean end() {
		return getParent().getMonitor().end();
	}

	@Override
	public Monitor getMonitor() {
		return this;
	}

}
