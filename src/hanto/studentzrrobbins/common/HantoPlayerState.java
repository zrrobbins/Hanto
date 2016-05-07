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
import java.util.HashMap;
import java.util.Map;

import hanto.common.*;
import static hanto.common.HantoPieceType.*;


/**
 * This class represents the player state, including player color, piece inventory, and if the player has 
 * played a butterfly.
 * @author zrrobbins
 *
 */
public class HantoPlayerState implements Serializable
{
	private static final long serialVersionUID = 3163948049959678708L;
	
	private HantoPlayerColor color;
	private Map<HantoPieceType, Integer> stockPile;
	private boolean hasPlayedButterfly;
	
	
	/**
	 * Constructor for HantoPlayerState
	 * @param color
	 */
	public HantoPlayerState(HantoPlayerColor color) 
	{
		this.color = color;
		stockPile = new HashMap<HantoPieceType, Integer>();
		hasPlayedButterfly = false;
	}
	
	
	/**
	 * Creates an entry in the stockPile for the pieceType given, with the number of pieces as the value
	 * @param pieceType
	 * @param numberOfPieces
	 */
	public void setStartingNumberofPieces(HantoPieceType pieceType, int numberOfPieces)
	{
		stockPile.put(pieceType, numberOfPieces);
	}
	
	
	/**
	 * Return piece requested from player stockPile if it exists. If not, return null.
	 * @param pieceType
	 * @return piece requested
	 */
	public HantoPiece getPieceFromStockPile(HantoPieceType pieceType) 
	{	
		// Check to see if any pieces are left of type requested are in stockpile
		if (stockPile.get(pieceType) > 0) {
			int numPieces = stockPile.get(pieceType);
			numPieces--;
			stockPile.replace(pieceType, numPieces);
			if (pieceType == BUTTERFLY) {
				hasPlayedButterfly = true;
			}
			return HantoPieceFactory.makeHantoPiece(pieceType, color);
		}
		else { // If none left, return null
			return null;
		}
	}
	
	
	/** 
	 * Return number of pieces of specified type in stockpile.
	 * @param type
	 * @return number of pieces
	 */
	public int piecesOfTypeLeftInStockPile(HantoPieceType type) 
	{
		try {
			return stockPile.get(type);
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	
	/**
	 * Returns true if butterfly has been played
	 * @return hasPlayedButterfly value
	 */
	public boolean hasPlayedButterfly() 
	{
		return hasPlayedButterfly;
	}
}
