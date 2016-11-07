package edu.vwc.core;

import java.util.Arrays;

class Response extends Code {
  
  public Response(int[] code) {
    super(code.clone());
  }
  
  @Override
  public int hashCode() {
    int sum = 0;
    for(int i = 0; i < this.code.length; i++) {
      sum = sum * 10 + this.code[i];
    }
    return sum;
  }
  
  @Override
  public final boolean equals(Object other) {
    if (other == this) return true;
    if (other == null) return false;
    if (this.getClass() != other.getClass()) return false;
    
    return Arrays.equals(this.code, ((Response)other).code);
  }
}
