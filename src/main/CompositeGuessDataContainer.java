package main;

import java.util.ArrayList;
import java.util.Hashtable;

class CompositeGuessDataContainer {
  private Key guess;
  private int codeIndex;
  private Hashtable<Response, ArrayList<Key>> responseGroups;
  
  CompositeGuessDataContainer(Key guess, int index, Hashtable<Response, ArrayList<Key>> responseGroup) {
    this.guess = guess;
    this.codeIndex = index;
    this.responseGroups = responseGroup;
  }
  
  Key getGuess() {
    return this.guess;
  }
  
  int getCodeIndex() {
    return this.codeIndex;
  }
  
  Hashtable<Response, ArrayList<Key>> getResponseGroups() {
    return this.responseGroups;
  }
}
