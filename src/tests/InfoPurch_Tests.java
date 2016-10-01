package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.PlayerPurchasedInfo;
import utils.Constants;

public class InfoPurch_Tests {
	private PlayerPurchasedInfo ppi;

	@Before
	public void setUp() throws Exception {
		int[] candidatesPerGame = new int[]{3, 5};
		ppi = new PlayerPurchasedInfo(2, candidatesPerGame);
	}

	@After
	public void tearDown() throws Exception {
		ppi = null;
	}

	@Test
	public void test() {
		ppi.addOnesToken(0, Constants.SECOND_BUY, 2);
		ppi.addZerosToken(1, Constants.FIRST_BUY, 4);

		assert ppi.getRoundTokens(0, Constants.FIRST_BUY).getOnes()[2] == 0;
		
		assert ppi.getRoundTokens(0, Constants.SECOND_BUY).getOnes()[2] == 1;
		assert ppi.getRoundTokens(0, Constants.SECOND_BUY).getAll()[2] == 1;
		
		assert ppi.getRoundTokens(1, Constants.FIRST_BUY).getOnes()[4] == 0;
		assert ppi.getRoundTokens(1, Constants.FIRST_BUY).getAll()[4] == 1;
	}

}
