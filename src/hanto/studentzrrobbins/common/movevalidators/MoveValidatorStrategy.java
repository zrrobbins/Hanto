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
package hanto.studentzrrobbins.common.movevalidators;

import hanto.common.HantoException;
import hanto.studentzrrobbins.common.HantoBoard;
import hanto.studentzrrobbins.common.HantoCoordinateImpl;

/**
 * The MoveValidatorStrategy is implemented by all movement strategies in a HantoGame (Walk, Fly, Run, etc.). 
 * MoveValidators are used by HantoPieces to determine if they can make a valid move.
 * @author zrrobbins
 *
 */
public interface MoveValidatorStrategy {
	
	/**
	 * Determines if a piece can move.
	 * @param from
	 * @param to
	 * @param board - the Hanto board
	 * @return if a valid move is possible
	 * @throws HantoException
	 */
	boolean canMove(HantoCoordinateImpl from, HantoCoordinateImpl to, HantoBoard board) throws HantoException;
}
