package main;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * Extended by ResponseTest and KeyTest
 */
public abstract class CodeTest {

  /**
   * Factory method for creating Codes (Response, Key)
   */
  public abstract Code createCode(int[] code);
  
  /**
   * Get the length of a Code
   */
  @Test
  public void testLength() {
    int[][] parameters = new int[][]{
      new int[]{1},
      new int[]{1, 2},
      new int[]{2, 2},
      new int[]{1, 2, 3},
      new int[]{1, 1, 1}
    };
    
    for (int[] code : parameters) {
      assertEquals("Code returned incorrect length", code.length, createCode(code).length());
    }
  }
  
  /**
   * Get the int[] used to instantiate a Code
   * int[]{1, 2, 3, 4} represents four pegs of different colors
   */
  @Test
  public void testGetCode() {
    int[] code = new int[]{1, 2, 3, 4, 5};
    Code codeObj = createCode(code);
    
    assertTrue(Arrays.equals(code, codeObj.getCode()));
    assertNotEquals(code, codeObj.getCode());
  }

  @Test
  public void testToString() {
    Code code = createCode(new int[]{1, 2, 3, 4, 3, 2, 1});
    assertEquals("[1, 2, 3, 4, 3, 2, 1]", code.toString());
  }
  
}
