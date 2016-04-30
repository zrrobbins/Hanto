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

package hanto.studentzrrobbins.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.HantoGameID.*;

import hanto.common.*;
import hanto.studentzrrobbins.common.BaseHantoGame;
import hanto.studentzrrobbins.common.HantoCoordinateImpl;
import hanto.studentzrrobbins.common.HantoPlayerStateFactory;

/**
 * The implementation of Beta Hanto.
 * @version Mar 16, 2016
 */
public class BetaHantoGame extends BaseHantoGame implements HantoGame
{
	/**
	 * Default constructor
	 * @param movesFirst - player that goes first in the game
	 */
	public BetaHantoGame(HantoPlayerColor movesFirst) 
	{
		super(movesFirst);
		maxTurns = 6;
		bluePlayerState = HantoPlayerStateFactory.makePlayerState(BETA_HANTO, BLUE);
		redPlayerState = HantoPlayerStateFactory.makePlayerState(BETA_HANTO, RED);
		currentPlayerState = movesFirst == BLUE ? bluePlayerState : redPlayerState;
		playerCanResign = false;
	}

	
	/*
	 * @see hanto.studentzrrobbins.common.BaseHantoGame#preMoveCheck()
	 */
	@Override
	protected void preMoveCheck() throws HantoException 
	{
		if (moveFrom != null) {
			throw new HantoException("You cannot move a piece in Beta Hanto!");
		}
		if ((typeOfPieceMoving != BUTTERFLY) && (typeOfPieceMoving != SPARROW)) {
			throw new HantoException("Only Butterflies and Sparrows are valid in Beta Hanto");
		}
		super.preMoveCheck();
	}

	/*
	 * @see hanto.studentzrrobbins.common.BaseHantoGame#hasAdjacentOpponent(hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.common.HantoPlayerColor)
	 */
	@Override
	protected boolean hasAdjacentOpponent(HantoCoordinateImpl loc, HantoPlayerColor color)
	{
		return false;
	}
}
