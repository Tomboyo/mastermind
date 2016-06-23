package main;

import java.util.Arrays;

abstract class Code {
  int[] code;
  
  public Code(int[] code) {
    if (code.length == 0) {
      throw new IllegalArgumentException();
    }
    this.code = code;
  }
  
  public final int length() {
    return this.code.length;
  }
  
  public final int[] getCode() {
    return this.code;
  }
  
  @Override
  public final String toString() {
    return Arrays.toString(this.code);
  }
}
