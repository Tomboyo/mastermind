package edu.vwc.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MastermindTest {

  public static Mastermind mastermind = null;
  
  /**
   * View configuration settings for a game of mastermind
   * Indicates number of unique colors allowed per code, number of pegs per code, and whether or not
   * the game will give verbose output (a diagram of moves to make based on responses in order to win quickly)
   */
  @Test
  public void testGetConfig() {
    mastermind = new Mastermind(5, 4, false);
    assertEquals("Colors: 5, Pegs: 4, Verbose?: false", mastermind.getConfig());
  }
  
  /**
   * Change the configuration for a game of mastermind
   * Set the number of unique colors allowed per code, number of pegs per code, and whether or not
   * the game will give verbose output (a diagram of moves to make based on responses in order to win quickly).
   * 
   * If colors or pegs is not a positive integer, the game will not run.
   */
  @Test
  public void testSetConfig() {
    mastermind = new Mastermind(5, 4, false);
    mastermind.setConfig(2,  1, true);
    assertEquals("Colors: 2, Pegs: 1, Verbose?: true", mastermind.getConfig());
  }

  /**
   * Print the command line instructions
   */
  @Test
  public void testGetUsage() {
    mastermind = new Mastermind(5, 4, false);
    assertEquals("Usage: Mastermind <int colors> <int pegs> [<boolean verbose?=false>]", mastermind.getUsage());
  }
  
  /**
   * After configuring a mastermind game, run it to generate results.
   * Improperly configured games (colors or pegs are <= 0) will raise exceptions.
   */
  @Test
  public void testRun() {
    mastermind = new Mastermind();
    try {
      mastermind.run();
      fail("Run should throw an exception if Mastermind is not configured correctly.");
    } catch (Exception e) {
    }
  }
}
