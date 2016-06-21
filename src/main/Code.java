package main;

import java.util.Arrays;

abstract class Code {
  int[] code;
  
  public int length() {
    return this.code.length;
  }
  
  @Override
  public String toString() {
    return Arrays.toString(this.code);
  }
}
