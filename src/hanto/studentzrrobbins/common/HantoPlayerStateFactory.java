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

import hanto.common.*;
import static hanto.common.HantoPieceType.*;

/**
 * Responsible for creating HantoPlayerState objects
 * @author zrrobbins
 *
 */
public class HantoPlayerStateFactory 
{
	
	/**
	 * Make a player state given a Hanto Game version and player color.
	 * @param version
	 * @param color
	 * @return player state
	 */
	public static HantoPlayerState makePlayerState(HantoGameID version, HantoPlayerColor color) 
	{
		HantoPlayerState playerState = new HantoPlayerState(color);

		switch(version) {
			case BETA_HANTO:
				playerState.setStartingNumberofPieces(BUTTERFLY, 1);
				playerState.setStartingNumberofPieces(SPARROW, 5);
				break;
			case GAMMA_HANTO:
				playerState.setStartingNumberofPieces(BUTTERFLY, 1);
				playerState.setStartingNumberofPieces(SPARROW, 5);
				break;
			case DELTA_HANTO:
				playerState.setStartingNumberofPieces(BUTTERFLY, 1);
				playerState.setStartingNumberofPieces(CRAB, 4);
				playerState.setStartingNumberofPieces(SPARROW, 4);
				break;
			case EPSILON_HANTO:
				playerState.setStartingNumberofPieces(BUTTERFLY, 1);
				playerState.setStartingNumberofPieces(SPARROW, 2);
				playerState.setStartingNumberofPieces(CRAB, 6);
				playerState.setStartingNumberofPieces(HORSE, 4);
				break;		
		default:
			playerState = null;
		}

		return playerState;
	}	
}
