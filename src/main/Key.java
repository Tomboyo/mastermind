package main;
import java.util.Arrays;

class Key extends Code {
  
  public Key(int[] code) {
    super(code.clone());
  }
  
  /*
   * Get the feedback for this code when compared against a given key
   * EXPECTS Code key, same length as this code.
   * RESPONSE is a short[]
   * 2 = peg in correct place, 1 = peg out of place, 0 = no feedback
   * Response pegs sorted in descending order (e.g, [2,2,2,1,1,0,0,0])
   */
  public Response getFeedbackFromKey(Key key) throws IllegalArgumentException {
    if (key.code.length != this.code.length) {
      throw new IllegalArgumentException("Length of Key codes are unequal.");
    }
    
    // 0:x 1:w 2:r
    int[] response = new int[code.length];
    boolean[] gmask = new boolean[code.length];
    boolean[] cmask = new boolean[code.length];
    short index = 0;
    
    // Pass 1: Count correct pegs
    for (int i = 0; i < code.length; i++) {
      if (code[i] == key.code[i]) {
        response[index++] = 2;
        gmask[i] = true;
        cmask[i] = true;
      }
    }
    
    // Pass 2: Count misplaced pegs
    for (int i = 0; i < code.length; i++) {
      if (gmask[i] != false) continue;
      
      for (int j = 0; j < key.code.length; j++) {
        if (cmask[j] != false) continue;
        
        if (code[i] == key.code[j]) {
          response[index++] = 1;
          cmask[j] = true;
        }
      }
    }
    
    return new Response(response); 
  }
  
  public boolean equals(Code other) {
    return Arrays.equals(this.code, other.code);
  }
}
