package main;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public abstract class CodeTest {

  // Factory method
  public abstract Code createCode(int[] code);
  
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
      assertEquals(code.length, createCode(code).length());
    }
  }
  
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
