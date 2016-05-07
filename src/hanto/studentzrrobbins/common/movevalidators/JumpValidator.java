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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hanto.common.HantoException;
import hanto.studentzrrobbins.common.HantoBoard;
import hanto.studentzrrobbins.common.HantoCoordinateImpl;

/**
 * Implementation of MoveValidatorStrategy to represent the Jump move in Hanto
 * @author zrrobbins
 *
 */
public class JumpValidator implements MoveValidatorStrategy, Serializable {

	private static final long serialVersionUID = -4270444671672851612L;
	
	private boolean pathFound;
	private HantoBoard board;
	private List<HantoCoordinateImpl> visited;
	
	/**
	 * Default constructor
	 */
	public JumpValidator() {
		visited = new ArrayList<HantoCoordinateImpl>();
	}
	
	
	@Override
	/*
	 * @see hanto.studentzrrobbins.common.movevalidators.MoveValidatorStrategy#canMove(hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.studentzrrobbins.common.HantoBoard)
	 */
	public boolean canMove(HantoCoordinateImpl from, HantoCoordinateImpl to, HantoBoard board) throws HantoException 
	{
		this.board = board;
		pathFound = false;
		
		// Destination cannot equal source
		if (from.equals(to)) {
			throw new HantoException("Jump destination cannot be the source!");
		}
		// Destination cannot be adjacent to source
		if (board.getAdjacentCoords(from).contains(to)) {
			throw new HantoException("Invalid move, you must jump over at least one piece");
		}
		// Make sure destination is unoccupied
		if (board.getPieceAt(to) != null) {
			throw new HantoException("Invalid move, destination of piece must be unoccupied");
		}
		// Make sure move doesn't break contiguity of existing pieces
		if (!board.compareDFSSize(from, to)) {
			throw new HantoException("Invalid move, contiguity of pieces broken!");
		}
		// Source and destination must share at least one axis (in order for jump to be a straight line)
		if (from.getX() != to.getX() && from.getY() != to.getY() 
				&& !(Math.abs(from.getX() - to.getX()) == manhattanDistance(from, to) 
				&& Math.abs(from.getY() - to.getY()) == manhattanDistance(from, to))) {
			throw new HantoException("Invalid move, jump must be in a straight line!");
		}
		
		visited.clear();
		checkJumpPathValidity(from, to);
		
		return pathFound;
	}
	
	
	/**
	 * DFS until a valid jump path is found from root to goal (if it exists)
	 * @param root
	 * @param goal
	 */
	private void checkJumpPathValidity(HantoCoordinateImpl root, HantoCoordinateImpl goal) 
	{
		if (pathFound) {
			return;
		}
		if (board.getAdjacentCoords(root).contains(goal)) {
			pathFound = true;
		}
		// We get adjacent PIECES, not EMPTIES, because you cannot jump over empty spaces!
		List<HantoCoordinateImpl> adj = board.getAdjacentPieces(root);
		visited.add(root);
		for (HantoCoordinateImpl coord : adj) {
			if (!visited.contains(coord) && checkIfInLine(root, goal, coord)) {
				checkJumpPathValidity(coord, goal);
			}
		}
		return;
	}
	
	
	/**
	 * Determine if root, goal, and coord are all along the same line, which would constitute a valid jump path
	 * @param root - source of jump
	 * @param goal - destination of jump
	 * @param coord - coordinate along jump path
	 * @return if coord is along valid jump path
	 */
	private boolean checkIfInLine(HantoCoordinateImpl root, HantoCoordinateImpl goal, HantoCoordinateImpl coord) 
	{
		int rootX = root.getX();
		int rootY = root.getY();
		int goalX = goal.getX();
		int goalY = goal.getY();
		int coordX = coord.getX();
		int coordY = coord.getY();
		
		return (rootX == coordX && coordX == goalX) || (rootY == coordY && coordY == goalY) ||
				((Math.abs(root.getX() - coord.getX()) == manhattanDistance(root, coord) 
				&& Math.abs(root.getY() - coord.getY()) == manhattanDistance(root, coord)) &&
				(Math.abs(coord.getX() - goal.getX()) == manhattanDistance(coord, goal) 
				&& Math.abs(coord.getY() - goal.getY()) == manhattanDistance(coord, goal)));	
	}

	
	/**
	 * Calculates the Manhattan Distance between the two given coordinates.
	 * @param source
	 * @param dest
	 * @return the Manhattan Distance between source and dest
	 */
	private int manhattanDistance(HantoCoordinateImpl source, HantoCoordinateImpl dest) 
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
