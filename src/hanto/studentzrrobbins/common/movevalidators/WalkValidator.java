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

import java.util.ArrayList;
import java.util.List;

import hanto.common.HantoException;
import hanto.studentzrrobbins.common.HantoBoard;
import hanto.studentzrrobbins.common.HantoCoordinateImpl;

/**
 * Implementation of MoveValidatorStrategy to represent the Walk move in Hanto
 * @author zrrobbins
 *
 */
public class WalkValidator implements MoveValidatorStrategy {
	
	private int maxWalkDistance;
	private boolean pathFound;
	private List<HantoCoordinateImpl> visited;
	
	
	/**
	 * Default constructor
	 */
	public WalkValidator() {
		pathFound = false;
		visited = new ArrayList<HantoCoordinateImpl>();
		maxWalkDistance = 1;
	}
	

	/**
	 * Constructor that takes a specified max walk distance
	 * @param maxWalkDistance
	 */
	public WalkValidator(int maxWalkDistance) {
		pathFound = false;
		visited = new ArrayList<HantoCoordinateImpl>();
		this.maxWalkDistance = maxWalkDistance;
	}

	/*
	 * @see hanto.studentzrrobbins.common.MoveValidatorStrategy#canMove(hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.studentzrrobbins.common.HantoBoard)
	 */
	public boolean canMove(HantoCoordinateImpl from, HantoCoordinateImpl to, HantoBoard board) throws HantoException
	{
		// Destination cannot equal source
		if (from.equals(to)) {
			throw new HantoException("Walk destination cannot be the source!");
		}
		// Make sure destination is unoccupied
		if (board.getPieceAt(to) != null) {
			throw new HantoException("Invalid move, destination of piece must be unoccupied");
		}
		pathFound = false;
		visited.removeAll(visited);
		calculatePath(from, to, board, 0);
		return pathFound;
	}
	
	/**
	 * Check to see if piece at from can "slide" as described in Hanto rules to destination
	 * @param from - source coord
	 * @param to - desired destination
	 * @param board - current board
	 * @return if the piece can slide
	 * @throws HantoException
	 */
	private boolean checkIfCanSlide(HantoCoordinateImpl from, HantoCoordinateImpl to, HantoBoard board) throws HantoException
	{
		boolean canSlide = false;
		List<HantoCoordinateImpl> commons = board.getCommonAdjacents(from, to);
		if (commons.contains(from)){
			commons.remove(from); // Remove checking itself as an adjacent piece
		}
		if (commons.size() < 2) {
			canSlide = board.compareDFSSize(from, to);
		}
		return canSlide;
	}
	
	
	/**
	 * Use depth first search to see if there is a valid path to goal
	 * @param root - start coord
	 * @param goal - desired destination coord
	 * @param board - current Hanto board
	 * @throws HantoException 
	 */
	private void calculatePath(HantoCoordinateImpl root, HantoCoordinateImpl goal, 
			HantoBoard board, int currentDistance) throws HantoException {
		if (currentDistance > maxWalkDistance) {
			return;
		}
		if (pathFound) {
			return;
		}
		if (root.equals(goal)) {
			pathFound = true;
		}
		
		List<HantoCoordinateImpl> adj = board.getAdjacentEmpties(root);
		visited.add(root);
		for (HantoCoordinateImpl coord : adj) {
			if (!visited.contains(coord) && checkIfCanSlide(root, coord, board)) {
				calculatePath(coord, goal, board, currentDistance + 1);
			}
		}
		return;
	}
}
