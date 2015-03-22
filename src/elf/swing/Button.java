package elf.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import elf.ui.meta.Action;
import elf.ui.meta.Entity;

/**
 * Swing implementation of a button.
 * @author casse
 */
public class Button extends Component implements elf.ui.Button, ActionListener, Action.Command, Entity.EntityListener {
	private Action action;
	private int style;
	private JButton button;
	private UI ui;
	
	public Button(Action action, int style) {
		this.action = action;
		this.style = style;
		action.addListener(this);
	}
	
	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public JComponent getComponent(View view) {
		if(button == null) {
			this.ui = view.getUI();
			
			// build the UI
			button = new JButton();
			configure();
			
			// put the dependency
			action.add(this);	
			button.addActionListener(this);
		}
		return button;
	}
	
	/**
	 * Configure the button.
	 */
	private void configure() {
		prepareButton(button, action);
		switch(style) {
		case STYLE_TEXT:
			button.setText(action.getLabel());
			break;
		case STYLE_ICON_TEXT:
			Icon icon = ui.getIcon(action.getIcon());
			if(icon != null)
				button.setIcon(icon.get(Icon.NORMAL, Icon.TEXTUAL));
			button.setText(action.getLabel());
			break;
		case STYLE_ICON:
			button.setIcon(ui.getIcon(action.getIcon()).get(Icon.NORMAL, Icon.TEXTUAL));
			break;
		case STYLE_TOOL:
			button.setIcon(ui.getIcon(action.getIcon()).get(Icon.NORMAL, Icon.TOOLBAR));
			break;
		case STYLE_TOOL_TEXT:
			button.setIcon(ui.getIcon(action.getIcon()).get(Icon.NORMAL, Icon.TOOLBAR));
			button.setVerticalTextPosition(JButton.BOTTOM);
			break;
		}		
	}

	@Override
	public void dispose() {
		button = null;
		action.remove(this);
	}

	@Override
	public void enable(boolean enabled) {
		button.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		action.run();
	}

	@Override
	public void onChange(Entity entity) {
		configure();
	}

}
