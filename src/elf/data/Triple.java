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
 * Triple of data.
 * @author casse
 *
 * @param <T1>		First data type.
 * @param <T2>		Second data type.
 * @param <T3>		Third data type.
 */
public final class Triple<T1, T2, T3> {
	public T1 fst;
	public T2 snd;
	public T3 thd;
	
	/**
	 * Build a triple.
	 * @param fst	First value.
	 * @param snd	Second value.
	 * @param thd	Third value.
	 * @return		Built pair.
	 */
	public static <T1, T2, T3> Triple<T1, T2, T3> cons(T1 fst, T2 snd, T3 thd) {
		return new Triple<T1, T2, T3>(fst, snd, thd);
	}
	
	/**
	 * Build a triple.
	 * @param fst	First value.
	 * @param snd	Second value.
	 */
	public Triple(T1 fst, T2 snd, T3 thd) {
		this.fst = fst;
		this.snd = snd;
		this.thd = thd;
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
	
	/**
	 * Get the third value.
	 * @return		Third value.
	 */
	public T3 third() {
		return thd;
	}
	

}
