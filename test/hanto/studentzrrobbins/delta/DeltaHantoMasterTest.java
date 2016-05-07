// $codepro.audit.disable methodJavadoc, missingAssertInTestMethod
/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentzrrobbins.delta;

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
public class DeltaHantoMasterTest
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
		blueGame = factory.makeHantoGame(HantoGameID.DELTA_HANTO);
		// Unless otherwise specified
		redGame = factory.makeHantoGame(HantoGameID.DELTA_HANTO, RED);
	}
	
	@Test	// 1
	public void redMakesValidMove() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		assertEquals(OK, blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1)));
	}
	
	@Test	// 2
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult moveResult = blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, moveResult);
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test	// 3
	public void bluePlacesInitialSparrowAtOrigin() throws HantoException
	{
		final MoveResult moveResult = blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, moveResult);
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test	// 4
	public void redPlacesInitialCrabAtOrigin() throws HantoException
	{
		assertEquals(OK, redGame.makeMove(CRAB, null, new TestHantoCoordinate(0, 0)));
		final HantoPiece p = redGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(CRAB, p.getType());
	}
	
	@Test(expected = HantoException.class)	 // 5
	public void redPlacesInitialCraneAtOrigin() throws HantoException
	{
		assertEquals(OK, redGame.makeMove(CRANE, null, new TestHantoCoordinate(0, 0)));
		final HantoPiece p = redGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(CRANE, p.getType());
	}

	@Test(expected = HantoException.class)	 // 6
	public void blueMovesToNonOriginOnFirstMove() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1));
	}
	
	@Test	 // 7
	public void redTriesToPlaceCrab() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1, 0));
	}

	@Test(expected = HantoException.class)	 // 8
	public void redPlacesButterflyAtInvalidLocation() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2, 0));
	}
	
	@Test(expected = HantoException.class)	 // 9
	public void redPlacesButterflyAtFilledOrigin() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
	}
	
	@Test(expected = HantoException.class)	 // 10
	public void bluePlacesCrabNextToRedSparrowOnThirdMove() throws HantoException
	{
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1, -1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1, 0));
	}
	
	@Test 	// 11
	public void blueButterflyIsAtOriginAfterMove() throws HantoException
	{
		assertNull(blueGame.getPieceAt(new TestHantoCoordinate(0, 0)));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final HantoPiece butterfly = blueGame.getPieceAt(new TestHantoCoordinate(0, 0));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(BLUE, butterfly.getColor());
	}
	
	@Test	// 12
	public void redButterflyIsAtCorrectPlaceAfterRedMoves() throws HantoException
	{
		blueGame.makeMove(HantoPieceType.BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1, -1));
		final HantoPiece butterfly = blueGame.getPieceAt(new TestHantoCoordinate(1, -1));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(RED, butterfly.getColor());
	}
	
	@Test	// 13
	public void redCrabInCorrectPlaceAfter3FullTurns() throws HantoException {
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-2,1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(2,0));
		final HantoPiece piece = blueGame.getPieceAt(new TestHantoCoordinate(1, 0));
		assertEquals(CRAB, piece.getType());
		assertEquals(RED, piece.getColor());
	}
	
	
	@Test	// 14
	public void blueSparrowFliesOverOccupiedSpots() throws HantoException
	{
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(BUTTERFLY, new TestHantoCoordinate(-1,0), new TestHantoCoordinate(-1,1));
		assertEquals(OK, blueGame.makeMove(SPARROW, new TestHantoCoordinate(2,0), new TestHantoCoordinate(-1,0)));
	}
	
	@Test	// 15
	public void blueSparrowFliesOverUnoccupiedSpots() throws HantoException
	{
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,-2));
		assertEquals(OK, blueGame.makeMove(SPARROW, new TestHantoCoordinate(0,-2), new TestHantoCoordinate(3,-3)));
		final HantoPiece blueSparrow = blueGame.getPieceAt(new TestHantoCoordinate(3, -3));
		final HantoPiece nullPiece = blueGame.getPieceAt(new TestHantoCoordinate(0, -2));
		assertEquals(SPARROW, blueSparrow.getType());
		assertEquals(BLUE, blueSparrow.getColor());
		assertNull(nullPiece);
	}
	
	@Test	// 16
	public void blueSparrowFlies4Spots() throws HantoException
	{
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,-2));
		assertEquals(OK, blueGame.makeMove(SPARROW, new TestHantoCoordinate(0,-2), new TestHantoCoordinate(3,-1)));
	}
	
	@Test(expected = HantoException.class)	// 17
	public void redSparrowFlies3SpotsToOccupiedLocation() throws HantoException
	{
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,-2));
		assertEquals(OK, blueGame.makeMove(SPARROW, new TestHantoCoordinate(0,-2), new TestHantoCoordinate(3,-2)));
	}
	
	@Test	// 18
	public void redSparrowFlies2Spots() throws HantoException
	{
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		redGame.makeMove(CRAB, null, new TestHantoCoordinate(1,-2));
		assertEquals(OK, redGame.makeMove(SPARROW, new TestHantoCoordinate(0,1), new TestHantoCoordinate(1,-1)));
	}
	
	@Test	// 19
	public void redSparrowFlies1Spot() throws HantoException
	{
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		redGame.makeMove(CRAB, null, new TestHantoCoordinate(1,-2));
		assertEquals(OK, redGame.makeMove(SPARROW, new TestHantoCoordinate(0,1), new TestHantoCoordinate(1,0)));
	}
	
	@Test(expected=HantoException.class)	// 20
	public void redSparrowFlies1SpotBreaksContiguity() throws HantoException
	{
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		redGame.makeMove(CRAB, null, new TestHantoCoordinate(1,-2));
		assertEquals(OK, redGame.makeMove(SPARROW, new TestHantoCoordinate(0,1), new TestHantoCoordinate(1,1)));
	}
	
	@Test	// 21
	public void blueCrabWalks3Spots() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		assertEquals(OK, blueGame.makeMove(CRAB, new TestHantoCoordinate(0,-1), new TestHantoCoordinate(0,1)));		
	}
	
	@Test(expected = HantoException.class)	// 22
	public void redCrabWalks3SpotsBreaksContiguity() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,1));
		assertEquals(OK, blueGame.makeMove(CRAB, new TestHantoCoordinate(1,0), new TestHantoCoordinate(0,-2)));
	}
	
	@Test(expected = HantoException.class)	// 23
	public void blueCrabWalks2SpotsToInvalidLocation() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		assertEquals(OK, blueGame.makeMove(CRAB, new TestHantoCoordinate(0,-1), new TestHantoCoordinate(1,0)));
	}
	
	@Test(expected = HantoException.class)	// 24
	public void blueCrabCannotSlideOnFirstWalkStep() throws HantoException
	{
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-2));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(-2,0), new TestHantoCoordinate(-1,1));
		assertEquals(OK, blueGame.makeMove(CRAB, new TestHantoCoordinate(0,0), new TestHantoCoordinate(-1,1)));
	}
	
	@Test	// 25
	public void redCrabIgnoresBadWalkPathAndFindsCorrectPath() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2,0));
		assertEquals(OK, blueGame.makeMove(CRAB, new TestHantoCoordinate(0,-1), new TestHantoCoordinate(0,1)));
	}
	
	@Test(expected = HantoException.class)	// 26
	public void redCrabTriesToWalk4Spaces() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2,0));
		assertEquals(OK, blueGame.makeMove(CRAB, new TestHantoCoordinate(0,-1), new TestHantoCoordinate(1,1)));
	}
	
	@Test(expected = HantoException.class)	// 27
	public void redSparrowTriesToFlyToSamePosition() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2,0));
		assertEquals(OK, blueGame.makeMove(SPARROW, new TestHantoCoordinate(0,-1), new TestHantoCoordinate(0,-1)));
	}
	
	@Test	// 28
	public void redSparrowFliesIntoEnclosure() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, new TestHantoCoordinate(0,0), new TestHantoCoordinate(0,1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-2));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(-2,1), new TestHantoCoordinate(-1,0));
		assertEquals(OK, blueGame.makeMove(SPARROW, new TestHantoCoordinate(1,-2), new TestHantoCoordinate(0,0)));
		final HantoPiece blueSparrow = blueGame.getPieceAt(new TestHantoCoordinate(0,0));
		final HantoPiece nullPiece = blueGame.getPieceAt(new TestHantoCoordinate(1,-2));
		assertEquals(SPARROW, blueSparrow.getType());
		assertEquals(RED, blueSparrow.getColor());
		assertNull(nullPiece);
	}
	
	@Test	// 29
	public void redPlayerResigns() throws HantoException 
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, new TestHantoCoordinate(0,0), new TestHantoCoordinate(0,1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		assertEquals(BLUE_WINS, blueGame.makeMove(null, null, null));
	}
	
	@Test	// 30
	public void redSparrowFliesToWin() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(2,0), new TestHantoCoordinate(0,1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(-2,1));
		assertEquals(RED_WINS, blueGame.makeMove(SPARROW, new TestHantoCoordinate(1,1), new TestHantoCoordinate(1,-1)));
	}
	
	@Test(expected = HantoException.class)	// 31
	public void tryToMoveAfterRedWins() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(2,0), new TestHantoCoordinate(0,1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(-2,1));
		assertEquals(RED_WINS, blueGame.makeMove(SPARROW, new TestHantoCoordinate(1,1), new TestHantoCoordinate(1,-1)));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(-2,1), new TestHantoCoordinate(-1,2));
	}
	
	@Test	// 32
	public void blueCrabWalksToWin() throws HantoException
	{
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(CRAB, null, new TestHantoCoordinate(2,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, new TestHantoCoordinate(1,1), new TestHantoCoordinate(0,1));
		redGame.makeMove(CRAB, null, new TestHantoCoordinate(-2,1));
		assertEquals(BLUE_WINS, redGame.makeMove(CRAB, new TestHantoCoordinate(2,0), new TestHantoCoordinate(1,-1)));
	}
	
	@Test(expected = HantoException.class)	// 33
	public void blueRunOutOfCrabs() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,2));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-2));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,3));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-3));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,4));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-4));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,5));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0,-5));
	}
	
	@Test(expected = HantoException.class)	// 34
	public void redRunOutOfSparrows() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,3));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-3));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,4));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-4));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,5));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-5));
	}
	
	@Test	// 35
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
	
	@Test	// 36
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
	
	@Test(expected = HantoException.class)	// 37
	public void moveBlueSparrowToInvalidLocationAfter2FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,0));
	}
	
	@Test(expected=HantoException.class)	// 38
	public void placeThirdPieceInOccupiedPlace() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
	}


	@Test	// 39
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

	@Test(expected=HantoException.class)	// 40
	public void placeThreeButterFliesInARow() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(2, 0));
	}

	@Test(expected=HantoException.class)	// 41
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

	@Test	// 42
	public void playFirstButterflies() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(3, 0));
	}

	@Test (expected=HantoException.class)	// 43
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

	@Test	// 44
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

	@Test 	// 45
	public void blueWins() throws HantoException
	{
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

	@Test	// 46
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

	@Test (expected=HantoException.class)		// 47	
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

	@Test (expected=HantoException.class)		// 48		
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

	@Test (expected=HantoException.class)	// 49
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

	@Test	// 50
	public void redPlacesFirstAtOrigin() throws HantoException
	{
		final MoveResult mr = redGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = redGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}


	@Test	// 51
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

	@Test	// 52
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

	@Test	// 53
	public void sparrowWalksOldPieceGone() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, -1));

		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(p, null);
	}

	@Test (expected=HantoException.class)	// 54
	public void sparrowWalksOnToOtherSparrow() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}

	@Test (expected=HantoException.class)	// 55
	public void sparrowPlacedAway() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));

	}

	@Test (expected=HantoException.class)	// 56
	public void sparrowWalksAway() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(-1, 0));
	}

	@Test(expected=HantoException.class) 	// 57
	public void BluePlaysNextToRed() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));

		blueGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
	}

	@Test(expected=HantoException.class)	// 58
	public void pieceJumpsNotWalks() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(2, 0));
	}

	@Test(expected=HantoException.class)	// 59
	public void trystoMoveAPieceFirst() throws HantoException
	{
		blueGame.makeMove(SPARROW, makeCoordinate(1,0), makeCoordinate(0, 0));
	}

	@Test(expected=HantoException.class)	// 60
	public void movesPieceThatIsNotThere() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(1, 1));
	}

	@Test(expected=HantoException.class)	// 61
	public void movesPieceThatIsOtherPlayers() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
		blueGame.makeMove(SPARROW, makeCoordinate(1, 0), makeCoordinate(0, 1));
	}


	@Test(expected=HantoException.class)	// 62
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


	@Test(expected=HantoException.class)	// 63
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

	
	@Test(expected=HantoException.class)	// 64
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
	
	
	@Test (expected=HantoException.class)	// 65
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
	
	@Test (expected=HantoException.class)	// 66
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

	@Test (expected=HantoException.class)	// 67
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

	@Test (expected=HantoException.class)	// 68
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


	@Test(expected = HantoException.class)	// 69
	public void noBlueButterflyOnBoardAfter4FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
	}
	
	@Test(expected = HantoException.class)	// 70
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
	
	@Test	// 71
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
	
	@Test(expected = HantoException.class)	// 72
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
	
	@Test(expected = HantoException.class)	// 73
	public void tryToPlaceTwoBlueButterflies() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
	}
	
	@Test(expected = HantoException.class)	// 74
	public void tryToPlaceTwoRedButterflies() throws HantoException
	{
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,0));
	}

	
	
	/*
	 * Testing the Print Method
	 */
	@Test	// 75
	public void printEmptyBoardBlueGame() throws HantoException
	{
		assertNotNull(blueGame.getPrintableBoard());
	}
	
	@Test	// 76
	public void printBoardWithButterfliesBlueGame() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		System.out.println(blueGame.getPrintableBoard());
		assertNotNull(blueGame.getPrintableBoard());
	}
	
	@Test	// 77
	public void thirdMoveBreaksContiguity() throws HantoException {
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
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * POLLICE TESTS FROM GAMMA HANTO
	 * 
	 * 
	 */
	
	class MoveData {
		final HantoPieceType type;
		final HantoCoordinate from, to;
		
		private MoveData(HantoPieceType type, HantoCoordinate from, HantoCoordinate to) 
		{
			this.type = type;
			this.from = from;
			this.to = to;
		}
	}
	
	@Test	// 78
	public void bluePlacesButterflyFirst() throws HantoException
	{
		final MoveResult mr = blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece piece = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, piece.getColor());
		assertEquals(BUTTERFLY, piece.getType());
	}
	
	@Test	// 79
	public void redPlacesSparrowFirst() throws HantoException
	{
		blueGame = factory.makeHantoGame(HantoGameID.DELTA_HANTO, RED);
		final MoveResult mr = blueGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
	}
	
	@Test	// 80
	public void blueMovesSparrow() throws HantoException
	{
		final MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(SPARROW, 0, -1, -1, 0));
		assertEquals(OK, mr);
		checkPieceAt(-1, 0, BLUE, SPARROW);
	}
	
	@Test(expected=HantoException.class)	// 81
	public void moveToDisconnectConfiguration() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, 0, -1));
	}
	
	@Test(expected=HantoException.class)	// 82
	public void moveButterflyToSameHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, 0, 0));
	}
	
	@Test(expected=HantoException.class)	// 83
	public void moveSparrowToOccupiedHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(SPARROW, 0, -1, 0, 0));
	}
	
	@Test(expected=HantoException.class)	// 84
	public void moveFromEmptyHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 1, 0, 1, -1));
	}
	
	@Test(expected=HantoException.class)	// 85
	public void tryToMoveTooFar() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, -1, 2));
	}
	
	@Test(expected=HantoException.class)	// 86
	public void tryToMoveWrongPieceType() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(BUTTERFLY, 0, -1, -1, 0));
	}
	
	@Test(expected=HantoException.class)	// 87
	public void tryToMoveWrongColorPiece() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(SPARROW, 0, 2, 1, 1));
	}
	
	@Test(expected=HantoException.class)	// 88
	public void tryToMoveWhenNotEnoughSpace() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(SPARROW, -1, 0), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1), md(SPARROW, 0, 3),
				md(BUTTERFLY, 0, 0, 0, -1));
	}
	
	@Test(expected=HantoException.class)	// 89
	public void tryToUseTooManyButterflies() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, -1));
	}
	
	@Test(expected=HantoException.class)	// 90
	public void tryToUseTooManySparrows() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(SPARROW, 0, -1), md(SPARROW, 0, 2),
				md(SPARROW, 0, -2), md(SPARROW, 0, 3),
				md(SPARROW, 0, -3), md(SPARROW, 0, 4),
				md(SPARROW, 0, -4), md(SPARROW, 0, 5),
				md(SPARROW, 0, -5), md(SPARROW, 0, 6),
				md(SPARROW, 0, -6));
	}
	
	@Test(expected=HantoException.class)	// 91
	public void tryToUsePieceNotInGame() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(CRANE, 0, -1));
	}
	
	@Test	// 92
	public void blueWins2() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, 1, 1),
				md(SPARROW, 1, -1), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(SPARROW, -1, 0, -1, 1));
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test	// 93
	public void redSelfLoses() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1), md(SPARROW, 1, 2),
				md(SPARROW, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(SPARROW, -1, 0, -1, 1), md(SPARROW, 1, 2, 1, 1));
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test(expected=HantoException.class)	// 94
	public void tryToPlacePieceNextToOpponent() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, -2, 0));
	}
	
	@Test(expected=HantoException.class)	// 95
	public void butterflyNotPlacedByFourthMoveByFirstPlayer() throws HantoException
	{
		makeMoves(md(SPARROW, 0, 0), md(SPARROW, 0, 1),
				md(SPARROW, 0, -1), md(SPARROW, 0, 2),
				md(SPARROW, 0, -2), md(SPARROW, 0, 3),
				md(SPARROW, 0, -3));
	}
	
	@Test(expected=HantoException.class)	// 96
	public void butterflyNotPlacedByFourthMoveBySecondPlayer() throws HantoException
	{
		makeMoves(md(SPARROW, 0, 0), md(SPARROW, 0, 1),
				md(BUTTERFLY, 0, -1), md(SPARROW, 0, 2),
				md(SPARROW, 0, -2), md(SPARROW, 0, 3),
				md(SPARROW, 0, -3), md(SPARROW, 0, 4));
	}
	
	@Test(expected=HantoException.class)	// 97
	public void tryToMoveAfterGameIsOver() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, 1, 1),
				md(SPARROW, 1, -1), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(SPARROW, -1, 0, -1, 1), md(SPARROW, 0, 3));
	}
	
	@Test(expected=HantoException.class)	// 98
	public void extraCreditMoveSparrowBeforeButterflyIsOnBoard() throws HantoException
	{
		makeMoves(md(SPARROW, 0, 0), md (BUTTERFLY, 0, 1), md(SPARROW, 0, 0, -1, 1));
		final HantoPiece piece = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(SPARROW, piece.getType());
		assertEquals(BLUE, piece.getColor());
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	/**
	 * Make sure that the piece at the location is what you expect
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param color piece color expected
	 * @param type piece type expected
	 */
	private void checkPieceAt(int x, int y, HantoPlayerColor color, HantoPieceType type)
	{
		final HantoPiece piece = blueGame.getPieceAt(makeCoordinate(x, y));
		assertEquals(color, piece.getColor());
		assertEquals(type, piece.getType());
	}
	
	/**
	 * Make a MoveData object given the piece type and the x and y coordinates of the
	 * desstination. This creates a move data that will place a piece (source == null)
	 * @param type piece type
	 * @param toX destination x-coordinate
	 * @param toY destination y-coordinate
	 * @return the desitred MoveData object
	 */
	private MoveData md(HantoPieceType type, int toX, int toY) 
	{
		return new MoveData(type, null, makeCoordinate(toX, toY));
	}
	
	private MoveData md(HantoPieceType type, int fromX, int fromY, int toX, int toY)
	{
		return new MoveData(type, makeCoordinate(fromX, fromY), makeCoordinate(toX, toY));
	}
	
	/**
	 * Make the moves specified. If there is no exception, return the move result of
	 * the last move.
	 * @param moves
	 * @return the last move result
	 * @throws HantoException
	 */
	private MoveResult makeMoves(MoveData... moves) throws HantoException
	{
		MoveResult mr = null;
		for (MoveData md : moves) {
			mr = blueGame.makeMove(md.type, md.from, md.to);
		}
		return mr;
	}
}
