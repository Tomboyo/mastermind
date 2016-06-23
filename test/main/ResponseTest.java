package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseTest extends CodeTest {

  // Factory method implementation
  @Override
  public Code createCode(int[] code) {
    return new Response(code);
  }

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

  @Test
  public void testHashCode() {
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
  
  @Test
  public void testEqualsCode() {
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
    
    // Equality must be tested at the Object level in order to ensure that Hashtables will behave correctly
    for (Object[] set : parameters) {
      Object response1 = new Response((int[])set[0]);
      Object response2 = new Response((int[])set[1]);
      
      assertEquals(true, response1.equals(response1));
      assertEquals(true, response2.equals(response2));
  
      assertEquals((boolean)set[2], response1.equals(response2));
      assertEquals((boolean)set[2], response2.equals(response1));
      
      // Verify that the hashcode complies with object equality
      assertEquals((boolean)set[2], response1.hashCode() == response2.hashCode());
      assertEquals((boolean)set[2], response2.hashCode() == response1.hashCode());

      assertEquals(true, response1.equals(new Response((int[])set[0])));
      assertEquals(true, response2.equals(new Response((int[])set[1])));
    }
  }

}
