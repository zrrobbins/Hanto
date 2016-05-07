package hanto.studentzrrobbins.tournament;

import static hanto.common.HantoGameID.*;
import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.tournament.HantoMoveRecord;

public class HantoTournamentMasterTest {
	
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
	
	//private static HantoGameFactory factory;
	//private HantoGame blueGame, redGame;
	
	HantoPlayer blueTournamentPlayer = new HantoPlayer();
	HantoPlayer redTournamentPlayer = new HantoPlayer();
	
	@Before
	public void setup()
	{
		blueTournamentPlayer.startGame(EPSILON_HANTO, BLUE, true);
		redTournamentPlayer.startGame(EPSILON_HANTO, RED, false);

	}
	
	@Test	// 1
	public void blueMakesFirstMove() throws HantoException
	{
		HantoMoveRecord result = blueTournamentPlayer.makeMove(null);
		assertEquals(BUTTERFLY, result.getPiece());
		assertNull(result.getFrom());
		assertEquals(0, result.getTo().getX());
		assertEquals(0, result.getTo().getY());
	}
	
	@Test	// 2
	public void blueMakesSecondMove() throws HantoException
	{
		HantoMoveRecord result = blueTournamentPlayer.makeMove(null);
		assertEquals(BUTTERFLY, result.getPiece());
		assertNull(result.getFrom());
		assertEquals(0, result.getTo().getX());
		assertEquals(0, result.getTo().getY());
		
		HantoMoveRecord opponentsMove = new HantoMoveRecord(BUTTERFLY, null, new TestHantoCoordinate(1,0));
		result = blueTournamentPlayer.makeMove(opponentsMove);
		System.out.println("Type: " + result.getPiece() + "   toX: " + result.getTo().getX() + "   toY: " + result.getTo().getY());
	}
	
	@Test	// 3
	public void blueCrabWalksToWin() throws HantoException
	{
		//blueTournamentPlayer.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0,0));
		blueTournamentPlayer.makeMove(null);
		// 0,0
		blueTournamentPlayer.makeMove(new HantoMoveRecord(BUTTERFLY, null, new TestHantoCoordinate(1,0)));
		// -1, 1
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(2,0)));
		// 0, 1
		blueTournamentPlayer.makeMove(new HantoMoveRecord(SPARROW, null, new TestHantoCoordinate(3,0)));
		// 1, -1
		blueTournamentPlayer.makeMove(new HantoMoveRecord(SPARROW, null,  new TestHantoCoordinate(4,0)));
		// BUTTERFLY 0,0 -->  0,-1
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(2,-1)));
		// SPARROW (0,0)
	}
	
	@Test	// 4
	public void redMakesFirstMove() throws HantoException
	{
		HantoMoveRecord result = blueTournamentPlayer.makeMove(new HantoMoveRecord(BUTTERFLY, null, new TestHantoCoordinate(0,0)));
		assertEquals(BUTTERFLY, result.getPiece());
		assertNull(result.getFrom());
		assertEquals(1, result.getTo().getX());
		assertEquals(0, result.getTo().getY());
		
	}
	
	@Test	// 5
	public void errorIsThrown() throws HantoException
	{
	    redTournamentPlayer.makeMove(null);
	    redTournamentPlayer.makeMove(new HantoMoveRecord(BUTTERFLY, null, new TestHantoCoordinate(0,0)));
	}
	
	@Test	// 5
	public void simulateGameAgainstItself() throws HantoException
	{
		blueTournamentPlayer.makeMove(null);
		blueTournamentPlayer.makeMove(new HantoMoveRecord(BUTTERFLY, null, new TestHantoCoordinate(1,0)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(1,1)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(2,0)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(2,-1)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(1,2)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(2,1)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(CRAB, null, new TestHantoCoordinate(2,2)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(HORSE, null, new TestHantoCoordinate(3,1)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(HORSE, null, new TestHantoCoordinate(3,0)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(HORSE, null, new TestHantoCoordinate(0,2)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(HORSE, null, new TestHantoCoordinate(2,3)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(SPARROW, null, new TestHantoCoordinate(3,2)));
		blueTournamentPlayer.makeMove(new HantoMoveRecord(SPARROW, null, new TestHantoCoordinate(1,3)));
	}
	

}
