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
package hanto.studentzrrobbins.delta;

import static hanto.common.HantoGameID.*;
import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;

import hanto.common.HantoGame;
import hanto.common.HantoPlayerColor;
import hanto.studentzrrobbins.common.BaseHantoGame;
import hanto.studentzrrobbins.common.HantoPlayerStateFactory;
import hanto.studentzrrobbins.common.movevalidators.FlyValidator;
import hanto.studentzrrobbins.common.movevalidators.WalkValidator;


/**
 * The implementation of Delta Hanto.
 * @author zrrobbins
 *
 */
public class DeltaHantoGame extends BaseHantoGame implements HantoGame {

	/**
	 * Creates a DeltaHantoGame with desired color as first move. This constructor should only ever be called by HantoGameFactory!
	 * @param movesFirst - the color of the player who moves first
	 */
	public DeltaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		bluePlayerState = HantoPlayerStateFactory.makePlayerState(DELTA_HANTO, BLUE);
		redPlayerState = HantoPlayerStateFactory.makePlayerState(DELTA_HANTO, RED);
		currentPlayerState = movesFirst == BLUE ? bluePlayerState : redPlayerState;
		movementStrategies.put(BUTTERFLY, new WalkValidator(1));
		movementStrategies.put(CRAB, new WalkValidator(3));
		movementStrategies.put(SPARROW, new FlyValidator(Integer.MAX_VALUE));
		playerCanResign = true;
	}
}
