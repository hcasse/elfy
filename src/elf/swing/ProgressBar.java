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

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import elf.ui.I18N;
import elf.ui.meta.Entity;
import elf.ui.meta.SingleVar;
import elf.ui.meta.Var;

/**
 * Swing implementation of ProgressBar.
 * @author casse
 */
public class ProgressBar extends Field implements elf.ui.ProgressBar, Var.Listener<Integer> {
	private Var<Integer> var, min, max;
	private int axis;
	private JProgressBar bar;
	private String format = I18N.STD.t("%02d %%");
	
	public ProgressBar(Var<Integer> var, Var<Integer> min, Var<Integer> max, int axis) {
		this.var = var;
		this.min = min;
		this.max = max;
		this.axis = axis;
		String label = var.getLabel();
		if(label != null)
			format = label;
	}
	
	@Override
	public Entity getEntity() {
		return var;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	/**
	 * Get the string for the bar.
	 * @return	Bar string.
	 */
	private String getBarString() {
		int inter = max.get() - min.get();
		int percent;
		if(inter == 0)
			percent = 0;
		else
			percent = (var.get() - min.get()) * 100 / inter;
		return String.format(format, percent);
	}
	
	@Override
	public JComponent getComponent() {
		if(bar == null) {
			bar = new JProgressBar(axis == HORIZONTAL ? JProgressBar.HORIZONTAL : JProgressBar.VERTICAL, min.get(), max.get());
			bar.setValue(var.get());
			var.addListener(this);
			max.addListener(this);
			min.addListener(this);
			if(axis == HORIZONTAL) {
				bar.setStringPainted(true);
				bar.setString(getBarString());
			}
		}
		return bar;
	}

	@Override
	public void setMin(int min) {
		this.min.removeListener(this);
		this.min = new SingleVar<Integer>(min);
		if(bar != null) {
			this.min.addListener(this);
			bar.setMinimum(min);
		}
	}

	@Override
	public void setMax(int max) {
		this.min.removeListener(this);
		this.min = new SingleVar<Integer>(max);
		if(bar != null) {
			this.max.addListener(this);
			bar.setMaximum(max);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		bar = null;
		var.removeListener(this);
		min.removeListener(this);
		max.removeListener(this);
	}

	@Override
	public void change(Var<Integer> data) {
		if(bar != null) {
			if(data == var) {
				bar.setValue(data.get());
				if(axis == HORIZONTAL)
					bar.setString(getBarString());				
			}
			else if(data == min)
				bar.setMinimum(min.get());
			else if(data == max)
				bar.setMaximum(max.get());
		}
	}

	@Override
	public void setText(String format) {
		this.format = format;
		if(bar != null)
			bar.setString(getBarString());
	}

}
