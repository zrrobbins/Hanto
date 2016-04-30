// $codepro.audit.disable methodJavadoc, oneStatementPerLine, missingAssertInTestMethod
/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentzrrobbins.gamma;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentzrrobbins.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Gamma Hanto.
 * @version Sep 14, 2014
 */
public class GammaHantoMasterTest
{
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		private TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}

	}
	
	private static HantoGameFactory factory;
	private HantoGame blueGame, redGame;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		blueGame = factory.makeHantoGame(HantoGameID.GAMMA_HANTO);
		// Unless otherwise specified
		redGame = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);
	}
	
	@Test	
	public void redMakesValidMove() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		assertEquals(OK, blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1)));
	}
	
	@Test	
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult moveResult = blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, moveResult);
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test	
	public void bluePlacesInitialSparrowAtOrigin() throws HantoException
	{
		final MoveResult moveResult = blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, moveResult);
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}

	@Test(expected = HantoException.class)	
	public void blueMovesToNonOriginOnFirstMove() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1));
	}
	
	@Test(expected = HantoException.class)	
	public void redTriesToPlaceCrab() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0, 0));
	}

	@Test(expected = HantoException.class)	
	public void redPlacesButterflyAtInvalidLocation() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2, 0));
	}
	
	@Test(expected = HantoException.class) 
	public void redPlacesButterflyAtFilledOrigin() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
	}
	
	@Test 	
	public void blueButterflyIsAtOriginAfterMove() throws HantoException
	{
		assertNull(blueGame.getPieceAt(new TestHantoCoordinate(0, 0)));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final HantoPiece butterfly = blueGame.getPieceAt(new TestHantoCoordinate(0, 0));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(BLUE, butterfly.getColor());
	}
	
	@Test	
	public void redButterflyIsAtCorrectPlaceAfterRedMoves() throws HantoException
	{
		blueGame.makeMove(HantoPieceType.BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1, -1));
		final HantoPiece butterfly = blueGame.getPieceAt(new TestHantoCoordinate(1, -1));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(RED, butterfly.getColor());
	}
	
	@Test 
	public void redSparrowInCorrectPlaceAfter3FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-2,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		final HantoPiece piece = blueGame.getPieceAt(new TestHantoCoordinate(1, 0));
		assertEquals(SPARROW, piece.getType());
		assertEquals(RED, piece.getColor());
	}
	
	@Test 
	public void tryToMoveBlueSparrowAfterPlacement() throws HantoException {
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,2));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(1,-1), new TestHantoCoordinate(1,0));
		final HantoPiece sparrow = blueGame.getPieceAt(new TestHantoCoordinate(1,0));
		final HantoPiece nullPiece = blueGame.getPieceAt(new TestHantoCoordinate(1,-1));
		assertEquals(SPARROW, sparrow.getType());
		assertEquals(BLUE, sparrow.getColor());
		assertNull(nullPiece);
	}
	
	@Test 
	public void tryToMoveRedButterflyAfterPlacement() throws HantoException {
		System.out.println("Test 12: ");
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(BUTTERFLY, new TestHantoCoordinate(0,0), new TestHantoCoordinate(1,0));
		final HantoPiece butterfly = redGame.getPieceAt(new TestHantoCoordinate(1,0));
		final HantoPiece nullPiece = redGame.getPieceAt(new TestHantoCoordinate(0,0));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(RED, butterfly.getColor());
		assertNull(nullPiece);
	}
	
	@Test(expected = HantoException.class) // 13
	public void moveBlueSparrowToInvalidLocationAfter2FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,0));
	}
	
	@Test(expected=HantoException.class)
	public void placeThirdPieceInOccupiedPlace() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
	}


	@Test 
	public void placeThreeSparrowsInARow() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		final MoveResult mr = blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));

		assertEquals(OK, mr);
		HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());

		p = blueGame.getPieceAt(makeCoordinate(1, 0));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());

		p = blueGame.getPieceAt(makeCoordinate(-1, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}

	@Test(expected=HantoException.class) 
	public void placeThreeButterFliesInARow() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(2, 0));
	}

	@Test(expected=HantoException.class) 
	public void placeALotOfSparrows() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(3, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(4, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(5, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(6, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(7, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(8, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(9, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(10, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(11, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(12, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(13, 0));
	}

	@Test	
	public void playFirstButterflies() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(3, 0));
	}

	@Test (expected=HantoException.class)	
	public void drawAndKeepPlaying() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(3, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(4, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(5, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(6, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(7, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(8, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(9, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(10, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(10, 0));
		final MoveResult mr = blueGame.makeMove(SPARROW, null, makeCoordinate(11, 0));
		assertEquals(mr, MoveResult.DRAW);
	}

	@Test 
	public void redWins() throws HantoException
	{
		MoveResult mr = null;
		for(int turns = 1; turns <= 6; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(1, -2)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 2)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(2, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(0, 2)); break;
			case(6): mr = blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(0, 1)); break;
			}
		}
		assertEquals(mr, MoveResult.RED_WINS);
	}

	@Test 		
	public void blueWins() throws HantoException
	{
		System.out.print("++++++++++++++++++++++++++++TEST BLUE WINS++++++++++++++++++++++++++++");
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueGame.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		final MoveResult mr = blueGame.makeMove(SPARROW, makeCoordinate(0, -1), makeCoordinate(1, -1));
		assertEquals(mr, MoveResult.BLUE_WINS);
	}

	@Test	
	public void bothPlayersWinAtTheSameTimes() throws HantoException
	{
		MoveResult mr = null;
		for(int turns = 1; turns <= 6; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(1, -2)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(2, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(0, 2)); break;
			case(6): mr = blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(0, 1)); break;
			}
		}
		assertEquals(mr, MoveResult.DRAW);
	}

	@Test (expected=HantoException.class)		
	public void redWinsAndStillPlays() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));

		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 1));
	}

	@Test (expected=HantoException.class)		
	public void blueWinsAndStillPlays() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 1));

		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 1));
	}

	@Test (expected=HantoException.class)	
	public void bothPlayersWinAtTheSameTimesAndKeepPlaying() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, -1));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 1));

		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 1));
	}

	@Test	
	public void redPlacesFirstAtOrigin() throws HantoException
	{
		final MoveResult mr = redGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = redGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}


	@Test	
	public void winOnLastMoveRed() throws HantoException 
	{	
		MoveResult mr = null;
		for(int turns = 1; turns <= 10; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 1)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(-2, 1), makeCoordinate(-2, 0)); System.out.println(blueGame.getPrintableBoard());break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(-2, 0), makeCoordinate(-1, -1)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(-1, -1), makeCoordinate(0, -2)); break;
			case(9): blueGame.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2)); break;
			case(10): mr = blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 2)); break;
			case(5): blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(9):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(10): mr = blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(0, 1)); break;
			}
		}
		assertEquals(MoveResult.RED_WINS, mr);
	}

	@Test 
	public void butterflyWalksPieceMoves() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, -1));
		System.out.println(blueGame.getPrintableBoard());
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(1, -1));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}

	@Test 
	public void sparrowWalksOldPieceGone() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, -1));

		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(p, null);
	}

	@Test (expected=HantoException.class) 
	public void sparrowWalksOnToOtherSparrow() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}

	@Test (expected=HantoException.class) 
	public void sparrowPlacedAway() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));

	}

	@Test (expected=HantoException.class) 
	public void sparrowWalksAway() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(-1, 0));
	}

	@Test(expected=HantoException.class)  
	public void BluePlaysNextToRed() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));

		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
	}

	@Test(expected=HantoException.class) 
	public void pieceJumpsNotWalks() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(2, 0));
	}

	@Test(expected=HantoException.class) 
	public void trystoMoveAPieceFirst() throws HantoException
	{
		blueGame.makeMove(SPARROW, makeCoordinate(1,0), makeCoordinate(0, 0));
	}

	@Test(expected=HantoException.class) 
	public void movesPieceThatIsNotThere() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(1, 1));
	}

	@Test(expected=HantoException.class) 
	public void movesPieceThatIsOtherPlayers() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(1, 0), makeCoordinate(0, 1));
	}


	@Test(expected=HantoException.class)	
	public void canNotMoveSurroundedBy5CDEFA() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(2, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		blueGame.makeMove(BUTTERFLY, makeCoordinate(2, -1), makeCoordinate(1, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-2, -1));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(0, -1));
	}


	@Test(expected=HantoException.class)	
	public void canNotMoveSurroundedBy6() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(2, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		blueGame.makeMove(BUTTERFLY, makeCoordinate(2, -1), makeCoordinate(1, -1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, -2));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(1, 1), makeCoordinate(0, 1));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(0, 1));
	}


	@Test (expected=HantoException.class)	
	public void errorat21turns() throws HantoException
	{
		for(int turns = 1; turns <= 21; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 1)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(-2, 1), makeCoordinate(-2, 0)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(-2, 0), makeCoordinate(-1, -1)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(-1, -1), makeCoordinate(0, -2)); break;
			case(9): blueGame.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2)); break;
			case(10): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			case(11): blueGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, -2)); break;
			case(12): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			case(13): blueGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, -2)); break;
			case(14): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			case(15): blueGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, -2)); break;
			case(16): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			case(17): blueGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, -2)); break;
			case(18): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			case(19): blueGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, -2)); break;
			case(20): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			case(21): blueGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, -2)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 2)); break;
			case(5):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(9):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(10): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(11):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(12): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(13):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(14): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(15):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(16): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(17):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(18): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(19):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(20): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			}
		}
	}

	
	@Test(expected=HantoException.class)
	public void redButterflyNotPlayedInTime() throws HantoException
	{
		for(int turns = 1; turns <= 5; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 2)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(0, 2)); break;
			}
		}
	}
	
	
	@Test (expected=HantoException.class)	
	public void playsToManyButterFlies() throws HantoException
	{
		for(int turns = 1; turns <= 11; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 1)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(-2, 1), makeCoordinate(-2, 0)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(-2, 0), makeCoordinate(-1, -1)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(-1, -1), makeCoordinate(0, -2)); break;
			case(9): blueGame.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2)); break;
			case(10): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 2)); break;
			case(5):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(9):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(10): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			}
		}
	}
	
	@Test (expected=HantoException.class)
	public void playsToManySparrows() throws HantoException
	{
		for(int turns = 1; turns <= 11; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -1)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 1)); break;
			case(6): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -2)); break;
			case(7): blueGame.makeMove(SPARROW, null, makeCoordinate(0, -3)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(-1, -1), makeCoordinate(0, -2)); break;
			case(9): blueGame.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2)); break;
			case(10): blueGame.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(1, -1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 2)); break;
			case(5):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(6): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(8): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			case(9):  blueGame.makeMove(SPARROW, makeCoordinate(1, 2), makeCoordinate(0, 2)); break;
			case(10): blueGame.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 2)); break;
			}
		}
	}

	@Test (expected=HantoException.class)
	public void cantMoveWithTripleLoop() throws HantoException
	{
		for(int turns = 1; turns <= 11; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 1)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 2)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 3)); break;
			case(5): blueGame.makeMove(SPARROW, makeCoordinate(-1, 3), makeCoordinate(0, 2)); break;
			case(6): blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0)); break;
			case(7): blueGame.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(0, -1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			case(2): blueGame.makeMove(SPARROW, null, makeCoordinate(1, 1)); break;
			case(3): blueGame.makeMove(SPARROW, null, makeCoordinate(2, -1)); break;
			case(4): blueGame.makeMove(SPARROW, null, makeCoordinate(2, -2)); break;
			case(5): blueGame.makeMove(SPARROW, null, makeCoordinate(1, -2)); break;
			case(6): blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0)); break;
			case(7): blueGame.makeMove(BUTTERFLY, makeCoordinate(1, 0), makeCoordinate(1, -1)); break;
			}
		}
	}

	@Test (expected=HantoException.class) 
	public void movesBeforeButterFlyPlace() throws HantoException
	{
		for(int turns = 1; turns <= 2; turns++)
		{
			switch(turns)
			{
			case(1): blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0)); break;
			case(2): blueGame.makeMove(SPARROW, makeCoordinate(0,0), makeCoordinate(0, 1)); break;
			}
			switch(turns)
			{
			case(1): blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0)); break;
			}
		}
	}


	@Test(expected = HantoException.class) 
	public void noBlueButterflyOnBoardAfter4FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
	}
	
	@Test(expected = HantoException.class) 
	public void noRedButterflyOnBoardAfter4FullTurns() throws HantoException {
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,1));
	}
	
	@Test 
	public void blueButterflyIsEnclosed() throws HantoException {
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));   
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(1,-1), new TestHantoCoordinate(2,-1));  // move blue sparrow
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,1));
		blueGame.makeMove(BUTTERFLY, new TestHantoCoordinate(0,0), new TestHantoCoordinate(1,0));  // move blue butterfly
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1)); 
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(2,-2), new TestHantoCoordinate(1,-1));   // move blue sparrow
		System.out.println(blueGame.getPrintableBoard());
		assertEquals(RED_WINS, blueGame.makeMove(SPARROW, new TestHantoCoordinate(-1,1), new TestHantoCoordinate(0,0)));   // move red sparrow to enclose blue butterfly
		
	}
	
	@Test(expected = HantoException.class)	
	public void attemptToMoveAfterBlueGameEnds() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,1));
		assertEquals(RED_WINS, blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0)));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,0));
	}
	
	@Test(expected = HantoException.class)	
	public void tryToPlaceTwoBlueButterflies() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
	}
	
	@Test(expected = HantoException.class)	
	public void tryToPlaceTwoRedButterflies() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,0));
	}
	
	
	/*
	 * 	 Tests that deal with when red is first player in game
	 */
	
	@Test	
	public void redPlacedInitialButterflyAtOrigin() throws HantoException
	{
		assertEquals(OK, redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0)));
		final HantoPiece piece = redGame.getPieceAt(new TestHantoCoordinate(0,0));
		assertEquals(BUTTERFLY, piece.getType());
		assertEquals(RED, piece.getColor());
	}
	
	@Test(expected = HantoException.class)	// 25
	public void redMovesToNonOriginOnFirstMove() throws HantoException
	{
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1));
	}
	
	@Test 	
	public void redSparrowIsAtOriginAfterFirstMove() throws HantoException
	{
		assertNull(redGame.getPieceAt(new TestHantoCoordinate(0, 0)));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0, 0));
		final HantoPiece piece = redGame.getPieceAt(new TestHantoCoordinate(0, 0));
		assertEquals(SPARROW, piece.getType());
		assertEquals(RED, piece.getColor());
	}
	
	@Test(expected = HantoException.class) 
	public void noRedButterflyOnBoardAfter4FullTurnsRedGame() throws HantoException {
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
	}
	
	@Test(expected = HantoException.class)	
	public void attemptToMoveAfterRedGameEnds() throws HantoException
	{
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,2));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		assertEquals(BLUE_WINS, redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,2)));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,2));
	}
	
	
	
	/*
	 * Testing the Print Method
	 */
	@Test	
	public void printEmptyBoardBlueGame() throws HantoException
	{
		assertNotNull(blueGame.getPrintableBoard());
	}
	
	@Test	
	public void printBoardWithButterfliesBlueGame() throws HantoException
	{
		System.out.println("Test 32:\n");
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		System.out.println(blueGame.getPrintableBoard());
		assertNotNull(blueGame.getPrintableBoard());
	}
	
	@Test
	public void thirdMoveBreaksContiguity() throws HantoException {
		System.out.println("----Contiguity Test----");
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		try {
			blueGame.makeMove(BUTTERFLY, new TestHantoCoordinate(0,0), new TestHantoCoordinate(0,-1));
			System.out.println("Contiguity test failed");
			fail();
		}
		catch (HantoException e) {
			assertTrue(true);
		}



	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
