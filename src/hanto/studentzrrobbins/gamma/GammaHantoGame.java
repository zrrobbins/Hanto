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

package hanto.studentzrrobbins.gamma;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.HantoGameID.*;

import hanto.common.*;
import hanto.studentzrrobbins.common.BaseHantoGame;
import hanto.studentzrrobbins.common.HantoPlayerStateFactory;
import hanto.studentzrrobbins.common.movevalidators.WalkValidator;

/**
 * The implementation of Gamma Hanto.
 * @version Mar 16, 2016
 */
public class GammaHantoGame extends BaseHantoGame implements HantoGame
{
	/**
	 * Creates a GammaHantoGame with desired color as first move. This constructor should only ever be called by HantoGameFactory!
	 * @param movesFirst - the color of the player who moves first
	 */
	public GammaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		maxTurns = 20;
		bluePlayerState = HantoPlayerStateFactory.makePlayerState(GAMMA_HANTO, BLUE);
		redPlayerState = HantoPlayerStateFactory.makePlayerState(GAMMA_HANTO, RED);
		currentPlayerState = movesFirst == BLUE ? bluePlayerState : redPlayerState;
		movementStrategies.put(BUTTERFLY, new WalkValidator(1));
		movementStrategies.put(SPARROW, new WalkValidator(1));
		playerCanResign = false;
	}

	
	@Override
	/*
	 * @see hanto.studentzrrobbins.common.BaseHantoGame#preMoveCheck()
	 */
	public void preMoveCheck() throws HantoException {
		if ((typeOfPieceMoving != BUTTERFLY) && (typeOfPieceMoving != SPARROW)) {
			throw new HantoException("Only Butterflies and Sparrows are valid in Gamma Hanto");
		}
		super.preMoveCheck();
	}
}
