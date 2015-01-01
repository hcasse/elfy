package elf.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import elf.ui.Icon;
import elf.ui.meta.Action;

/**
 * Swing implementation of a button.
 * @author casse
 */
public class Button extends Component implements elf.ui.Button, ActionListener, Action.Command {
	private Action action;
	private int style;
	private JButton button;
	
	public Button(Action action, int style) {
		this.action = action;
		this.style = style;
	}
	
	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public JComponent getComponent() {
		if(button == null) {
			
			// build the UI
			button = new JButton();
			prepareButton(button, action);
			switch(style) {
			case STYLE_TEXT:
				button.setText(action.getLabel());
				break;
			case STYLE_ICON_TEXT:
				Icon icon = action.getIcon();
				if(icon != null)
					button.setIcon(icon.get(Icon.NORMAL, Icon.TEXTUAL));
				button.setText(action.getLabel());
				break;
			case STYLE_ICON:
				button.setIcon(action.getIcon().get(Icon.NORMAL, Icon.TEXTUAL));
				break;
			case STYLE_TOOL:
				button.setIcon(action.getIcon().get(Icon.NORMAL, Icon.TOOLBAR));
				break;
			case STYLE_TOOL_TEXT:
				button.setIcon(action.getIcon().get(Icon.NORMAL, Icon.TOOLBAR));
				button.setVerticalTextPosition(JButton.BOTTOM);
				break;
			}
			
			// put the dependency
			action.add(this);	
			button.addActionListener(this);
		}
		return button;
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

}