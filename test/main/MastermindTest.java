package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class MastermindTest {

  public static Mastermind mastermind = null;
  
  @Test
  public void testGetConfig() {
    mastermind = new Mastermind(5, 4, false);
    assertEquals("Colors: 5, Pegs: 4, Verbose?: false", mastermind.getConfig());
  }
  
  @Test
  public void testSetConfig() {
    mastermind = new Mastermind(5, 4, false);
    mastermind.setConfig(2,  1, true);
    assertEquals("Colors: 2, Pegs: 1, Verbose?: true", mastermind.getConfig());
  }

  @Test
  public void testGetUsage() {
    mastermind = new Mastermind(5, 4, false);
    assertEquals("Mastermind <int colors> <int pegs> [<boolean verbose?=false>]", mastermind.getUsage());
  }
  
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
