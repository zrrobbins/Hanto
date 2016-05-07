/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentzrrobbins.tournament;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;

import java.util.List;

import hanto.common.*;
import hanto.studentzrrobbins.HantoGameFactory;
import hanto.studentzrrobbins.common.BaseHantoGame;
import hanto.studentzrrobbins.common.HantoCoordinateImpl;
import hanto.tournament.*;

/**
 * Description
 * @version Oct 13, 2014
 */
public class HantoPlayer implements HantoGamePlayer
{
	BaseHantoGame game;
	HantoPlayerColor myColor;

	/*
	 * @see hanto.tournament.HantoGamePlayer#startGame(hanto.common.HantoGameID, hanto.common.HantoPlayerColor, boolean)
	 */
	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst)
	{
		//System.out.println("zrrobbins: startGame("+version+","+myColor+","+doIMoveFirst+")");
		this.myColor = myColor;
		HantoPlayerColor goesFirst = doIMoveFirst ? myColor : reverseColor(myColor);
		HantoGameFactory hantoGameFactory = HantoGameFactory.getInstance();
		game = (BaseHantoGame)hantoGameFactory.makeHantoGame(version, goesFirst);
	}
	

	/*
	 * @see hanto.tournament.HantoGamePlayer#makeMove(hanto.tournament.HantoMoveRecord)
	 */
	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove)
	{
		HantoPieceType opponentsPieceType;
		HantoCoordinate opponentsFrom;
		HantoCoordinate opponentsTo;
		HantoMoveRecord result = null;
		
		try {
			if (opponentsMove == null) { // We have first move
				game.makeMove(BUTTERFLY, null, new HantoCoordinateImpl(0,0));
				result = new HantoMoveRecord(BUTTERFLY, null, new HantoCoordinateImpl(0,0));
			}
			else if (opponentsMove != null && game.getBoard().getPlayerButterflyLocation(myColor) == null) {
				// We don't have first move, but we should play butterfly immediately
				game.makeMove(opponentsMove.getPiece(), opponentsMove.getFrom(), opponentsMove.getTo());
				game.makeMove(BUTTERFLY, null, new HantoCoordinateImpl(1,0));
				result = new HantoMoveRecord(BUTTERFLY, null, new HantoCoordinateImpl(1,0));
			}
			else {
				opponentsPieceType = opponentsMove.getPiece();
				opponentsFrom = opponentsMove.getFrom();
				opponentsTo = opponentsMove.getTo();

				// Update board with opponents move
				game.makeMove(opponentsPieceType, opponentsFrom, opponentsTo);

				// Generate all possible VALID moves 
				List<HantoMoveRecord> possibleMoves = game.generateAllPossibleMoves();

				// As long as there is at least one valid move, make it. Otherwise forfeit
				if(!possibleMoves.isEmpty()) {
					result = possibleMoves.get(0);
				}
				else {
					result = new HantoMoveRecord(null, null, null);
				}
				
				// Make chosen move
				game.makeMove(result.getPiece(), result.getFrom(), result.getTo());
			}
		} catch (HantoException e) {
			System.out.println("The below error has been caught.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
		

	/**
	 * Gives opponent's color
	 * @param color - the color passed
	 * @return the opposing player's color
	 */
	private HantoPlayerColor reverseColor(HantoPlayerColor color) {
		return color == RED ? BLUE : RED;
	}

}
