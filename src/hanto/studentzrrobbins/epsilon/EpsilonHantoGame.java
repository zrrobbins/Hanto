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
package hanto.studentzrrobbins.epsilon;

import static hanto.common.HantoGameID.*;
import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;

import java.util.ArrayList;
import java.util.List;

import hanto.common.HantoGame;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;
import hanto.studentzrrobbins.common.BaseHantoGame;
import hanto.studentzrrobbins.common.HantoMove;
import hanto.studentzrrobbins.common.HantoPlayerStateFactory;
import hanto.studentzrrobbins.common.movevalidators.FlyValidator;
import hanto.studentzrrobbins.common.movevalidators.JumpValidator;
import hanto.studentzrrobbins.common.movevalidators.WalkValidator;
import hanto.tournament.HantoMoveRecord;


/**
 * The implementation of Epsilon Hanto.
 * @author zrrobbins
 *
 */
public class EpsilonHantoGame extends BaseHantoGame implements HantoGame {

	/**
	 * Creates a EpsilonHantoGame with desired color as first move. This constructor should only ever be called by HantoGameFactory!
	 * @param movesFirst - the color of the player who moves first
	 */
	public EpsilonHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		bluePlayerState = HantoPlayerStateFactory.makePlayerState(EPSILON_HANTO, BLUE);
		redPlayerState = HantoPlayerStateFactory.makePlayerState(EPSILON_HANTO, RED);
		currentPlayerState = movesFirst == BLUE ? bluePlayerState : redPlayerState;
		movementStrategies.put(BUTTERFLY, new WalkValidator(1));
		movementStrategies.put(SPARROW, new FlyValidator(4));
		movementStrategies.put(CRAB, new WalkValidator(1));
		movementStrategies.put(HORSE, new JumpValidator());
		playerCanResign = true;
	}

	@Override
	/*
	 * @see hanto.studentzrrobbins.common.BaseHantoGame#playerResigns()
	 */
	protected MoveResult playerResigns() throws HantoPrematureResignationException {		
		List<HantoMoveRecord> possibleMoves = new ArrayList<HantoMoveRecord>();
		possibleMoves = generateAllPossibleMoves(); // generate all possible moves AND placements		
		if (!possibleMoves.isEmpty()) {
			throw new HantoPrematureResignationException();
		}	
		return super.playerResigns();
	}
	
	
}

