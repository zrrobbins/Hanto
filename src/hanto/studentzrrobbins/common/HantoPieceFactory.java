/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/
package hanto.studentzrrobbins.common;

import hanto.common.*;

/**
 * Factory for HantoPiece objects
 * @author zrrobbins
 *
 */
public class HantoPieceFactory 
{
	/**
	 * Returns a new HantoPiece of the pieceType and color given
	 * @param pieceType
	 * @param color
	 * @return new HantoPiece object
	 */
	public static HantoPiece makeHantoPiece(HantoPieceType pieceType, HantoPlayerColor color) 
	{
		return new HantoPieceImpl(color, pieceType);
	}
}
