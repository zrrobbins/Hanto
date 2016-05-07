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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hanto.common.*;

/**
 * This class represents the board upon which Hanto is played, and contains many of the operations concerning
 * piece interaction and detection during the game.
 * @author zrrobbins
 *
 */
public class HantoBoard implements Serializable
{
	private static final long serialVersionUID = -8946320793430709358L;
	
	private Map<HantoCoordinateImpl, HantoPiece> board;
	private List<HantoCoordinateImpl> visited;
	private int numPieces;
	
	
	/**
	 * Make a new HantoBoard
	 */
	public HantoBoard() 
	{
		board = new HashMap<HantoCoordinateImpl, HantoPiece>();
		visited = new ArrayList<HantoCoordinateImpl>();
	}
	
	
	/**
	 * Returns piece at given coordinate, or null if there isn't one.
	 * @param coord - coordinate to check for piece at
	 * @return - piece at coordinate
	 */
	public HantoPiece getPieceAt(HantoCoordinateImpl coord) 
	{
		return board.get(coord);
	}
	
	
	/**
	 * Add a piece to the board.
	 * @param piece - piece to add
	 * @param coord - coordinate of piece to add
	 */
	public void addPiece(HantoPiece piece, HantoCoordinateImpl coord) 
	{
		board.put(coord, piece);
	}
	
	
	/**
	 * Return size of the board (i.e. number of pieces in play)
	 * @return
	 */
	public int getBoardSize() 
	{
		return board.keySet().size();
	}
	
	/**
	 * Returns a list of all adjacent coordinates to a given coordinate
	 * @param coordinate
	 * @return the list of adjacent coordinates
	 */
	public List<HantoCoordinateImpl> getAdjacentCoords(HantoCoordinateImpl coordinate) 
	{
		int x = coordinate.getX();
		int y = coordinate.getY();
		List<HantoCoordinateImpl> adjacentCoords = new ArrayList<HantoCoordinateImpl>();
		
		adjacentCoords.add(new HantoCoordinateImpl(x, y+1));
		adjacentCoords.add(new HantoCoordinateImpl(x+1, y));
		adjacentCoords.add(new HantoCoordinateImpl(x+1, y-1));
		adjacentCoords.add(new HantoCoordinateImpl(x, y-1));
		adjacentCoords.add(new HantoCoordinateImpl(x-1, y));
		adjacentCoords.add(new HantoCoordinateImpl(x-1, y+1));
		
		return adjacentCoords;
	}
	
	
	/**
	 * Determines and returns a list of the coordinates of any adjacent pieces to a given coordinate. 
	 * @param coordinate - the coordinate to check adjacency of
	 * @return a list of adjacent piece coordinates
	 */
	public List<HantoCoordinateImpl> getAdjacentPieces(HantoCoordinateImpl coordinate) 
	{
		List<HantoCoordinateImpl> adjacentPieces = new ArrayList<HantoCoordinateImpl>();
		
		for (HantoCoordinateImpl coord : getAdjacentCoords(coordinate)) {
			if (getPieceAt(coord) != null) {
				adjacentPieces.add(coord);
			}
		}
		
		return adjacentPieces;
	}
	
	
	/**
	 * Determine the common coordinates given two lists of coordinates
	 * @param piece_1
	 * @param piece_2
	 * @return the list of common adjacent coordinates
	 */
	public List<HantoCoordinateImpl> getCommonAdjacents(HantoCoordinateImpl piece_1, HantoCoordinateImpl piece_2) 
	{
		List<HantoCoordinateImpl> adjacentPieceList_1 = getAdjacentPieces(piece_1);
		List<HantoCoordinateImpl> adjacentPieceList_2 = getAdjacentPieces(piece_2);	
		List<HantoCoordinateImpl> common = new ArrayList<HantoCoordinateImpl>();
		
		for (HantoCoordinateImpl pieceA : adjacentPieceList_1) {
			for (HantoCoordinateImpl pieceB : adjacentPieceList_2) {
				if (pieceA.equals(pieceB)) {
					common.add(pieceA);
				}
			}
		}
		
		return common;
	}
	
