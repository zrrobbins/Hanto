/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2015 Gary F. Pollice
 *******************************************************************************/

package hanto.studentzrrobbins.common;

import hanto.common.HantoPieceType;

/**
 * Stores the data needed to make a single HantoMove. Used for keeping track of possible moves when detecting
 * resignations and for AI movement decisions.
 * @author zrrobbins
 *
 */
public class HantoMove 
{
	HantoPieceType pieceType;
	HantoCoordinateImpl from;
	HantoCoordinateImpl to;
	
	/**
	 * Default constructor
	 * @param pieceType
	 * @param from
	 * @param to
	 */
	HantoMove(HantoPieceType pieceType, HantoCoordinateImpl from, HantoCoordinateImpl to) {
		this.pieceType = pieceType;
		this.from = from;
		this.to = to;
	}
	
	/**
	 * Getter for piece type
	 * @return pieceType
	 */
	public HantoPieceType getPieceType() {
		return pieceType;
	}

	/**
	 * Getter for from coord
	 * @return from
	 */
	public HantoCoordinateImpl getFrom() {
		return from;
	}

	/**
	 * Getter for to coord
	 * @return to
	 */
	public HantoCoordinateImpl getTo() {
		return to;
	}

}
