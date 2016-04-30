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
import static hanto.common.MoveResult.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		System.out.println("startGame");
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
		/*
		 * Make opponents move in my version of the game
		 * 
		 * Generate all possible moves
		 * Make each move
		 * 	- If the move throws an exception, revert to previous game state
		 *  - If the move is valid, see if it results in DRAW, OK, WIN, or a loss
		 *  - Assign it a value (priority) based on this
		 *  - Revert to previous game state to check next move
		 * If no possible moves, resign
		 * 
		 * Could also assign priority based on how close the move lands to their butterfly (try to surround)
		 * - Actually may try to do this
		 * - Also have a priority to keep as few pieces near my butterfly as possible
		 */
		HantoPieceType myPieceType, opponentsPieceType;
		HantoCoordinate myFrom, opponentsFrom;
		HantoCoordinate myTo, opponentsTo;
		HantoMoveRecord result = null;
		
		try {
			if (opponentsMove == null) { // We have first move
				game.makeMove(BUTTERFLY, null, new HantoCoordinateImpl(0,0));
				result = new HantoMoveRecord(BUTTERFLY, null, new HantoCoordinateImpl(0,0));
			}
			else {
				opponentsPieceType = opponentsMove.getPiece();
				opponentsFrom = opponentsMove.getFrom();
				opponentsTo = opponentsMove.getTo();

				// Update board with opponents move
				game.makeMove(opponentsPieceType, opponentsFrom, opponentsTo);

				// Generate all possible VALID moves 
				List<HantoMoveRecord> possibleMoves = game.generateAllPossibleMoves();

				// Prioritize moves
				Map<HantoMoveRecord, Integer> prioritizedMoves = prioritizeMoves(possibleMoves);
				
				//TODO: Test print, remove later
				System.out.println("Weighted Moves: ");
				int fromX, fromY, toX, toY;
				HantoPieceType type;
				HantoPlayerColor color;
				int weight;
				
				for(HantoMoveRecord move : prioritizedMoves.keySet()) {
					if (move.getFrom() != null) {
						fromX = move.getFrom().getX();
						fromY = move.getFrom().getY();
					}
					else {
						fromX = Integer.MAX_VALUE;
						fromY = Integer.MAX_VALUE;
					}
					
					toX = move.getTo().getX();
					toY = move.getTo().getY();
					type = move.getPiece() != null ? move.getPiece() : null;
					weight = prioritizedMoves.get(move);
					System.out.println("Color: " + myColor + "   Type: " + type + "   Weight: " + weight + "   fromX: " + fromX + "   fromY: " + fromY 
							+ "   toX: " + toX + "   toY: " + toY + "\n");
				}
				
				// Choose move with highest priority and make it
				int maxPriority = Integer.MIN_VALUE;
				for (HantoMoveRecord move : prioritizedMoves.keySet()) {
					if (prioritizedMoves.get(move) > maxPriority) {
						maxPriority = prioritizedMoves.get(move);
						result = move;
					}
				}
				game.makeMove(result.getPiece(), result.getFrom(), result.getTo());
			}
		} catch (HantoException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		
		return result;
	}
		

	private Map<HantoMoveRecord, Integer> prioritizeMoves(List<HantoMoveRecord> possibleMoves)  throws HantoException
	{
		Map<HantoMoveRecord, Integer> prioritizedMoveList = new HashMap<HantoMoveRecord, Integer>();
		
		/*
		 * First start all moves at value 100
		 * 
		 * If move results in OK, subtract the Manhattan distance from priority
		 * 		e.g. MDIstance = 9 ----> Priority = 100 - 9 = 91
		 * 
		 * If move results in myColor.WIN, assign priority of 1000
		 * 
		 * If move results in myColor.LOSE, assign priority of -1000
		 * 
		 * If piece to move is myColor.BUTTERFLY, check to see how many pieces surround it (getAdjacentPieces)
		 * 		refine this later to use (getNumberOfSlidePossibilities) to check to see how many ways we could slide out
		 * Instead of starting at 100, start at 70, and add +10 for each piece surrounding myColor.BUTTERFLY
		 * 
		 */
		
		// Create a copy of game to test moves with
		//BaseHantoGame moveTestGame = new BaseHantoGame(game);
		HantoMoveRecord myMove;
		
		for (HantoMoveRecord move : possibleMoves) {
			// Create a fresh copy of game to test moves with
			BaseHantoGame moveTestGame = new BaseHantoGame(game);
			// TODO: REMOVE - for debug only
			int fromX,fromY;
			if (move.getFrom() != null) {
				fromX = move.getFrom().getX();
				fromY = move.getFrom().getY();
			}
			else {
				fromX = Integer.MAX_VALUE;
				fromY = Integer.MAX_VALUE;
			}
			System.out.println("About to make test move with: piece=" + move.getPiece() + " fromX=" + 
					fromX + " fromY: " + fromY + " toX: " + move.getTo().getX() + " toY: " + move.getTo().getY() + "\n");
			
			MoveResult moveResult = moveTestGame.makeMove(move.getPiece(), move.getFrom(), move.getTo());
			myMove = new HantoMoveRecord(move.getPiece(), move.getFrom(), move.getTo());
			switch(moveResult) {
				case BLUE_WINS:
					if (myColor == BLUE) {
						prioritizedMoveList.put(myMove, 1000);
					} else {
						prioritizedMoveList.put(myMove, -1000);
					}
					break;
				case RED_WINS:
					if (myColor == RED) {
						prioritizedMoveList.put(myMove, 1000);
					} else {
						prioritizedMoveList.put(myMove, -1000);
					}
					break;
				case DRAW:
					prioritizedMoveList.put(myMove, -500);
					break;
				case OK:
					int manhattanDistance = 0;
					if (moveTestGame.getBoard().getPlayerButterflyLocation(reverseColor(myColor)) != null) {
						manhattanDistance = manhattanDistance(move.getTo(), moveTestGame.getBoard().getPlayerButterflyLocation(reverseColor(myColor)));
					}
					if (move.getPiece() == BUTTERFLY) {
						int numberOfPiecesAdjacentToButterfly = moveTestGame.getBoard().getAdjacentPieces((HantoCoordinateImpl)move.getFrom()).size();
						prioritizedMoveList.put(myMove, 70 + (numberOfPiecesAdjacentToButterfly * 10));
					}
					else {
						prioritizedMoveList.put(myMove, 100 - manhattanDistance);
					}
					break;		
			}
		}
		return prioritizedMoveList;
	}


	/**
	 * Gives opponent's color
	 * @param color - the color passed
	 * @return the opposing player's color
	 */
	private HantoPlayerColor reverseColor(HantoPlayerColor color) {
		return color == RED ? BLUE : RED;
	}

	
	/**
	 * Calculates the Manhattan Distance between the two given coordinates.
	 * @param source
	 * @param dest
	 * @return the Manhattan Distance between source and dest
	 */
	private int manhattanDistance(HantoCoordinate source, HantoCoordinate dest) 
	{
		int x1 = source.getX();
		int x2 = dest.getX();
		int y1 = source.getY();
		int y2 = dest.getY();
		int z1 = ((-1 * x1) - y1);
		int z2 = ((-1 * x2) - y2);
		return Math.max(Math.abs(x2 - x1), Math.max(Math.abs(y2 - y1), Math.abs(z2 - z1)));
	}

}
