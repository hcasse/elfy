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
package elf.data;

/**
 * Pair of data.
 * @author casse
 *
 * @param <T1>		First data type.
 * @param <T2>		Second data type.
 */
public final class Pair<T1, T2> {
	public T1 fst;
	public T2 snd;
	
	/**
	 * Build a pair.
	 * @param fst	First value.
	 * @param snd	Second value.
	 * @return		Built pair.
	 */
	static <T1, T2> Pair<T1, T2> cons(T1 fst, T2 snd) {
		return new Pair<T1, T2>(fst, snd);
	}
	
	/**
	 * Build a pair.
	 * @param fst	First value.
	 * @param snd	Second value.
	 */
	public Pair(T1 fst, T2 snd) {
		this.fst = fst;
		this.snd = snd;
	}
	
	/**
	 * Get the first value.
	 * @return		First value.
	 */
	public T1 first() {
		return fst;
	}
	
	/**
	 * Get the second value.
	 * @return		Second value.
	 */
	public T2 second() {
		return snd;
	}
	
}
