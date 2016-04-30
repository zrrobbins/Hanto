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

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.MoveResult.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanto.common.*;
import hanto.studentzrrobbins.common.movevalidators.MoveValidatorStrategy;
import hanto.tournament.HantoMoveRecord;

/**
 * Abstract base class for all Hanto Game derivations.
 * @author zrrobbins
 *
 */
public class BaseHantoGame implements HantoGame 
{
	protected final HantoPlayerColor movesFirst;
	protected final HantoBoard board;
	protected HantoPieceType typeOfPieceMoving;
	protected HantoCoordinateImpl moveTo;
	protected HantoCoordinateImpl moveFrom;
	protected HantoPlayerColor currentPlayerColor;
	protected int moveNumber;
	protected int gameTurn;
	protected HantoCoordinateImpl blueButterflyLocation, redButterflyLocation;
	protected HantoPlayerState bluePlayerState, redPlayerState;
	protected HantoPlayerState currentPlayerState;
	protected int maxTurns;
	protected boolean playerCanResign;
	protected Map<HantoPieceType, MoveValidatorStrategy> movementStrategies;
	
	private boolean gameOver;
	private final int NO_TURN_LIMIT = Integer.MAX_VALUE;

	
	/**
	 * Default constructor
	 * @param movesFirst - player that goes first
	 */
	protected BaseHantoGame(HantoPlayerColor movesFirst)
	{
		this.movesFirst = movesFirst;
		board = new HantoBoard();
		currentPlayerColor = movesFirst;
		moveNumber = 0;
		gameTurn = 1;
		blueButterflyLocation = null;
		redButterflyLocation = null;
		maxTurns = NO_TURN_LIMIT;
		gameOver = false;
		playerCanResign = true;
		movementStrategies = new HashMap<HantoPieceType, MoveValidatorStrategy>();
	}
	
	
	/**
	 * Copy constructor to create a new instance of a HantoBaseGame from another instance of HantoBaseGame
	 * @param another - an instance of HantoBaseGame
	 */
	public BaseHantoGame(BaseHantoGame another) {
		this.movesFirst = another.movesFirst;
		this.board = another.board; //needs to be copy
		this.typeOfPieceMoving = another.typeOfPieceMoving;
		this.moveTo = another.moveTo;
		this.moveFrom = another.moveFrom;
		this.currentPlayerColor = another.currentPlayerColor; //needs to be copy
		this.moveNumber = another.moveNumber; // needs to be copy
		this.gameTurn = another.gameTurn; //needs to be copy
		this.blueButterflyLocation = another.blueButterflyLocation;
		this.redButterflyLocation = another.redButterflyLocation;
		this.bluePlayerState = another.bluePlayerState;
		this.redPlayerState = another.redPlayerState;
		this.currentPlayerState = another.currentPlayerState;
		this.maxTurns = another.maxTurns;
		this.playerCanResign = another.playerCanResign;
		this.movementStrategies = another.movementStrategies;
	}
	
	
	@Override
	/*
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to)
			throws HantoException {
		
		MoveResult moveResult = null;
		
		typeOfPieceMoving = pieceType;
		moveFrom =  (from != null) ? new HantoCoordinateImpl(from) : null;
		moveTo = (to != null) ? new HantoCoordinateImpl(to) : null;
		if (playerCanResign && checkIfMoveIsResignation()) {
			moveResult = playerResigns();
		}
		else {
			preMoveCheck();
			movePiece();
			postMoveCheck();
			updateGameState(); // switch with postMoveCheck
			moveResult = determineMoveOutcome();
		}	
		return moveResult;
	}
	
	
	/**
	 * Checks conditions of game before move, then depending on the movement type, calls a placement validity check
	 * function or a movement validity check function.
	 * @throws HantoException
	 */
	protected void preMoveCheck() throws HantoException
	{
		// Check to see if game is over
		if (gameOver) {
			throw new HantoException("You cannot move after the game is finished");
		}		
		if (moveFrom == null) {
			placementIsValid();
		}
		else {
			movementIsValid();
		}
	}
	
	
	/**
	 * Checks to see if a player has made a resignation move.
	 * @return true if resignation move has been made by player
	 */
	private boolean checkIfMoveIsResignation() 
	{
		return (moveFrom == null && moveTo == null && typeOfPieceMoving == null);
	}

	
	/**
	 * Method that is called to end game if a player resigns
	 * @return opposing player win condition
	 */
	protected MoveResult playerResigns() throws HantoPrematureResignationException
	{
		gameOver = true;
		return currentPlayerState.equals(bluePlayerState) ? RED_WINS : BLUE_WINS;
	}

	
	/**
	 * Checks the conditions of the game after a move has been made.
	 * @throws HantoException
	 */
	protected void postMoveCheck() throws HantoException 
	{	
		// Make sure butterfly is placed by 4th turn
		if (gameTurn > 3 && !currentPlayerState.hasPlayedButterfly()) {
			throw new HantoException("Invalid move, " + currentPlayerColor + "'s butterfly must be placed by 4th turn");
		}
	}
	
	
	/**
	 * Updates the state of a game after a move has been made. This is especially necessary in preparation
	 * of the next player's move.
	 */
	protected void updateGameState() 
	{	
		moveNumber++;
		if (moveNumber % 2 == 0) {
			gameTurn++;
		}
		if (typeOfPieceMoving == BUTTERFLY) 
		{
			if (currentPlayerState == bluePlayerState) {
				blueButterflyLocation = moveTo;
			}
			else if (currentPlayerState == redPlayerState) {
				redButterflyLocation = moveTo;
			}
		}
		currentPlayerState = (currentPlayerState.equals(bluePlayerState)) ? redPlayerState : bluePlayerState;
		currentPlayerColor = currentPlayerColor == BLUE ? RED : BLUE;
	}
	
	
	/**
	 * Check that a piece placement operation is valid.
	 * @throws HantoException
	 */
	protected void placementIsValid() throws HantoException
	{		
		// Limit piece placement to defined numbers 
		int piecesLeft = currentPlayerState.piecesOfTypeLeftInStockPile(typeOfPieceMoving);
		if (piecesLeft == 0) {
			throw new HantoException(currentPlayerColor.toString() + " has run out of " 
					+ typeOfPieceMoving.toString() + " to place");
		}
		// Make sure first move is on origin
		if (board.getBoardSize() == 0) {
			if (moveTo.equals(new HantoCoordinateImpl(0,0))) {
				return;
			}
			else {
				throw new HantoException(currentPlayerColor.toString() + "'s first move must be on origin!");
			}
		}
		// Make sure second move is next to first move
		if (board.getBoardSize() == 1) {
			if (board.getAdjacentCoords(moveTo).contains(new HantoCoordinateImpl(0,0))) {
				return;
			}
			else {
				throw new HantoException("Invalid move, second move must be adjacent to first move!");
			}		
		}
		// Make sure destination is unoccupied
		if (board.getPieceAt(moveTo) != null) {
			throw new HantoException("Invalid move, destination of piece must be unoccupied");
		}
		// Make sure piece is not placed without touching existing contiguous formation
		if (board.getAdjacentPieces(moveTo).size() < 1) {
			throw new HantoException("Invalid move, cannot place a piece by itself");
		}
		// Make sure piece placement is adjacent to at least one friendly piece, and not adjacent to any enemy piece
		if (hasAdjacentOpponent(moveTo, currentPlayerColor)) {
			throw new HantoException("Cannot place piece next to enemy piece!");
		}
	}

	
	/**
	 * Checks to see that a piece movement operation is valid.
	 * @throws HantoException
	 */
	protected void movementIsValid() throws HantoException 
	{
		HantoPiece fromPiece = getPieceAt(moveFrom);
		// Make sure source piece exists, and is of correct type and color requested
		if (fromPiece == null || fromPiece.getColor() != currentPlayerColor || fromPiece.getType() != typeOfPieceMoving) {
			throw new HantoException("Invalid move");
		}
		// Check to see if player has played butterfly
		if (!currentPlayerState.hasPlayedButterfly()) {
			throw new HantoException("Butterfly must be placed before moving a piece");
		}
		// Use move validators to determine move validity
		if (!movementStrategies.get(typeOfPieceMoving).canMove(moveFrom, moveTo, board)) {
			throw new HantoException("Move is invalid");
		}
	}
	
	
	/**
	 * Determine the outcome of a move. Called every time a piece is placed/moved, and returns the result of that move,
	 * which can either be OK, DRAW, or a win for either player.
	 * @return the result of the move
	 */
	protected MoveResult determineMoveOutcome()
	{	
		boolean redWins = bluePlayerState.hasPlayedButterfly() ? board.isHexSurrounded(blueButterflyLocation) : false;
		boolean blueWins = redPlayerState.hasPlayedButterfly() ? board.isHexSurrounded(redButterflyLocation) : false;
		
		MoveResult moveResult = null;
			moveResult = redWins && blueWins ? DRAW
					: blueWins ? BLUE_WINS
					: redWins ? RED_WINS
					: gameTurn > maxTurns ? DRAW
					: OK;
		
		gameOver = moveResult == DRAW
				|| moveResult == BLUE_WINS
				|| moveResult == RED_WINS;	
		
		return moveResult;
	}

	

