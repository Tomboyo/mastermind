package main;

import static org.junit.Assert.*;
import org.junit.Test;

public class KeyTest extends CodeTest {

  /**
   * Override the Code factory method so that CodeTest's methods will test functionality of Keys
   */
  @Override
  public Code createCode(int[] code) {
    return new Key(code);
  }
  
  /**
   * Keys must be instantiated with a non-empty int[] representing the sequence of pegs
   * new Key(new int[]{1, 1, 2, 2} generates a four-peg Key made of two colors
   */
  @Test
  public void testKey() {
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
   * Get the Response by comparing a guess Key to a master Key
   * The guess and the master must be the same length
   * The response will match the expected output from a traditional game of mastermind
   */
  @Test
  public void testGetFeedbackFromKey() {
    
    // guess code, master key, expected response code
    Object[][] parameters = new Object[][]{
      // The guess has pegs all of the correct color andplacement
      new Object[]{
          new int[]{1}, new int[]{1}, new int[]{2}
      },
      new Object[]{
          new int[]{1, 2}, new int[]{1, 2}, new int[]{2, 2}
      },
      
      // The guess has a few pegs of the correct color, but they are misplaced
      new Object[]{
          new int[]{1, 2}, new int[]{2, 1}, new int[]{1, 1}
      },
      new Object[]{
          new int[]{1, 1, 2}, new int[]{1, 2, 1}, new int[]{2, 1, 1}
      },
      new Object[]{
          new int[]{1, 2, 1}, new int[]{2, 1, 1}, new int[]{2, 1, 1}
      },
      new Object[]{
          new int[]{1, 2, 3, 4}, new int[]{2, 3, 1, 4}, new int[]{2, 1, 1, 1}
      },
      
      // Guess has no pegs of the correct color
      new Object[]{
          new int[]{1, 1, 1}, new int[]{2, 2, 2}, new int[]{0, 0, 0}
      },
      new Object[]{
          new int[]{1}, new int[]{0}, new int[]{0}
      },
      
      // Guess and master are not of the correct length; comparing them should throw an error
      new Object[]{
          new int[]{1, 1, 1}, new int[]{1}  // Unequal lengths, similar elements
      },
      new Object[]{
          new int[]{1, 1, 1}, new int[]{2}  // Unequal lengths, differing elements
      }
    };
    
    for (Object[] set : parameters) {
      Key guessCode = new Key((int[])set[0]);
      Key masterKey = new Key((int[])set[1]);
      
      // Parameter sets withut a third element do not have an "expected" value, which means they are supposed to throw
      if (set.length == 3) {
        Response correctFeedback = new Response((int[])set[2]);
        
        // Assumes that the ResponseTest class has passed for testEquals()
        assertEquals(true, correctFeedback.equals(guessCode.getFeedbackFromKey(masterKey)));
        assertEquals(true, correctFeedback.equals(masterKey.getFeedbackFromKey(guessCode)));
      } else {
        try{
          guessCode.getFeedbackFromKey(masterKey);
          fail("Method did not generate exception: " + guessCode + ", " + masterKey);
        } catch (Exception e1) {
          try {
            masterKey.getFeedbackFromKey(guessCode);
            fail("Method did not generate exception: " + guessCode + ", " + masterKey);
          } catch (Exception e2){
          }
        }
      }
    }
  }
  
  /**
   * A Key equals another Key if they have the same number of pegs and the pegs are the same color
   */
  @Test
  public void testEqualsCode() {
    
    // Key1, Key2, expected equals?
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
      Key key1 = new Key((int[])set[0]);
      Key key2 = new Key((int[])set[1]);
      
      assertEquals(true, key1.equals(key1));
      assertEquals(true, key2.equals(key2));
  
      assertEquals((boolean)set[2], key1.equals(key2));
      assertEquals((boolean)set[2], key2.equals(key1));
      
      assertEquals(true, key1.equals(new Key((int[])set[0])));
      assertEquals(true, key2.equals(new Key((int[])set[1])));
    }
  }

}
