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

import hanto.common.HantoException;
import hanto.studentzrrobbins.common.HantoBoard;
import hanto.studentzrrobbins.common.HantoCoordinateImpl;


/**
 * Implementation of MoveValidatorStrategy to represent the Fly move in Hanto
 * @author zrrobbins
 *
 */
public class FlyValidator implements MoveValidatorStrategy, Serializable {

	private static final long serialVersionUID = 3682356342150592742L;
	
	private int maxFlyDistance;
	
	/**
	 * Constructor that takes a specified max fly distance
	 * @param maxFlyDistance
	 */
	public FlyValidator(int maxFlyDistance) {
		
		this.maxFlyDistance = maxFlyDistance;
	}

	
	@Override
	/*
	 * @see hanto.studentzrrobbins.common.movevalidators.MoveValidatorStrategy#canMove(hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.studentzrrobbins.common.HantoCoordinateImpl, hanto.studentzrrobbins.common.HantoBoard)
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
		// Make sure move doesn't break contiguity of existing pieces
		if (!board.compareDFSSize(from, to)) {
			throw new HantoException("Invalid move, contiguity of pieces broken!");
		}
		return (manhattanDistance(from, to) <= maxFlyDistance) ? true : false;
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
