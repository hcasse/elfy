package elf.ui.meta;

import elf.ui.Icon;

/**
 * This class allows to join two actions.
 * Command enabled will AND'ed and run  action will be called
 * on both actions. Entity methods will be taken from the first action.
 * @author casse
 */
public class ActionJoin extends Action {
	Action fst, snd;
	
	@Override
	public String getLabel() {
		return fst.getLabel();
	}

	@Override
	public String getHelp() {
		return fst.getHelp();
	}

	@Override
	public int getMnemonic() {
		return fst.getMnemonic();
	}

	@Override
	public int getControl() {
		return fst.getControl();
	}

	@Override
	public Icon getIcon() {
		return fst.getIcon();
	}

	@Override
	public void fireEntityChange() {
		super.fireEntityChange();
		fst.fireEntityChange();
		snd.fireEntityChange();
	}

	@Override
	public void run() {
		fst.run();
		snd.run();
	}

	@Override
	public boolean isEnabled() {
		return fst.isEnabled() && snd.isEnabled();
	}

	@Override
	public void update() {
		super.update();
		fst.update();
		snd.update();
	}

}
