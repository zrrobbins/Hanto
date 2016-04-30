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

package hanto.studentzrrobbins.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentzrrobbins.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * @version Sep 14, 2014
 */
public class BetaHantoMasterTest
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
		blueGame = factory.makeHantoGame(HantoGameID.BETA_HANTO);
		// Unless otherwise specified
		redGame = factory.makeHantoGame(HantoGameID.BETA_HANTO, RED);
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
		//assertEquals(p, null);
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class)	// 4
	public void blueMovesToNonOriginOnFirstMove() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1));
	}
	
	@Test(expected = HantoException.class)	// 5
	public void redTriesToPlaceCrab() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(CRAB, null, new TestHantoCoordinate(0, 0));
	}

	@Test(expected = HantoException.class)	// 6
	public void redPlacesButterflyAtInvalidLocation() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2, 0));
	}
	
	@Test(expected = HantoException.class) // 7
	public void redPlacesButterflyAtFilledOrigin() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
	}
	
	@Test 	// 8
	public void blueButterflyIsAtOriginAfterMove() throws HantoException
	{
		assertNull(blueGame.getPieceAt(new TestHantoCoordinate(0, 0)));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final HantoPiece butterfly = blueGame.getPieceAt(new TestHantoCoordinate(0, 0));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(BLUE, butterfly.getColor());
	}
	
	@Test	// 9
	public void redButterflyIsAtCorrectPlaceAfterRedMoves() throws HantoException
	{
		blueGame.makeMove(HantoPieceType.BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1, -1));
		final HantoPiece butterfly = blueGame.getPieceAt(new TestHantoCoordinate(1, -1));
		assertEquals(BUTTERFLY, butterfly.getType());
		assertEquals(RED, butterfly.getColor());
	}
	
	@Test // 10
	public void redSparrowInCorrectPlaceAfter3FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		final HantoPiece piece = blueGame.getPieceAt(new TestHantoCoordinate(1, 0));
		assertEquals(SPARROW, piece.getType());
		assertEquals(RED, piece.getColor());
	}
	
	@Test(expected = HantoException.class) // 11
	public void tryToMoveBlueSparrowAfterPlacement() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, new TestHantoCoordinate(0,0), new TestHantoCoordinate(1,0));
	}
	
	@Test(expected = HantoException.class) // 12
	public void tryToMoveRedButterflyAfterPlacement() throws HantoException {
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, new TestHantoCoordinate(0,0), new TestHantoCoordinate(0,0));
	}
	
	@Test(expected = HantoException.class) // 13
	public void moveBlueSparrowToInvalidLocationAfter2FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,0));
	}
	
	@Test(expected = HantoException.class) // 14
	public void noBlueButterflyOnBoardAfter4FullTurns() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
	}
	
	@Test(expected = HantoException.class) // 15
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
	
	@Test // 16
	public void noPiecesLeftAfter6FullTurnsBlueGame() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(4,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(5,1));
		assertEquals(DRAW, blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(6,1)));
	}
	
	@Test // 17
	public void redButterflyIsEnclosed() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-1));
		assertEquals(BLUE_WINS, blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1)));
	}
	
	@Test // 18
	public void blueButterflyIsEnclosed() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,1));
		assertEquals(RED_WINS, blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0)));
	}
	
	@Test // 19
	public void bothButterfliesAreEnclosedBlueGame() throws HantoException {
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		assertEquals(DRAW, blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0)));
	}
	
	@Test // 20
	public void blueButterflyIsMostRecentMoveAndCausesItsOwnEnclosure() throws HantoException {
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-1));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-2));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		assertEquals(RED_WINS, blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,-1)));
	}
	
	@Test(expected = HantoException.class)	// 21
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
	
	@Test(expected = HantoException.class)	// 21
	public void tryToPlaceTwoBlueButterflies() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
	}
	
	@Test(expected = HantoException.class)	// 23
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
	
	@Test	// 24
	public void redPLacedInitialButterflyAtOrigin() throws HantoException
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
	
	@Test 	// 26
	public void redSparrowIsAtOriginAfterFirstMove() throws HantoException
	{
		assertNull(redGame.getPieceAt(new TestHantoCoordinate(0, 0)));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0, 0));
		final HantoPiece piece = redGame.getPieceAt(new TestHantoCoordinate(0, 0));
		assertEquals(SPARROW, piece.getType());
		assertEquals(RED, piece.getColor());
	}
	
	@Test(expected = HantoException.class) // 27
	public void noRedButterflyOnBoardAfter4FullTurnsRedGame() throws HantoException {
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,1));
	}
	
	@Test // 28
	public void noPiecesLeftAfter6FullTurnsRedGame() throws HantoException {
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(4,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(5,1));
		assertEquals(DRAW, redGame.makeMove(SPARROW, null, new TestHantoCoordinate(6,1)));
	}
	
	@Test // 29
	public void bothButterfliesAreEnclosedRedGame() throws HantoException {
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-2,2));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,2));
		assertEquals(DRAW, redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,1)));
	}
	
	@Test(expected = HantoException.class)	// 30
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
	@Test	// 31
	public void printEmptyBoardBlueGame() throws HantoException
	{
		assertNotNull(blueGame.getPrintableBoard());
	}
	
	@Test	// 32
	public void printBoardWithButterfliesBlueGame() throws HantoException
	{
		System.out.println("Test 32:\n");
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		System.out.println(blueGame.getPrintableBoard());
		assertNotNull(blueGame.getPrintableBoard());
	}
	
	@Test	// 33
	public void printBoardWithBlueButterflyAnd5SparrowsRedGame() throws HantoException
	{
		System.out.println("Test 33:\n");
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		System.out.println(redGame.getPrintableBoard());
		assertNotNull(redGame.getPrintableBoard());
	}
	
	@Test // 34
	public void printBoardAfterRedGameEnds() throws HantoException {
		System.out.println("Test 34:\n");
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,0));
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(-1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(1,-1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,0));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(0,-1));
		System.out.println("About to place error");
		redGame.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(2,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(3,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(4,1));
		redGame.makeMove(SPARROW, null, new TestHantoCoordinate(5,1));
		assertEquals(DRAW, redGame.makeMove(SPARROW, null, new TestHantoCoordinate(6,1)));
		System.out.println(redGame.getPrintableBoard());
		assertNotNull(redGame.getPrintableBoard());
	}

	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
