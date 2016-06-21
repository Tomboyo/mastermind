package main;

import java.util.Arrays;

class Response extends Code {
  
  public Response(int[] code) {
    this.code = code;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (this.getClass() != o.getClass()) return false;
    
    return Arrays.equals(this.code, ((Response)o).code);
  }
  
  @Override
  public int hashCode() {
    int sum = 0;
    for(int i = 0; i < this.code.length; i++) {
      sum = sum * 10 + this.code[i];
    }
    return sum;
  }
}
