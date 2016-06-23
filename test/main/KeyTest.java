package main;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class KeyTest {

  @Test
  public void testKey() {
    assertNotNull(new Key(new int[]{}));
  }

  @Test
  public void testGetFeedbackFromKey() {
    Object[][] parameters = new Object[][]{
      //Empty Equality
      new Object[]{
          new int[]{}, new int[]{}, new int[]{}
      },
      // Equality
      new Object[]{
          new int[]{1}, new int[]{1}, new int[]{2}
      },
      new Object[]{
          new int[]{1, 2}, new int[]{1, 2}, new int[]{2, 2}
      },
      // Misplacements
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
      // No feedback
      new Object[]{
          new int[]{1, 1, 1}, new int[]{2, 2, 2}, new int[]{0, 0, 0}
      },
      new Object[]{
          new int[]{1}, new int[]{0}, new int[]{0}
      },
      // Throw
      new Object[]{
          new int[]{1, 1, 1}, new int[]{1}  // Unequal lengths, same elements
      },
      new Object[]{
          new int[]{1, 1, 1}, new int[]{2}  // Unequal lengths, differing elements
      },
      new Object[]{
          new int[]{}, new int[]{1, 1, 1}   // Unequal lengths
      },
    };
    
    for (Object[] set : parameters) {
      Key key1 = new Key((int[])set[0]);
      Key key2 = new Key((int[])set[1]);
      
      if (set.length == 3) {
        Response correctFeedback = new Response((int[])set[2]);
        
        // Assumes that the ResponseTest class has passed for testEquals()
        assertEquals(correctFeedback, key1.getFeedbackFromKey(key2));
        assertEquals(correctFeedback, key2.getFeedbackFromKey(key1));
      } else {
        try{
          key1.getFeedbackFromKey(key2);
          fail("Method did not generate exception: " + key1 + ", " + key2);
        } catch (Exception e1) {
          try {
            key2.getFeedbackFromKey(key1);
            fail("Method did not generate exception: " + key1 + ", " + key2);
          } catch (Exception e2){
          }
        }
      }
      
    }
  }

  @Test
  public void testEqualsCode() {
    Object[][] parameters = new Object[][]{
      new Object[]{
          new int[]{}, new int[]{}, true
      },
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

  @Test
  public void testLength() {
    int[][] parameters = new int[][]{
      new int[]{},
      new int[]{1},
      new int[]{1, 2},
      new int[]{1, 1},
      new int[]{1, 2, 3},
      new int[]{1, 1, 1}
    };
    
    for (int[] code : parameters) {
      Key key = new Key(code);
      assertEquals(code.length, key.length());
    }
  }

  @Test
  public void testToString() {
    int[][] parameters = new int[][]{
      new int[]{},
      new int[]{1},
      new int[]{1, 2},
      new int[]{1, 1},
      new int[]{1, 2, 3},
      new int[]{1, 1, 1}
    };
    
    for (int[] code : parameters) {
      assertEquals(Arrays.toString(code), (new Key(code)).toString());
    }
  }

}
