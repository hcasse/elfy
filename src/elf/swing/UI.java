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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Timer;

import elf.ui.View;
import elf.ui.meta.Entity;

/**
 * Swing UI implementation.
 * @author casse
 */
public class UI implements elf.ui.UI {
	private Hashtable<Task, TaskTimer> timers = new Hashtable<Task, TaskTimer>();
	
	@Override
	public View makeView(Entity entity) {
		return new elf.swing.View(entity);
	}

	@Override
	public void start(Task task) {
		TaskTimer timer = timers.get(task);
		if(timer == null)
			timer = new TaskTimer(task);
		timer.start();
	}

	@Override
	public void stop(Task task) {
		TaskTimer timer = timers.get(task);
		if(timer != null)
			timer.stop();
	}
	
	private class TaskTimer implements ActionListener {
		private Timer timer;
		private Task task;
		
		public TaskTimer(Task task) {
			this.task = task;
		}
		
		public void start() {
			if(timer != null)
				return;
			timer = new Timer((int)task.getPeriod(), this);
			timer.setRepeats(task.isPeriodic());
			timers.put(task, this);
			timer.start();
		}
		
		public void stop() {
			timer.stop();
			timers.remove(task);
			timer = null;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			task.run();
		}
		
	}

}