	/**
	 * Returns the list of adjacent empty spaces around a given HantoCoordinateImpl
	 * @param coord
	 * @return the list of empty spaces
	 */
	public List<HantoCoordinateImpl> getAdjacentEmpties(HantoCoordinateImpl coord) {
		List<HantoCoordinateImpl> adjacentCoords = getAdjacentCoords(coord);
		List<HantoCoordinateImpl> adjacentPieces = getAdjacentPieces(coord);
		List<HantoCoordinateImpl> adjacentEmpties = new ArrayList<HantoCoordinateImpl>();
		
		for (HantoCoordinateImpl c : adjacentCoords) {
			if (!adjacentPieces.contains(c)) {
				adjacentEmpties.add(c);
			}
		}
		
		return adjacentEmpties;
	}
	

	/**
	 * Determines whether or not the given hex is surrounded by pieces on all of its 6 adjacent sides.
	 * @param coordinate - the hex to check 
	 * @return if the hex is surrounded or not
	 */
	public boolean isHexSurrounded(HantoCoordinateImpl coordinate) 
	{	
		return (getAdjacentPieces(coordinate).size() == 6);
	}
	
	
	/**
	 * Changes the location of the piece at moveFrom to moveTo
	 * @param moveFrom - original location of piece
	 * @param moveTo - new location of piece
	 */
	public void changePieceLocation(HantoCoordinateImpl moveFrom, HantoCoordinateImpl moveTo) 
	{
		board.put(moveTo, board.remove(moveFrom));		
	}
	
	
	/**
	 * Calls depth first search on the two given coordinates and compares the values.
	 * @param src - the first coordinate to call DFS on
	 * @param dest - the second coordinate to call DFS on
	 * @return true if the DFS sizes are equal, false otherwise
	 * @throws HantoException
	 */
	public boolean compareDFSSize(HantoCoordinateImpl src, HantoCoordinateImpl dest) throws HantoException
	{
		int srcSize, destSize;
		
		visited.removeAll(visited);
		visited.add(dest);
		srcSize = getDFSSize(src);
 		visited.removeAll(visited);
		visited.add(src);
		destSize = getDFSSize(dest);
		return srcSize == destSize ? true : false;
	}
	
	
	/**
	 * Depth first search of playing board, will return number of pieces connected to root.
	 * @param root - the start position
	 * @return size of graph connected to root (graph nodes are pieces on board)
	 * @throws HantoException
	 */
	public int getDFSSize(HantoCoordinateImpl root) throws HantoException
	{
		numPieces = 0;
		dfs(root);
		return numPieces;
	}
	
	
	/**
	 * Helper method for getDFSize()
	 * @param root
	 */
	public void dfs(HantoCoordinateImpl root) 
	{
		List<HantoCoordinateImpl> adj = getAdjacentPieces(root);
		visited.add(root);
		for (HantoCoordinateImpl coord : adj) {
			if (!visited.contains(coord)) {
				numPieces++;
				dfs(coord);
			}
		}
	}
	
	
	/**
	 * Get set of keys for printing
	 * @return
	 */
	public Set<HantoCoordinateImpl> getCoordinateSet() {
		return board.keySet();
	}


	/**
	 * Generate a list of the locations of all pieces on the board belonging to the specified player
	 * @param currentPlayerColor - the color of the player to fetch pieces for
	 * @return - pieces belonging to player of currentPlayerColor
	 */
	public List<HantoCoordinateImpl> getPlayerPieceLocations(HantoPlayerColor currentPlayerColor) {
		List<HantoCoordinateImpl> playerPieces = new ArrayList<HantoCoordinateImpl>();
		for (HantoCoordinateImpl coord : getCoordinateSet()) {
			if (currentPlayerColor == getPieceAt(coord).getColor()) {
				playerPieces.add(coord);
			}
		}
		return playerPieces;
	}	
	
	
	/**
	 * Return location of butterfly of requested color, or null if it doesn't exist
	 * @param color
	 * @return location of butterfly
	 */
	public HantoCoordinateImpl getPlayerButterflyLocation(HantoPlayerColor color) {
		HantoCoordinateImpl butterflyLocation = null;
		for (HantoCoordinateImpl pieceLocation : board.keySet()) {
			if (board.get(pieceLocation).getColor() == color && board.get(pieceLocation).getType() == HantoPieceType.BUTTERFLY) {
				butterflyLocation = pieceLocation;
			}
		}
		return butterflyLocation;
	}
}
