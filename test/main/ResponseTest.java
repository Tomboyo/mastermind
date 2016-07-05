package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseTest extends CodeTest {

  /**
  * Override the Code factory method so that CodeTest's methods will test functionality of Keys
  */
  @Override
  public Code createCode(int[] code) {
    return new Response(code);
  }

  /**
   * Responses must be instantiated with a non-empty int[] representing the feedback from a guess code.
   * new Response(new int[]{1, 1, 2, 2} generates a four-peg Response.
   * Response pegs should be numbered 0, 1, or 2.
   * 2 indicates a peg in a guess code was correctly placed,
   * 1 indicates a peg in a guess code was in the wrong position, and
   * 0 indicates no particular feedback (this peg of the response says nothing)
   * Note that Responses should have pegs ordered in descending number: 2, 2, 1, 0, 0
   * Responses code format corresponds to traditional mastermind rules.
   */
  @Test
  public void testResponse() {
    assertNotNull(new Key(new int[]{1}));

    int[][] parameters = new int[][]{
      null,
      new int[]{}
    };

    for (int[] code : parameters) {
      try {
        new Key(code);
        fail("Constructor should throw.");
      } catch (Exception e) {
      }
    }
  }

  /**
   * A response's hash code is the concatenation of its code.
   * Ex: new Response(new int[]{2, 2, 1,0}) should hash to 2210
   * This is used to key Hashtables in Tree.java
   */
  @Test
  public void testHashCode() {
    // response, expected hash code
    Object[][] parameters = new Object[][]{
      new Object[]{ new int[]{1}, 1},
      new Object[]{ new int[]{1, 2}, 12},
      new Object[]{ new int[]{2, 2}, 22},
      new Object[]{ new int[]{1, 2, 3}, 123},
      new Object[]{ new int[]{1, 1, 1}, 111}
    };

    
    for (Object[] set : parameters) {
      Response r1 = new Response((int[])set[0]);
      Response r2 = new Response((int[])set[0]);
      assertEquals((int)set[1], r1.hashCode());
      assertEquals((int)set[1], r2.hashCode());
      assertEquals(r1.hashCode(), r2.hashCode());
      
      // Hashcode equality and Object equality must be maintained
      assertTrue(((Object)r1).equals(r2));
    }
  }
  
  /*
   * Verify that two responses are considered equal if their pegs match (by length and color)
   * Tests Object.equals(Object) to ensure Hashtables will work correctly
   */
  @Test
  public void testEqualsCode() {
    // response1, response2, expected equal?
    Object[][] parameters = new Object[][]{
      new Object[]{
          new int[]{1}, new int[]{1}, true
      },
      new Object[]{
          new int[]{1, 2}, new int[]{1, 2}, true
      },
      new Object[]{
          new int[]{1, 2}, new int[]{1}, false
      },
      new Object[]{
          new int[]{1}, new int[]{1, 2}, false
      },
      new Object[]{
          new int[]{1, 2}, new int[]{2, 1}, false
      }
    };
    
    for (Object[] set : parameters) {
      Object response1 = new Response((int[])set[0]);
      Object response2 = new Response((int[])set[1]);
      
      // Equals itself
      assertEquals(true, response1.equals(response1));
      assertEquals(true, response2.equals(response2));
  
      // Reflexive
      assertEquals((boolean)set[2], response1.equals(response2));
      assertEquals((boolean)set[2], response2.equals(response1));
      
      // Verify that the hashcode corresponds to object equality
      assertEquals((boolean)set[2], response1.hashCode() == response2.hashCode());
      assertEquals((boolean)set[2], response2.hashCode() == response1.hashCode());

      // Response is equal to a copy of itself, as well.
      assertEquals(true, response1.equals(new Response((int[])set[0])));
      assertEquals(true, response2.equals(new Response((int[])set[1])));
    }
  }

}
