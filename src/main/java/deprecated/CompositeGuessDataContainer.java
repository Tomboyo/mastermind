package deprecated;

import java.util.ArrayList;
import java.util.Hashtable;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

class CompositeGuessDataContainer {
  private Code guess;
  private int codeIndex;
  private Hashtable<Response, ArrayList<Code>> responseGroups;
  
  CompositeGuessDataContainer(Code guess, int index, Hashtable<Response, ArrayList<Code>> responseGroup) {
    this.guess = guess;
    this.codeIndex = index;
    this.responseGroups = responseGroup;
  }
  
  Code getGuess() {
    return this.guess;
  }
  
  int getCodeIndex() {
    return this.codeIndex;
  }
  
  Hashtable<Response, ArrayList<Code>> getResponseGroups() {
    return this.responseGroups;
  }
}
