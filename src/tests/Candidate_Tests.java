package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Candidate;
import utils.Constants;

public class Candidate_Tests {
	private Candidate candidate;

	@Before
	public void setUp() throws Exception {
		this.candidate = new Candidate(1, 45);
	}

	@After
	public void tearDown() throws Exception {
		this.candidate = null;
	}

}
