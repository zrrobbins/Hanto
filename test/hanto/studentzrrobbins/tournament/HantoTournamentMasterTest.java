package hanto.studentzrrobbins.tournament;

import static hanto.common.HantoGameID.*;
import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.MoveResult.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.studentzrrobbins.HantoGameFactory;
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
	
	@BeforeClass
	public static void initializeClass()
	{
		//factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		blueTournamentPlayer.startGame(EPSILON_HANTO, BLUE, true);
		// By default, blue moves first.
		//blueGame = factory.makeHantoGame(HantoGameID.EPSILON_HANTO);
		// Unless otherwise specified
		//redGame = factory.makeHantoGame(HantoGameID.EPSILON_HANTO, RED);
		
		
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

}