	/**
	 * Returns true if there is at least one enemy piece adjacent to the coordinate given.
	 * @param loc - location to check
	 * @param color - player color at location
	 * @return if there is an enemy piece adjacent to loc
	 */
	protected boolean hasAdjacentOpponent(HantoCoordinateImpl loc, HantoPlayerColor color) {
		for (HantoCoordinateImpl coord: board.getAdjacentPieces(loc)) {
			if (board.getPieceAt(coord).getColor() != color) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Generates all possible moves that can be made by the current player, including placements.
	 * @return list of all possible moves, empty if no legal moves can be made
	 */
	public List<HantoMoveRecord> generateAllPossibleMoves() 
	{
		List<HantoMoveRecord> possibleMoves = new ArrayList<HantoMoveRecord>();
		
		/* FOR CURRENT PLAYER:
		 * 
		 * for each piece type not placed ONLY GENERATE ONE SET OF MOVES FOR EACH NON PLACED PIECE TYPE
		 * i.e. if we have three unplaced crabs, we don't want to generate 3 identical possible move sets
		 * 
		 * for each piece already on the board, generate all possible moves
		 * 
		 * how to generate placement moves for a piece:
		 * - if first move, only valid spot is origin
		 * - if second move, only valid spot is adjacent to origin
		 * - if 3rd or greater move, generate a list of all empty spots adjacent to at least one piece
		 * 		- ArrayList<HantoCoordinateImpl> generateFrontier()
		 * - filter the list to include only piece adjacent to at least one friendly and not adjacent to any enemies
		 * 		- ArrayList<HantoCoordinateImpl> filterFrontierForPlacement()
		 * 
		 * how to generate movement moves for a piece:
		 * - if butterfly is not placed, no moves possible
		 * - generate a list of all adjacent spots adjacent to at least one piece
		 * 		- generateFrontier()
		 * - for each piece of a type, use its move verifier on each spot in frontier and record if movable and what
		 * result would be 
		 */
		List<HantoCoordinateImpl> frontier = getFrontier();
		
		moveFrom = null;
		// Generate all placement move possibilities for each piece type currently in stockpile
		for(HantoPieceType pieceType : HantoPieceType.values()){
			if (currentPlayerState.piecesOfTypeLeftInStockPile(pieceType) > 0) {
				for (HantoCoordinateImpl coord : frontier) {
					try {
						typeOfPieceMoving = pieceType;
						moveTo = coord;
						preMoveCheck();
						possibleMoves.add(new HantoMoveRecord(pieceType, null, moveTo));
					} catch (HantoException e) {
						//System.out.println("EXCEPTION when generating placement moves: " + e.getMessage());
					}
				}
			}
		}
		
		// Generate all possible movement move possibilities for each piece on the board belonging to current player
		for(HantoCoordinateImpl pieceCoord : board.getPlayerPieceLocations(currentPlayerColor)){
			for(HantoCoordinateImpl coord : frontier) {
				try {
					typeOfPieceMoving = board.getPieceAt(pieceCoord).getType();
					moveTo = coord;
					moveFrom = pieceCoord;
					preMoveCheck();
					possibleMoves.add(new HantoMoveRecord(board.getPieceAt(pieceCoord).getType(), moveFrom, moveTo));
				} catch (HantoException e) {
					//System.out.println("EXCEPTION when generating movement moves: " + e.getMessage());
				}
			}
		}
		
		
		System.out.println("Possible Moves: ");
		int fromX, fromY, toX, toY;
		HantoPieceType type;
		HantoPlayerColor color;
		System.out.println("Number of possible moves: " + possibleMoves.size());
		
		for(HantoMoveRecord move : possibleMoves) {
			move.getFrom();
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
			color = currentPlayerColor;
			System.out.println("Color: " + color + "\t" + "Type: " + type + "\t" + "fromX: " + fromX + "\t" + "fromY: " + fromY 
					+ "\t" + "toX: " + toX + "\t" + "toY: " + toY + "\n");
		}
		
		return possibleMoves;
	}
	
	
	/**
	 * Getter for board, used for AI player
	 * @return the board
	 */
	public HantoBoard getBoard() {
		return board;
	}
	
	
	/**
	 * Generates and returns a list of all HantoCoordinates that are empty and adjacent to at least one
	 * other piece.
	 * @return frontier
	 */
	private List<HantoCoordinateImpl> getFrontier() 
	{
		List<HantoCoordinateImpl> frontier = new ArrayList<HantoCoordinateImpl>();
		for(HantoCoordinateImpl coord : board.getCoordinateSet()) {
			for(HantoCoordinateImpl emptyCoord : board.getAdjacentEmpties(coord)){
				if (!frontier.contains(emptyCoord)) {
					frontier.add(emptyCoord);
				}
			}
		}
		return frontier;
	}


	/**
	 * Places/moves a piece.
	 * @throws HantoException
	 */
	private void movePiece() throws HantoException 
	{
		if (moveFrom == null) {
			board.addPiece(currentPlayerState.getPieceFromStockPile(typeOfPieceMoving), moveTo);
		}
		else {
			board.changePieceLocation(moveFrom, moveTo);
		}
	}

	
	/*
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate coordinate)
	{
		HantoCoordinateImpl coord = new HantoCoordinateImpl(coordinate);
		return board.getPieceAt(coord);
	}

	
	/*
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard()
	{
		int x, y;
		HantoPieceType type;
		HantoPlayerColor color;
		String resultString = "Current Board:\n";
		
		for(HantoCoordinateImpl coord : board.getCoordinateSet()) {
			x = coord.getX();
			y = coord.getY();
			type = getPieceAt(coord) != null ? getPieceAt(coord).getType() : null;
			color = getPieceAt(coord) != null ? getPieceAt(coord).getColor() : null;
			resultString += "Color: " + color + "\t" + "Type: " + type + "\t" + "X: " + x + "\t" + "Y: " + y + "\n";
		}

		return resultString;
	}
}
